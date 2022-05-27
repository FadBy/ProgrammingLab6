package template;

import java.util.function.Predicate;

public class MonoLabel implements Label {
    private final String message;
    private final Predicate<String> predicate;
    private final boolean isServerDependent;

    public MonoLabel(String message, Predicate<String> predicate) {
        this(message, predicate, false);
    }

    public MonoLabel(String message, Predicate<String> predicate, boolean isServerDependent) {
        this.message = message;
        this.predicate = predicate;
        this.isServerDependent = isServerDependent;
    }

    public String checkValidity(String value) {
        if (predicate.test(value)) {
            return "";
        }
        return message;

    }
}
