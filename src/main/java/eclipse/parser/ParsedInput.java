package eclipse.parser;

import eclipse.exceptions.EclipseException;
import eclipse.Command;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Encapsulates the results of parsing a user command.
 * Stores the command type and any associated parameters like descriptions,
 * start dates, end dates, or deadlines.
 */
public class ParsedInput {
    private Command command;
    private final Optional<String> optionalParams;
    private final Optional<LocalDate> optionalFrom;
    private final Optional<LocalDate> optionalTo;
    private final Optional<LocalDate> optionalBy;

    /**
     * Initializes a ParsedInput with all possible fields.
     *
     * @param command        The command type identified.
     * @param optionalParams The main description or parameters of the command.
     * @param optionalFrom   The start date for event tasks.
     * @param optionalTo     The end date for event tasks.
     * @param optionalBy     The deadline date for deadline tasks.
     */
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

    /**
     * Initializes a ParsedInput for commands that only require a description (e.g., TODO).
     *
     * @param command        The command type.
     * @param optionalParams The task description.
     */
    public ParsedInput(
            Command command,
            Optional<String> optionalParams) {
        this(command, optionalParams, Optional.empty(), Optional.empty(), Optional.empty());
    }

    /**
     * Initializes a ParsedInput specifically for Event commands.
     *
     * @param command The command type (expected to be EVENT).
     * @param params  The event description.
     * @param from    The start date of the event.
     * @param to      The end date of the event.
     */
    public ParsedInput(
            Command command,
            String params,
            LocalDate from,
            LocalDate to) {
        this(command, Optional.of(params), Optional.of(from), Optional.of(to), Optional.empty());
    }

    /**
     * Initializes a ParsedInput specifically for Deadline commands.
     *
     * @param command The command type (expected to be DEADLINE).
     * @param params  The deadline description.
     * @param by      The date of the deadline.
     */
    public ParsedInput(
            Command command,
            String params,
            LocalDate by) {
        this(command, Optional.of(params), Optional.empty(), Optional.empty(), Optional.of(by));
    }

    /**
     * Returns the command type identified by the parser.
     *
     * @return The command type.
     */
    public Command getCommand() {
        return this.command;
    }

    /**
     * Returns the raw optional containing the command parameters.
     *
     * @return An Optional containing the parameter string if present.
     */
    public Optional<String> getOptionalParams() {
        return this.optionalParams;
    }

    /**
     * Retrieves the parameters/description of the command.
     *
     * @return The parameter string.
     * @throws EclipseException If the parameter field is empty or not detected.
     */
    public String getParams() throws EclipseException {
        try {
            return this.optionalParams.orElseThrow();
        } catch (Exception e) {
            throw new EclipseException("The parameter is not detected in the parsed output", e);
        }
    }

    /**
     * Retrieves the deadline date from the parsed input.
     *
     * @return The LocalDate representing the deadline.
     * @throws EclipseException If the 'by' field is not present in the parsed output.
     */
    public LocalDate getBy() throws EclipseException {
        try {
            return this.optionalBy.orElseThrow();
        } catch (Exception e) {
            throw new EclipseException("The 'by' field is not detected in parsed output", e);
        }
    }

    /**
     * Retrieves the end date of an event.
     *
     * @return The LocalDate representing the end of the event.
     * @throws EclipseException If the 'to' field is not present in the parsed output.
     */
    public LocalDate getTo() throws EclipseException {
        try {
            return this.optionalTo.orElseThrow();
        } catch (Exception e) {
            throw new EclipseException("The 'to' field is not detected in parsed output", e);
        }
    }

    /**
     * Retrieves the start date of an event.
     *
     * @return The LocalDate representing the start of the event.
     * @throws EclipseException If the 'from' field is not present in the parsed output.
     */
    public LocalDate getFrom() throws EclipseException {
        try {
            return this.optionalFrom.orElseThrow();
        } catch (Exception e) {
            throw new EclipseException("The 'from' field is not detected in parsed output", e);
        }
    }
}