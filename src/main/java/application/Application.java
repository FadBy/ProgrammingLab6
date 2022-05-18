//package application;
//
//import commander.*;
//import exceptions.ApplicationException;
//import exceptions.ApplicationRuntimeException;
//import file_data.Console;
//import file_data.FileData;
//
//import java.io.IOException;
//import java.time.LocalDateTime;
//import java.util.*;
//
//public class Application {
//
//    private boolean isRun = false;
//    private boolean exitState = false;
//
//    public void run() {
//        if (isRun) {
//            throw new ApplicationRuntimeException();
//        }
//        isRun = true;
//        start();
//        programCycle();
//    }
//
//    private void start() {
//
//    }
//
//    private void programCycle() {
//        while (!exitState) {
//            try {
//                List<String> result = commander.parseCommand(console.nextLine("Введите команду: "));
//                commander.executeCommand(result);
//                history.addCommand(result.get(0));
//            } catch(ApplicationException| IOException e) {
//                console.printException(e);
//            }
//        }
//    }
//
//    public void exit() {
//        exitState = true;
//    }
//}
