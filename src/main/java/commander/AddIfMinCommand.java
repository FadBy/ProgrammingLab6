package commander;

import application.Environment;
import exceptions.IncorrectCommandException;
import file_data.Input;
import file_data.Output;

import java.io.IOException;
import java.util.List;

public class AddIfMinCommand implements CreatingCommand {
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
    public void execute(List<String> args, Input input, Output output) throws IncorrectCommandException, IOException {
        Environment.Row.Builder builder = buildBuilder(environment, input, output);
        long id = environment.addRow(builder);
        Environment.Row newRow = environment.getRow(id);
        for (long rowId : environment) {
            if (environment.compareRowsByField(id, rowId) > 0) {
                environment.removeById(id);
                break;
            }
        }
    }
}
