package commander;

import exceptions.IncorrectCommandException;
import file_data.Input;
import file_data.Output;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Commander {
    private final Map<String, Command> commands = new LinkedHashMap<>();
    private final Input defaultInput;
    private final Output defaultOutput;

    public Commander(Input input, Output output) {
        defaultInput = input;
        defaultOutput = output;
    }

    public void setCommands(Collection<Command> commands) {
        this.commands.clear();
        this.commands.putAll(commands.stream().collect(Collectors.toMap(Command::getName, x -> x)));
    }

    public void executeCommand(List<String> args) throws IncorrectCommandException, IOException {
        executeCommand(args, defaultInput, defaultOutput);
    }

    public void executeCommand(List<String> args, Input input) throws IncorrectCommandException, IOException {
        executeCommand(args, input, defaultOutput);
    }

    public void executeCommand(List<String> args, Output output) throws IncorrectCommandException, IOException {
        executeCommand(args, defaultInput, output);
    }

    public void executeCommand(List<String> args, Input input, Output output) throws IncorrectCommandException, IOException {
        if (args.size() == 0) {
            throw new IncorrectCommandException("Any command wasn't found");
        }
        String name = args.get(0);
        if (!commands.containsKey(name)) {
            throw new IncorrectCommandException("Command " + name + " doesn't exist");
        }
        commands.get(name).execute(args, input, output);
    }

    public List<String> parseCommand(String commandText) throws IncorrectCommandException {
        if (commandText == null) {
            throw new IncorrectCommandException("Input is empty");
        }
        return Arrays.asList(commandText.trim().split("[\t ]+"));
    }

    public Map<String, String> getCommandsInfo() {
        return commands.values().stream().collect(Collectors.toMap(Command::getName, Command::getDescription, (x, y) -> y, LinkedHashMap::new));
    }
}
