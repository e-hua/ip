package eclipse;

import eclipse.exceptions.EclipseException;
import eclipse.parser.ParsedInput;
import eclipse.parser.Parser;

import java.util.Scanner;

/**
 * The entry point of the Eclipse chatbot application.
 * This class initializes the chatbot environment
 * and manages the main execution loop.
 */
public class App {

    /**
     * Main method that drives the application.
     * It initializes the {@link Eclipse} object,
     * and enters a continuous loop to parse user commands
     * with the static methods from the {@link Parser} class
     *
     * @param args Command line arguments (not used).
     */
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
