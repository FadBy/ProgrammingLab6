package commander;

import exceptions.ApplicationException;
import file_data.Errput;
import file_data.Input;
import file_data.JsonData;
import file_data.Output;

import java.io.IOException;
import java.util.List;

public class SaveCommand implements Command {
    private JsonData jsonData;

    public SaveCommand(JsonData jsonData) {
        this.jsonData = jsonData;
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
    public void execute(List<String> args, Input input, Output output, Errput errput) {
        try {
            jsonData.save();
        } catch (IOException e) {
            errput.printException(new ApplicationException("Something wrong with collection file"));
        }
    }
}
