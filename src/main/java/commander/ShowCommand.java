package commander;

import application.Environment;
import file_data.Errput;
import file_data.Input;
import file_data.Output;

import java.util.List;

public class ShowCommand extends Command {
    private final Environment environment;

    public ShowCommand(Environment environment) {
        this.environment = environment;
    }

    @Override
    public String getName() {
        return "show";
    }

    @Override
    public void execute(List<String> args, Input input, Output output, Errput errput) {

    }
}
