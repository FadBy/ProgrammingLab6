package commander;

import exceptions.IncorrectCommandException;
import file_data.FileData;
import data.Input;
import data.Output;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ExecuteScriptCommand implements Command {
    private final Commander commander;
    private final FileData fileData;
    private final Set<String> executeScripts = new HashSet<>();

    public ExecuteScriptCommand(Commander commander, FileData fileData) {
        this.commander = commander;
        this.fileData = fileData;
    }

    @Override
    public String getName() {
        return "execute_script";
    }

    @Override
    public String getDescription() {
        return "считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.";
    }

    @Override
    public void execute(List<String> args, Map<String, Map<String, String>> objectBuilder, Output output) throws IncorrectCommandException, IOException {
        if (args.size() == 1) {
            throw new IncorrectCommandException(getName() + " takes file name as an argument");
        }
        if (executeScripts.contains(args.get(1))) {
            throw new IncorrectCommandException("Recursive call");
        }
        executeScripts.add(args.get(1));
        try {
            fileData.changePath(Paths.get(args.get(1)));

//            while (fileData.hasNextLine()) {
//                commander.executeCommand(commander.parseCommand(fileData.nextLine()), (Input) fileData, new Output() {
//                    @Override
//                    public void printResult(String line) {
//                        output.printResult(line);
//                    }
//
//                    @Override
//                    public String makeRequest(String line, Input input) throws IOException {
//                        return input.nextLine();
//                    }
//                });
//            }
        } catch (IOException e) {
            throw new IncorrectCommandException("Something goes wrong with file");
        }
    }

    @Override
    public boolean getIsClientCommand() {
        return true;
    }
}
