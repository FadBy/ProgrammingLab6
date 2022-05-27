import application.Application;

public class Client {
    public static void main(String[] args) {
        Application app = new Application("localhost", 2232);
        if (app.isReadyToRun()) {
            app.run();
        }
    }
}
