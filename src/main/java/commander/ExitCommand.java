package commander;

import application.Application;
import file_data.Input;
import file_data.Output;

import java.util.List;

public class ExitCommand implements Command {
    private final Application application;

    public ExitCommand(Application application) {
        this.application = application;
    }

    @Override
    public String getName() {
        return "exit";
    }

    @Override
    public String getDescription() {
        return "завершить программу (без сохранения в файл)";
    }

    @Override
    public void execute(List<String> args, Input input, Output output) {
        application.exit();
    }
}
