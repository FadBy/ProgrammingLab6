package application;

import java.util.List;
import java.util.function.BiPredicate;

public class BiLabel implements Label {
    private final String message;
    private final BiPredicate<String, List<String>> predicate;
    private final Environment environment;
    private final String tableName;
    private final String fieldName;

    public BiLabel(String message, BiPredicate<String, List<String>> predicate, Environment environment, String tableName, String fieldName) {
        this.message = message;
        this.predicate = predicate;
        this.environment = environment;
        this.tableName = tableName;
        this.fieldName = fieldName;
    }

    @Override
    public String checkValidity(String value) {
        if (predicate.test(value, environment.getColumnTable(tableName, fieldName))) {
            return "";
        }
        return message;

    }
}
