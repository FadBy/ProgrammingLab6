package commander;


import exceptions.IncorrectCommandException;
import validation.Input;
import validation.Output;
import validation.TableTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class HelpCommand implements Command {
    private final Commander commander;
    public HelpCommand(Commander commander) {
        this.commander = commander;
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "вывести справку по доступным командам";
    }

    @Override
    public void execute(List<String> args, Map<String, Map<String, String>> objectBuilder, Input input, Output output) throws IncorrectCommandException, IOException {
        for (Map.Entry<String, String> command : commander.getCommandsInfo().entrySet()) {
            output.printResult(command.getKey() + ": " + command.getValue());
        }
    }
}
