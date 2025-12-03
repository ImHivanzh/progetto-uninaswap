package dbQuery;

import model.Utente;
import utils.DataCheck;
import db.dbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UtenteDB {

    public UtenteDB() {
    }

    /**
     * Tenta di registrare un nuovo utente.
     * @throws IllegalArgumentException se i dati non sono validi o l'utente esiste già.
     * @throws SQLException se c'è un errore tecnico del database.
     */
    public boolean registraUtente(String username, String email, String password, String numeroTelefono) throws SQLException {

        // Pulizia input: rimuovo spazi vuoti accidentali all'inizio/fine
        // Spesso il problema "formato non valido" è dato da uno spazio invisibile
        if (email != null) email = email.trim();
        if (username != null) username = username.trim();
        if (numeroTelefono != null) numeroTelefono = numeroTelefono.trim();

        // 1. Controllo Validità Dati (usa utils.DataCheck)
        if (!DataCheck.isValidEmail(email)) {
            // Aggiungo il valore nel messaggio per capire meglio cosa sta fallendo
            throw new IllegalArgumentException("Formato email non valido: '" + email + "'");
        }

        if (!DataCheck.isValidPhoneNumber(numeroTelefono)) {
            throw new IllegalArgumentException("Numero di telefono non valido (richieste 10 cifre).");
        }

        if (!DataCheck.isStrongPassword(password)) {
            throw new IllegalArgumentException("Password debole: serve min. 8 caratteri, una maiuscola, un numero e un carattere speciale.");
        }

        // 2. Controllo Esistenza Utente
        if (esisteUtente(username)) {
            throw new IllegalArgumentException("L'username '" + username + "' è già in uso.");
        }

        // 3. Inserimento nel Database
        String sql = "INSERT INTO utente (nomeutente, mail, password, numerotelefono) VALUES (?, ?, ?, ?)";

        try (Connection conn = dbConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.setString(4, numeroTelefono);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        }
        // Nota: Non catturo SQLException qui, la lascio risalire al Controller
        // così può distinguere tra errore di validazione (IllegalArgument) ed errore tecnico (SQL)
    }
    public boolean registraUtente(Utente utente) throws SQLException {
        return registraUtente(utente.getUsername(), utente.getEmail(), utente.getPassword(), utente.getNumeroTelefono());
    }

    public Utente autenticaUtente(String username, String password) throws SQLException {
        String sql = "SELECT * FROM utente WHERE nomeutente = ? AND password = ?";

        try (Connection conn = dbConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String email = rs.getString("mail");
                    String numTel = rs.getString("numerotelefono");
                    return new Utente(username, email, password, numTel);
                }
            }
        }
        return null;
    }

    public boolean esisteUtente(String username) throws SQLException {
        String sql = "SELECT 1 FROM utente WHERE nomeutente = ?";

        try (Connection conn = dbConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // Ritorna true se trova almeno un risultato
            }
        }
    }

    /**
     * Aggiorna la password di un utente esistente.
     *
     * @param username       Lo username dell'utente.
     * @param nuovaPassword  La nuova password (già validata).
     * @return true se l'aggiornamento ha avuto successo, false altrimenti.
     * @throws SQLException se c'è un errore nel DB.
     */
    public boolean aggiornaPassword(String username, String nuovaPassword) throws SQLException {
        if (!esisteUtente(username)) {
            throw new IllegalArgumentException("Utente non trovato: " + username);
        }

        // Controllo robustezza password prima di scrivere nel DB
        if (!DataCheck.isStrongPassword(nuovaPassword)) {
            throw new IllegalArgumentException("La nuova password non rispetta i criteri di sicurezza.");
        }

        String sql = "UPDATE utente SET password = ? WHERE nomeutente = ?";

        try (Connection conn = dbConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nuovaPassword);
            ps.setString(2, username);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        }
    }

}