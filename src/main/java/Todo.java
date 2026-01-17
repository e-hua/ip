public class Todo extends Task {

    public Todo(String description) {
        super(description);
    }

    // [T][ ] borrow book
    @Override
    public String toString() {
        return String.format("[T]%s", super.toString());
    }
}
