package commander;

import file_data.Input;
//import file_data.JsonData;
import file_data.Output;

import java.util.List;

public class SaveCommand implements Command {
//    private JsonData jsonData;

//    public SaveCommand(JsonData jsonData) {
//        this.jsonData = jsonData;
//    }

    @Override
    public String getName() {
        return "save";
    }

    @Override
    public String getDescription() {
        return "сохранить коллекцию в файл";
    }

    @Override
    public void execute(List<String> args, Input input, Output output) {
//        try {
////            jsonData.save();
//        } catch (IOException e) {
//            errput.printException(new ApplicationException("Something wrong with collection file"));
//        }
    }
}
