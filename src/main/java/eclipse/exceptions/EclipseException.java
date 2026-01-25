package eclipse.exceptions;

/**
 * Represents an exception thrown by the Eclipse chatbot when
 * input is invalid or the chatbot has an internal failure
 */
public class EclipseException extends Exception {
    /**
     * Constructs a new EclipseException with the specified message
     *
     * @param message The message contained in the exception
     */
    public EclipseException(String message) {
        super(message);
    }

    /**
     * Constructs a new EclipseException with the specified message and throwable cause
     *
     * @param message The message contained in the exception
     * @param cause   The cause of this exception
     */
    public EclipseException(String message, Throwable cause) {
        super(message, cause);
    }
}
