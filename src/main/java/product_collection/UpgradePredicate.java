package product_collection;

import java.util.function.Predicate;

public class UpgradePredicate {
    public final String message;
    public final Predicate<String> predicate;

    public UpgradePredicate(String message, Predicate<String> predicate){
        this.message = message;
        this.predicate = predicate;
    }

    public boolean test(String value) {
        return predicate.test(value);
    }
}
