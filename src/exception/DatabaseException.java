package exception;

public class DatabaseException extends Exception {

  /**
   * Creates an exception with a message.
   *
   * @param message error message
   */
  public DatabaseException(String message) {
    super(message);
  }

  /**
   * Creates an exception with a message and cause.
   *
   * @param message error message
   * @param cause root cause
   */
  public DatabaseException(String message, Throwable cause) {
    super(message, cause);
  }
}
