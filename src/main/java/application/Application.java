package application;

import commander.*;
import exceptions.ApplicationException;
import exceptions.ApplicationRuntimeException;
import file_data.Console;
import file_data.FileData;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Application {
    private final FileData fileData = new FileData();
    private final Console console = new Console();
    private final Environment environment = new Environment();
//    private final JsonData collectionData = new JsonData("", productCollection);
    private final Commander commander = new Commander(console, console);
    private final CommandHistory history = new CommandHistory();
    private boolean isRun = false;
    private boolean exitState = false;

    public void run() {
        if (isRun) {
            throw new ApplicationRuntimeException();
        }
        isRun = true;
        start();
        programCycle();
    }

    private void start() {
        List<Command> commands = new ArrayList<>(Arrays.asList(
                new HelpCommand(commander),
                new ExitCommand(this),
                new AddCommand(environment),
                new InfoCommand(environment),
                new ShowCommand(environment),
                new RemoveByIdCommand(environment),
                new ClearCommand(environment),
                new UpdateByIdCommand(environment),
                new AddIfMinCommand(environment),
                new RemoveLowerCommand(environment),
                new HistoryCommand(history),
                new ExecuteScriptCommand(commander, fileData)
        ));
        commander.setCommands(commands);

        Label isNotNull = new MonoLabel("can't be null", Objects::nonNull);
        Label isNotEmpty = new MonoLabel("can't be empty", x -> x != null && !x.isEmpty());
        Label isIdProductUnique = new BiLabel("must be unique", (String x, List<String> lst) -> x != null && !lst.contains(x), environment, "Product", "id");
        Label isPartNumberUnique = new BiLabel("must be unique", (String x, List<String> lst) -> x != null && !lst.contains(x), environment, "Product", "partNumber");
        Label isIdOrganizationUnique = new BiLabel("must be unique", (String x, List<String> lst) -> x != null && !lst.contains(x), environment, "Organization", "id");
        Label isMoreThanZero = new MonoLabel("must be more than zero", x -> x != null && Double.parseDouble(x) > 0);
        Label isMaxSix = new MonoLabel("must be more than six", x -> x != null && Double.parseDouble(x) <= 6);

        FieldTemplate x = new FieldTemplate("x", FieldType.Long, Arrays.asList(isNotNull, isMaxSix));
        FieldTemplate y = new FieldTemplate("y", FieldType.Long, Arrays.asList(isNotNull));

        TableTemplate coordinateTemplate = new TableTemplate("Coordinates", Arrays.asList(x, y), "x");

        FieldTemplate idOrg = new FieldTemplate("id", FieldType.Integer, Arrays.asList(isMoreThanZero, isIdProductUnique), () -> environment.getAutoIncrement("Organization", "id"));
        FieldTemplate nameOrg = new FieldTemplate("name", FieldType.String, Arrays.asList(isNotNull, isNotEmpty));
        FieldTemplate fullName = new FieldTemplate("fullName", FieldType.String, Arrays.asList());
        FieldTemplate annualTurnover = new FieldTemplate("annualTurnover", FieldType.Long, Arrays.asList(isMoreThanZero));
        FieldTemplate employeesCount = new FieldTemplate("employeesCount", FieldType.Long, Arrays.asList(isMoreThanZero));

        TableTemplate organization = new TableTemplate("Organization", Arrays.asList(idOrg, nameOrg, fullName, annualTurnover, employeesCount), "name");

        FieldTemplate id = new FieldTemplate("id", FieldType.Long, Arrays.asList(isNotNull, isIdProductUnique, isMoreThanZero), () -> environment.getAutoIncrement("Product", "id"));
        FieldTemplate nameProduct = new FieldTemplate("name", FieldType.String, Arrays.asList(isNotNull, isNotEmpty));
        FieldTemplate coordinates = new FieldTemplate("coordinates", FieldType.Long, Arrays.asList(isNotNull), coordinateTemplate);
        FieldTemplate creationDate = new FieldTemplate("creationDate", FieldType.String, Arrays.asList(isNotNull), () -> LocalDateTime.now().toString());
        FieldTemplate price = new FieldTemplate("price", FieldType.Double, Arrays.asList(isMoreThanZero));
        FieldTemplate partNumber = new FieldTemplate("partNumber", FieldType.String, Arrays.asList(isNotEmpty, isPartNumberUnique));
        FieldTemplate manufactureCost = new FieldTemplate("manufactureCost", FieldType.Double, Arrays.asList(isNotNull));
        FieldTemplate manufacturer = new FieldTemplate("manufacturer", FieldType.Long, Arrays.asList(), organization);

        TableTemplate productTemplate = new TableTemplate("Product", Arrays.asList(id, nameProduct, coordinates, creationDate, price, partNumber, manufactureCost, manufacturer), "coordinates.x");

        environment.setTables(LocalDateTime.now(),"Product", Arrays.asList(productTemplate, coordinateTemplate, organization));
    }

    private void programCycle() {
        while (!exitState) {
            try {
                List<String> result = commander.parseCommand(console.nextLine("Введите команду: "));
                commander.executeCommand(result);
                history.addCommand(result.get(0));
            } catch(ApplicationException| IOException e) {
                console.printException(e);
            }
        }
    }

    public void exit() {
        exitState = true;
    }
}
