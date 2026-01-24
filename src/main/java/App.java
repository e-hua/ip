import Exceptions.EclipseException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        Storage tasksStorage;
        Eclipse chatbot;
        String dirPath = "./data";

        try {
            tasksStorage = new Storage(new StorageParser(), dirPath);
            chatbot = new Eclipse(tasksStorage.readTasks());
        } catch (EclipseException e) {
            Eclipse.printIndentedHorizontalLine();
            Eclipse.printIndentedLine("Fatal, chatbot failed to boot!!! " + e.getMessage());
            Eclipse.printIndentedHorizontalLine();
            return;
        }

        Scanner scanner = new Scanner(System.in);

        Eclipse.greet();

        scanLoop:
        while (true) {
            String input = scanner.nextLine();

            try {
                ParsedInput parsedInput = Parser.parse(input);
                switch (parsedInput.getCommand()) {
                case BYE:
                    Eclipse.exit();
                    break scanLoop;
                case LIST:
                    chatbot.list();
                    break;
                case MARK:
                    chatbot.mark(Parser.parseListIndex(parsedInput.getParams(), chatbot));
                    tasksStorage.storeTasks(chatbot.getTasks());
                    break;
                case UNMARK:
                    chatbot.unmark(Parser.parseListIndex(parsedInput.getParams(), chatbot));
                    tasksStorage.storeTasks(chatbot.getTasks());
                    break;
                case EVENT, DEADLINE, TODO:
                    chatbot.add(parsedInput);
                    tasksStorage.storeTasks(chatbot.getTasks());
                    break;
                case DELETE:
                    chatbot.delete(Parser.parseListIndex(parsedInput.getParams(), chatbot));
                    tasksStorage.storeTasks(chatbot.getTasks());
                    break;
                default:
                    throw new EclipseException("Unknown input: " + input);
                }
            } catch (EclipseException e) {
                Eclipse.printIndentedHorizontalLine();
                Eclipse.printIndentedLine("OOPS!!! " + e.getMessage());
                Eclipse.printIndentedHorizontalLine();
            }
        }

        // Preventing memory leak
        scanner.close();
    }
}
