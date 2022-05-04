package commander;

import exceptions.IncorrectCommandException;
import file_data.Errput;
import file_data.Input;
import file_data.Output;

import java.util.*;
import java.util.stream.Collectors;

public class Commander {
    private final Map<String, Command> commands = new HashMap<>();
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
        if (!commands.containsKey(name)) {
            throw new IncorrectCommandException("Command " + name + " doesn't exist");
        }
        commands.get(name).execute(args, defaultInput, defaultOutput, defaultErrput);
    }

    public List<String> parseCommand(String commandText) throws IncorrectCommandException{
        List<String> result = Arrays.asList(commandText.trim().split("[\t ]"));
        if (result.size() == 0) {
            throw new IncorrectCommandException("Input is empty");
        }
        return result;
    }
}
