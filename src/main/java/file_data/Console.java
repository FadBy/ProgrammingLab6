package file_data;

import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Console implements Input, Output, Errput {
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void printException(Exception exception) {
        printException(exception.getMessage());
    }

    @Override
    public void printException(String message) {
        System.out.println("Error: " + message);
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

    public String nextLine(String inputRequest) {
        System.out.print(inputRequest);
        return nextLine();
    }

    @Override
    public void printResult(String line) {
        System.out.println(line);
    }

    @Override
    public void printRequest(String line) {
        System.out.print(line);
    }
}
