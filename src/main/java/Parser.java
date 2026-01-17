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
            (?<params>.*): optional params, of any character, any length,
        )? Which is optional
    \\s*: spaces (zero or more times)
     */
    private static final Pattern inputPattern = Pattern.compile(
            "\\s*(?<command>\\S+)(?:\\s+(?<params>.*))?\\s*"
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
            default -> new ParsedInput(Command.INVALID, maybeParams);
        };
    }
}
