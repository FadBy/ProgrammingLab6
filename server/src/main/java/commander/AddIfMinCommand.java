package commander;

import environment.Environment;
import exceptions.IncorrectCommandException;
import data.Input;
import data.Output;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class AddIfMinCommand implements Command {
    private final Environment environment;

    public AddIfMinCommand(Environment environment) {
        this.environment = environment;
    }

    @Override
    public String getName() {
        return "add_if_min";
    }

    @Override
    public String getDescription() {
        return "завершить программу (без сохранения в файл)";
    }

    @Override
    public boolean getIsClientCommand() {
        return true;
    }

    @Override
    public void execute(List<String> args, Map<String, Map<String, String>> objectBuilder, Output output) throws IncorrectCommandException, IOException {
        long id = environment.addRow(objectBuilder);
        Environment.Row newRow = environment.getRow(id);
        for (long rowId : environment) {
            if (environment.compareRowsByField(id, rowId) > 0) {
                environment.removeById(id);
                break;
            }
        }
    }
}
