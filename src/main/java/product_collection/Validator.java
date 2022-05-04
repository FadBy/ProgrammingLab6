package product_collection;

import com.google.gson.JsonObject;

import java.util.*;

public class Validator {
    private final Map<String, List<Field>> collectionTypes;

    public Validator(Map<String, List<Field>> collectionTypes) {
        this.collectionTypes = collectionTypes;
    }

    public void SetTypes() {

    }

    public String isValidFor(JsonObject value, String type) {
        return null;
    }
}
