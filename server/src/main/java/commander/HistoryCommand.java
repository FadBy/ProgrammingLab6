package commander;

import exceptions.IncorrectCommandException;
import validation.Input;
import validation.Output;
import validation.TableTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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
    public void execute(List<String> args, Map<String, Map<String, String>> objectBuilder, Input input, Output output) throws IncorrectCommandException, IOException {
        for (String text : history.getHistory(15)) {
            output.printResult(text);
        }
    }
}
