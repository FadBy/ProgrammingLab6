package validation;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class FieldTemplate {
    private final List<Label> labels;
    public final FieldType type;
    public final TableTemplate relativeTable;
    public final String name;
    public final static long relativeInitializeId = -1;
    public final boolean autoField;

    public FieldTemplate(String name, FieldType type, List<Label> labels, TableTemplate relativeTableName, boolean autoField) {
        this.name = name;
        this.type = type;
        this.labels = labels;
        this.relativeTable = relativeTableName;
        this.autoField = autoField;
    }

    public FieldTemplate(String name, FieldType type, List<Label> labels, boolean autoField)  {
        this(name, type, labels, null, autoField);
    }

    public FieldTemplate(String name, FieldType type, List<Label> labels, TableTemplate relativeTableName)  {
        this(name, type, labels, relativeTableName, false);
    }

    public FieldTemplate(String name, FieldType type, List<Label> labels) {
        this(name, type, labels, null, false);
    }

    public String checkValidity(String value) {
        String message = type.checkTypeMatch(value);
        if (!message.isEmpty()) {
            return message;
        }
        Optional<Label> label = labels.stream().filter(x -> !x.checkValidity(value).isEmpty()).findFirst();
        if (!label.isPresent()) {
            return "";
        }
        return label.get().checkValidity(value);
    }
}
