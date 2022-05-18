package exceptions;

public class IncorrectCommandException extends ApplicationException {
    public IncorrectCommandException() {
    }

    public IncorrectCommandException(String message) {
        super(message);
    }
}
