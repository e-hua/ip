import Exceptions.EclipseException;

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
            throw new EclipseException("StorageParser detected that there's an empty line");
        }

        Matcher storedTaskMatcher = storageTaskPattern.matcher(trimmedLine);

        if (!storedTaskMatcher.matches()) {
            throw new EclipseException("StorageParser failed to parse this line: \n" + storedLine);
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
            throw new EclipseException("StorageParser failed to parse this content as Deadline : \n" + deadlineContent);
        }

        String deadlineDescription = storedDeadlineMatcher.group("taskDescription");
        String by = storedDeadlineMatcher.group("by");

        return new Deadline(deadlineDescription, isDone, by);
    }

    private static Event parseEventContent(String eventContent, boolean isDone) throws EclipseException {
        Matcher storedEventMatcher = storageEventPattern.matcher(eventContent);

        if (!storedEventMatcher.matches()) {
            throw new EclipseException("StorageParser failed to parse this content as Event : \n" + eventContent);
        }

        String eventDescription = storedEventMatcher.group("taskDescription");
        String from = storedEventMatcher.group("from");
        String to = storedEventMatcher.group("to");

        return new Event(eventDescription, isDone, from, to);
    }
}
