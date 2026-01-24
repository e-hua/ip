package eclipse.exceptions;

public class EclipseException extends Exception {
    public EclipseException(String message) {
        super(message);
    }

    public EclipseException(String message, Throwable cause) {
        super(message, cause);
    }
}
