package application;

import exceptions.ApplicationRuntimeException;

import java.util.Comparator;

public enum FieldType {
    String(),
    Double(),
    Long(),
    Integer();

    FieldType() {
    }

    public String checkTypeMatch(String value) {
        if (value == null) {
            return "";
        }
        boolean check;
        switch (this) {
            case String: check = true; break;
            case Double: check = true; try { java.lang.Double.parseDouble(value); } catch (NumberFormatException e) { check = false; } break;
            case Long: check = true; try { java.lang.Long.parseLong(value); } catch (NumberFormatException e) { check = false; } break;
            default: throw new ApplicationRuntimeException("This fieldType wasn't considered");
        }
        if (!check) {
            return "must be " + this.name();
        }
        return "";
    }
}
