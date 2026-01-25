package eclipse.parser;

import eclipse.exceptions.EclipseException;
import eclipse.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handles the parsing of user input strings into executable {@link ParsedInput} objects.
 * This class uses <code> regular expressions(Regex) <code/> to
 * identify commands and extract their parameters.
 */
public class Parser {

    /**
     * Pattern for initial splitting of input.
     * Captures the first word as "command" and the rest as "params".
     * The "params" group uses a lazy quantifier to avoid capturing trailing spaces.
     */
    private static final Pattern inputPattern = Pattern.compile(
            "\\s*"     // spaces (zero or more times)
                    + "(?<command>\\S+)" // optional non-space command (one or more times)
                    + "(?:" // A non-captured group
                    + "\\s+" // spaces (one or more times) as prefix
                    + "(?<params>.*?)" // optional params, of any character, any length(lazy)
                    + ")?" // This non-captured group is optional
                    + "\\s*" // spaces (zero or more times)
    );

    /**
     * Pattern to extract the task description and deadline date.
     */
    private static final Pattern deadlinePattern = Pattern.compile(
            // return book /by 2019-10-15
            "(?<subject>.*?)\\s+/by\\s+(?<date>.*?)\\s*"
    );

    /**
     * Pattern to extract the event description, start date, and end date.
     */
    private static final Pattern eventPattern = Pattern.compile(
            // project meeting /from 2019-10-15 /to 2019-10-16
            "(?<subject>.*?)\\s+/from\\s+(?<fromDate>.*?)\\s+/to\\s+(?<toDate>.*?)\\s*"
    );

    /**
     * Parses the raw user input and converts it into a {@link ParsedInput}.
     * This method identifies the command type and further parses parameters for
     * complex tasks like Deadlines and Events.
     *
     * @param input The raw string entered by the user.
     * @return A {@link ParsedInput} object containing the identified command and data.
     * @throws EclipseException If the input format is invalid or if date strings
     *                          do not follow the yyyy-mm-dd format.
     */
    public static ParsedInput parse(String input) throws EclipseException {
        Matcher inputPatternMatcher = inputPattern.matcher(input);

        if (!inputPatternMatcher.matches()) {
            throw new EclipseException("Failed to parse, invalid input format detected: '" + input + "'");
        }

        String commandStr = inputPatternMatcher.group("command");
        Optional<String> maybeParams = Optional.ofNullable(inputPatternMatcher.group("params"));

        return switch (commandStr) {
            case "bye" -> new ParsedInput(Command.BYE, maybeParams);
            case "list" -> new ParsedInput(Command.LIST, maybeParams);
            case "mark" -> new ParsedInput(Command.MARK, maybeParams);
            case "unmark" -> new ParsedInput(Command.UNMARK, maybeParams);
            case "delete" -> new ParsedInput(Command.DELETE, maybeParams);
            case "todo" -> new ParsedInput(Command.TODO, maybeParams);
            case "deadline" -> {
                Matcher deadlinePatternMatcher = deadlinePattern.matcher(maybeParams.orElseThrow());
                if (!deadlinePatternMatcher.matches()) {
                    throw new EclipseException(
                            "Invalid input format for command type 'deadline': " + maybeParams.orElseThrow()
                    );
                }

                String dateString = deadlinePatternMatcher.group("date");
                try {
                    LocalDate date = LocalDate.parse(dateString);
                    yield new ParsedInput(
                            Command.DEADLINE,
                            deadlinePatternMatcher.group("subject"),
                            date
                    );
                } catch (DateTimeParseException e) {
                    throw new EclipseException(
                            "Invalid date format for attribute 'by' in 'deadline' task: " + dateString,
                            e
                    );
                }
            }
            case "event" -> {
                Matcher eventPatternMatcher = eventPattern.matcher(maybeParams.orElseThrow());
                if (!eventPatternMatcher.matches()) {
                    throw new EclipseException(
                            "Invalid input format for command type 'event': " + maybeParams.orElseThrow()
                    );
                }

                String from = eventPatternMatcher.group("fromDate");
                String to = eventPatternMatcher.group("toDate");
                try {
                    LocalDate fromDate = LocalDate.parse(from);
                    LocalDate toDate = LocalDate.parse(to);
                    yield new ParsedInput(
                            Command.EVENT,
                            eventPatternMatcher.group("subject"),
                            fromDate,
                            toDate
                    );
                } catch (DateTimeParseException e) {
                    throw new EclipseException(
                            "Invalid date format for attribute 'from' or 'to' in 'event' task: " + from + "/" + to,
                            e
                    );
                }
            }
            default -> new ParsedInput(Command.INVALID, maybeParams);
        };
    }

    /**
     * Converts a 1-based index string from user input into a 0-based integer.
     * Validates that the index is a number and falls within the current list boundaries.
     *
     * @param inputIndex The string representation of the task number (e.g., "1").
     * @param chatbot    The {@link Eclipse} instance used to check the current task count.
     * @return The validated 0-based index as an integer.
     * @throws EclipseException If the input is not a number or the index is out of bounds.
     */
    public static int parseListIndex(String inputIndex, Eclipse chatbot) throws EclipseException {
        int inputIndexParsed;
        try {
            inputIndexParsed = Integer.parseInt(inputIndex);
        } catch (Exception e) {
            throw new EclipseException(
                    String.format("Invalid input index detected, wrong format: %s", inputIndex),
                    e
            );
        }

        if (inputIndexParsed <= 0 || inputIndexParsed > chatbot.getNumberOfTasks()) {
            throw new EclipseException(
                    String.format("Index %s not in the list, length of the list: %d", inputIndex, chatbot.getNumberOfTasks())
            );
        }

        return inputIndexParsed - 1;
    }
}
