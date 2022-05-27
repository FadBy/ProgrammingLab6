package commander;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import environment.Environment;
import exceptions.IncorrectCommandException;
import data.Input;
import data.Output;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ShowCommand implements Command {
    private final Environment environment;

    public ShowCommand(Environment environment) {
        this.environment = environment;
    }

    @Override
    public String getName() {
        return "show";
    }

    @Override
    public String getDescription() {
        return "вывести в стандартный поток вывода все элементы коллекции в строковом представлении";
    }

    @Override
    public void execute(List<String> args, Map<String, Map<String, String>> objectBuilder, Output output) throws IncorrectCommandException, IOException {
        for (JsonElement json : environment.toJson()) {
            output.printResult(json.toString());
        }
    }

    @Override
    public boolean getIsClientCommand() {
        return true;
    }
}
