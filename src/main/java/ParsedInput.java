import Exceptions.EclipseException;

import java.time.LocalDate;
import java.util.Optional;

public class ParsedInput {
    private Command command;
    private final Optional<String> optionalParams;
    private final Optional<LocalDate> optionalFrom;
    private final Optional<LocalDate> optionalTo;
    private final Optional<LocalDate> optionalBy;

    public ParsedInput(
            Command command,
            Optional<String> optionalParams,
            Optional<LocalDate> optionalFrom,
            Optional<LocalDate> optionalTo,
            Optional<LocalDate> optionalBy
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
            LocalDate from,
            LocalDate to) {
        this(command, Optional.of(params), Optional.of(from), Optional.of(to), Optional.empty());
    }

    public ParsedInput(
            Command command,
            String params,
            LocalDate by) {
        this(command, Optional.of(params), Optional.empty(), Optional.empty(), Optional.of(by));
    }

    public Command getCommand() {
        return this.command;
    }

    public Optional<String> getOptionalParams() {
        return this.optionalParams;
    }

    public String getParams() throws EclipseException {
        try {
            return this.optionalParams.orElseThrow();
        } catch (Exception e) {
            throw new EclipseException("The parameter is not detected in the parsed output", e);
        }
    }

    public LocalDate getBy() throws EclipseException {
        try {
            return this.optionalBy.orElseThrow();
        } catch (Exception e) {
            throw new EclipseException("The 'by' field is not detected in parsed output", e);
        }
    }

    public LocalDate getTo() throws EclipseException {
        try {
            return this.optionalTo.orElseThrow();
        } catch (Exception e) {
            throw new EclipseException("The 'to' field is not detected in parsed output", e);
        }
    }

    public LocalDate getFrom() throws EclipseException {
        try {
            return this.optionalFrom.orElseThrow();
        } catch (Exception e) {
            throw new EclipseException("The 'from' field is not detected in parsed output", e);
        }
    }
}