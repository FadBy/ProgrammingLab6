package commander;

import file_data.Errput;
import file_data.Input;
import file_data.Output;
import product_collection.ProductCollection;

import java.util.List;

public class InfoCommand extends Command {
    private ProductCollection collection;

    public InfoCommand(ProductCollection collection) {
        this.collection = collection;
    }

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public void execute(List<String> args, Input input, Output output, Errput errput) {
        output.printResult(collection.getInfo());
    }
}
