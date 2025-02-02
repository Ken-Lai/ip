package tasket.exception;

/**
 * The class for Tasket Exception
 */
public class TasketException extends Exception {

    /**
     * Constructs a Tasket Exception.
     *
     * @param message The error message to be shown.
     */
    public TasketException(String message) {
        super(message);
    }
}
