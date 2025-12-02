package dbQuery;

import model.Utente;
import db.dbConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet; // Importante importarlo
import java.sql.SQLException;

public class UtenteDB {

    public UtenteDB() {
    }

    public void registraUtente(String username, String email, String password, String numeroTelefono) throws SQLException {
        String sql = "INSERT INTO utente (nomeutente, mail, password, numerotelefono) VALUES (?, ?, ?, ?)";

        try (Connection conn = dbConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.setString(4, numeroTelefono);
            ps.executeUpdate();

        }
    }

    public Utente autenticaUtente(String username, String password) throws SQLException {
        String sql = "SELECT * FROM utente WHERE nomeutente = ? AND password = ?";

        try (Connection conn = dbConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) { // Anche il ResultSet va nel try
                if (rs.next()) {
                    String email = rs.getString("mail");
                    String numTel = rs.getString("numerotelefono");
                    return new Utente(username, email, password, numTel);
                }
            }
        }
        return null;
    }
}
