package commander;

import exceptions.IncorrectCommandException;
import data.Input;
import data.Output;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CommandExecuter {
    private final Map<String, Command> commands = new LinkedHashMap<>();

    public void setCommands(Map<String, Command> commands) {
        this.commands.clear();
        this.commands.putAll(commands);
    }

    public String execute(List<String> args, Input input, Output output) throws IncorrectCommandException {
        if (args.size() == 0) {
            throw new IncorrectCommandException("You left command entry empty");
        }
        String name = args.get(0);
        if (!commands.containsKey(name)) {
            throw new IncorrectCommandException("There is no such command - " + name);
        }
        return commands.get(name).execute(args, input, output);
    }
}
