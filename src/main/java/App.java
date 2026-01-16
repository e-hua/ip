import java.util.Scanner;

public class App {

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    Eclipse.greet();

    scanLoop: while (true) {
      String command = scanner.nextLine();
      switch (command) {
        case "bye":
          Eclipse.exit();
          break scanLoop;
        default:
          Eclipse.add(command);
      }
    }

    // Preventing memory leak
    scanner.close();
  }
}
