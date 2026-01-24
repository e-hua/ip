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

public class StorageParser {

    //
    private static final String barBetweenSpaces = "\\s\\|\\s";

    // For example
    // D | 0 | return book | June 6th
    private static final Pattern storageTaskPattern = Pattern.compile(
            "(?<taskType>[TDE])" + // Starting with T/D/E as task type
                    barBetweenSpaces +
                    "(?<isDone>[01])" + // 0 or 1
                    barBetweenSpaces +
                    "(?<taskContent>.*?)" // return book | June 6th
    );

    // return book | June 6th
    private static final Pattern storageDeadlinePattern = Pattern.compile(
            "(?<taskDescription>[^|]*)" +
                    barBetweenSpaces +
                    "(?<by>[^|]*)"
    );

    // project meeting | Aug 6th 2-4pm
    private static final Pattern storageEventPattern = Pattern.compile(
            "(?<taskDescription>[^|]*)" +
                    barBetweenSpaces +
                    "(?<from>[^|]*)" +
                    "=>" + // Separated by the => signal
                    "(?<to>[^|]*)"
    );

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

    private static Deadline parseDeadlineContent(String deadlineContent, boolean isDone) throws EclipseException {
        Matcher storedDeadlineMatcher = storageDeadlinePattern.matcher(deadlineContent);

        if (!storedDeadlineMatcher.matches()) {
            throw new EclipseException("eclipse.storage.StorageParser failed to parse this content as eclipse.task.Deadline : \n" + deadlineContent);
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

    private static Event parseEventContent(String eventContent, boolean isDone) throws EclipseException {
        Matcher storedEventMatcher = storageEventPattern.matcher(eventContent);

        if (!storedEventMatcher.matches()) {
            throw new EclipseException("eclipse.storage.StorageParser failed to parse this content as eclipse.task.Event : \n" + eventContent);
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
                    "Invalid date format detected in the storage file for attribute 'from' or 'to' in 'event' task: " + from + "/" + to,
                    e
            );
        }
    }
}
