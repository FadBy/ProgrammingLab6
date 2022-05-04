package product_collection;

import com.google.gson.JsonObject;
import exceptions.IncorrectInputException;

import java.util.Objects;
import java.util.function.Function;

public interface ApplicationConsumer {
    void accept(JsonObject t, String name) throws IncorrectInputException;

    default ApplicationConsumer andThen(ApplicationConsumer more) {
        Objects.requireNonNull(more);
        return (x, name) -> { if (this == nullable && x.isJsonNull()) { return; } more.accept(x, name); accept(x, name); };
    }

    ApplicationConsumer nullable = (x, name) -> {};
    final ApplicationConsumer isDouble = (x, name) -> {try { x.getAsJsonPrimitive().getAsDouble(); } catch(NumberFormatException e) { throw new IncorrectInputException(name + "must be double"); }};
    final ApplicationConsumer isLong = (x, name) -> {try { x.getAsJsonPrimitive().getAsLong(); } catch (NumberFormatException e) { throw new IncorrectInputException(name + "must be long");}};
    final ApplicationConsumer notNull = (x, name) -> {if (x.isJsonNull()) throw new IncorrectInputException(name + " can't be null");};
    final ApplicationConsumer moreThanZero = (x, name) -> {if (x.getAsJsonPrimitive().getAsDouble() <= 0) throw new IncorrectInputException(name + " must be more than 0");};
    final ApplicationConsumer notEmpty = (x, name) -> {if (x.getAsJsonPrimitive().getAsString().isEmpty()) throw new IncorrectInputException(name + " can't be empty");};
    final ApplicationConsumer notMoreThanSix = (x, name) -> {if (x.getAsJsonPrimitive().getAsDouble() > 6) throw new IncorrectInputException(name + " must be lower than 6");};

}
