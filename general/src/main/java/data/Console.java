package data;

import data.Input;
import data.Output;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Console implements Input, Output {
    private final Scanner scanner = new Scanner(System.in);

    public void printException(Exception exception) {
        printException(exception.getMessage());
    }

    public void printException(String message) {
        System.out.println(message);
    }

    @Override
    public boolean hasNextLine() {
        return true;
    }

    @Override
    public String nextLine() {
        String text = "";
        try {
            text = scanner.nextLine();
        } catch (NoSuchElementException e) {
            System.out.println("Program force quit");
            System.exit(0);
        }
        text = text.trim();
        if (text.isEmpty()) {
            text = null;
        }
        return text;
    }

    @Override
    public boolean isContinuing() {
        return true;
    }

    public String nextLine(String inputRequest) {
        System.out.print(inputRequest);
        return nextLine();
    }

    @Override
    public void printResult(String line) {
        System.out.println(line);
    }

    @Override
    public String makeRequest(String request, Input input) throws IOException {
        System.out.print(request);
        return input.nextLine();
    }
}
