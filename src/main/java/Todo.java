public class Todo extends Task {

    public Todo(String description) {
        super(description);
    }

    public Todo(String description, boolean isDone) {
        super(description, isDone);
    }

    // [T][ ] borrow book
    @Override
    public String toString() {
        return String.format("[T]%s", super.toString());
    }

    // T | 1 | read book
    @Override
    public String toStorageString() {
        return String.format("%s | %s", "T", super.toStorageString());
    }
}
