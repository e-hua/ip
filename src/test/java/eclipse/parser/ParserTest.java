package eclipse.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import eclipse.Command;
import eclipse.exceptions.EclipseException;

public class ParserTest {

    @Test
    public void parse_emptyInput_exceptionThrown() {
        String emptyInput = "  ";
        try {
            ParsedInput parsedInput = Parser.parse(emptyInput);
            fail();
        } catch (EclipseException e) {
            assertEquals(
                    "Failed to parse, invalid input format detected: '" + emptyInput + "'",
                    e.getMessage(),
                    "Exception thrown when parsing empty command"
            );
        }
    }

    @Test
    public void parse_invalidCommand_invalidParsedInput() throws EclipseException {
        String blahInput = " blah ";
        ParsedInput parsedInput = Parser.parse(blahInput);
        assertEquals(Command.INVALID, parsedInput.getCommand(), "extracted command type to be 'INVALID'");
    }

    @Test
    public void parse_validTodo_success() throws EclipseException {
        String todoInput = " todo borrow book ";
        ParsedInput parsedInput = Parser.parse(todoInput);
        assertEquals(Command.TODO, parsedInput.getCommand(), "extracted command type to be 'TODO'");
        assertEquals("borrow book", parsedInput.getParams(), "extracted param string to be 'borrow book'");
    }

    @Test
    public void parse_validDeadline_success() throws EclipseException {
        String deadlineInput = "deadline return book /by 2019-10-15";
        ParsedInput parsedInput = Parser.parse(deadlineInput);
        assertEquals(Command.DEADLINE, parsedInput.getCommand(), "extracted command type to be 'DEADLINE'");
        assertEquals("2019-10-15", parsedInput.getBy().toString());
    }

    @Test
    public void parse_invalidDeadlineDate_exceptionThrown() {
        String invalidDeadlineInput = " deadline do homework /by no idea :-p ";
        try {
            Parser.parse(invalidDeadlineInput);
            fail();
        } catch (EclipseException e) {
            assertEquals(
                    "Invalid date format for attribute 'by' in 'deadline' task: " + "no idea :-p",
                    e.getMessage(),
                    "Exception thrown while parsing invalid deadline date type"
            );
        }
    }

    @Test
    public void parse_invalidDeadlineFormat_exceptionThrown() {
        String invalidDeadlineFormatInput = "deadline return book by 2019-10-15";
        try {
            Parser.parse(invalidDeadlineFormatInput);
            fail();
        } catch (EclipseException e) {
            assertEquals(
                    "Invalid input format for command type 'deadline': " + "return book by 2019-10-15",
                    e.getMessage(),
                    "Exception thrown while parsing deadline params with invalid format");
        }
    }

    @Test
    public void parse_validEvent_success() throws EclipseException {
        String eventInput = "event project meeting /from 2019-10-15 /to 2019-10-16";
        ParsedInput parsedInput = Parser.parse(eventInput);
        assertEquals(Command.EVENT, parsedInput.getCommand(), "extracted command type to be 'EVENT'");
        assertEquals("2019-10-15", parsedInput.getFrom().toString());
        assertEquals("2019-10-16", parsedInput.getTo().toString());
    }

    @Test
    public void parse_invalidEventFromDate_exceptionThrown() {
        String invalidEventInput = "event project meeting /from 2019-10-150 /to 2019-10-16";
        try {
            Parser.parse(invalidEventInput);
            fail();
        } catch (EclipseException e) {
            assertEquals(
                    "Invalid date format for attribute 'from' or 'to' in 'event' task: 2019-10-150/2019-10-16",
                    e.getMessage(),
                    "Exception thrown while parsing invalid date type for 'from' attribute"
            );
        }
    }

    @Test
    public void parse_invalidEventToDate_exceptionThrown() {
        String invalidEventInput = "event project meeting /from 2019-10-15 /to 2019-100-16";
        try {
            Parser.parse(invalidEventInput);
            fail();
        } catch (EclipseException e) {
            assertEquals(
                    "Invalid date format for attribute 'from' or 'to' in 'event' task: 2019-10-15/2019-100-16",
                    e.getMessage(),
                    "Exception thrown while parsing invalid date type for 'to' attribute"
            );
        }
    }

    @Test
    public void parse_invalidEventFormat_exceptionThrown() {
        String invalidEventFormatInput = "event project meeting";
        try {
            Parser.parse(invalidEventFormatInput);
            fail();
        } catch (EclipseException e) {
            assertEquals(
                    "Invalid input format for command type 'event': project meeting",
                    e.getMessage(),
                    "Exception thrown while parsing event params with invalid format");
        }
    }
}
