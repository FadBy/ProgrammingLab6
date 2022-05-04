package application;

import commander.Command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CommandHistory {
    private final List<String> banList = new ArrayList<>(Arrays.asList("help", "history"));
    public final List<String> history = new ArrayList<>();

    public void addCommand(String command) {
        if (!banList.contains(command)) {
            history.add(command);
        }
    }

    public List<String> getHistory(int c) {
        return history.stream().limit(c).collect(Collectors.toList());
    }
}
