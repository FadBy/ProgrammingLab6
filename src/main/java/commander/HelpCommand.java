package commander;

import file_data.Input;
import file_data.Output;

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
    public void execute(List<String> args, Input input, Output output) {
        for (Map.Entry<String, String> command : commander.getCommandsInfo().entrySet()) {
            output.printResult(command.getKey() + ": " + command.getValue());
        }
    }
}
