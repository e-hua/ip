package eclipse;

import eclipse.exceptions.EclipseException;

public class Ui {
    private static final String HORIZONTAL_LINE = "____________________________________________________________";
    private static final String INDENT_SPACES = "    ";

    private void printIndentedLine(String str) {
        System.out.println(INDENT_SPACES + str);
    }

    private void printIndentedHorizontalLine() {
        this.printIndentedLine(HORIZONTAL_LINE);
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
        printIndentedLine(HORIZONTAL_LINE);
        printIndentedLine("Hello! I'm " + chatbotName);
        printIndentedLine("What can I do for you?");
        printIndentedLine(HORIZONTAL_LINE);
        System.out.println();
    }

    public void exit() {
        printIndentedLine(HORIZONTAL_LINE);
        printIndentedLine("Bye. Hope to see you again soon!");
        printIndentedLine(HORIZONTAL_LINE);
        System.out.println();
    }
}
