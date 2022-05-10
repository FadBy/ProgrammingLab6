package commander;

import application.Environment;
import file_data.Errput;
import file_data.Input;
import file_data.Output;

import java.util.List;

public class ClearCommand implements Command {
    private final Environment environment;

    public ClearCommand(Environment environment) {
        this.environment = environment;
    }
    @Override
    public String getName() {
        return "clear";
    }

    @Override
    public String getDescription() {
        return "очистить коллекцию";
    }

    @Override
    public void execute(List<String> args, Input input, Output output, Errput errput) {
        environment.clear();
    }
}
