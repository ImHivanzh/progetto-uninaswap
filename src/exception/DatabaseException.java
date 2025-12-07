package exception; // O nel package che preferisci

public class DatabaseException extends Exception {

  // Costruttore con messaggio personalizzato
  public DatabaseException(String message) {
    super(message);
  }

  public DatabaseException(String message, Throwable cause) {
    super(message, cause);
  }
}