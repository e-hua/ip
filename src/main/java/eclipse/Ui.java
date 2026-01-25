package eclipse;

import eclipse.exceptions.EclipseException;

/**
 * Handles the user interface(UI) of the application.
 * This class is responsible for formatting and printing messages to the standard output stream,
 * including greetings, errors, and task-related notifications.
 */
public class Ui {
    private static final String horizontalLine = "____________________________________________________________";
    private static final String indentSpaces = "    ";

    /**
     * Prints a string to the standard out with the standard indentation prefix.
     *
     * @param str The string to be printed.
     */
    private void printIndentedLine(String str) {
        System.out.println(indentSpaces + str);
    }

    /**
     * Prints a horizontal line with indentation as separator.
     */
    private void printIndentedHorizontalLine() {
        this.printIndentedLine(horizontalLine);
    }

    /**
     * Displays a formatted error message to the user when an {@link EclipseException} occurs.
     *
     * @param e The exception containing the error message to display.
     */
    public void showRecoverableError(EclipseException e) {
        printIndentedHorizontalLine();
        printIndentedLine("OOPS!!! " + e.getMessage());
        printIndentedHorizontalLine();
    }

    /**
     * Displays a decorative border to separate sections of output.
     */
    public void showBorder() {
        this.printIndentedHorizontalLine();
    }

    /**
     * Prints a single line of content with indentation.
     *
     * @param content The text to display.
     */
    public void showContent(String content) {
        this.printIndentedLine(content);
    }

    /**
     * Prints an empty line to signal the end of a specific output block.
     */
    public void endOutput() {
        System.out.println();
    }

    /**
     * Displays the welcome message and greeting to the user.
     *
     * @param chatbotName The name of the chatbot to display in the greeting.
     */
    public void greet(String chatbotName) {
        printIndentedLine(horizontalLine);
        printIndentedLine("Hello! I'm " + chatbotName);
        printIndentedLine("What can I do for you?");
        printIndentedLine(horizontalLine);
        System.out.println();
    }

    /**
     * Displays the exit message when the application exits.
     */
    public void exit() {
        printIndentedLine(horizontalLine);
        printIndentedLine("Bye. Hope to see you again soon!");
        printIndentedLine(horizontalLine);
        System.out.println();
    }
}
