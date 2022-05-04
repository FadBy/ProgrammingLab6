package application;

import exceptions.ApplicationRuntimeException;

import java.util.*;
import java.util.stream.Collectors;

public class TableTemplate {
    private final Map<String, FieldTemplate> fields;
    public final String name;

    public TableTemplate(String name, List<FieldTemplate> fields) {
        this.name = name;
        this.fields = fields.stream().collect(Collectors.toMap(x -> x.name, x -> x));
//        relatives = fields.stream().map(x -> x.relativeTable).filter(Objects::nonNull).collect(Collectors.toList());
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

    public class RowBuilder {
        private final Map<String, String> fields = new HashMap<>();

        public RowBuilder() {
            for (FieldTemplate temp : TableTemplate.this.getFields()) {
                fields.put(temp.name, null);
            }
        }

        public TableTemplate getTableTemplate() {
            return TableTemplate.this;
        }

        public String setField(String fieldName, String value) {
            String message = checkValidity(fieldName, value);
            if (message.isEmpty()) {
                fields.put(fieldName, value);
            }
            return message;
        }

        Table.Row build(Environment environment) {
            Table table = environment.getTable(TableTemplate.this);
            String message = checkValidityAll(fields);
            if (!message.isEmpty()) {
                return null;
            }
            return table.new Row(fields);
        }
    }
}
