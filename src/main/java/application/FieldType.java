package application;

import exceptions.ApplicationRuntimeException;

import java.util.regex.Pattern;

public enum FieldType {
    String("String"),
    Double("Double"),
    Long("Long"),
    Integer("Integer");

    private FieldType(String value) {
    }

    private static final Pattern doublePattern = Pattern.compile("-?\\d+(\\.\\d+)?");
    private static final Pattern integerPattern = Pattern.compile("-?\\d+");

    public String checkTypeMatch(String value) {
        boolean check;
        switch (this) {
            case String: check = true; break;
            case Double: check = doublePattern.matcher(value).matches(); break;
            case Long:
            case Integer: check = integerPattern.matcher(value).matches(); break;
            default: throw new ApplicationRuntimeException("This fieldType wasn't considered");
        }
        if (check) {
            return "must be " + value;
        }
        return "";
    }
}
