package commander;

import application.Application;
import exceptions.IncorrectCommandException;
import data.Input;
import data.Output;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ExitCommand implements Command {
    private final Application application;

    public ExitCommand(Application application) {
        this.application = application;
    }

    @Override
    public String getName() {
        return "exit";
    }

    @Override
    public String getDescription() {
        return "завершить программу (без сохранения в файл)";
    }

    @Override
    public void execute(List<String> args, Map<String, Map<String, String>> objectBuilder, Output output) throws IncorrectCommandException, IOException {
        application.exit();
    }

    @Override
    public boolean getIsClientCommand() {
        return false;
    }
}
