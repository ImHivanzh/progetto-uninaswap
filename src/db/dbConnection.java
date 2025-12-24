package db;

import exception.DatabaseException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class dbConnection {

    private static dbConnection instance;
    private Connection connection;

    private static final String NOME = "postgres.wzzmgxzgtpsvazdwdbqr";
    private static final String PASSWORD = "UninaSwapDB";
    private static final String URL = "jdbc:postgresql://aws-1-eu-west-1.pooler.supabase.com:5432/postgres";

    /**
     * Impedisce diretto istanziazione di singleton.
     */
    private dbConnection() {
    }

    /**
     * Restituisce condiviso connessione manager istanza.
     *
     * @return singleton istanza
     */
    public static synchronized dbConnection getInstance() {
        if (instance == null) {
            instance = new dbConnection();
        }
        return instance;
    }

    /**
     * Restituisce attivo connessione, aprendola quando necessario.
     *
     * @return attivo JDBC connessione
     * @throws DatabaseException quando connessione non puo essere stabilita
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

    /**
     * Chiude attivo connessione se aperto.
     *
     * @throws DatabaseException quando chiusura connessione fallisce
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
