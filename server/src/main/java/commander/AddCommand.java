package commander;

import environment.Environment;
import exceptions.IncorrectCommandException;
import data.Input;
import data.Output;

import java.io.IOException;
import java.util.*;

public class AddCommand implements Command {
    private final Environment environment;

    public AddCommand(Environment environment) {
        this.environment = environment;
    }

    @Override
    public String getName() {
        return "add";
    }

    @Override
    public String getDescription() {
        return "добавить новый элемент в коллекцию";
    }

    @Override
    public boolean getIsClientCommand() {
        return true;
    }

    @Override
    public void execute(List<String> args, Map<String, Map<String, String>> objectBuilder, Output output) throws IncorrectCommandException, IOException {
        environment.addRow(objectBuilder);
    }
}
