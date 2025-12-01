package dbQuery;
import model.Utente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import db.dbConnection;


public class UtenteDB {
    private Connection dbConn;

    public UtenteDB() throws Exception {
        try {
            dbConn = dbConnection.getInstance().getConnection();
        } catch (Exception e) {
            throw new Exception("Errore durante la connessione al database: " + e.getMessage());
        }
    }

    public void registraUtente(String username, String email, String password, String numeroTelefono) throws SQLException {
        // Implementazione della logica per registrare l'utente nel database
        String sql = "INSERT INTO utenti (username, email, password, numeroTelefono) VALUES (?, ?, ?, ?)";

        try (PreparedStatement registratiPS = dbConn.prepareStatement(sql)) {
            registratiPS.setString(1, username);
            registratiPS.setString(2, email);
            registratiPS.setString(3, password);
            registratiPS.setString(4, numeroTelefono);
            registratiPS.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Utente autenticaUtente(String username, String password) throws SQLException {
        // Implementazione della logica per autenticare l'utente
        String sql = "SELECT * FROM utenti WHERE username = ? AND password = ?";

        try (PreparedStatement autenticaPS = dbConn.prepareStatement(sql)) {
            autenticaPS.setString(1, username);
            autenticaPS.setString(2, password);

            var rs = autenticaPS.executeQuery();
            if (rs.next()) {
                String email = rs.getString("email");
                String numeroTelefono = rs.getString("numeroTelefono");
                return new Utente(username, email, password, numeroTelefono);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
