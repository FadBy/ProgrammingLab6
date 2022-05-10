package commander;

import exceptions.IncorrectCommandException;
import file_data.Errput;
import file_data.Input;
import file_data.Output;

import java.util.*;
import java.util.stream.Collectors;

public class Commander {
    private final Map<String, Command> commands = new LinkedHashMap<>();
    private final Input defaultInput;
    private final Output defaultOutput;
    private final Errput defaultErrput;

    public Commander(Input input, Output output, Errput errput) {
        defaultErrput = errput;
        defaultInput = input;
        defaultOutput = output;
    }

    public void setCommands(Collection<Command> commands) {
        this.commands.clear();
        this.commands.putAll(commands.stream().collect(Collectors.toMap(Command::getName, x -> x)));
    }

    public void executeCommand(String name, List<String> args) throws IncorrectCommandException {
        executeCommand(name, args, defaultInput, defaultOutput, defaultErrput);
    }

    public void executeCommand(String name, List<String> args, Input input) throws IncorrectCommandException {
        executeCommand(name, args, input, defaultOutput, defaultErrput);
    }

    public void executeCommand(String name, List<String> args, Output output) throws IncorrectCommandException {
        executeCommand(name, args, defaultInput, output, defaultErrput);
    }

    public void executeCommand(String name, List<String> args, Errput errput) throws IncorrectCommandException {
        executeCommand(name, args, defaultInput, defaultOutput, errput);
    }

    public void executeCommand(String name, List<String> args, Input input, Output output) throws IncorrectCommandException {
        executeCommand(name, args, input, output, defaultErrput);
    }

    public void executeCommand(String name, List<String> args, Input input, Errput errput) throws IncorrectCommandException {
        executeCommand(name, args, input, defaultOutput, errput);
    }

    public void executeCommand(String name, List<String> args, Output output, Errput errput) throws IncorrectCommandException {
        executeCommand(name, args, defaultInput, output, errput);
    }

    public void executeCommand(String name, List<String> args, Input input, Output output, Errput errput) throws IncorrectCommandException {
        if (!commands.containsKey(name)) {
            throw new IncorrectCommandException("Command " + name + " doesn't exist");
        }
        commands.get(name).execute(args, defaultInput, defaultOutput, defaultErrput);
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
