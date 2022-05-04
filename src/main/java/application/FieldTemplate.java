package application;

import exceptions.ApplicationRuntimeException;

import java.util.List;
import java.util.Optional;

public class FieldTemplate {
    public final String name;
    private final List<Label> labels;
    public final FieldType type;
    public final TableTemplate relativeTable;

    public FieldTemplate(String name, FieldType type, List<Label> labels, TableTemplate relativeTable) {
        this.type = type;
        this.labels = labels;
        this.name = name;
        this.relativeTable = relativeTable;
    }

    public FieldTemplate(String name, FieldType type, List<Label> labels) {
        this(name, type, labels, null);
    }

    public String checkValidity(String value) {
        type.checkTypeMatch(value);
        Optional<Label> label = labels.stream().filter(x -> !x.checkValidity(value).isEmpty()).findFirst();
        if (label.isEmpty()) {
            return "";
        }
        return label.get().checkValidity(value);
    }
}
