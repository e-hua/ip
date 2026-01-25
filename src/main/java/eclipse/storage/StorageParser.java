package eclipse.storage;

import eclipse.exceptions.EclipseException;
import eclipse.task.Deadline;
import eclipse.task.Event;
import eclipse.task.Task;
import eclipse.task.Todo;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses raw strings from the storage file into {@link Task} objects.
 * This class encapsulates logic to interpret the specific data format used for persistence,
 * including handling task types, completion status, and date parsing.
 */
public class StorageParser {

    /**
     * The regex separator used between different fields in the storage file.
     */
    private static final String barBetweenSpaces = "\\s\\|\\s";

    /**
     * Pattern for the general task structure: Type | Status | Content.
     * Example: D | 0 | return book | 2024-12-01
     */
    private static final Pattern storageTaskPattern = Pattern.compile(
            "(?<taskType>[TDE])" // Starting with T/D/E as task type
                    + barBetweenSpaces
                    + "(?<isDone>[01])" // 0 or 1
                    + barBetweenSpaces
                    + "(?<taskContent>.*?)" // return book | June 6th
    );

    /**
     * Pattern for the specific deadline parameter structure: TaskDescription | by
     * Example: return book | 2024-12-01
     */
    private static final Pattern storageDeadlinePattern = Pattern.compile(
            "(?<taskDescription>[^|]*)"
                    + barBetweenSpaces
                    + "(?<by>[^|]*)"
    );

    /**
     * Pattern for the specific event parameter structure: TaskDescription | from=>to
     * Example: return book | 2024-12-01=>2024-12-02
     */
    private static final Pattern storageEventPattern = Pattern.compile(
            "(?<taskDescription>[^|]*)"
                    + barBetweenSpaces
                    + "(?<from>[^|]*)"
                    + "=>"  // Separated by the => signal
                    + "(?<to>[^|]*)"
    );

    /**
     * Converts a single line from the storage file into a {@link Task} object.
     *
     * @param storedLine The raw string line read from the storage file.
     * @return The reconstructed {@link Task} (Todo, Deadline, or Event).
     * @throws EclipseException If the line format is invalid or the line is empty.
     */
    public Task parseStoredLine(String storedLine) throws EclipseException {
        String trimmedLine = storedLine.trim();

        if (trimmedLine.isEmpty()) {
            throw new EclipseException("eclipse.storage.StorageParser detected that there's an empty line");
        }

        Matcher storedTaskMatcher = storageTaskPattern.matcher(trimmedLine);

        if (!storedTaskMatcher.matches()) {
            throw new EclipseException("eclipse.storage.StorageParser failed to parse this line: \n" + storedLine);
        }

        String taskType = storedTaskMatcher.group("taskType");
        boolean isDone = storedTaskMatcher.group("isDone").equals("1");
        String taskContent = storedTaskMatcher.group("taskContent");

        return switch (taskType) {
            case "T" -> new Todo(taskContent, isDone);
            case "D" -> parseDeadlineContent(taskContent, isDone);
            default -> parseEventContent(taskContent, isDone);
        };
    }

    /**
     * Extracts deadline-specific details from the task content string.
     *
     * @param deadlineContent The content string containing description and date.
     * @param isDone          The completion status of the task.
     * @return A reconstructed {@link Deadline} object.
     * @throws EclipseException If the date format is invalid or the regex fails to match.
     */
    private static Deadline parseDeadlineContent(String deadlineContent, boolean isDone) throws EclipseException {
        Matcher storedDeadlineMatcher = storageDeadlinePattern.matcher(deadlineContent);

        if (!storedDeadlineMatcher.matches()) {
            throw new EclipseException(
                    "eclipse.storage.StorageParser failed to parse this content as eclipse.task.Deadline : \n"
                            + deadlineContent);
        }

        String deadlineDescription = storedDeadlineMatcher.group("taskDescription");
        String by = storedDeadlineMatcher.group("by");

        try {
            LocalDate byDate = LocalDate.parse(by);
            return new Deadline(deadlineDescription, isDone, byDate);
        } catch (DateTimeParseException e) {
            throw new EclipseException(
                    "Invalid date format detected in storage file for attribute 'by' in 'deadline' task: " + by,
                    e
            );
        }
    }

    /**
     * Extracts event-specific details from the task content string.
     *
     * @param eventContent The content string containing description, from(start) date, and to(end) date.
     * @param isDone       The completion status of the task.
     * @return A reconstructed {@link Event} object.
     * @throws EclipseException If the date formats are invalid or the regex fails to match.
     */
    private static Event parseEventContent(String eventContent, boolean isDone) throws EclipseException {
        Matcher storedEventMatcher = storageEventPattern.matcher(eventContent);

        if (!storedEventMatcher.matches()) {
            throw new EclipseException(
                    "eclipse.storage.StorageParser failed to parse this content as eclipse.task.Event : \n"
                            + eventContent
            );
        }

        String eventDescription = storedEventMatcher.group("taskDescription");
        String from = storedEventMatcher.group("from");
        String to = storedEventMatcher.group("to");

        try {
            LocalDate fromDate = LocalDate.parse(from);
            LocalDate toDate = LocalDate.parse(to);
            return new Event(eventDescription, isDone, fromDate, toDate);
        } catch (DateTimeParseException e) {
            throw new EclipseException(
                    "Invalid date format detected in the storage file for attribute 'from' or 'to' in 'event' task: "
                            + from
                            + "/"
                            + to,
                    e
            );
        }
    }
}
