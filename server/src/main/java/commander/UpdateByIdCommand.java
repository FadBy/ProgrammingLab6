package commander;

import environment.Environment;
import exceptions.IncorrectCommandException;
import data.Input;
import data.Output;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class UpdateByIdCommand implements Command {
    private final Environment environment;

    public UpdateByIdCommand(Environment environment) {
        this.environment = environment;
    }

    @Override
    public String getName() {
        return "update_by_id";
    }

    @Override
    public String getDescription() {
        return "обновить значение элемента коллекции, id которого равен заданному";
    }

    @Override
    public void execute(List<String> args, Map<String, Map<String, String>> objectBuilder, Output output) throws IncorrectCommandException, IOException {
        if (args.size() == 1) {
            throw new IncorrectCommandException(getName() + "must have id argument");
        }
        long id;
        try {
            id = Long.parseLong(args.get(1));
        } catch (NumberFormatException e) {
            throw new IncorrectCommandException("id must be long");
        }
        environment.updateRow(environment.findId(environment.rootTable, "id", Long.toString(id)), objectBuilder);
    }

    @Override
    public boolean getIsClientCommand() {
        return true;
    }
}
