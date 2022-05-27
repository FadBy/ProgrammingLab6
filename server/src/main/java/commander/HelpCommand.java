package commander;


import exceptions.IncorrectCommandException;
import data.Input;
import data.Output;

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
    public void execute(List<String> args, Map<String, Map<String, String>> objectBuilder, Output output) throws IncorrectCommandException, IOException {
        for (Map.Entry<String, String> command : commander.getCommandsInfo().entrySet()) {
            if (commander.getIsClientCommand(command.getKey())) {
                output.printResult(command.getKey() + ": " + command.getValue());
            }
        }
    }

    @Override
    public boolean getIsClientCommand() {
        return true;
    }
}
