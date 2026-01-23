public class Task {
    private boolean isDone = false;
    private String description;

    public Task(String description) {
        this.description = description;
    }

    public void markAsDone() {
        this.isDone = true;
    }

    public void markAsNotDone() {
        this.isDone = false;
    }

    @Override
    public String toString() {
        String mark = this.isDone ? "X" : " ";
        return String.format("[%s] %s", mark, this.description);
    }


    // 1 | read book
    public String toStorageString() {
        return String.format("%s | %s", isDone ? "1" : "0", this.description);
    }
}