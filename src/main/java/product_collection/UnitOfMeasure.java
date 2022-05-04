package product_collection;

import exceptions.IncorrectInputException;

public enum UnitOfMeasure {
    SQUARE_METERS("SQUARE_METERS"),
    MILLILITERS("MILLILITERS"),
    MILLIGRAMS("MILLIGRAMS");

    public final String value;

    private UnitOfMeasure(String value) {
        this.value = value;
    }

    public UnitOfMeasure convertToUnitOfMeasure(String value) throws IncorrectInputException {
        switch(value) {
            case "SQUARE_METERS": return SQUARE_METERS;
            case "MILLILITERS": return MILLILITERS;
            case "MILLIGRAMS": return MILLIGRAMS;
            default: throw new IncorrectInputException(value + "- no such UnitOfMeasure");
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
