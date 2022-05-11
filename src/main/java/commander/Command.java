package commander;

import exceptions.IncorrectCommandException;
import file_data.Input;
import file_data.Output;

import java.io.IOException;
import java.util.List;

public interface Command {
    String getName();
    String getDescription();

   void execute(List<String> args, Input input, Output output) throws IncorrectCommandException, IOException;
}
