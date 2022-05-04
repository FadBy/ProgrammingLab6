package product_collection;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import exceptions.ApplicationRuntimeException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class Product implements Comparable<Product> {
//    public final long id;
//    public final String name;
//    public final Coordinates coordinates;
//    public final LocalDateTime creationDate;
//    public final Double price;
//    public final String partNumber;
//    public final double manufactureCost;
//    public final UnitOfMeasure unitOfMeasure;
//    public final Organization manufacturer;

    public JsonElement getField(String name) {
        return fields.get(name);
    }

    private final JsonObject fields;

    public Product(JsonObject json) {
        fields = json;
    }

    public JsonObject convertToJson() {
        return fields;
    }

    @Override
    public int compareTo(Product o) {
        return fields.get("name").getAsString().compareTo(o.fields.get("name").getAsString());
    }

    @Override
    public String toString() {
        return fields.toString();
    }
}
