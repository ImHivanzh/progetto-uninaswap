package exception;

public class DatabaseException extends Exception {

  /**
   * Crea eccezione con messaggio.
   *
   * @param message errore messaggio
   */
  public DatabaseException(String message) {
    super(message);
  }

  /**
   * Crea eccezione con messaggio e causa.
   *
   * @param message errore messaggio
   * @param cause causa principale
   */
  public DatabaseException(String message, Throwable cause) {
    super(message, cause);
  }
}
