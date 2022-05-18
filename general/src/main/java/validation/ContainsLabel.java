package validation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.BiPredicate;

public class ContainsLabel implements Label {
    private final String message;
    private final BiPredicate<String, Collection<String>> predicate;
    private Collection<String> values = new ArrayList<>();

    public ContainsLabel(String message, BiPredicate<String, Collection<String>> predicate) {
        this.message = message;
        this.predicate = predicate;
    }

    public void setCollection(Collection<String> values) {
        this.values = values;
    }

    @Override
    public String checkValidity(String value) {
        if (predicate.test(value, values)) {
            return "";
        }
        return message;
    }
}
