import Exceptions.EclipseException;

public class Ui {
    private static final String horizontalLine = "____________________________________________________________";
    private static final String indentSpaces = "    ";

    private void printIndentedLine(String str) {
        System.out.println(indentSpaces + str);
    }

    private void printIndentedHorizontalLine() {
        this.printIndentedLine(horizontalLine);
    }

    public void showRecoverableError(EclipseException e) {
        printIndentedHorizontalLine();
        printIndentedLine("OOPS!!! " + e.getMessage());
        printIndentedHorizontalLine();
    }

    public void showBorder() {
        this.printIndentedHorizontalLine();
    }

    public void showContent(String content) {
        this.printIndentedLine(content);
    }

    public void endOutput() {
        System.out.println();
    }

    public void greet(String chatbotName) {
        printIndentedLine(horizontalLine);
        printIndentedLine("Hello! I'm " + chatbotName);
        printIndentedLine("What can I do for you?");
        printIndentedLine(horizontalLine);
        System.out.println();
    }

    public void exit() {
        printIndentedLine(horizontalLine);
        printIndentedLine("Bye. Hope to see you again soon!");
        printIndentedLine(horizontalLine);
        System.out.println();
    }
}
