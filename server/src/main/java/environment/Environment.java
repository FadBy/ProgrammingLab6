package environment;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import exceptions.ApplicationRuntimeException;
import validation.FieldTemplate;
import validation.FieldType;
import validation.TableTemplate;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Environment implements Iterable<Long> {
    private final Map<TableTemplate, Table> tables = new LinkedHashMap<>();
    private final Map<String, TableTemplate> templates = new LinkedHashMap<>();
    private final Map<Long, Row> rows = new TreeMap<>();

    private long nextId = 0;
    public String rootTable;
    private LocalDateTime creationDate = LocalDateTime.now();

    public int getSize() {
        return rows.size();
    }

    private long getNextId() {
        return nextId++;
    }

    public void setTables(LocalDateTime creationDate, String rootTable, List<TableTemplate> templates) {
        this.creationDate = creationDate;
        this.tables.clear();
        this.tables.putAll(templates.stream().collect(Collectors.toMap(x -> x, x -> new Table())));
        this.templates.clear();
        for (TableTemplate template : templates) {
            if (this.templates.containsKey(template.name)) {
                throw new ApplicationRuntimeException("environment.Table with this name already exist");
            }
            this.templates.put(template.name, template);
        }
        this.rootTable = rootTable;
    }

    public List<String> getColumnTable(String tableName, String fieldName) {
        return getTable(tableName).getColumn(fieldName);
    }

    private void addRow(long id, Map<String, Map<String, String>> builders) {
        Map<String, Table.Row> envRows = new HashMap<>();
        for (Map.Entry<String, Map<String, String>> tableBuilderEnt : builders.entrySet()) {
            String tableName = tableBuilderEnt.getKey();
            Map<String, String> tableBuilder = tableBuilderEnt.getValue();
            for (Map.Entry<String, String> fieldEnt : tableBuilder.entrySet()) {
                FieldTemplate field = getTableTemplate(tableName).getField(fieldEnt.getKey());
                if (field.relativeTable != null) {
                    tableBuilder.put(field.name, Long.toString(id));
                }
                if (field.autoField) {
                    if (field.type == FieldType.DateTime) {
                        tableBuilder.put(field.name, LocalDateTime.now().toString());
                    } else {
                        tableBuilder.put(field.name, getAutoIncrement(tableName, field.name));
                    }
                }
            }
//            if (!tableBuilder.checkBuilderValidity().isEmpty()) {
//                throw new ApplicationRuntimeException("Attempt to add invalid row");
//            }
            Table.Row row = new Table.Row(tableBuilder);
            envRows.put(tableName, row);
            getTable(tableName).addRow(id, row);
        }
        rows.put(id, new Row(getTableTemplate(rootTable), envRows));
    }

    public int compareRowsByField(long rowId1, long rowId2, String fieldChain) {
        FieldTemplate field = rows.get(rowId1).getFieldTemplate(fieldChain);
        if (field.relativeTable != null) {
            List<String> chain = Row.parseToList(fieldChain);
            return compareRowsByField(rowId1, rowId2, chain + "." + field.relativeTable.comparisonField);
        } else {
            return field.type.getComparator().compare(rows.get(rowId1).getValue(fieldChain), rows.get(rowId2).getValue(fieldChain));
        }
    }

    public int compareRowsByField(long rowId1, long rowId2) {
        return compareRowsByField(rowId1, rowId2, templates.get(rootTable).comparisonField);
    }

    private Table getTable(String tableName) {
        return tables.get(templates.get(tableName));
    }

    public TableTemplate getTableTemplate(String tableName) {
        return templates.get(tableName);
    }

    public Row getRow(long id) {
        return rows.get(id);
    }

    public long addRow(Map<String, Map<String, String>> builder) {
        long id = getNextId();
        addRow(id, builder);
        return id;
    }

    public void removeById(long id) {
        if (!getTable(rootTable).containsId(id)) {
            throw new ApplicationRuntimeException(id + "wasn't found in environment");
        }
        rows.remove(id);
        for (Table table : tables.values()) {
            table.removeRow(id);
        }
    }

    public long findId(String tableName, String fieldToFind, String value) {
        return getTable(tableName).getRowIdByField(fieldToFind, value);
    }

    public void updateRow(long id, Map<String, Map<String, String>> builder) {
        removeById(id);
        addRow(id, builder);
    }

    public void clear() {
        for (Table table : tables.values()) {
            table.clear();
        }
        rows.clear();
    }

    public String getInfo() {
        return "Тип: HashSet\n" +
                "Дата инициализации: " + creationDate.toString() + "\n" +
                "Колво элементов: " + getSize();
    }

    public String getAutoIncrement(String tableName, String fieldName) {
        FieldTemplate template = templates.get(tableName).getField(fieldName);
        Function<String, Number> parser;
        if (template.type == FieldType.Long) {
            parser = Long::parseLong;
        } else if (template.type == FieldType.Integer) {
            parser = Integer::parseInt;
        } else {
            throw new ApplicationRuntimeException(fieldName + " isn't integer or long");
        }
        return Long.toString(getTable(tableName).getColumn(fieldName).stream().map(parser).
                max(Comparator.comparing(Number::longValue)).orElse(parser.apply("0")).longValue() + 1);
    }

    public JsonArray toJson() {
        return toJson(rows.keySet());
    }

    public JsonArray toJson(Collection<Long> rowIds) {
        JsonArray json = new JsonArray();
        for (Long id : rowIds) {
            json.add(rows.get(id).toJson());
        }
        return json;
    }

    public JsonObject toJson(long id) {
        return rows.get(id).toJson();
    }

    @Override
    public Iterator<Long> iterator() {
        return rows.keySet().iterator();
    }

    public static class Row {
        private final Entry rootEntry;
        private final Map<String, Table.Row> rows;

        private Row(TableTemplate rootTable,  Map<String, Table.Row> rows) {
            this.rows = rows;
            rootEntry = fillEntry(rootTable);
        }

        private Entry fillEntry(TableTemplate table) {
            Entry entry = new Entry(table, rows.get(table.name));
            for (FieldTemplate field : table.getFields()) {
                if (field.relativeTable != null) {
                    entry.connection.put(field.name, fillEntry(field.relativeTable));
                }
            }
            return entry;
        }

        public FieldTemplate getFieldTemplate(String fieldChain) {
            List<String> fieldChainList = parseToList(fieldChain);
            return rootEntry.getEntry(fieldChainList).table.getField(fieldChainList.get(fieldChainList.size() - 1));
        }

        private static List<String> parseToList(String fieldChain) {
            return Arrays.asList(fieldChain.split("\\."));
        }

        public String getValue(List<String> fieldChain) {
            return rootEntry.getEntry(fieldChain.subList(0, fieldChain.size() - 1)).row.getValue(fieldChain.get(fieldChain.size() - 1));
        }

        public String getValue(String fieldChain) {
            return getValue(parseToList(fieldChain));
        }

        public void setValue(List<String> fieldChain, String value) {
            rootEntry.getEntry(fieldChain.subList(0, fieldChain.size() - 1)).row.setValue(fieldChain.get(fieldChain.size() - 1), value);
        }

        public JsonObject toJson() {
            return toJson(rootEntry);
        }

        private JsonObject toJson(Entry entry) {
            JsonObject json = rows.get(entry.table.name).toJson();
            for (FieldTemplate field : entry.table.getFields()) {
                if (field.relativeTable != null) {
                    json.remove(field.name);
                    json.add(field.name, toJson(entry.connection.get(field.name)));
                }
            }
            return json;
        }

        private static class Entry {
            public final TableTemplate table;
            public final Table.Row row;
            public Map<String, Entry> connection;

            public Entry(TableTemplate table, Table.Row row) {
                this.connection = new HashMap<>();
                this.table = table;
                this.row = row;
            }

            public Entry getEntry(List<String> fieldChain) {
                if (fieldChain.size() == 0) {
                    return this;
                }
                return connection.get(fieldChain.get(0)).getEntry(fieldChain.stream().skip(1).collect(Collectors.toList()));
            }

            public Entry getEntry(String fieldChain) {
                return getEntry(parseToList(fieldChain));
            }
        }
    }
}
