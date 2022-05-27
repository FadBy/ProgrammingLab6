package application;

import commander.*;
import data.Console;
import exceptions.ApplicationException;
import exceptions.ApplicationRuntimeException;
import exceptions.IncorrectCommandException;
import org.jetbrains.annotations.NotNull;
import template.*;

import java.net.*;
import java.util.*;

public class Application {
    private boolean exitState = false;
    private boolean readyToRun = true;
    private final Console console = new Console();
    private final Map<String, TableTemplate> tables = new HashMap<>();
    private final ClientServer server;
    private final CommandParser parser = new CommandParser();
    private final CommandExecuter executor = new CommandExecuter();
    private final ObjectBuilder builder = new ObjectBuilder();

    public Application(@NotNull String host, int port) {
        ClientServer server1;
        try {
            server1 = new ClientServer(new InetSocketAddress(host, port));
        } catch (ApplicationException e) {
            console.printException(e);
            readyToRun = false;
            server1 = null;
        }
        server = server1;
    }

    public boolean isReadyToRun() {
        return readyToRun;
    }

    public void run() {
        if (!readyToRun) {
            throw new ApplicationRuntimeException("Application had already run or not ready for run");
        }
        readyToRun = false;
        try {
            start();
            processCycle();
        } catch (ApplicationException e) {
            System.out.println(e.getMessage());
        }
    }

    private void start() {
        Label isNotNull = new MonoLabel("can't be null", Objects::nonNull);
        Label isNotEmpty = new MonoLabel("can't be empty", x -> x != null && !x.isEmpty());
        Label isUnique = new MonoLabel("must be unique", x -> true);
        Label isMoreThanZero = new MonoLabel("must be more than zero", x -> x != null && Double.parseDouble(x) > 0);
        Label isMaxSix = new MonoLabel("must be more than six", x -> x != null && Double.parseDouble(x) <= 6);

        FieldTemplate x = new FieldTemplate("x", FieldType.Long, Arrays.asList(isNotNull, isMaxSix));
        FieldTemplate y = new FieldTemplate("y", FieldType.Long, Arrays.asList(isNotNull));

        TableTemplate coordinateTemplate = new TableTemplate("Coordinates", Arrays.asList(x, y), "x");
        tables.put(coordinateTemplate.name, coordinateTemplate);

        FieldTemplate idOrg = new FieldTemplate("id", FieldType.Integer, Arrays.asList(isMoreThanZero, isUnique), true);
        FieldTemplate nameOrg = new FieldTemplate("name", FieldType.String, Arrays.asList(isNotNull, isNotEmpty));
        FieldTemplate fullName = new FieldTemplate("fullName", FieldType.String, Arrays.asList());
        FieldTemplate annualTurnover = new FieldTemplate("annualTurnover", FieldType.Long, Arrays.asList(isMoreThanZero));
        FieldTemplate employeesCount = new FieldTemplate("employeesCount", FieldType.Long, Arrays.asList(isMoreThanZero));

        TableTemplate organization = new TableTemplate("Organization", Arrays.asList(idOrg, nameOrg, fullName, annualTurnover, employeesCount), "name");
        tables.put(organization.name, organization);

        FieldTemplate id = new FieldTemplate("id", FieldType.Long, Arrays.asList(isNotNull, isUnique, isMoreThanZero), true);
        FieldTemplate nameProduct = new FieldTemplate("name", FieldType.String, Arrays.asList(isNotNull, isNotEmpty));
        FieldTemplate coordinates = new FieldTemplate("coordinates", FieldType.Long, Arrays.asList(isNotNull), coordinateTemplate);
        FieldTemplate creationDate = new FieldTemplate("creationDate", FieldType.DateTime, Arrays.asList(isNotNull), true);
        FieldTemplate price = new FieldTemplate("price", FieldType.Double, Arrays.asList(isMoreThanZero));
        FieldTemplate partNumber = new FieldTemplate("partNumber", FieldType.String, Arrays.asList(isNotEmpty, isUnique));
        FieldTemplate manufactureCost = new FieldTemplate("manufactureCost", FieldType.Double, Arrays.asList(isNotNull));
        FieldTemplate manufacturer = new FieldTemplate("manufacturer", FieldType.Long, Arrays.asList(), organization);

        TableTemplate product = new TableTemplate("Product", Arrays.asList(id, nameProduct, coordinates, creationDate, price, partNumber, manufactureCost, manufacturer), "coordinates.x");;
        tables.put(product.name, product);
        builder.setTables(tables);

        Map<String, Command> commands = new LinkedHashMap<>();

        commands.put("help", new ServerCommand("help", 0, "", server, builder));
        commands.put("info", new ServerCommand("info", 0, "", server, builder));
        commands.put("show", new ServerCommand("show", 0, "", server, builder));
        commands.put("add", new ServerCommand("add", 0, "Product", server, builder));
        commands.put("update_by_id", new ServerCommand("update_by_id", 1, "Product", server, builder));
        commands.put("remove_by_id", new ServerCommand("remove_by_id", 1, "Product", server, builder));
        commands.put("clear", new ServerCommand("clear", 0, "", server, builder));
        commands.put("execute_script", new ServerCommand("execute_script", 1, "", server, builder));
        commands.put("exit", new ExitCommand(this));
        commands.put("add_it_min", new ServerCommand("add_it_min", 0, "Product", server, builder));
        commands.put("remove_lower", new ServerCommand("remove_lower", 0, "Product", server, builder));
        commands.put("history", new ServerCommand("history", 0, "", server, builder));

        executor.setCommands(commands);
    }

    private void processCycle() throws ApplicationException {
        while(!exitState) {
            try {
                String commandResult = executor.execute(parser.parseCommand(console.nextLine("Enter command: ")), console, console);
                if (!commandResult.isEmpty()) {
                    console.printResult(commandResult);
                }
            } catch (IncorrectCommandException e) {
                console.printException(e);
            }
        }
    }

    public void exit() {
        exitState = true;
    }
}
