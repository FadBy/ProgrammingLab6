package template;

import exceptions.ApplicationRuntimeException;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Comparator;

public enum FieldType {
    String(),
    Double(),
    Long(),
    Integer(),
    DateTime();

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
            case Integer: check = true; try { java.lang.Integer.parseInt(value); } catch (NumberFormatException e) { check = false; } break;
            case Long: check = true; try { java.lang.Long.parseLong(value); } catch (NumberFormatException e) { check = false; } break;
            case DateTime: check = true;
                try { LocalDateTime.parse(value); } catch (DateTimeParseException e) {check = false;} break;
            default: throw new ApplicationRuntimeException("This fieldType wasn't considered");
        }
        if (!check) {
            return "must be " + this.name();
        }
        return "";
    }

    public Comparator<String> getComparator() {
        switch (this) {
            case String: return Comparator.comparing(x -> x);
            case Double: return Comparator.comparing(java.lang.Double::parseDouble);
            case Integer: return Comparator.comparing(java.lang.Integer::parseInt);
            case Long: return Comparator.comparing(java.lang.Long::parseLong);
            default: throw new ApplicationRuntimeException("This fieldType wasn't considered");
        }
    }
}
