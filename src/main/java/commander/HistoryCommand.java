package commander;

import application.CommandHistory;
import exceptions.IncorrectCommandException;
import file_data.Input;
import file_data.Output;

import java.util.List;

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
    public void execute(List<String> args, Input input, Output output) throws IncorrectCommandException {
        for (String text : history.getHistory(15)) {
            output.printResult(text);
        }
    }
}
