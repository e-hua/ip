package eclipse.parser;

import eclipse.exceptions.EclipseException;
import eclipse.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    // This regular expression stands for:

    /*
    \\s*: spaces (zero or more times)
        (?<command>\\S+): optional non-space command (one or more times)
    \\s+: spaces (one or more times)
        (?: A non-captured group
            \\s+: spaces (one or more times) as prefix
            (?<params>.*?): optional params, of any character, any length,
            P.S.
                Note that here I used ".*?"(the lazy version) instead of ".*"(the greedy version),
                To prevent any trailing space characters from being included in the arguments
        )? Which is optional
    \\s*: spaces (zero or more times)
     */
    private static final Pattern inputPattern = Pattern.compile(
            "\\s*(?<command>\\S+)(?:\\s+(?<params>.*?))?\\s*"
    );

    // return book /by 2019-10-15
    private static final Pattern deadlinePattern = Pattern.compile(
            "(?<subject>.*?)\\s+/by\\s+(?<date>.*?)\\s*"
    );

    // project meeting /from 2019-10-15 /to 2019-10-16
    private static final Pattern eventPattern = Pattern.compile(
            "(?<subject>.*?)\\s+/from\\s+(?<fromDate>.*?)\\s+/to\\s+(?<toDate>.*?)\\s*"
    );

    public static ParsedInput parse(String input) throws EclipseException {
        Matcher inputPatternMatcher = inputPattern.matcher(input);

        if (!inputPatternMatcher.matches()) {
            return new ParsedInput(Command.INVALID, Optional.empty());
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
