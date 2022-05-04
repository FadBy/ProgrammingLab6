package product_collection;

import java.util.List;
import java.util.Optional;

public class Field {
    public final String name;
    private final List<UpgradePredicate> predicates;
    public final String description;
    public static final String messageIfCorrect = "Yes";

    public Field(String name, List<UpgradePredicate> predicates) {
        this(name, predicates, "");
    }

    public Field(String name, List<UpgradePredicate> predicates, String description) {
        this.name = name;
        this.predicates = predicates;
        this.description = description;
    }

    public String test(String value, String name) {
        Optional<UpgradePredicate> pred = predicates.stream().filter(x -> !x.test(value)).findFirst();
        if (pred.isEmpty()) {
            return messageIfCorrect;
        }
        return name + " - " + pred.get().message;
    }

}
