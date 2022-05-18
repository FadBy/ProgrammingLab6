import server.Channel;
import com.google.gson.*;
import commander.*;
import environment.Environment;
import exceptions.ApplicationException;
import exceptions.IncorrectCommandException;
import exceptions.IncorrectJsonException;
import file_data.InetOutput;
import file_data.JsonData;
import validation.Response;
import validation.*;

import java.io.*;
import java.net.*;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;

public class Application {
    private Path jsonPath;
    private final String envPath;
    private JsonData jsonData = new JsonData();
    private final Input fakeInput = new Input() {
        @Override
        public boolean hasNextLine() {
            return false;
        }

        @Override
        public String nextLine() throws IOException {
            return null;
        }

        @Override
        public boolean isContinuing() {
            return false;
        }
    };
    private final InetOutput clientResponse = new InetOutput();
    private final Commander commander = new Commander(fakeInput, clientResponse);
    private final Environment environment = new Environment();
    private final List<String> saveCommands = new ArrayList<>();
    private final CommandHistory history = new CommandHistory();

    public Application(String envPath) {
        this.envPath = envPath;
    }

    public void run() {
        try {
            start();
            load();
            processCycle();
        } catch (ApplicationException e) {
            System.out.println(e.getMessage());
        }
    }

    private void load() throws ApplicationException {
        System.out.println(System.getenv(envPath));
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
//                new ExitCommand(this),
                new AddCommand(environment),
                new InfoCommand(environment),
                new ShowCommand(environment),
                new RemoveByIdCommand(environment),
                new ClearCommand(environment),
                new UpdateByIdCommand(environment),
                new AddIfMinCommand(environment),
                new RemoveLowerCommand(environment),
                new HistoryCommand(history),
//                new ExecuteScriptCommand(commander, fileData)
                new SaveCommand(jsonData, environment)
        ));
        commander.setCommands(commands);

        Label isNotNull = new MonoLabel("can't be null", Objects::nonNull);
        Label isNotEmpty = new MonoLabel("can't be empty", x -> x != null && !x.isEmpty());
        Label isIdProductUnique = new ContainsLabel("must be unique", (String x, Collection<String> lst) -> x != null && !lst.contains(x));
        Label isPartNumberUnique = new ContainsLabel("must be unique", (String x, Collection<String> lst) -> x != null && !lst.contains(x));
        Label isIdOrganizationUnique = new ContainsLabel("must be unique", (String x, Collection<String> lst) -> x != null && !lst.contains(x));
        Label isMoreThanZero = new MonoLabel("must be more than zero", x -> x != null && Double.parseDouble(x) > 0);
        Label isMaxSix = new MonoLabel("must be more than six", x -> x != null && Double.parseDouble(x) <= 6);

        FieldTemplate x = new FieldTemplate("x", FieldType.Long, Arrays.asList(isNotNull, isMaxSix));
        FieldTemplate y = new FieldTemplate("y", FieldType.Long, Arrays.asList(isNotNull));

        TableTemplate coordinateTemplate = new TableTemplate("Coordinates", Arrays.asList(x, y), "x");

        FieldTemplate idOrg = new FieldTemplate("id", FieldType.Integer, Arrays.asList(isMoreThanZero, isIdProductUnique), true);
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

        saveCommands.addAll(Arrays.asList("add", "add_if_min", "clear", "remove_by_id", "remove_lower", "update_by_id"));
    }

    private void processCycle() throws ApplicationException {
        Channel server = new Channel(new InetSocketAddress("localhost", 2232));
        server.setBlocking(false);
        while (true) {
            try {
                Serializable obj = server.receive();
                if (obj == null || server.getAddress() == null) {
                    continue;
                }
                CommandPackage commandPackage = (CommandPackage) obj;
                commander.executeCommand(commandPackage.args, commandPackage.builder, fakeInput, clientResponse);
                if (server.getAddress() == null) {
                    continue;
                }
                server.send(new Response(clientResponse.getText()));
                history.addCommand(commandPackage.args.get(0));
                clientResponse.clear();
                if (saveCommands.contains(commandPackage.args.get(0))) {
                    commander.executeCommand(Arrays.asList("save"), null, fakeInput, clientResponse);
                }
            } catch (IOException|IncorrectCommandException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
