package application;

import com.google.gson.JsonObject;
import exceptions.ApplicationRuntimeException;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

class Table implements Iterable<Long> {
    private final Map<Long, Row> rowsById = new LinkedHashMap<>();

    public void addRow(long id, Row row) {
        rowsById.put(id, row);
    }

    public List<String> getColumn(String fieldName) {
        return rowsById.values().stream().map(x -> x.getValue(fieldName)).collect(Collectors.toList());
    }

    public void updateRowsField(long id, String fieldToChange, String newValue) {
        rowsById.get(id).setValue(fieldToChange, newValue);
    }

    public void removeRow(long id) {
        rowsById.remove(id);
    }

    public long getRowIdByField(String fieldName, String valueName) {
        for (Map.Entry<Long, Row> entry : rowsById.entrySet()) {
            if (Objects.equals(entry.getValue().getValue(fieldName), valueName)) {
                return entry.getKey();
            }
        }
        return -1;
    }

    public boolean containsId(long id) {
        return rowsById.containsKey(id);
    }

    public void clear() {
        rowsById.clear();
    }

    public Row getRowById(long id) {
        return rowsById.get(id);
    }

    public long getIdByRow(Row row) {
        return rowsById.entrySet().stream().filter(x -> x.getValue().equals(row)).map(Map.Entry::getKey).findFirst().orElse((long) -1);
    }

    @Override
    public Iterator<Long> iterator() {
        return rowsById.keySet().iterator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Table longs = (Table) o;
        return Objects.equals(rowsById, longs.rowsById);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new Object[]{ rowsById });
    }

    static class Row {
        private final Map<String, String> fields;

        private Row(Map<String, String> fields) {
            this.fields = fields;
        }

        public void setValue(String fieldName, String value) {
            if (!fields.containsKey(fieldName)) {
                throw new ApplicationRuntimeException(fieldName + "isn't contained in this row");
            }
            fields.put(fieldName, value);
        }

        public String getValue(String fieldName) {
            return fields.get(fieldName);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Row row = (Row) o;
            return Objects.equals(fields, row.fields);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(new Object[]{ fields });
        }

        public JsonObject toJson() {
            JsonObject json = new JsonObject();
            fields.forEach(json::addProperty);
            return json;
        }

        public static class Builder implements Iterable<FieldTemplate> {
            private final Map<String, String> fields = new HashMap<>();
            private final TableTemplate template;

            public Builder(TableTemplate template) {
                this.template = template;
                for (FieldTemplate field : template.getFields()) {
                    if (field.relativeTable != null) {
                        fields.put(field.name, "-1");
                    } else if (field.fieldGeneration != null) {
                        fields.put(field.name, field.fieldGeneration.get());
                    }else {
                        fields.put(field.name, null);
                    }
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

            public Table.Row build() {
                String message = checkBuilderValidity();
                if (!message.isEmpty()) {
                    throw new ApplicationRuntimeException("Attempt to build invalid row");
                }
                for (String fieldName : fields.keySet()) {
                    FieldTemplate field = template.getField(fieldName);
                    if (field.fieldGeneration != null) {
                        setField(fieldName, field.fieldGeneration.get());
                    }
                }
                return new Table.Row(fields);
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
}
