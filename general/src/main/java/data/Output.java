package data;

import java.io.IOException;

public interface Output {
    void printResult(String line);
    String makeRequest(String request, Input input) throws IOException;
}
