package commander;

import application.Environment;
import file_data.Errput;
import file_data.Input;
import file_data.Output;

import java.util.*;

public class AddCommand implements Command {
    private final Environment environment;

    public AddCommand(Environment environment) {
        this.environment = environment;
    }

    @Override
    public String getName() {
        return "add";
    }

    @Override
    public String getDescription() {
        return "добавить новый элемент в коллекцию";
    }

    @Override
    public void execute(List<String> args, Input input, Output output, Errput errput) {
        Environment.Row.Builder builder = new Environment.Row.Builder(environment.getTableTemplate(environment.rootTable));
        for (Iterator<String> iter = builder.iterator(); iter.hasNext(); ) {
            String field = iter.next();
            output.printRequest(field + ": ");
            String textInput = input.nextLine();
            String message = builder.checkFieldValidity(field, textInput);
            while (!message.isEmpty()) {
                output.printResult(message);
                output.printRequest(field + ": ");
                textInput = input.nextLine();
                message = builder.checkFieldValidity(field, textInput);
            }
            builder.setField(field, textInput);
        }
        environment.addRow(builder);
//        Map<String, RowBuilder> builders = fillBuilders(rootTable, input, errput, output);
//        environment.addRow(builders);
    }
}
