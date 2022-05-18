package validation;

import validation.TableTemplate;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class CommandPackage implements Serializable {

    public List<String> args;
    public Map<String, Map<String, String>> builder;

    public CommandPackage(List<String> args, Map<String, Map<String, String>> builder) {
        this.builder = builder;
        this.args = args;
    }
}
