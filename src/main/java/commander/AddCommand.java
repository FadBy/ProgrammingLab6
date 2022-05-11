package commander;

import application.Environment;
import exceptions.IncorrectCommandException;
import file_data.Input;
import file_data.Output;

import java.io.IOException;
import java.util.*;

public class AddCommand implements CreatingCommand {
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
    public void execute(List<String> args, Input input, Output output) throws IOException, IncorrectCommandException {
        environment.addRow(buildBuilder(environment, input, output));
    }
}
