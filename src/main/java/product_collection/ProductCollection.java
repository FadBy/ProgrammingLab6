package product_collection;

import com.google.gson.JsonObject;
import exceptions.ApplicationException;
import exceptions.IncorrectInputException;

import java.time.LocalDateTime;
import java.util.*;

public class ProductCollection implements Storage {
    private final LocalDateTime creationDate = LocalDateTime.now();

    private final TreeSet<Product> collection = new TreeSet<>();

    public final Map<String, String> descriptions = new HashMap<>();

//    private final Validator validator;

    public ProductCollection() {
//        validator = new Validator(this);
//        productFields.put(Product.manufactureCostName, ApplicationConsumer.nullable);
    }

    public String getInfo() {
        return  "Тип: HashSet\n" +
                "Дата инициализации: " + creationDate + "\n" +
                "Колво элементов: " + size();
    }

    public int size() {
        return collection.size();
    }

    public HashSet<Product> getCollection() {
        return new HashSet<>(collection);
    }

    public Product add(JsonObject fields) throws ApplicationException {
//        checkValidity();
        Product product = new Product(fields);
        collection.add(product);
        return product;
    }

    public void remove(Product product) {
        collection.remove(product);
    }

//    public Product findById(long id) throws IncorrectInputException {
////        Optional<Product> product = collection.stream().filter((x) -> x.id == id).findAny();
////        if (product.isEmpty()) {
////            throw new IncorrectInputException("There is no id " + id);
////        }
////        return product.get();
//    }

    public void clear() {
        collection.clear();
    }

    public Product getMin() {
        return collection.first();
    }

    public void removeAll(Collection<Product> products) {
        collection.removeAll(products);
    }

    public SortedSet<Product> headSet(Product product) {
        return collection.headSet(product);
    }

    public boolean checkValidity(JsonObject json) {
        return true;
    }

    @Override
    public boolean containsOf(String name, String value) {
        return collection.stream().anyMatch(x -> x.getField(name).getAsString().equals(value));
    }
}
