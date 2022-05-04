package application;

import exceptions.ApplicationRuntimeException;

import java.util.*;
import java.util.stream.Collectors;

public class Environment {
    private final Map<TableTemplate, Table> tables = new HashMap<>();
    private Table root;
    private long nextId = 0;

    private long getNextId() {
        return nextId++;
    }

    public void setTables(List<TableTemplate> tables) {
        if (tables.size() == 0) {
            root = null;
        } else {
            root = new Table(tables.get(0));
        }
        this.tables.clear();
        this.tables.putAll(tables.stream().map(Table::new).collect(Collectors.toMap(x -> x.template, x -> x)));
    }

    public List<String> getColumnTable(String tableName, String fieldName) {
        return tables.get(tables.keySet().stream().filter(x -> Objects.equals(x.name, tableName)).findFirst().get()).getColumn(fieldName);
    }

    public TableTemplate getRootTemplate() {
        return root.template;
    }

    Table getTable(TableTemplate template) {
        return tables.get(template);
    }

    public long addRow(List<TableTemplate.RowBuilder> builders) {
        if (builders.size() != tables.size()) {
            throw new ApplicationRuntimeException("not all tables was considered");
        }
        Map<TableTemplate, Table.Row> rows = builders.stream().collect(Collectors.toMap(TableTemplate.RowBuilder::getTableTemplate, x -> x.build(this)));
        long id = getNextId();
        for (Map.Entry<TableTemplate, Table.Row> entry : rows.entrySet()) {
            tables.get(entry.getKey()).addRow(id, entry.getValue());
        }
        return id;
    }
}
