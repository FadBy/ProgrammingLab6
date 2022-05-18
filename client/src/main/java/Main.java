import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) {
        Application app = new Application(new InetSocketAddress("localhost", 2232));
        app.run();
    }
}
