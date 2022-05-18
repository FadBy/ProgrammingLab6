package exceptions;

public class IncorrectInputException extends ApplicationException {
    public IncorrectInputException() {
    }

    public IncorrectInputException(String message) {
        super(message);
    }
}
