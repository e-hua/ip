import java.util.Optional;

public class ParsedInput {
    private Command command;
    private final Optional<String> optionalParams;

    public ParsedInput(Command command, Optional<String> optionalParams) {
        this.command = command;
        this.optionalParams = optionalParams;
    }

    public Command getCommand() {
        return this.command;
    }

    public Optional<String> getOptionalParams() {
        return this.optionalParams;
    }
}
