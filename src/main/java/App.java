import java.util.Scanner;

public class App {

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    Eclipse.greet();

    scanLoop: while (true) {
      String input = scanner.nextLine();
      ParsedInput parsedInput = Parser.parse(input);

      switch (parsedInput.getCommand()) {
        case BYE:
          Eclipse.exit();
          break scanLoop;
        case LIST:
          Eclipse.list();
          break;
        default:
          Eclipse.add(input);
      }
    }

    // Preventing memory leak
    scanner.close();
  }
}
