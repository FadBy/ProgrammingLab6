package commander;

import environment.Environment;
import exceptions.IncorrectCommandException;
import file_data.JsonData;
import data.Input;
import data.Output;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SaveCommand implements Command {
    private final JsonData jsonData;
    private final Environment environment;

    public SaveCommand(JsonData jsonData, Environment environment) {
        this.jsonData = jsonData;
        this.environment = environment;
    }
    @Override
    public String getName() {
        return "save";
    }

    @Override
    public String getDescription() {
        return "сохранить коллекцию в файл";
    }

    @Override
    public void execute(List<String> args, Map<String, Map<String, String>> objectBuilder, Output output) throws IncorrectCommandException, IOException {
        jsonData.save(environment.toJson());
    }

    @Override
    public boolean getIsClientCommand() {
        return false;
    }
}
