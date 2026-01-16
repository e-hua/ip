import java.util.Arrays;

public class Eclipse {
    public static final String name = "Eclipse";

    private static final String horizontalLine = "____________________________________________________________";
    private static final String indentSpaces = "    ";

    public Eclipse() {

    }

    private static void printIndentedLine(String str) {
        System.out.println(indentSpaces + str);
    }

    public static void greet() {
        printIndentedLine(horizontalLine);
        printIndentedLine("Hello! I'm " + Eclipse.name);
        printIndentedLine("What can I do for you?");
        printIndentedLine(horizontalLine);
    }

    public static void exit() {
        printIndentedLine(horizontalLine);
        printIndentedLine("Bye. Hope to see you again soon!");
        printIndentedLine(horizontalLine);
    }

    public static void echo(String[] args) {
        String aggregateCommand = Arrays.stream(args).reduce("", (a, b) -> a + " " + b);
        printIndentedLine(aggregateCommand);
    }
}
