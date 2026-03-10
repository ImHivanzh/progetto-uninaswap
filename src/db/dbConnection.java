package db;

import exception.DatabaseException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton per la gestione della connessione al database.
 */
public class dbConnection {

    /**
     * Istanza singleton.
     */
    private static dbConnection instance;
    /**
     * Connessione attiva.
     */
    private Connection connection;

    /**
     * Nome utente del database.
     */
    private static final String NOME = "postgres.wzzmgxzgtpsvazdwdbqr";
    /**
     * Password del database.
     */
    private static final String PASSWORD = "UninaSwapDB";
    /**
     * URL di connessione al database.
     */
    private static final String URL = "jdbc:postgresql://aws-1-eu-west-1.pooler.supabase.com:5432/postgres";

    /**
     * Impedisce l'istanziazione diretta del singleton.
     */
    private dbConnection() {
    }

    /**
     * Restituisce l'istanza singleton del connection manager.
     *
     * @return istanza singleton
     */
    public static synchronized dbConnection getInstance() {
        if (instance == null) {
            instance = new dbConnection();
        }
        return instance;
    }

    /**
     * Restituisce la connessione attiva, aprendola se necessario.
     *
     * @return connessione JDBC attiva
     * @throws DatabaseException se la connessione non può essere stabilita
     */
    public Connection getConnection() throws DatabaseException {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, NOME, PASSWORD);
            }
            return connection;
        } catch (SQLException e) {
            throw new DatabaseException("Errore durante la connessione al Database: " + e.getMessage(), e);
        }
    }

}
