package eclipse;

import eclipse.exceptions.EclipseException;
import eclipse.parser.ParsedInput;
import eclipse.parser.Parser;

import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        String dirPath = "./data";
        Eclipse chatbot = new Eclipse(dirPath);

        Scanner scanner = new Scanner(System.in);

        chatbot.greet();

        scanLoop:
        while (true) {
            String input = scanner.nextLine();

            try {
                ParsedInput parsedInput = Parser.parse(input);
                switch (parsedInput.getCommand()) {
                case BYE:
                    chatbot.exit();
                    break scanLoop;
                case LIST:
                    chatbot.list();
                    break;
                case MARK:
                    chatbot.mark(Parser.parseListIndex(parsedInput.getParams(), chatbot));
                    chatbot.saveTasks();
                    break;
                case UNMARK:
                    chatbot.unmark(Parser.parseListIndex(parsedInput.getParams(), chatbot));
                    chatbot.saveTasks();
                    break;
                case EVENT, DEADLINE, TODO:
                    chatbot.add(parsedInput);
                    chatbot.saveTasks();
                    break;
                case DELETE:
                    chatbot.delete(Parser.parseListIndex(parsedInput.getParams(), chatbot));
                    chatbot.saveTasks();
                    break;
                case FIND:
                    chatbot.find(parsedInput);
                    break;
                default:
                    throw new EclipseException("Unknown input command: " + input);
                }
            } catch (EclipseException e) {
                chatbot.handleRecoverableError(e);
            }
        }

        // Preventing memory leak
        scanner.close();
    }
}
