package commander;

import file_data.Errput;
import file_data.Input;
import file_data.Output;

import java.io.IOException;
import java.util.List;

public abstract class Command {
    public abstract String getName();

    public abstract void execute(List<String> args, Input input, Output output, Errput errput);
}
