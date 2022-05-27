package commander;

import exceptions.IncorrectCommandException;
import data.Input;
import data.Output;

import java.util.List;

public interface Command {

    String execute(List<String> args, Input input, Output output) throws IncorrectCommandException;
}
