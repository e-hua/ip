import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import Exceptions.EclipseException;

public class TaskList {
    private final List<Task> tasks;

    public TaskList(List<Task> tasks) {
        this.tasks = tasks;
    }

    public TaskList() {
        this(new ArrayList<>());
    }

    public List<Task> getTasks() {
        return this.tasks;
    }

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

    public Optional<Task> getTaskById(int id) {
        try {
            return Optional.of(tasks.get(id));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public int getNumberOfTasks() {
        return tasks.size();
    }
}
