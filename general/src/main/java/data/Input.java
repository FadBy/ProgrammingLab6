package data;

import java.io.IOException;

public interface Input {
    boolean hasNextLine();
    String nextLine() throws IOException;
    boolean isContinuing();
}
