package application;

import commander.*;
import exceptions.ApplicationException;
import exceptions.ApplicationRuntimeException;
import file_data.Console;
import file_data.FileData;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Application {
//    private final ProductCollection productCollection = new ProductCollection();
    private final FileData fileData = new FileData();
    private final Console console = new Console();
    private final Environment environment = new Environment();
//    private final JsonData collectionData = new JsonData("", productCollection);
    private final Commander commander = new Commander(console, console, console);
    private final CommandHistory history = new CommandHistory();
//    private final Validator validator = new Validator();
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
                new HelpCommand(),
                new ExitCommand(this),
                new AddConsoleCommand(environment, console)
//                new InfoCommand(productCollection),
//                new RemoveByIdCommand(productCollection),
//                new ShowCommand(productCollection)
        ));
        commander.setCommands(commands);

        Label isNotNull = new MonoLabel("can't be null", Objects::nonNull);
        Label isNotEmpty = new MonoLabel("can't be empty", String::isEmpty);
        Label isIdProductUnique = new BiLabel("must be unique", (String x, List<String> lst) -> !lst.contains(x), environment, "Product", "id");
        Label isPartNumberUnique = new BiLabel("must be unique", (String x, List<String> lst) -> !lst.contains(x), environment, "Product", "partNumber");
        Label isIdOrganizationUnique = new BiLabel("must be unique", (String x, List<String> lst) -> !lst.contains(x), environment, "Organization", "id");
        Label isMoreThanZero = new MonoLabel("must be more than zero", x -> Double.parseDouble(x) > 0);
        Label isMaxSix = new MonoLabel("must be more than six", x -> Double.parseDouble(x) <= 6);

        FieldTemplate id = new FieldTemplate("id", FieldType.Long, Arrays.asList(isNotNull, isIdProductUnique, isMoreThanZero));

        TableTemplate productTemplate = new TableTemplate("Product", Arrays.asList(id));

        environment.setTables(Arrays.asList(productTemplate));

    }

    private void programCycle() {
        while (!exitState) {
            try {
                console.nextLine("Введите команду");
                List<String> result = commander.parseCommand(console.nextLine());
                commander.executeCommand(result.get(0), result.stream().skip(1).collect(Collectors.toList()));
                history.addCommand(result.get(0));
            } catch(ApplicationException e) {
                console.printException(e);
            }
        }
    }

    public void exit() {
        exitState = true;
    }

}
