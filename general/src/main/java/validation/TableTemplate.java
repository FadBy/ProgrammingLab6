package validation;

import com.google.gson.JsonObject;
import exceptions.ApplicationRuntimeException;
import exceptions.IncorrectJsonException;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TableTemplate {
    private final Map<String, FieldTemplate> fields = new LinkedHashMap<>();
    public final String name;
    public final String comparisonField;

    public TableTemplate(String name, List<FieldTemplate> fields, String comparisonField) {
        this.comparisonField = comparisonField;
        this.name = name;
        this.fields.putAll(fields.stream().collect(Collectors.toMap(x -> x.name, x -> x)));
    }

    public FieldTemplate getField(String fieldName) {
        if (!fields.containsKey(fieldName)) {
            throw new ApplicationRuntimeException(fieldName + " isn't contained in " + name + "TableTemplate");
        }
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

    public boolean contains(String field) {
        return fields.containsKey(field);
    }

    public List<FieldTemplate> getFields() {
        return new ArrayList<>(fields.values());
    }

    public static class Builder implements Iterable<FieldTemplate> {
        private final Map<String, String> fields = new HashMap<>();
        private final TableTemplate template;

        public Builder(TableTemplate template) {
            this.template = template;
            for (FieldTemplate field : template.getFields()) {
                if (field.relativeTable != null) {
                    fields.put(field.name, "-1");
                } else if (field.autoField && field.type != FieldType.DateTime) {
                    fields.put(field.name, "1");
                } else if (field.autoField) {
                    fields.put(field.name, LocalDateTime.now().toString());
                } else {
                    fields.put(field.name, null);
                }
            }
        }

        public Builder(TableTemplate template, JsonObject json) throws IncorrectJsonException {
            this(template);
            for (String el : json.keySet()) {
                if (!template.contains(el)) {
                    throw new IncorrectJsonException(el + "isn't contained in " + template.name);
                }
                if (template.getField(el).relativeTable != null) {
                    continue;
                }
                if (!json.get(el).isJsonPrimitive()) {
                    throw new IncorrectJsonException(el + "must be primitive");
                }
                String message = checkFieldValidity(el, json.get(el).getAsJsonPrimitive().getAsString());
                if (!message.isEmpty()) {
                    throw new IncorrectJsonException(el + ": message");
                }
                setField(el, json.getAsJsonPrimitive(el).getAsString());
            }
        }

        public String getTableName() {
            return template.name;
        }

        public String checkFieldValidity(String fieldName, String value) {
            return template.checkValidity(fieldName, value);
        }

        public void setField(String fieldName, String value) {
            if (!checkFieldValidity(fieldName, value).isEmpty()) {
                throw new ApplicationRuntimeException("Attempt to set invalid field value");
            }
            fields.put(fieldName, value);
        }

        public String checkBuilderValidity() {
            return template.checkValidityAll(fields);
        }

        public Map<String, String> build() {
            String message = checkBuilderValidity();
            if (!message.isEmpty()) {
                throw new ApplicationRuntimeException("Attempt to build invalid row");
            }
            return fields;
        }

        public String getField(String fieldName) {
            return fields.get(fieldName);
        }

        @Override
        public Iterator<FieldTemplate> iterator() {
            return template.getFields().iterator();
        }
    }
}
