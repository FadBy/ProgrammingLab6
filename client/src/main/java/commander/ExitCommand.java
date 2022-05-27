package commander;

import application.Application;
import exceptions.IncorrectCommandException;
import data.Input;
import data.Output;

import java.util.List;

public class ExitCommand implements Command {
    private final Application application;

    public ExitCommand(Application application) {
        this.application = application;
    }

    @Override
    public String execute(List<String> args, Input input, Output output) throws IncorrectCommandException {
        application.exit();
        return "";
    }
}
