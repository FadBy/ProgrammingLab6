package template;

import com.google.gson.JsonObject;
import exceptions.ApplicationRuntimeException;
import exceptions.IncorrectJsonException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RowBuilder implements Iterable<String> {
    private final TableTemplate rootTable;
    private final Map<String, TableTemplate.Builder> builders = new LinkedHashMap<>();

    public RowBuilder(TableTemplate rootTable) {
        this.rootTable = rootTable;
        fillBuilders(rootTable);
    }

    public RowBuilder(JsonObject json, TableTemplate rootTable) throws IncorrectJsonException {
        this(rootTable);
        if (json.size() != rootTable.getFields().size()) {
            throw new IncorrectJsonException("Json has incorrect size: " + json.size() + " != " + rootTable.getFields().size());
        }
        builders.put(rootTable.name, new TableTemplate.Builder(rootTable, json));
        for (String el : json.keySet()) {
            if (!rootTable.contains(el)) {
                throw new IncorrectJsonException(el + "isn't contained in " + rootTable.name);
            }
            if (rootTable.getField(el).relativeTable != null) {
                if (!json.get(el).isJsonObject()) {
                    throw new IncorrectJsonException(el + "isn't an jsonObject");
                }
                builders.put(rootTable.getField(el).relativeTable.name, new TableTemplate.Builder(rootTable.getField(el).relativeTable, json.getAsJsonObject(el)));
            }
        }
    }

    private void fillBuilders(TableTemplate template) {
        builders.put(template.name, new TableTemplate.Builder(template));
        for (FieldTemplate field : template.getFields()) {
            if (field.relativeTable != null) {
                fillBuilders(field.relativeTable);
            }
        }
    }

    private List<String> separate(String fieldChain) {
        return Arrays.asList(fieldChain.split("\\."));
    }

    public String checkFieldValidity(List<String> fieldChain, String value) {
        return lastTableInChain(fieldChain).getField(fieldChain.get(fieldChain.size() - 1)).checkValidity(value);
    }

    public String checkFieldValidity(String fieldChain, String value) {
        return checkFieldValidity(separate(fieldChain), value);
    }

    public void setField(List<String> fieldChain, String value) {
        if (!checkFieldValidity(fieldChain, value).isEmpty()) {
            throw new ApplicationRuntimeException("Attempt to set invalid field");
        }
        builders.get(lastTableInChain(fieldChain).name).setField(fieldChain.get(fieldChain.size() - 1), value);
        lastTableInChain(fieldChain).checkValidity(fieldChain.get(fieldChain.size() - 1), value);
    }

    public void setField(String fieldChain, String value) {
        setField(separate(fieldChain), value);
    }

    private TableTemplate lastTableInChain(List<String> fieldChain) {
        FieldTemplate field;
        TableTemplate current = rootTable;
        for (int i = 0; i < fieldChain.size() - 1; i++) {
            field = current.getField(fieldChain.get(i));
            if (field.relativeTable == null) {
                throw new ApplicationRuntimeException(fieldChain + "is invalid");
            }
            current = field.relativeTable;
        }
        return current;
    }

    public String checkBuilderValidity() {
        return builders.values().stream().map(TableTemplate.Builder::checkBuilderValidity).filter(x -> !x.isEmpty()).findFirst().orElse("");
    }

    public Map<String, Map<String, String>> build() {
//        for (TableTemplate.Builder builder : builders.values()) {
//            for (FieldTemplate field : builder) {
//                if (field.relativeTable != null) {
//                    builder.setField(field.name, Long.toString(id));
//                }
//            }
//        }
//        return new Row(rootTable, builders.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, x -> x.getValue().build())));
        return builders.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, x -> x.getValue().build()));
    }

    @Override
    public Iterator<String> iterator() {
        return new BuilderIterator();
    }

    private class BuilderIterator implements Iterator<String> {
        private final List<Iterator<FieldTemplate>> iterators = new ArrayList<Iterator<FieldTemplate>>() {{ add(rootTable.getFields().stream().filter(x -> !x.autoField).iterator()); }};
        private final List<String> fieldChain = new ArrayList<>();

        @Override
        public boolean hasNext() {
            return iterators.stream().anyMatch(Iterator::hasNext);
        }

        @Override
        public String next() {
            if (!hasNext()) {
                throw new ApplicationRuntimeException("There is no more elements in iterator");
            }
            while (!iterators.get(iterators.size() - 1).hasNext()) {
                iterators.remove(iterators.size() - 1);
                fieldChain.remove(fieldChain.size() - 1);
            }
            FieldTemplate field = iterators.get(iterators.size() - 1).next();
            if (field.relativeTable != null) {
                iterators.add(field.relativeTable.getFields().stream().filter(x -> !x.autoField).iterator());
                fieldChain.add(field.name);
                field = iterators.get(iterators.size() - 1).next();
            }
            return Stream.of(fieldChain, Arrays.asList(field.name)).flatMap(Collection::stream).collect(Collectors.joining("."));
        }
    }
}
