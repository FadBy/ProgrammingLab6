package commander;

import application.Environment;
import exceptions.IncorrectCommandException;
import file_data.Errput;
import file_data.Input;
import file_data.Output;

import java.util.List;

public class AddIfMinCommand implements Command {
    public AddIfMinCommand() {
    }

    @Override
    public String getName() {
        return "add_if_min";
    }

    @Override
    public String getDescription() {
        return "завершить программу (без сохранения в файл)";
    }

    @Override
    public void execute(List<String> args, Input input, Output output, Errput errput) throws IncorrectCommandException {

    }
}
