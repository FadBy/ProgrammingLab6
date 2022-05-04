package commander;

import exceptions.IncorrectCommandException;
import exceptions.IncorrectInputException;
import file_data.Errput;
import file_data.Input;
import file_data.Output;
import product_collection.ProductCollection;

import java.util.List;

public class RemoveByIdCommand extends Command {
    private ProductCollection collection;

    public RemoveByIdCommand(ProductCollection collection) {
        this.collection = collection;
    }

    @Override
    public String getName() {
        return "remove_by_id";
    }

    @Override
    public void execute(List<String> args, Input input, Output output, Errput errput) {
        if (args.size() == 0) {
            errput.printException(new IncorrectCommandException("remove_by_id must have id"));
        }
//        try {
//            collection.remove(collection.findById(Long.parseLong(args.get(0))));
//        } catch (IncorrectInputException e) {
//            errput.printException(e);
//        }
    }
}
