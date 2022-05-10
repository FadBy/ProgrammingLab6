package commander;

import application.Environment;
import exceptions.IncorrectCommandException;
import exceptions.IncorrectInputException;
import file_data.Errput;
import file_data.Input;
import file_data.Output;

import java.util.List;

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
    public void execute(List<String> args, Input input, Output output, Errput errput) {
        if (args.size() == 0) {
            errput.printException(new IncorrectCommandException("remove_by_id must have id"));
            return;
        }
        long id = Long.parseLong(args.get(0));
        long trueId = environment.findId(environment.rootTable, "id", Long.toString(id));
        if (trueId == -1) {
            errput.printException(new IncorrectInputException(id + " wasn't found in collection"));
            return;
        }
        environment.removeById(trueId);
    }
}
