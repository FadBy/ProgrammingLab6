import exceptions.ApplicationException;
import exceptions.ApplicationRuntimeException;
import server.Channel;
import validation.*;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.*;

public class Application {
    private final Map<String, Command> commands = new HashMap<>();
    private boolean exitState = false;
    private boolean isRun = false;
    private final Console console = new Console();
    private TableTemplate rootTable;
    private final SocketAddress address;

    public Application(SocketAddress address) {
        this.address = address;
    }

    public void run() {
        if (isRun) {
            throw new ApplicationRuntimeException("Application had already run");
        }
        isRun = true;
        try {
            start();
            processCycle();
        } catch (ApplicationException e) {
            System.out.println(e.getMessage());
        }
    }

    private void start() {
        commands.put("help", new Command("help", 0, false));
        commands.put("info", new Command("info", 0, false));
        commands.put("show", new Command("show", 0, false));
        commands.put("add", new Command("add", 0, true));
        commands.put("update_by_id", new Command("update_by_id", 1, true));
        commands.put("remove_by_id", new Command("remove_by_id", 1, false));
        commands.put("clear", new Command("clear", 0, false));
        commands.put("execute_script", new Command("execute_script", 1, false));
        commands.put("exit", new Command("exit", 0, false));
        commands.put("add_it_min", new Command("add_it_min", 0, true));
        commands.put("remove_lower", new Command("remove_lower", 0, true));
        commands.put("history", new Command("history", 0, false));

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

        rootTable = productTemplate;
    }

    private void processCycle() throws ApplicationException {
        Channel channel = new Channel(null);
        channel.setBlocking(true);
        while(!exitState) {
            try {
                System.out.print("Enter command: ");

                String text = console.nextLine();
                if (text == null) {
                    text = "";
                }
                List<String> parsed = Arrays.asList(text.split("[\t ]"));
                if (parsed.size() == 1 && parsed.get(0).isEmpty()) {
                    console.printException("You left input empty");
                    continue;
                }
                String name = parsed.get(0);
                if (Objects.equals(name, "exit")) {
                    exit();
                    continue;
                }
                if (!commands.containsKey(name)) {
                    console.printException("Command " + name + " doesn't exist");
                    continue;
                }
                Command command = commands.get(name);
                if (parsed.size() != 1 + command.argsNumber) {
                    console.printException("Command " + name + " has " + command.argsNumber + ", you wrote only " + parsed.size());
                    continue;
                }
                Map<String, Map<String, String>> builders = null;
                if (command.haveObject) {
                    RowBuilder builder = new RowBuilder(rootTable);
                    for (String field : builder) {
                        String request = console.makeRequest(field + ": ", console);
                        String message = builder.checkFieldValidity(field, request);
                        while (!message.isEmpty()) {
                            console.printException(message);
                            request = console.makeRequest(field + ": ", console);
                            message = builder.checkFieldValidity(field, request);
                        }
                        builder.setField(field, request);
                    }
                    builders = builder.build();
                }
                CommandPackage packagee = new CommandPackage(parsed, builders);
                channel.setAddress(address);
                channel.send(packagee);
                Response response = (Response) channel.receive();
                System.out.println(response.text);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            } catch (ApplicationException e) {
                e.printStackTrace();
            }
        }
    }

    public void exit() {
        exitState = true;
    }
}
