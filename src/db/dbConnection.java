package db;

import exception.DatabaseException;

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
    private static final String NOME = "postgres.wzzmgxzgtpsvazdwdbqr";
    private static final String PASSWORD = "UninaSwapDB"; // Lascia vuoto se non hai password locale
    private static final String URL = "jdbc:postgresql://aws-1-eu-west-1.pooler.supabase.com:5432/postgres";

    /**
     * Costruttore privato per impedire l'istanziazione diretta.
     */
    private dbConnection() {
        // Lazy Loading
    }

    /**
     * Restituisce l'istanza singleton della classe Manager.
     */
    public static synchronized dbConnection getInstance() {
        if (instance == null) {
            instance = new dbConnection();
        }
        return instance;
    }

    /**
     * Restituisce l'oggetto {@code Connection} attivo.
     * @throws DatabaseException se la connessione non può essere stabilita.
     */
    public Connection getConnection() throws DatabaseException {
        try {
            if (connection == null || connection.isClosed()) {
                // Utilizzo le costanti definite in alto
                connection = DriverManager.getConnection(URL, NOME, PASSWORD);
            }
            return connection;
        } catch (SQLException e) {
            // Avvolgiamo l'eccezione SQL nella nostra eccezione personalizzata
            throw new DatabaseException("Errore durante la connessione al Database: " + e.getMessage(), e);
        }
    }

    /**
     * Chiude la connessione al database se aperta.
     */
    public void closeConnection() throws DatabaseException {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new DatabaseException("Errore durante la chiusura della connessione", e);
        }
    }
}