import Exceptions.EclipseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Eclipse {
    public final String name = "Eclipse";

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
        this.ui.greet(this.name);
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
