package commander;

import environment.Environment;
import exceptions.IncorrectCommandException;
import data.Input;
import data.Output;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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
    public void execute(List<String> args, Map<String, Map<String, String>> objectBuilder, Output output) throws IncorrectCommandException, IOException {
        environment.clear();
    }

    @Override
    public boolean getIsClientCommand() {
        return true;
    }
}
