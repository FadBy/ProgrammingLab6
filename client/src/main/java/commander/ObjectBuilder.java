package commander;

import exceptions.ApplicationRuntimeException;
import data.Input;
import data.Output;
import org.jetbrains.annotations.NotNull;
import template.RowBuilder;
import template.TableTemplate;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class ObjectBuilder {
    private final Map<String, TableTemplate> tables = new LinkedHashMap<>();

    public void setTables(Map<String, TableTemplate> tables) {
        this.tables.clear();
        this.tables.putAll(tables);
    }

    public Map<String, Map<String, String>> buildObject(@NotNull String envObject, @NotNull Input input, @NotNull Output output) throws IOException {
        if (envObject.isEmpty()) {
            throw new ApplicationRuntimeException("Attempt to build null object");
        }
        if (!tables.containsKey(envObject)) {
            throw new ApplicationRuntimeException("There is such table - " + envObject);
        }
        RowBuilder builder = new RowBuilder(tables.get(envObject));
        for (String field : builder) {
            String request = output.makeRequest(field + ": ", input);
            String message = builder.checkFieldValidity(field, request);
            while (!message.isEmpty()) {
                output.printResult(message);
                request = output.makeRequest(field + ": ", input);
                message = builder.checkFieldValidity(field, request);
            }
            builder.setField(field, request);
        }
        return builder.build();
    }

    public void makeServerRequest() {

    }
}
