package commander;

import application.Environment;
import application.FieldTemplate;
import application.TableTemplate;
import file_data.Console;
import file_data.Errput;
import file_data.Input;
import file_data.Output;

import java.util.ArrayList;
import java.util.List;

public class AddConsoleCommand extends Command {
    private final Console console;
    private final Environment environment;

    public AddConsoleCommand(Environment environment, Console console) {
        this.console = console;
        this.environment = environment;
    }

    @Override
    public String getName() {
        return "add";
    }

    @Override
    public void execute(List<String> args, Input input, Output output, Errput errput) {
        environment.addRow(fillBuilders(environment.getRootTemplate().new RowBuilder(), output));
    }

    private List<TableTemplate.RowBuilder> fillBuilders(TableTemplate.RowBuilder builder, Output output) {
        List<TableTemplate.RowBuilder> builders = new ArrayList<>();
        TableTemplate template = builder.getTableTemplate();
        String name = "";
        if (environment.getRootTemplate() == builder.getTableTemplate()) {
            name = builder.getTableTemplate().name + ".";
        }
        for (FieldTemplate iter : template.getFields()) {
            if (iter.relativeTable != null) {
                builders.addAll(fillBuilders(iter.relativeTable.new RowBuilder(), output));
            }
            String text = console.nextLine("Введите " + name + iter.name + ": ");
            String message = iter.checkValidity(text);
            while (!message.isEmpty()) {
                output.printResult(message);
                text = console.nextLine("Введите " + name + iter.name + ": ");
                message = iter.checkValidity(text);
            }
            builder.setField(iter.name, text);
        }
        builders.add(builder);
        return builders;
    }
}
