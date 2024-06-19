package ubikesystem;

/**
 * The implementation of a custom exception in the system.
 * The exception is thrown when the system encounters an error.
 */

public class CustomException extends Exception {
    public CustomException(String message) {
        super(message);
    }
}