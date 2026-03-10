package dao;

import model.Utente;
import utils.DataCheck;
import utils.Logger;
import db.dbConnection;
import exception.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO per l'accesso ai dati degli utenti.
 */
public class UtenteDAO {

    /**
     * Connessione al database.
     */
    private Connection con;

    /**
     * Crea il DAO e inizializza la connessione al database.
     */
    public UtenteDAO() {
        try {
            this.con = dbConnection.getInstance().getConnection();
        } catch (DatabaseException e) {
            Logger.error("Errore connessione DB in UtenteDAO", e);
        }
    }

    /**
     * Registra un nuovo utente dopo aver validato l'input.
     *
     * @param username username
     * @param email indirizzo email
     * @param password password
     * @param numeroTelefono numero di telefono
     * @return true se l'inserimento ha successo
     * @throws DatabaseException se l'inserimento fallisce
     */
    public boolean registraUtente(String username, String email, String password, String numeroTelefono)
            throws DatabaseException {
        if (email != null) email = email.trim();
        if (username != null) username = username.trim();
        if (numeroTelefono != null) numeroTelefono = numeroTelefono.trim();

        if (!DataCheck.isValidEmail(email)) throw new IllegalArgumentException("Formato email non valido.");
        if (!DataCheck.isValidPhoneNumber(numeroTelefono)) throw new IllegalArgumentException("Numero di telefono non valido (richieste 10 cifre).");
        if (!DataCheck.isStrongPassword(password)) throw new IllegalArgumentException("Password debole: serve min. 8 caratteri, una maiuscola, un numero e un carattere speciale.");
        if (esisteUtente(username)) throw new IllegalArgumentException("L'username '" + username + "' è già in uso.");

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
     * Registra un utente usando un'istanza del modello.
     *
     * @param utente istanza di Utente
     * @return true se l'inserimento ha successo
     * @throws DatabaseException se l'inserimento fallisce
     */
    public boolean registraUtente(Utente utente) throws DatabaseException {
        return registraUtente(utente.getUsername(), utente.getEmail(), utente.getPassword(), utente.getNumeroTelefono());
    }

    /**
     * Autentica un utente dato username e password.
     *
     * @param username username
     * @param password password
     * @return istanza di Utente se trovato, altrimenti null
     * @throws DatabaseException se la query fallisce
     */
    public Utente autenticaUtente(String username, String password) throws DatabaseException {
        String sql = "SELECT idutente, nomeutente, password, mail, numerotelefono FROM utente WHERE nomeutente = ? AND password = ?";

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
     * Restituisce un utente dato l'ID.
     *
     * @param id ID dell'utente
     * @return istanza di Utente, o null se non trovato
     * @throws DatabaseException se la query fallisce
     */
    public Utente getUserByID(int id) throws DatabaseException {
        String sql = "SELECT idutente, nomeutente, password, mail, numerotelefono FROM utente WHERE idutente = ?";

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
     * Restituisce un utente dato l'username.
     *
     * @param username username
     * @return istanza di Utente, o null se non trovato
     * @throws DatabaseException se la query fallisce
     */
    public Utente getUserByUsername(String username) throws DatabaseException {
        String sql = "SELECT idutente, nomeutente, password, mail, numerotelefono FROM utente WHERE nomeutente = ?";

        if (con == null) throw new DatabaseException("Connessione non disponibile");

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);

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
            throw new DatabaseException("Errore durante il recupero dell'utente per username", e);
        }
        return null;
    }

    /**
     * Verifica se l'username esiste già.
     *
     * @param username username da verificare
     * @return true se l'utente esiste
     * @throws DatabaseException se la query fallisce
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
     * Aggiorna la password dell'utente dopo validazione.
     *
     * @param username username
     * @param nuovaPassword nuova password
     * @return true se l'aggiornamento ha successo
     * @throws DatabaseException se l'aggiornamento fallisce
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
