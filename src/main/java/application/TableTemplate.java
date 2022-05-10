package application;

import exceptions.ApplicationRuntimeException;

import java.util.*;
import java.util.stream.Collectors;

public class TableTemplate {
    private final Map<String, FieldTemplate> fields = new LinkedHashMap<>();
    public final String name;

    public TableTemplate(String name, List<FieldTemplate> fields) {
        this.name = name;
        this.fields.putAll(fields.stream().collect(Collectors.toMap(x -> x.name, x -> x)));
    }

    public FieldTemplate getField(String fieldName) {
        return fields.get(fieldName);
    }

    public String checkValidity(String fieldName, String value) {
        return fields.get(fieldName).checkValidity(value);
    }

    public String checkValidityAll(Map<String, String> values) {
        if (values.size() != fields.size()) {
            throw new ApplicationRuntimeException("Not all fields was filled");
        }
        for (String name : values.keySet()) {
            if (!fields.containsKey(name)) {
                throw new ApplicationRuntimeException("One of fields has wrong name");
            }
        }
        for (Map.Entry<String, String> i : values.entrySet()) {
            String message = checkValidity(i.getKey(), i.getValue());
            if (!message.isEmpty()) {
                return message;
            }
        }
        return "";
    }

    public List<FieldTemplate> getFields() {
        return new ArrayList<>(fields.values());
    }
}
