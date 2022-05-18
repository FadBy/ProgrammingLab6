package file_data;

import exceptions.ApplicationRuntimeException;
import validation.Input;

import java.io.*;
import java.nio.file.Path;

public class FileData implements Input, Closeable {
    private BufferedReader buffer;
    private String nextBufferLine;
    private boolean closed = true;


    public void changePath(Path path) throws IOException {
        if (!closed) {
            close();
        }
        buffer = new BufferedReader(new FileReader(path.toFile()));
        nextBufferLine = buffer.readLine();
        closed = false;
    }

    @Override
    public boolean hasNextLine() {
        return nextBufferLine != null;
    }

    @Override
    public String nextLine() throws IOException {
        if (nextBufferLine == null) {
            throw new ApplicationRuntimeException("File had finished");
        }
        String nextBufferLine1 = nextBufferLine;
        nextBufferLine = buffer.readLine();
        return nextBufferLine1;
    }

    @Override
    public boolean isContinuing() {
        return false;
    }

    @Override
    public void close() throws IOException {
        buffer.close();
        nextBufferLine = null;
        closed = false;
    }
}
