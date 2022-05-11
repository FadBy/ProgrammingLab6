package commander;

import application.Environment;
import exceptions.IncorrectCommandException;
import file_data.Input;
import file_data.Output;

import java.io.IOException;
import java.util.Iterator;

public interface CreatingCommand extends Command {
    default Environment.Row.Builder buildBuilder(Environment environment, Input input, Output output) throws IOException, IncorrectCommandException {
        Environment.Row.Builder builder = new Environment.Row.Builder(environment.getTableTemplate(environment.rootTable));
        for (Iterator<String> iter = builder.iterator(); iter.hasNext(); ) {
            String field = iter.next();
            String textInput = output.makeRequest(field + ": ", input);
            String message = builder.checkFieldValidity(field, textInput);
            if (!input.isContinuing() && !message.isEmpty()) {
                throw new IncorrectCommandException("incorrect input - " + message);
            }
            while (!message.isEmpty()) {

                output.printResult(message);
                textInput = output.makeRequest(field + ": ", input);
                message = builder.checkFieldValidity(field, textInput);
            }
            builder.setField(field, textInput);
        }
        return builder;
    }
}
