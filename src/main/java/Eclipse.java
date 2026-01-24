import Exceptions.EclipseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Eclipse {
    public static final String name = "Eclipse";

    private static final String horizontalLine = "____________________________________________________________";
    private static final String indentSpaces = "    ";

    private final List<Task> tasks;

    public Eclipse(List<Task> tasks) {
        this.tasks = tasks;
    }

    public List<Task> getTasks() {
        return this.tasks;
    }

    public static void printIndentedLine(String str) {
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

    public void add(ParsedInput parsedInput) throws EclipseException {
        printIndentedLine(horizontalLine);
        Task newTask = switch (parsedInput.getCommand()) {
            case TODO -> new Todo(parsedInput.getParams());
            case DEADLINE -> new Deadline(
                    parsedInput.getParams(),
                    parsedInput.getBy()
            );
            case EVENT -> new Event(
                    parsedInput.getParams(),
                    parsedInput.getFrom(),
                    parsedInput.getTo()
            );
            default -> throw new EclipseException("Invalid parsed input, cannot be added as task" + parsedInput);
        };

        tasks.add(newTask);
        printIndentedLine("Got it. I've added this task:");
        printIndentedLine("  " + newTask);
        printIndentedLine(String.format("Now you have %d tasks in the list.", this.getNumberOfTasks()));
        printIndentedLine(horizontalLine);
        System.out.println();
    }


    public void list() {
        printIndentedLine(horizontalLine);
        printIndentedLine("Here are the tasks in your list:");
        for (int idx = 0; idx < tasks.size(); idx++) {
            Task currItem = tasks.get(idx);
            String formattedEntry = String.format("%d. %s", idx + 1, currItem);
            printIndentedLine(formattedEntry);
        }
        printIndentedLine(horizontalLine);
        System.out.println();
    }

    private Optional<Task> getTaskById(int id) {
        try {
            return Optional.of(tasks.get(id));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public void mark(int idx) {
        Optional<Task> maybeTask = getTaskById(idx);
        maybeTask.ifPresent((task) -> {
            task.markAsDone();
            printIndentedLine(horizontalLine);
            printIndentedLine("Nice! I've marked this task as done:");
            printIndentedLine(task.toString());
            printIndentedLine(horizontalLine);
        });
    }

    public void unmark(int idx) {
        Optional<Task> maybeTask = getTaskById(idx);
        maybeTask.ifPresent((task) -> {
            task.markAsNotDone();
            printIndentedLine(horizontalLine);
            printIndentedLine("OK, I've marked this task as not done yet:");
            printIndentedLine(task.toString());
            printIndentedLine(horizontalLine);
        });
    }

    public void delete(int idx) {
        Optional<Task> maybeTask = getTaskById(idx);
        maybeTask.ifPresent((task) -> {
            tasks.remove(idx);
            printIndentedLine(horizontalLine);
            printIndentedLine("Noted. I've removed this task:");
            printIndentedLine(task.toString());
            printIndentedLine(String.format("Now you have %d tasks in the list.", this.getNumberOfTasks()));
            printIndentedLine(horizontalLine);
        });
    }


    public int getNumberOfTasks() {
        return tasks.size();
    }

    public static void printIndentedHorizontalLine() {
        printIndentedLine(horizontalLine);
    }
}
