package application;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class FieldTemplate {
    private final List<Label> labels;
    public final FieldType type;
    public final TableTemplate relativeTable;
    public final String name;
    public final static long relativeInitializeId = -1;
    public final Supplier<String> fieldGeneration;

    public FieldTemplate(String name, FieldType type, List<Label> labels, TableTemplate relativeTableName, Supplier<String> fieldGeneration) {
        this.name = name;
        this.type = type;
        this.labels = labels;
        this.relativeTable = relativeTableName;
        this.fieldGeneration = fieldGeneration;
    }

    public FieldTemplate(String name, FieldType type, List<Label> labels, Supplier<String> fieldGeneration)  {
        this(name, type, labels, null, fieldGeneration);
    }

    public FieldTemplate(String name, FieldType type, List<Label> labels, TableTemplate relativeTableName)  {
        this(name, type, labels, relativeTableName, null);
    }

    public FieldTemplate(String name, FieldType type, List<Label> labels) {
        this(name, type, labels, null, null);
    }

    public String checkValidity(String value) {
        String message = type.checkTypeMatch(value);
        if (!message.isEmpty()) {
            return message;
        }
        Optional<Label> label = labels.stream().filter(x -> !x.checkValidity(value).isEmpty()).findFirst();
        if (label.isEmpty()) {
            return "";
        }
        return label.get().checkValidity(value);
    }
}
