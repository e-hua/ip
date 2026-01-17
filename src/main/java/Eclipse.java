import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;

public class Eclipse {
    public static final String name = "Eclipse";

    private static final List<Task> tasks = new ArrayList<>();

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

    public static void add(String taskDescription) {
        printIndentedLine(horizontalLine);
        Task newTask = new Task(taskDescription);
        tasks.add(newTask);
        printIndentedLine("added: " + newTask);
        printIndentedLine(horizontalLine);
        System.out.println();
    }

    public static void list() {
        printIndentedLine(horizontalLine);
        printIndentedLine("Here are the tasks in your list:");
        for(int idx = 0; idx < tasks.size(); idx++) {
            Task currItem = tasks.get(idx);
            String formattedEntry = String.format("%d. %s", idx + 1, currItem);
            printIndentedLine(formattedEntry);
        }
        printIndentedLine(horizontalLine);
        System.out.println();
    }

    private static Optional<Task> getTaskById(int id) {
        try {
            return Optional.of(tasks.get(id));
        } catch(Exception e) {
            return Optional.empty();
        }
    }

    public static void mark(int idx) {
        Optional<Task> maybeTask = getTaskById(idx);
        maybeTask.ifPresent(Task::markAsDone);
    }

    public static void unmark(int idx) {
        Optional<Task> maybeTask = getTaskById(idx);
        maybeTask.ifPresent(Task::markAsNotDone);
    }
}
