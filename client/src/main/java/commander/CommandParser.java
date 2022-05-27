package commander;

import exceptions.IncorrectCommandException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandParser {
    public List<String> parseCommand(String text) throws IncorrectCommandException {
        if (text == null) {
            return Collections.emptyList();
        }
        return Arrays.asList(text.split("[\t ]"));
    }
}
