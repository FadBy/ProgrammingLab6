package exceptions;

public class IncorrectInputException extends ApplicationException {
    public IncorrectInputException(String message) {
        super("Incorrect input: " + message);
    }
}
