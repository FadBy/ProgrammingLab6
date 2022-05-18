package commander;

import environment.Environment;
import exceptions.IncorrectCommandException;
import validation.Input;
import validation.Output;
import validation.TableTemplate;

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
    public void execute(List<String> args, Map<String, Map<String, String>> objectBuilder, Input input, Output output) throws IncorrectCommandException, IOException {
        environment.addRow(objectBuilder);
    }
}
