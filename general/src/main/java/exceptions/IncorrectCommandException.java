package exceptions;

public class IncorrectCommandException extends ApplicationException {
    public IncorrectCommandException(String message) {
        super("Incorrect command: " + message);
    }
}
