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
                        parsedInput.getOptionalParams().ifPresent((param) -> Eclipse.mark(Parser.parseListIndex(param)));
                        break;
                    case UNMARK:
                        parsedInput.getOptionalParams().ifPresent((param) -> Eclipse.unmark(Parser.parseListIndex(param)));
                        break;
                    case EVENT, DEADLINE, TODO:
                        Eclipse.add(parsedInput);
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid input: " + input);
                }
            } catch (Exception e) {
                System.err.println(e);
            }
        }

        // Preventing memory leak
        scanner.close();
    }
}
