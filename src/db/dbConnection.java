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
     * Prevents direct instantiation of the singleton.
     */
    private dbConnection() {
    }

    /**
     * Returns the shared connection manager instance.
     *
     * @return singleton instance
     */
    public static synchronized dbConnection getInstance() {
        if (instance == null) {
            instance = new dbConnection();
        }
        return instance;
    }

    /**
     * Returns an active connection, opening it when needed.
     *
     * @return active JDBC connection
     * @throws DatabaseException when a connection cannot be established
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
     * Closes the active connection if open.
     *
     * @throws DatabaseException when closing the connection fails
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
