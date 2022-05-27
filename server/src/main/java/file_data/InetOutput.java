package file_data;

import data.Input;
import data.Output;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class InetOutput implements Output {
    private final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

    @Override
    public void printResult(String line) {
        try {
            byteStream.write((line + "\n").getBytes());
        } catch (IOException ignored) {

        }
    }

    @Override
    public String makeRequest(String request, Input input) throws IOException {
        return null;
    }

    public String getText() {
        return byteStream.toString();
    }

    public void clear() {
        byteStream.reset();
    }
}
