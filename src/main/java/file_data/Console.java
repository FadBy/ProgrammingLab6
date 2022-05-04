package file_data;

import java.util.Collections;
import java.util.Scanner;

public class Console implements Input, Output, Errput {
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void printException(Exception exception) {
        System.out.println("Error: " + exception.getMessage());
    }

    @Override
    public boolean hasNextLine() {
        return true;
    }

    @Override
    public String nextLine() {
        return scanner.nextLine();
    }

    public String nextLine(String inputRequest) {
        System.out.print(inputRequest);
        return scanner.nextLine();
    }

    @Override
    public void printResult(String line) {
        System.out.println(line);
    }

    public void requestInput(String request) {
        System.out.print(request);
    }
}
