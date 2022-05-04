package application;

import exceptions.ApplicationRuntimeException;

import java.util.*;

class Table {
    private final TreeSet<Row> rows = new TreeSet<>(Comparator.comparing(x -> x.getValue("id")));
    private final HashMap<Long, Row> rowsById = new HashMap<>();
    private final Map<String, Column> columns = new HashMap<>();
    public final TableTemplate template;

    public Table(TableTemplate template) {
        this.template = template;
        for (FieldTemplate temp : template.getFields()) {
            columns.put(temp.name, new Column(temp));
        }
    }

    public void addRow(long id, Row row) {
        rows.add(row);
        rowsById.put(id, row);
    }

    public String getName() {
        return template.name;
    }

    public List<String> getColumn(String fieldName) {
        return List.copyOf(columns.get(fieldName).values);
    }

    public class Row {
        public final Map<String, String> fields;

        public Row(Map<String, String> fields) {
            this.fields = fields;
        }

        public String setValue(String fieldName, String value) {
            String message = template.checkValidity(fieldName, value);
            if (message.isEmpty()) {
                fields.put(fieldName, value);
            }
            return message;
        }

        public String getValue(String fieldName) {
            return fields.get(fieldName);
        }
    }

    private class Column {
        private final FieldTemplate template;
        private final List<String> values = new ArrayList<>();

        public Column(FieldTemplate template) {
            this.template = template;
        }

        public void addValue(String value) {
            String message = template.checkValidity(value);
            if (!message.isEmpty()) {
                throw new ApplicationRuntimeException(message);
            }
            values.add(value);
        }
    }
}
