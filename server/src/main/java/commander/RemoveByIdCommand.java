package commander;

import environment.Environment;
import exceptions.IncorrectCommandException;
import validation.Input;
import validation.Output;
import validation.TableTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class RemoveByIdCommand implements Command {
    private final Environment environment;

    public RemoveByIdCommand(Environment environment) {
        this.environment = environment;
    }

    @Override
    public String getName() {
        return "remove_by_id";
    }

    @Override
    public String getDescription() {
        return "удалить элемент из коллекции по его id";
    }

    @Override
    public void execute(List<String> args, Map<String, Map<String, String>> objectBuilder, Input input, Output output) throws IncorrectCommandException, IOException {
        if (args.size() == 0) {
            throw new IncorrectCommandException("remove_by_id must have id");
        }
        long id = Long.parseLong(args.get(0));
        long trueId = environment.findId(environment.rootTable, "id", Long.toString(id));
        if (trueId == -1) {
            throw new IncorrectCommandException(id + " wasn't found in collection");
        }
        environment.removeById(trueId);
    }
}
