public class Command {
    public final boolean haveObject;
    public final String name;
    public final int argsNumber;

    public Command(String name, int argsNumber, boolean haveObject) {
        this.name = name;
        this.argsNumber = argsNumber;
        this.haveObject = haveObject;
    }
}
