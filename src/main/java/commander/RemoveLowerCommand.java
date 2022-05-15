package commander;

import application.Environment;
import exceptions.IncorrectCommandException;
import file_data.Input;
import file_data.Output;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RemoveLowerCommand implements CreatingCommand {
    private final Environment environment;

    public RemoveLowerCommand(Environment environment) {
        this.environment = environment;
    }

    @Override
    public String getName() {
        return "remove_lower";
    }

    @Override
    public String getDescription() {
        return "удалить из коллекции все элементы, меньшие, чем заданный";
    }

    @Override
    public void execute(List<String> args, Input input, Output output) throws IncorrectCommandException, IOException {
        long id = environment.addRow(buildBuilder(environment, input, output));
        Set<Long> toDelete = new HashSet<>();
        for (long rowId : environment) {
            if (environment.compareRowsByField(rowId, id) < 0) {
                toDelete.add(rowId);
            }
        }
        for (long rowId : toDelete) {
            environment.removeById(rowId);
        }
        environment.removeById(id);
    }
}
