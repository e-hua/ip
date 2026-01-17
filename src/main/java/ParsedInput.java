import java.util.Optional;

public class ParsedInput {
    private Command command;
    private final Optional<String> optionalParams;
    private final Optional<String> optionalFrom;
    private final Optional<String> optionalTo;
    private final Optional<String> optionalBy;

    public ParsedInput(
            Command command,
            Optional<String> optionalParams,
            Optional<String> optionalFrom,
            Optional<String> optionalTo,
            Optional<String> optionalBy
    ) {
        this.command = command;
        this.optionalParams = optionalParams;
        this.optionalFrom = optionalFrom;
        this.optionalTo = optionalTo;
        this.optionalBy = optionalBy;
    }

    public ParsedInput(
            Command command,
            Optional<String> optionalParams) {
        this(command, optionalParams, Optional.empty(), Optional.empty(), Optional.empty());
    }

    public ParsedInput(
            Command command,
            String params,
            String to,
            String from) {
        this(command, Optional.of(params), Optional.of(from), Optional.of(to), Optional.empty());
    }

    public ParsedInput(
            Command command,
            String params,
            String by) {
        this(command, Optional.of(params), Optional.empty(), Optional.empty(), Optional.of(by));
    }

    public Command getCommand() {
        return this.command;
    }

    public Optional<String> getOptionalParams() {
        return this.optionalParams;
    }

    public String getParams() throws IllegalArgumentException {
        return this.optionalParams.orElseThrow(() -> new IllegalArgumentException("No parameter provided in user input"));
    }

    public String getBy() throws IllegalArgumentException {
        return this.optionalBy.orElseThrow(() -> new IllegalArgumentException("No 'by' field provided in user input"));
    }

    public String getTo() throws IllegalArgumentException {
        return this.optionalTo.orElseThrow(() -> new IllegalArgumentException("No 'to' field provided in user input"));
    }

    public String getFrom() throws IllegalArgumentException {
        return this.optionalFrom.orElseThrow(() -> new IllegalArgumentException("No 'from' field provided in user input"));
    }
}