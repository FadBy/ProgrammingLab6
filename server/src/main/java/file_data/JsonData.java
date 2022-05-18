package file_data;


import com.google.gson.JsonElement;
import environment.Environment;
import exceptions.ApplicationException;

import java.io.*;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.stream.Collectors;

public class JsonData implements Data {
    private Path path;

    public void setPath(Path path) {
        this.path = path;
    }

    public void save(JsonElement json) throws IOException {
        write(json.toString());
    }

    @Override
    public void rewrite(String text) throws IOException {
        throw new UnsupportedOperationException("rewrite not implemented");
    }

    @Override
    public void write(String text) throws IOException {
        FileWriter writer = new FileWriter(path.toFile());
        writer.write(text);
        writer.close();
    }

    @Override
    public String readLine() throws IOException {
        throw new UnsupportedOperationException("rewrite not implemented");
    }

    public String readAll() throws FileNotFoundException, ApplicationException {
        try (BufferedReader buffer = new BufferedReader(new FileReader(path.toFile()))) {
            return buffer.lines().collect(Collectors.joining(""));
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException(path.toString() + " is not found");
        } catch (IOException e) {
            throw new ApplicationException(e);
        }
    }
}
