package commander;

import application.Environment;
import exceptions.IncorrectCommandException;
import file_data.Errput;
import file_data.Input;
import file_data.Output;

import java.util.List;

public class UpdateByIdCommand implements Command {

    public UpdateByIdCommand() {
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
    public void execute(List<String> args, Input input, Output output, Errput errput) throws IncorrectCommandException {

    }
}
