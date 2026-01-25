package eclipse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import eclipse.exceptions.EclipseException;
import eclipse.parser.ParsedInput;
import eclipse.task.Deadline;
import eclipse.task.Event;
import eclipse.task.Task;
import eclipse.task.Todo;

/**
 * Provides methods to add, delete, and retrieve tasks and so on
 * Encapsulates a <code>List</code> of {@link Task} objects
 */
public class TaskList {
    /**
     * The internal list of tasks.
     */
    private final List<Task> tasks;

    /**
     * Initializes a TaskList with a <code>List</code> of {@link Task} objects
     *
     * @param tasks The initial list of tasks.
     */
    public TaskList(List<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Initializes a TaskList with an empty <code>List</code> of {@link Task} objects
     *
     */
    public TaskList() {
        this(new ArrayList<>());
    }

    /**
     * Returns the list of tasks.
     *
     * @return The list of tasks.
     */
    public List<Task> getTasks() {
        return this.tasks;
    }

    /**
     * Creates and adds a new task to the list based on the parsed user input.
     *
     * @param parsedInput The parsed user input.
     * @return The newly created {@link Task}.
     * @throws EclipseException If the command is of invalid type
     */
    public Task add(ParsedInput parsedInput) throws EclipseException {
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
        return newTask;
    }

    /**
     * Deletes a task from the list at the specified index.
     *
     * @param idx The 0-based index of the task to be removed.
     * @return The {@link Task} that was removed.
     * @throws EclipseException If the provided index is out of bounds.
     */
    public Task delete(int idx) throws EclipseException {
        Optional<Task> maybeTask = getTaskById(idx);
        Task taskToDelete = maybeTask.orElseThrow
                (
                        () -> new EclipseException(
                                "Given index does not exists in the task list, current task list length: " +
                                        this.tasks.size()
                        )
                );
        tasks.remove(taskToDelete);
        return taskToDelete;
    }

    /**
     * Retrieves an optional task at a specific index.
     *
     * @param id The 0-based index of the task.
     * @return An {@link Optional} containing the task if found, or empty if the index is invalid.
     */
    public Optional<Task> getTaskById(int id) {
        try {
            return Optional.of(tasks.get(id));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Returns the total number of tasks currently in the list.
     *
     * @return The size of the task list.
     */
    public int getNumberOfTasks() {
        return tasks.size();
    }
}
