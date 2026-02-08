package eclipse;

import java.util.Optional;
import java.util.stream.IntStream;

import eclipse.exceptions.EclipseException;
import eclipse.parser.ParsedInput;
import eclipse.parser.Parser;
import eclipse.storage.Storage;
import eclipse.storage.StorageParser;
import eclipse.task.Task;

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
    public String greet() {
        return this.ui.greet(this.CHATBOT_NAME);
    }

    /**
     * Displays the goodbye message via the UI.
     */
    public String exit() {
        return this.ui.exit();
    }

    /**
     * Display all current tasks to the user in UI.
     */
    public String list() {
        StringBuilder listContent = new StringBuilder();

        this.ui.showBorder();
        this.ui.showContent("Here are the tasks in your list:");

        for (int idx = 0; idx < tasks.getNumberOfTasks(); idx++) {
            Optional<Task> maybeCurrTask = tasks.getTaskById(idx);
            if (maybeCurrTask.isPresent()) {
                Task currTask = maybeCurrTask.get();
                String formattedEntry = String.format("%d. %s", idx + 1, currTask);
                this.ui.showContent(formattedEntry);

                listContent.append(formattedEntry).append("\n");
            }
        }

        this.ui.showBorder();
        this.ui.endOutput();
        return listContent.toString().trim();
    }

    /**
     * Adds a new task to the list based on the provided parsed input,
     * notifies the user via the UI.
     * Validates that the task description is not blank before adding.
     *
     * @param parsedInput The structured representation of the user's add command.
     * @throws EclipseException If the description is empty or adding fails.
     */
    public String add(ParsedInput parsedInput) throws EclipseException {
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

        return "Got it. I've added this task: \n"
                + "  " + newTask + "\n"
                + String.format("Now you have %d tasks in the list.", this.tasks.getNumberOfTasks());
    }

    /**
     * Removes a task from the list and notifies the user via the UI .
     *
     * @param idx The 0-based index of the task to be deleted.
     * @throws EclipseException If the index is invalid.
     */
    public String delete(int idx) throws EclipseException {
        Task deletedTask = tasks.delete(idx);

        this.ui.showBorder();

        this.ui.showContent("Noted. I've removed this task:");
        this.ui.showContent("  " + deletedTask);
        this.ui.showContent(String.format("Now you have %d tasks in the list.", this.tasks.getNumberOfTasks()));

        this.ui.showBorder();
        this.ui.endOutput();

        return "Noted. I've removed this task: \n"
                + "  " + deletedTask + "\n"
                + String.format("Now you have %d tasks in the list.", this.tasks.getNumberOfTasks());
    }

    /**
     * Marks the specified task as completed.
     *
     * @param idx The 0-based index of the task.
     */
    public String mark(int idx) throws EclipseException {
        Optional<Task> maybeTask = this.tasks.getTaskById(idx);
        maybeTask.orElseThrow(() -> new EclipseException("No task to be marked"));

        maybeTask.ifPresent((task) -> {
            task.markAsDone();
            this.ui.showBorder();

            this.ui.showContent("Nice! I've marked this task as done:");
            this.ui.showContent(task.toString());

            this.ui.showBorder();
        });

        return "Nice! I've marked this task as done:\n" + maybeTask.get();
    }

    /**
     * Marks the specified task as incomplete.
     *
     * @param idx The 0-based index of the task.
     */
    public String unmark(int idx) throws EclipseException {
        Optional<Task> maybeTask = this.tasks.getTaskById(idx);
        maybeTask.orElseThrow(() -> new EclipseException("No task to be unmarked"));

        maybeTask.ifPresent((task) -> {
            task.markAsNotDone();
            this.ui.showBorder();

            this.ui.showContent("OK, I've marked this task as not done yet:");
            this.ui.showContent(task.toString());

            this.ui.showBorder();
        });

        return "OK, I've marked this task as not done yet:\n" + maybeTask.get();
    }

    //CHECKSTYLE.OFF: MissingJavadocMethod
    public String find(ParsedInput parsedInput) throws EclipseException {
        StringBuilder foundTasks = new StringBuilder();

        String keyword = parsedInput.getParams();

        this.ui.showBorder();
        this.ui.showContent("Here are the matching tasks in your list:");

        IntStream.range(0, this.tasks.getNumberOfTasks())
                .filter((idx) ->
                        this.tasks.getTaskById(idx)
                                .filter((task) -> task.getDescription().contains(keyword))
                                .isPresent()
                )
                .forEach((idx) ->
                        this.tasks.getTaskById(idx)
                                .ifPresent(task -> {
                                    String formattedEntry = String.format("%d. %s", idx + 1, task);
                                    this.ui.showContent(formattedEntry);
                                    foundTasks.append(formattedEntry).append("\n");
                                }));

        this.ui.showBorder();
        this.ui.endOutput();

        return foundTasks.toString().trim();
    }

    public String stats() {
        return String.format("There are %d tasks in total", this.tasks.getNumberOfTasks());
    }

    //CHECKSTYLE.ON: MissingJavadocMethod

    /**
     * Passes a recoverable error to the UI to be displayed to the user.
     *
     * @param e The exception containing the error details.
     */
    public String handleRecoverableError(EclipseException e) {
        this.ui.showRecoverableError(e);
        return e.getMessage();
    }

    /**
     * Returns the number of tasks currently managed by the chatbot.
     *
     * @return The size of the task list.
     */
    public int getNumberOfTasks() {
        return this.tasks.getNumberOfTasks();
    }


    public String getResponse(String input) {
        String message;
        try {
            ParsedInput parsedInput = Parser.parse(input);
            switch (parsedInput.getCommand()) {
            case BYE:
                message = this.exit();
                break;
            case LIST:
                message = this.list();
                break;
            case MARK:
                message = this.mark(Parser.parseListIndex(parsedInput.getParams(), this));

                this.saveTasks();
                break;
            case UNMARK:
                message = this.unmark(Parser.parseListIndex(parsedInput.getParams(), this));

                this.saveTasks();
                break;
            case EVENT, DEADLINE, TODO:
                message = this.add(parsedInput);

                this.saveTasks();
                break;
            case DELETE:
                message = this.delete(Parser.parseListIndex(parsedInput.getParams(), this));

                this.saveTasks();
                break;
            case FIND:
                message = this.find(parsedInput);
                break;
            case STATISTICS:
                message = this.stats();
                break;
            default:
                throw new EclipseException("Unknown input command: " + input);
            }
        } catch (EclipseException e) {
            message = this.handleRecoverableError(e);
        }
        return message;
    }
}
