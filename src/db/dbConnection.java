package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe singleton per la gestione della connessione al database PostgreSQL.
 * Gestisce automaticamente la riconnessione se la connessione viene chiusa.
 */
public class dbConnection {

    private static dbConnection instance;
    private Connection connection;

    // Costanti di configurazione del Database
    private static final String NOME = "ivanbuonocore";
    private static final String PASSWORD = ""; // Lascia vuoto se non hai password locale
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";

    /**
     * Costruttore privato per impedire l'istanziazione diretta.
     */
    private dbConnection() {
        // Non apriamo la connessione qui.
        // Ci pensa il metodo getConnection() a farlo quando serve (Lazy Loading).
    }

    /**
     * Restituisce l'istanza singleton della classe Manager.
     * @return l'unica istanza di {@code dbConnection}
     */
    public static synchronized dbConnection getInstance() {
        if (instance == null) {
            instance = new dbConnection();
        }
        return instance;
    }

    /**
     * Restituisce l'oggetto {@code Connection} attivo.
     * Se la connessione è null o è stata chiusa (es. dal try-with-resources),
     * ne apre una nuova automaticamente.
     *
     * @return l'oggetto {@code Connection} valido.
     * @throws SQLException se la connessione non può essere stabilita.
     */
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // Utilizzo le costanti definite in alto
                connection = DriverManager.getConnection(URL, NOME, PASSWORD);
            } catch (SQLException e) {
                // Rilancio l'eccezione per gestirla nella GUI (mostrare errore all'utente)
                throw new SQLException("Errore durante la connessione al Database: " + e.getMessage());
            }
        }
        return connection;
    }

    /**
     * Chiude la connessione al database se aperta.
     * Utile alla chiusura dell'applicazione.
     */
    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}