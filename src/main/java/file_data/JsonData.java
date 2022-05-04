package file_data;

import product_collection.Product;
import product_collection.ProductCollection;

import java.io.IOException;

public class JsonData extends Data {
    private final ProductCollection collection;

    public JsonData(String path, ProductCollection collection) {
        this.collection = collection;
        super.changePath(path);
    }

    @Override
    public void changePath(String path) {
        throw new UnsupportedOperationException();
    }

    public void save() throws IOException {
        StringBuilder builder = new StringBuilder();
        collection.getCollection().stream().forEach(x -> builder.append(x.convertToJson()));
        write(builder.toString());
    }
}
