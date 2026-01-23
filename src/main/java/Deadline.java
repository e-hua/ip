public class Deadline extends Task {
    protected String by;

    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    // [D][ ] return book (by: Sunday)
    @Override
    public String toString() {
        return String.format("[D]%s (by: %s)", super.toString(), this.by);
    }


    // D | 0 | return book | June 6th
    @Override
    public String toStorageString() {
        return String.format("%s | %s | %s", "D", super.toStorageString(), this.by);
    }
}
