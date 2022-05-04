package file_data;

import java.io.*;
import java.util.stream.Collectors;

public abstract class Data {
    private String path;

    public void changePath(String path) {
        this.path = path;
    }

    public void rewrite(String text) throws IOException {
        try (Writer writer = new FileWriter(path)) {
            writer.write(text);
        }
    }

    public void write(String text) throws IOException {
        try (Writer writer = new FileWriter(path)) {
            writer.append(text);
        }
    }

    public String readLine() throws IOException {
        try (Reader reader = new FileReader(path);
             BufferedReader buffer = new BufferedReader(reader)) {
            return buffer.readLine();
        }
    }

    public String readAll() throws IOException {
        try (Reader reader = new FileReader(path);
             BufferedReader buffer = new BufferedReader(reader)) {
            return buffer.lines().collect(Collectors.joining("\n"));
        }
    }
}
