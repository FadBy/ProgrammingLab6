package commander;

import file_data.Console;
import file_data.Errput;
import file_data.Input;
import file_data.Output;

import java.util.List;

public class HelpCommand extends Command {
    @Override
    public String getName() {
        return "help";
    }

    @Override
    public void execute(List<String> args, Input input, Output output, Errput errput) {
        output.printResult("help : вывести справку по доступным командам\n" +
                "info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)\n" +
                "show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении\n" +
                "add {element} : добавить новый элемент в коллекцию\n" +
                "update id {element} : обновить значение элемента коллекции, id которого равен заданному\n" +
                "remove_by_id id : удалить элемент из коллекции по его id\n" +
                "clear : очистить коллекцию\n" +
                "save : сохранить коллекцию в файл\n" +
                "execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.\n" +
                "exit : завершить программу (без сохранения в файл)\n" +
                "add_if_min {element} : добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции\n" +
                "remove_lower {element} : удалить из коллекции все элементы, меньшие, чем заданный\n" +
                "history : вывести последние 15 команд (без их аргументов)\n" +
                "count_less_than_manufacturer manufacturer : вывести количество элементов, значение поля manufacturer которых меньше заданного\n" +
                "print_ascending : вывести элементы коллекции в порядке возрастания\n" +
                "print_field_descending_manufacturer : вывести значения поля manufacturer всех элементов в порядке убывания");
    }
}
