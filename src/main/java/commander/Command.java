package commander;

import exceptions.IncorrectCommandException;
import file_data.Errput;
import file_data.Input;
import file_data.Output;

import java.util.List;

public interface Command {
    String getName();
    String getDescription();

   void execute(List<String> args, Input input, Output output, Errput errput) throws IncorrectCommandException;
}
