import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Eclipse {
    public static final String name = "Eclipse";

    private static final List<String> items = new ArrayList<>();

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
        System.out.println();
    }

    public static void exit() {
        printIndentedLine(horizontalLine);
        printIndentedLine("Bye. Hope to see you again soon!");
        printIndentedLine(horizontalLine);
        System.out.println();
    }

    public static void add(String item) {
        printIndentedLine(horizontalLine);
        items.add(item);
        printIndentedLine("added: " + item);
        printIndentedLine(horizontalLine);
        System.out.println();
    }

    public static void list() {
        printIndentedLine(horizontalLine);
        for(int idx = 0; idx < items.size(); idx++) {
            String currItem = items.get(idx);
            String formattedEntry = String.format("%d. %s", idx + 1, currItem);
            printIndentedLine(formattedEntry);
        }
        printIndentedLine(horizontalLine);
        System.out.println();
    }
}
