package commander;

import exceptions.IncorrectCommandException;
import data.Input;
import data.Output;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HistoryCommand implements Command {
    private final CommandHistory history;

    public HistoryCommand(CommandHistory history) {
        this.history = history;
    }

    @Override
    public String getName() {
        return "history";
    }

    @Override
    public String getDescription() {
        return "вывести последние 15 команд (без их аргументов)";
    }

    @Override
    public void execute(List<String> args, Map<String, Map<String, String>> objectBuilder, Output output) throws IncorrectCommandException, IOException {
        output.printResult(String.join("\n", history.getHistory(15)));
    }

    @Override
    public boolean getIsClientCommand() {
        return true;
    }
}
