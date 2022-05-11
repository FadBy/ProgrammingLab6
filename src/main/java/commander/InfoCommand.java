package commander;

import application.Environment;
import file_data.Input;
import file_data.Output;

import java.util.List;

public class InfoCommand implements Command {
    private final Environment environment;

    public InfoCommand(Environment environment) {
        this.environment = environment;
    }

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getDescription() {
        return "вывести в стандартный поток вывода информацию о коллекции";
    }

    @Override
    public void execute(List<String> args, Input input, Output output) {
        output.printResult(environment.getInfo());
    }
}
