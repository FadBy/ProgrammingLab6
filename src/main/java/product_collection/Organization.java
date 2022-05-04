package product_collection;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class Organization {
    public final int id;
    public final String name;
    public final String fullName;
    public final Long annualTurnover;
    public final long employeesCount;

    public static final String idName = "id";
    public static final String nameName = "name";
    public static final String fullNameName = "fullName";
    public static final String annualTurnoverName = "annualTurnover";
    public static final String employeesCountName = "employeesCount";

    public Organization(int id, String name, String fullName, Long annualTurnover, long employeesCount) {
        this.id = id;
        this.name = name;
        this.fullName = fullName;
        this.annualTurnover = annualTurnover;
        this.employeesCount = employeesCount;
    }

    public Organization(JsonObject json) {
        id = json.get(idName).getAsInt();
        name = json.get(nameName).getAsString();
        fullName = json.get(fullNameName).isJsonNull() ? null : json.get(fullNameName).getAsString();
        annualTurnover = json.get(annualTurnoverName).isJsonNull() ? null : json.get(annualTurnoverName).getAsLong();
        employeesCount = json.get(employeesCountName).getAsLong();
    }

    public JsonObject convertToJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty(idName, id);
        obj.addProperty(nameName, name);
        obj.add(fullNameName, fullName == null ? JsonNull.INSTANCE : new JsonPrimitive(fullName));
        obj.add(annualTurnoverName, annualTurnover == null ? JsonNull.INSTANCE : new JsonPrimitive(annualTurnover));
        obj.addProperty(employeesCountName, employeesCount);
        return obj;
    }

    @Override
    public String toString() {
        return "Organization{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", fullName='" + fullName + '\'' +
                ", annualTurnover=" + annualTurnover +
                ", employeesCount=" + employeesCount +
                '}';
    }
}
