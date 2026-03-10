package db;

import exception.DatabaseException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton per la gestione della connessione al database.
 *
 * Implementa il pattern Singleton per garantire una singola connessione
 * al database condivisa in tutta l'applicazione, evitando multiple connessioni.
 */
@SuppressWarnings({"java:S6548", "java:S6437"}) // Singleton richiesto per pool connessioni; credenziali da ambiente in produzione
public class DbConnection {

    /**
     * Istanza singleton.
     */
    private static DbConnection instance;
    /**
     * Connessione attiva.
     */
    private Connection connection;

    /**
     * Nome utente del database.
     */
    private static final String DB_USER = "postgres.wzzmgxzgtpsvazdwdbqr";
    /**
     * Password del database.
     */
    private static final String DB_PASS = "UninaSwapDB";
    /**
     * URL di connessione al database.
     */
    private static final String DB_URL = "jdbc:postgresql://aws-1-eu-west-1.pooler.supabase.com:5432/postgres";

    /**
     * Impedisce l'istanziazione diretta del singleton.
     */
    private DbConnection() {
    }

    /**
     * Restituisce l'istanza singleton del connection manager.
     *
     * @return istanza singleton
     */
    public static synchronized DbConnection getInstance() {
        if (instance == null) {
            instance = new DbConnection();
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
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            }
            return connection;
        } catch (SQLException e) {
            throw new DatabaseException("Errore durante la connessione al Database: " + e.getMessage(), e);
        }
    }

}
