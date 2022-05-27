package exceptions;

public class IncorrectJsonException extends Exception {
    public IncorrectJsonException(String message) {
        super("Incorrect json: " + message);
    }
}
