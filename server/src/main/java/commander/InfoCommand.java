package commander;

import environment.Environment;
import exceptions.IncorrectCommandException;
import data.Input;
import data.Output;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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
    public void execute(List<String> args, Map<String, Map<String, String>> objectBuilder, Output output) throws IncorrectCommandException, IOException {
        output.printResult(environment.getInfo());
    }

    @Override
    public boolean getIsClientCommand() {
        return true;
    }
}
