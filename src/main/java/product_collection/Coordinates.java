package product_collection;

import com.google.gson.JsonObject;

public class Coordinates {
    public final long x;
    public final long y;

    public static final String xName = "x";
    public static final String yName = "y";

    public Coordinates(long x, long y) {
        this.x = x;
        this.y = y;
    }

    public Coordinates(JsonObject json) {
        x = json.get(xName).getAsLong();
        y = json.get(yName).getAsLong();
    }

    public JsonObject convertToJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty(xName, x);
        obj.addProperty(yName, y);
        return obj;
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
