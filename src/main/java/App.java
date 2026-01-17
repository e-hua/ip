import Exceptions.EclipseException;

import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Eclipse.greet();

        scanLoop:
        while (true) {
            String input = scanner.nextLine();
            ParsedInput parsedInput = Parser.parse(input);

            try {
                switch (parsedInput.getCommand()) {
                    case BYE:
                        Eclipse.exit();
                        break scanLoop;
                    case LIST:
                        Eclipse.list();
                        break;
                    case MARK:
                        Eclipse.mark(Parser.parseListIndex(parsedInput.getParams()));
                        break;
                    case UNMARK:
                        Eclipse.unmark(Parser.parseListIndex(parsedInput.getParams()));
                        break;
                    case EVENT, DEADLINE, TODO:
                        Eclipse.add(parsedInput);
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
