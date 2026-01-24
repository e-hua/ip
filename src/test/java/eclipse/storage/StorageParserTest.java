package eclipse.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import eclipse.exceptions.EclipseException;
import eclipse.task.Deadline;
import eclipse.task.Event;
import eclipse.task.Task;
import eclipse.task.Todo;

public class StorageParserTest {
    StorageParser storageParser = new StorageParser();

    @Test
    public void parseStoredLine_parseInvalidLine_exceptionThrown() {
        String storedInvalidLine = "A | 3 | return book\n";

        try {
            storageParser.parseStoredLine(storedInvalidLine);
            fail();
        } catch (EclipseException e) {
            assertEquals(
                    "eclipse.storage.StorageParser failed to parse this line: \n" + storedInvalidLine,
                    e.getMessage()
            );
        }
    }

    @Test
    public void parseStoredLine_parseValidTodo_success() throws EclipseException {
        String storedValidTodoLine = "T | 1 | return book\n";

        Task parsedTodo = storageParser.parseStoredLine(storedValidTodoLine);
        assertEquals(parsedTodo.toString(), new Todo("return book", true).toString());
        assertEquals(parsedTodo.toStorageString(), new Todo("return book", true).toStorageString());
        assertEquals(parsedTodo.toStorageString(), storedValidTodoLine.trim());
    }

    @Test
    public void parseStoredLine_parseInvalidTodo_exceptionThrown() {
        String storedInvalidTodoLine = "T | 3 | return book\n";

        try {
            storageParser.parseStoredLine(storedInvalidTodoLine);
            fail();
        } catch (EclipseException e) {
            assertEquals(
                    "eclipse.storage.StorageParser failed to parse this line: \n" + storedInvalidTodoLine,
                    e.getMessage()
            );
        }
    }

    @Test
    public void parseStoredLine_parseValidDeadline_success() throws EclipseException {
        String storedValidTodoLine = " \n D | 0 | return book | 2026-01-24\n";

        Task parsedTodo = storageParser.parseStoredLine(storedValidTodoLine);
        Deadline expectedDeadline = new Deadline("return book", false, LocalDate.parse("2026-01-24"));
        assertEquals(parsedTodo.toString(), expectedDeadline.toString());
        assertEquals(parsedTodo.toStorageString(), expectedDeadline.toStorageString());
        assertEquals(parsedTodo.toStorageString(), storedValidTodoLine.trim());
    }

    @Test
    public void parseStoredLine_parseInvalidDeadline_exceptionThrown() {
        // With one extra space in between
        String storedInvalidDeadlineLine = " D | 0  | return book | 2026-01-24";

        try {
            storageParser.parseStoredLine(storedInvalidDeadlineLine);
            fail();
        } catch (EclipseException e) {
            assertEquals(
                    "eclipse.storage.StorageParser failed to parse this line: \n" + storedInvalidDeadlineLine,
                    e.getMessage()
            );
        }
    }

    @Test
    public void parseStoredLine_parseValidEvent_success() throws EclipseException {
        String storedValidEventLine = " \n E | 0 | return book | 2026-01-24=>2026-01-25\n";

        Task parsedTodo = storageParser.parseStoredLine(storedValidEventLine);
        Event expectedEvent = new Event("return book", false, LocalDate.parse("2026-01-24"), LocalDate.parse("2026-01-25"));
        assertEquals(parsedTodo.toString(), expectedEvent.toString());
        assertEquals(parsedTodo.toStorageString(), expectedEvent.toStorageString());
        assertEquals(parsedTodo.toStorageString(), storedValidEventLine.trim());
    }

    @Test
    public void parseStoredLine_parseInvalidEvent_exceptionThrown() {
        String storedInvalidEventLine = " E | 0 | return book | 2026-01-240=>2026-01-25";

        try {
            storageParser.parseStoredLine(storedInvalidEventLine);
            fail();
        } catch (EclipseException e) {
            assertEquals(
                    "Invalid date format detected in the storage file for attribute 'from' or 'to' in 'event' task: 2026-01-240/2026-01-25",
                    e.getMessage()
            );
        }
    }
}
