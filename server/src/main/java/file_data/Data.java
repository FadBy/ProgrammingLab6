package file_data;

import java.io.*;
import java.util.stream.Collectors;

public interface Data {
    void rewrite(String text) throws IOException;

    void write(String text) throws IOException;

    String readLine() throws IOException;
}
