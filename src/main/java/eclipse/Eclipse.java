package eclipse;

import eclipse.exceptions.EclipseException;
import eclipse.parser.ParsedInput;
import eclipse.storage.Storage;
import eclipse.storage.StorageParser;
import eclipse.task.Task;

import java.util.Optional;

public class Eclipse {
    public static final String CHATBOT_NAME = "Eclipse";

    private Storage storage;
    private TaskList tasks;
    private Ui ui;

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

    public void saveTasks() throws EclipseException {
        this.storage.storeTasks(this.tasks.getTasks());
    }

    public void greet() {
        this.ui.greet(this.CHATBOT_NAME);
    }

    public void exit() {
        this.ui.exit();
    }

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

    public void delete(int idx) throws EclipseException {
        Task deletedTask = tasks.delete(idx);

        this.ui.showBorder();

        this.ui.showContent("Noted. I've removed this task:");
        this.ui.showContent("  " + deletedTask);
        this.ui.showContent(String.format("Now you have %d tasks in the list.", this.tasks.getNumberOfTasks()));

        this.ui.showBorder();
        this.ui.endOutput();
    }

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

    public void handleRecoverableError(EclipseException e) {
        this.ui.showRecoverableError(e);
    }

    public int getNumberOfTasks() {
        return this.tasks.getNumberOfTasks();
    }
}
