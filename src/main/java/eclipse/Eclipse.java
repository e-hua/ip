package eclipse;

import eclipse.exceptions.EclipseException;
import eclipse.parser.ParsedInput;
import eclipse.storage.Storage;
import eclipse.storage.StorageParser;
import eclipse.task.Task;

import java.util.Optional;

/**
 * Handling the interaction with UI, in-memory tasks and the stored task fiel
 * Represents the chatbot user is interacting with
 */
public class Eclipse {
    public static final String CHATBOT_NAME = "Eclipse";

    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    /**
     * Initializes a new Eclipse chatbot instance.
     * Sets up the UI, storage, and attempts to load existing tasks from the disk.
     * If loading fails, an empty task list is initialized instead.
     *
     * @param dirPath The relative directory path where task data should be stored/loaded.
     */
    public Eclipse(String dirPath) {
        this.ui = new Ui();
        this.storage = new Storage(new StorageParser(), dirPath);

        try {
            this.tasks = new TaskList(this.storage.readTasks());
        } catch (EclipseException e) {
            this.ui.showRecoverableError(e);
            this.tasks = new TaskList();
        }
    }

    /**
     * Saves the current list of tasks to the local storage.
     *
     * @throws EclipseException If an error occurs during the saving process.
     */
    public void saveTasks() throws EclipseException {
        this.storage.storeTasks(this.tasks.getTasks());
    }

    /**
     * Displays the greeting message via the UI.
     */
    public void greet() {
        this.ui.greet(this.CHATBOT_NAME);
    }

    /**
     * Displays the goodbye message via the UI.
     */
    public void exit() {
        this.ui.exit();
    }

    /**
     * Display all current tasks to the user in UI.
     */
    public void list() {
        this.ui.showBorder();
        this.ui.showContent("Here are the tasks in your list:");

        for (int idx = 0; idx < tasks.getNumberOfTasks(); idx++) {
            Optional<Task> maybeCurrTask = tasks.getTaskById(idx);
            if (maybeCurrTask.isPresent()) {
                Task currTask = maybeCurrTask.get();
                String formattedEntry = String.format("%d. %s", idx + 1, currTask);
                this.ui.showContent(formattedEntry);
            }
        }

        this.ui.showBorder();
        this.ui.endOutput();
    }

    /**
     * Adds a new task to the list based on the provided parsed input,
     * notifies the user via the UI.
     * Validates that the task description is not blank before adding.
     *
     * @param parsedInput The structured representation of the user's add command.
     * @throws EclipseException If the description is empty or adding fails.
     */
    public void add(ParsedInput parsedInput) throws EclipseException {
        Task newTask = tasks.add(parsedInput);

        if (newTask.getDescription().trim().isEmpty()) {
            throw new EclipseException("Meaningless description: '" + newTask.getDescription() + "'");
        }

        this.ui.showBorder();

        this.ui.showContent("Got it. I've added this task:");
        this.ui.showContent("  " + newTask);
        this.ui.showContent(String.format("Now you have %d tasks in the list.", this.tasks.getNumberOfTasks()));

        this.ui.showBorder();
        this.ui.endOutput();
    }

    /**
     * Removes a task from the list and notifies the user via the UI .
     *
     * @param idx The 0-based index of the task to be deleted.
     * @throws EclipseException If the index is invalid.
     */
    public void delete(int idx) throws EclipseException {
        Task deletedTask = tasks.delete(idx);

        this.ui.showBorder();

        this.ui.showContent("Noted. I've removed this task:");
        this.ui.showContent("  " + deletedTask);
        this.ui.showContent(String.format("Now you have %d tasks in the list.", this.tasks.getNumberOfTasks()));

        this.ui.showBorder();
        this.ui.endOutput();
    }

    /**
     * Marks the specified task as completed.
     *
     * @param idx The 0-based index of the task.
     */
    public void mark(int idx) {
        Optional<Task> maybeTask = this.tasks.getTaskById(idx);
        maybeTask.ifPresent((task) -> {
            task.markAsDone();
            this.ui.showBorder();

            this.ui.showContent("Nice! I've marked this task as done:");
            this.ui.showContent(task.toString());

            this.ui.showBorder();
        });
    }

    /**
     * Marks the specified task as incomplete.
     *
     * @param idx The 0-based index of the task.
     */
    public void unmark(int idx) {
        Optional<Task> maybeTask = this.tasks.getTaskById(idx);
        maybeTask.ifPresent((task) -> {
            task.markAsNotDone();
            this.ui.showBorder();

            this.ui.showContent("OK, I've marked this task as not done yet:");
            this.ui.showContent(task.toString());

            this.ui.showBorder();
        });
    }

    /**
     * Passes a recoverable error to the UI to be displayed to the user.
     *
     * @param e The exception containing the error details.
     */
    public void handleRecoverableError(EclipseException e) {
        this.ui.showRecoverableError(e);
    }

    /**
     * Returns the number of tasks currently managed by the chatbot.
     *
     * @return The size of the task list.
     */
    public int getNumberOfTasks() {
        return this.tasks.getNumberOfTasks();
    }
}
