package application;

import com.google.gson.*;
import commander.*;
import data.Console;
import environment.Environment;
import exceptions.ApplicationException;
import exceptions.ApplicationRuntimeException;
import exceptions.IncorrectCommandException;
import exceptions.IncorrectJsonException;
import file_data.InetOutput;
import file_data.JsonData;
import org.jetbrains.annotations.NotNull;
import server.CommandPackage;
import template.*;
import server.Response;
import data.*;

import java.io.*;
import java.net.*;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;

public class Application {
    private Path jsonPath;
    private final String envPath;
    private JsonData jsonData = new JsonData();
    private final DatagramServer server;
    private final Commander commander = new Commander();
    private final Environment environment = new Environment();
    private final CommandHistory history = new CommandHistory();
    private boolean readyToRun = true;
    private boolean exitState = false;
    private final Console console = new Console();

    public Application(@NotNull String envPath, int port) {
        this.envPath = envPath;
        DatagramServer server1;
        try {
            server1 = new DatagramServer(port);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            server1 = null;
            readyToRun = false;
        }
        server = server1;
    }

    public void run() {
        if (!readyToRun) {
            throw new ApplicationRuntimeException("application.Application is already run or not ready to run");
        }
        readyToRun = false;
        try {
            start();
            load();
            processCycle();
        } catch (ApplicationException e) {
            System.out.println(e.getMessage());
        }
    }

    private void load() throws ApplicationException {
        jsonPath = new File(System.getenv(envPath)).toPath();
        try {
            jsonData.setPath(jsonPath);
            JsonArray arr = new Gson().fromJson(jsonData.readAll(), JsonArray.class);
            if (arr == null) {
                throw new JsonSyntaxException("json is empty");
            }
            for (JsonElement json : arr) {
                if (!json.isJsonObject()) {
                    throw new IncorrectJsonException("Json is not array of objects");
                }
                environment.addRow(new RowBuilder(json.getAsJsonObject(), environment.getTableTemplate(environment.rootTable)).build());
            }
        } catch (IOException | IncorrectJsonException e) {
            throw new ApplicationException(e.getMessage());
        } catch (JsonSyntaxException e) {
            throw new ApplicationException("Wrong format of json");
        }
    }

    private void start() {
        List<Command> commands = new ArrayList<>(Arrays.asList(
                new HelpCommand(commander),
                new AddCommand(environment),
                new InfoCommand(environment),
                new ShowCommand(environment),
                new RemoveByIdCommand(environment),
                new ClearCommand(environment),
                new UpdateByIdCommand(environment),
                new AddIfMinCommand(environment),
                new RemoveLowerCommand(environment),
                new HistoryCommand(history),
                new SaveCommand(jsonData, environment),
                new ExitCommand(this)
        ));
        commander.setCommands(commands);

        Label isNotNull = new MonoLabel("can't be null", Objects::nonNull);
        Label isNotEmpty = new MonoLabel("can't be empty", x -> x == null || !x.isEmpty());
        Label isIdProductUnique = new MonoLabel("must be unique", x -> x == null || !environment.getColumnTable("Product", "id").contains(x));
        Label isPartNumberUnique = new MonoLabel("must be unique", x -> x == null || !environment.getColumnTable("Product", "partNumber").contains(x));
        Label isIdOrganizationUnique = new MonoLabel("must be unique", x -> x == null || !environment.getColumnTable("Organization", "id").contains(x));
        Label isMoreThanZero = new MonoLabel("must be more than zero", x -> x != null && Double.parseDouble(x) > 0);
        Label isMaxSix = new MonoLabel("must be more than six", x -> x != null && Double.parseDouble(x) <= 6);

        FieldTemplate x = new FieldTemplate("x", FieldType.Long, Arrays.asList(isNotNull, isMaxSix));
        FieldTemplate y = new FieldTemplate("y", FieldType.Long, Arrays.asList(isNotNull));

        TableTemplate coordinateTemplate = new TableTemplate("Coordinates", Arrays.asList(x, y), "x");

        FieldTemplate idOrg = new FieldTemplate("id", FieldType.Integer, Arrays.asList(isMoreThanZero, isIdOrganizationUnique), true);
        FieldTemplate nameOrg = new FieldTemplate("name", FieldType.String, Arrays.asList(isNotNull, isNotEmpty));
        FieldTemplate fullName = new FieldTemplate("fullName", FieldType.String, Arrays.asList());
        FieldTemplate annualTurnover = new FieldTemplate("annualTurnover", FieldType.Long, Arrays.asList(isMoreThanZero));
        FieldTemplate employeesCount = new FieldTemplate("employeesCount", FieldType.Long, Arrays.asList(isMoreThanZero));

        TableTemplate organization = new TableTemplate("Organization", Arrays.asList(idOrg, nameOrg, fullName, annualTurnover, employeesCount), "name");

        FieldTemplate id = new FieldTemplate("id", FieldType.Long, Arrays.asList(isNotNull, isIdProductUnique, isMoreThanZero), true);
        FieldTemplate nameProduct = new FieldTemplate("name", FieldType.String, Arrays.asList(isNotNull, isNotEmpty));
        FieldTemplate coordinates = new FieldTemplate("coordinates", FieldType.Long, Arrays.asList(isNotNull), coordinateTemplate);
        FieldTemplate creationDate = new FieldTemplate("creationDate", FieldType.DateTime, Arrays.asList(isNotNull), true);
        FieldTemplate price = new FieldTemplate("price", FieldType.Double, Arrays.asList(isMoreThanZero));
        FieldTemplate partNumber = new FieldTemplate("partNumber", FieldType.String, Arrays.asList(isNotEmpty, isPartNumberUnique));
        FieldTemplate manufactureCost = new FieldTemplate("manufactureCost", FieldType.Double, Arrays.asList(isNotNull));
        FieldTemplate manufacturer = new FieldTemplate("manufacturer", FieldType.Long, Arrays.asList(), organization);

        TableTemplate productTemplate = new TableTemplate("Product", Arrays.asList(id, nameProduct, coordinates, creationDate, price, partNumber, manufactureCost, manufacturer), "coordinates.x");

        environment.setTables(LocalDateTime.now(), "Product", Arrays.asList(organization, coordinateTemplate, productTemplate));
    }

    private void processCycle() {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                try {
                    CommandPackage commandPackage;
                    InetOutput clientResponse = new InetOutput();
                    try {
                        commandPackage = (CommandPackage) server.receive();
                        if (exitState) {
                            return;
                        }
                        new Thread(this).start();
                    } catch (ClassNotFoundException e) {
                        throw new ApplicationException("Got invalid object");
                    }

                    commander.executeCommand(commandPackage.args, commandPackage.builder, clientResponse);
                    server.send(new Response(clientResponse.getText()));
                    history.addCommand(commandPackage.args.get(0));
                    clientResponse.clear();

                } catch (IOException | ApplicationException e) {
                    System.out.println(e.getMessage());
                }
            }
        };
        new Thread(run).start();
        while (!exitState) {
            try {
                String text = console.makeRequest("Enter command: ", console);
                commander.executeCommand(commander.parseCommand(text), null, console);
            } catch (IOException | IncorrectCommandException e) {
                console.printException(e);
            }
        }
    }

    public void exit() {
        server.disconnect();
        exitState = true;
    }
}
