package application;

import java.util.function.Predicate;

public class MonoLabel implements Label {
    private final String message;
    private final Predicate<String> predicate;

    public MonoLabel(String message, Predicate<String> predicate) {
        this.message = message;
        this.predicate = predicate;
    }

    public String checkValidity(String value) {
        if (predicate.test(value)) {
            return "";
        }
        return message;

    }
}
