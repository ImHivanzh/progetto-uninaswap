package dao;

import model.Utente;
import utils.DataCheck;
import db.dbConnection;
import exception.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UtenteDAO {

    private Connection con;

    /**
     * Creates the DAO and initializes the database connection.
     */
    public UtenteDAO() {
        try {
            this.con = dbConnection.getInstance().getConnection();
        } catch (DatabaseException e) {
            System.err.println("Errore connessione DB in UtenteDAO: " + e.getMessage());
        }
    }

    /**
     * Registers a new user after validating the input.
     *
     * @param username username
     * @param email email address
     * @param password password
     * @param numeroTelefono phone number
     * @return true when the insert succeeds
     * @throws DatabaseException when the insert fails
     */
    public boolean registraUtente(String username, String email, String password, String numeroTelefono)
            throws DatabaseException {
        if (email != null) email = email.trim();
        if (username != null) username = username.trim();
        if (numeroTelefono != null) numeroTelefono = numeroTelefono.trim();

        if (!DataCheck.isValidEmail(email)) throw new IllegalArgumentException("Formato email non valido.");
        if (!DataCheck.isValidPhoneNumber(numeroTelefono)) throw new IllegalArgumentException("Numero di telefono non valido (richieste 10 cifre).");
        if (!DataCheck.isStrongPassword(password)) throw new IllegalArgumentException("Password debole: serve min. 8 caratteri, una maiuscola, un numero e un carattere speciale.");
        if (esisteUtente(username)) throw new IllegalArgumentException("L'username '" + username + "' e gia in uso.");

        if (con == null) throw new DatabaseException("Connessione non disponibile");

        String sql = "INSERT INTO utente (nomeutente, mail, password, numerotelefono) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.setString(4, numeroTelefono);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Errore durante la registrazione dell'utente", e);
        }
    }

    /**
     * Registers a user using a model instance.
     *
     * @param utente user instance
     * @return true when the insert succeeds
     * @throws DatabaseException when the insert fails
     */
    public boolean registraUtente(Utente utente) throws DatabaseException {
        return registraUtente(utente.getUsername(), utente.getEmail(), utente.getPassword(), utente.getNumeroTelefono());
    }

    /**
     * Authenticates a user by username and password.
     *
     * @param username username
     * @param password password
     * @return user instance when found, otherwise null
     * @throws DatabaseException when the query fails
     */
    public Utente autenticaUtente(String username, String password) throws DatabaseException {
        String sql = "SELECT * FROM utente WHERE nomeutente = ? AND password = ?";

        if (con == null) return null;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Utente(
                            rs.getInt("idutente"),
                            username,
                            password,
                            rs.getString("mail"),
                            rs.getString("numerotelefono")
                    );
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Errore durante l'autenticazione", e);
        }
        return null;
    }

    /**
     * Returns a user by id.
     *
     * @param id user id
     * @return user instance or null
     * @throws DatabaseException when the query fails
     */
    public Utente getUserByID(int id) throws DatabaseException {
        String sql = "SELECT * FROM utente WHERE idutente = ?";

        if (con == null) throw new DatabaseException("Connessione non disponibile");

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Utente(
                            rs.getInt("idutente"),
                            rs.getString("nomeutente"),
                            rs.getString("password"),
                            rs.getString("mail"),
                            rs.getString("numerotelefono")
                    );
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Errore durante il recupero dell'utente per ID", e);
        }
        return null;
    }

    /**
     * Checks if a username already exists.
     *
     * @param username username to check
     * @return true when the user exists
     * @throws DatabaseException when the query fails
     */
    public boolean esisteUtente(String username) throws DatabaseException {
        String sql = "SELECT 1 FROM utente WHERE nomeutente = ?";
        if (con == null) return false;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new DatabaseException("Errore durante la verifica dell'utente", e);
        }
    }

    /**
     * Updates a user's password after validation.
     *
     * @param username username
     * @param nuovaPassword new password
     * @return true when the update succeeds
     * @throws DatabaseException when the update fails
     */
    public boolean aggiornaPassword(String username, String nuovaPassword) throws DatabaseException {
        if (!esisteUtente(username)) throw new IllegalArgumentException("Utente non trovato: " + username);
        if (!DataCheck.isStrongPassword(nuovaPassword)) throw new IllegalArgumentException("La nuova password non rispetta i criteri di sicurezza.");

        String sql = "UPDATE utente SET password = ? WHERE nomeutente = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nuovaPassword);
            ps.setString(2, username);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Errore durante l'aggiornamento della password", e);
        }
    }
}
