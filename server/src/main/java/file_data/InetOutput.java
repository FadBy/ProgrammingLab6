package file_data;

import validation.Input;
import validation.Output;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

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
