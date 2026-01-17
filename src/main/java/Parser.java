import Exceptions.EclipseException;

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

    // return book /by Sunday
    private static final Pattern deadlinePattern = Pattern.compile(
            "(?<subject>.*?)\\s+/by\\s+(?<date>.*?)\\s*"
    );

    // project meeting /from Mon 2pm /to 4pm
    private static final Pattern eventPattern = Pattern.compile(
            "(?<subject>.*?)\\s+/from\\s+(?<fromDate>.*?)\\s+/to\\s+(?<toDate>.*?)\\s*"
    );

    public static ParsedInput parse(String input) {
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
            case "todo" -> new ParsedInput(Command.TODO, maybeParams);
            case "deadline" -> {
                Matcher deadlinePatternMatcher = deadlinePattern.matcher(maybeParams.orElseThrow());
                if (!deadlinePatternMatcher.matches()) {
                    yield new ParsedInput(Command.INVALID, maybeParams);
                }
                yield new ParsedInput(
                        Command.DEADLINE,
                        deadlinePatternMatcher.group("subject"),
                        deadlinePatternMatcher.group("date")
                );
            }
            case "event" -> {
                Matcher eventPatternMatcher = eventPattern.matcher(maybeParams.orElseThrow());
                if (!eventPatternMatcher.matches()) {
                    yield new ParsedInput(Command.INVALID, maybeParams);
                }

                yield new ParsedInput(
                        Command.EVENT,
                        eventPatternMatcher.group("subject"),
                        eventPatternMatcher.group("toDate"),
                        eventPatternMatcher.group("fromDate")
                );
            }

            default -> new ParsedInput(Command.INVALID, maybeParams);
        };
    }

    public static int parseListIndex(String inputIndex) throws EclipseException {
        int inputIndexParsed;
        try {
            inputIndexParsed = Integer.parseInt(inputIndex);
        } catch (Exception e) {
            throw new EclipseException(
                    String.format("Invalid input index detected, wrong format: %s", inputIndex),
                    e
            );
        }

        if (inputIndexParsed <= 0 || inputIndexParsed > Eclipse.getNumberOfTasks()) {
            throw new EclipseException(
                    String.format("Index %s not in the list, length of the list: %d", inputIndex, Eclipse.getNumberOfTasks())
            );
        }

        return inputIndexParsed - 1;
    }
}
