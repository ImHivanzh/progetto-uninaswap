package dao;

import db.dbConnection;
import exception.DatabaseException;
import model.PropostaRiepilogo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PropostaDAO {

    private final Connection con;
    private static final String SQL_PROPOSTE_RICEVUTE =
            "SELECT a.idannuncio, a.titolo, a.tipoannuncio, u.nomeutente AS utente, " +
            "       ('Offerta: ' || COALESCE(CAST(v.controofferta AS VARCHAR), 'N/A')) AS dettaglio, " +
            "       v.accettato, v.inattesa " +
            "  FROM vendita v " +
            "  JOIN annuncio a ON v.idannuncio = a.idannuncio " +
            "  JOIN utente u ON v.idutente = u.idutente " +
            " WHERE a.idutente = ? " +
            "UNION ALL " +
            "SELECT a.idannuncio, a.titolo, a.tipoannuncio, u.nomeutente AS utente, " +
            "       ('Scambio proposto: ' || COALESCE(s.propscambio, 'N/A')) AS dettaglio, " +
            "       s.accettato, s.inattesa " +
            "  FROM scambio s " +
            "  JOIN annuncio a ON s.idannuncio = a.idannuncio " +
            "  JOIN utente u ON s.idutente = u.idutente " +
            " WHERE a.idutente = ? " +
            "UNION ALL " +
            "SELECT a.idannuncio, a.titolo, a.tipoannuncio, u.nomeutente AS utente, " +
            "       ('Richiesta regalo' || COALESCE(' del ' || r.dataprenotazione, '')) AS dettaglio, " +
            "       r.accettato, r.inattesa " +
            "  FROM regalo r " +
            "  JOIN annuncio a ON r.idannuncio = a.idannuncio " +
            "  JOIN utente u ON r.idutente = u.idutente " +
            " WHERE a.idutente = ? " +
            "ORDER BY titolo";

    private static final String SQL_PROPOSTE_INVIATE =
            "SELECT a.idannuncio, a.titolo, a.tipoannuncio, u.nomeutente AS utente, " +
            "       ('Offerta: ' || COALESCE(CAST(v.controofferta AS VARCHAR), 'N/A')) AS dettaglio, " +
            "       v.accettato, v.inattesa " +
            "  FROM vendita v " +
            "  JOIN annuncio a ON v.idannuncio = a.idannuncio " +
            "  JOIN utente u ON a.idutente = u.idutente " +
            " WHERE v.idutente = ? " +
            "UNION ALL " +
            "SELECT a.idannuncio, a.titolo, a.tipoannuncio, u.nomeutente AS utente, " +
            "       ('Scambio proposto: ' || COALESCE(s.propscambio, 'N/A')) AS dettaglio, " +
            "       s.accettato, s.inattesa " +
            "  FROM scambio s " +
            "  JOIN annuncio a ON s.idannuncio = a.idannuncio " +
            "  JOIN utente u ON a.idutente = u.idutente " +
            " WHERE s.idutente = ? " +
            "UNION ALL " +
            "SELECT a.idannuncio, a.titolo, a.tipoannuncio, u.nomeutente AS utente, " +
            "       ('Richiesta regalo' || COALESCE(' del ' || r.dataprenotazione, '')) AS dettaglio, " +
            "       r.accettato, r.inattesa " +
            "  FROM regalo r " +
            "  JOIN annuncio a ON r.idannuncio = a.idannuncio " +
            "  JOIN utente u ON a.idutente = u.idutente " +
            " WHERE r.idutente = ? " +
            "ORDER BY titolo";

    /**
     * Creates the DAO and initializes the database connection.
     *
     * @throws DatabaseException when the database is unavailable
     */
    public PropostaDAO() throws DatabaseException {
        this.con = dbConnection.getInstance().getConnection();
        if (this.con == null) {
            throw new DatabaseException("Connessione al database non disponibile.");
        }
    }

    /**
     * Inserts a sales proposal for a listing.
     *
     * @param idUtente proposer user id
     * @param idAnnuncio listing id
     * @param controOfferta counter offer
     * @return true when the insert succeeds
     * @throws DatabaseException when the insert fails
     */
    public boolean inserisciPropostaVendita(int idUtente, int idAnnuncio, double controOfferta)
            throws DatabaseException {
        String sql = "INSERT INTO vendita(idutente, idannuncio, controofferta, accettato) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUtente);
            ps.setInt(2, idAnnuncio);
            ps.setDouble(3, controOfferta);
            ps.setBoolean(4, false);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Errore durante l'inserimento della proposta di vendita", e);
        }
    }

    /**
     * Inserts an exchange proposal for a listing.
     *
     * @param idUtente proposer user id
     * @param idAnnuncio listing id
     * @param propScambio exchange description
     * @return true when the insert succeeds
     * @throws DatabaseException when the insert fails
     */
    public boolean inserisciPropostaScambio(int idUtente, int idAnnuncio, String propScambio)
            throws DatabaseException {
        return inserisciPropostaScambio(idUtente, idAnnuncio, propScambio, null);
    }

    /**
     * Inserts an exchange proposal with optional image.
     *
     * @param idUtente proposer user id
     * @param idAnnuncio listing id
     * @param propScambio exchange description
     * @param immagine optional image bytes
     * @return true when the insert succeeds
     * @throws DatabaseException when the insert fails
     */
    public boolean inserisciPropostaScambio(
            int idUtente, int idAnnuncio, String propScambio, byte[] immagine)
            throws DatabaseException {
        String sql =
                "INSERT INTO scambio(idutente, idannuncio, propscambio, immagine, accettato) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUtente);
            ps.setInt(2, idAnnuncio);
            ps.setString(3, propScambio);
            if (immagine != null && immagine.length > 0) {
                ps.setBytes(4, immagine);
            } else {
                ps.setNull(4, Types.BINARY);
            }
            ps.setBoolean(5, false);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Errore durante l'inserimento della proposta di scambio", e);
        }
    }

    /**
     * Inserts a gift request for a listing.
     *
     * @param idUtente requester user id
     * @param idAnnuncio listing id
     * @return true when the insert succeeds
     * @throws DatabaseException when the insert fails
     */
    public boolean inserisciPropostaRegalo(int idUtente, int idAnnuncio) throws DatabaseException {
        String sql = "INSERT INTO regalo(dataprenotazione, accettato, idutente, idannuncio) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            ps.setBoolean(2, false);
            ps.setInt(3, idUtente);
            ps.setInt(4, idAnnuncio);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("Errore durante l'inserimento della proposta di regalo", e);
        }
    }

    /**
     * Returns proposals received for listings owned by the user.
     *
     * @param idUtente user id
     * @return list of proposals
     * @throws DatabaseException when the query fails
     */
    public List<PropostaRiepilogo> getProposteRicevute(int idUtente) throws DatabaseException {
        return getProposte(idUtente, SQL_PROPOSTE_RICEVUTE);
    }

    /**
     * Returns proposals sent by the user.
     *
     * @param idUtente user id
     * @return list of proposals
     * @throws DatabaseException when the query fails
     */
    public List<PropostaRiepilogo> getProposteInviate(int idUtente) throws DatabaseException {
        return getProposte(idUtente, SQL_PROPOSTE_INVIATE);
    }

    /**
     * Executes the proposal list query and maps results.
     *
     * @param idUtente user id
     * @param query SQL query
     * @return list of proposals
     * @throws DatabaseException when the query fails
     */
    private List<PropostaRiepilogo> getProposte(int idUtente, String query) throws DatabaseException {
        List<PropostaRiepilogo> proposte = new ArrayList<>();

        if (con == null) throw new DatabaseException("Connessione al database non disponibile.");

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, idUtente);
            ps.setInt(2, idUtente);
            ps.setInt(3, idUtente);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PropostaRiepilogo proposta = new PropostaRiepilogo(
                            rs.getInt("idannuncio"),
                            rs.getString("titolo"),
                            rs.getString("tipoannuncio"),
                            rs.getString("utente"),
                            rs.getString("dettaglio"),
                            rs.getBoolean("accettato"),
                            rs.getBoolean("inattesa")
                    );
                    proposte.add(proposta);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Errore durante il recupero delle proposte", e);
        }

        return proposte;
    }

    /**
     * Updates the proposal status for a listing.
     *
     * @param idAnnuncio listing id
     * @param tipoAnnuncio listing type
     * @param usernameProponente proposer username
     * @param accettata accepted flag
     * @param inattesa pending flag
     * @return true when the update succeeds
     * @throws DatabaseException when the update fails
     */
    public boolean aggiornaEsitoProposta(
            int idAnnuncio, String tipoAnnuncio, String usernameProponente, boolean accettata, boolean inattesa)
            throws DatabaseException {
        String tabella = resolveTabellaProposta(tipoAnnuncio);
        if (tabella == null) {
            throw new DatabaseException("Tipo annuncio non riconosciuto: " + tipoAnnuncio);
        }
        if (con == null) throw new DatabaseException("Connessione al database non disponibile.");

        String sql =
                "UPDATE " + tabella + " SET accettato = ?, inattesa = ? " +
                " WHERE idannuncio = ? " +
                "   AND idutente = (SELECT idutente FROM utente WHERE nomeutente = ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBoolean(1, accettata);
            ps.setBoolean(2, inattesa);
            ps.setInt(3, idAnnuncio);
            ps.setString(4, usernameProponente);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("Errore durante l'aggiornamento dello stato proposta", e);
        }
    }

    /**
     * Deletes a proposal for a listing.
     *
     * @param idAnnuncio listing id
     * @param tipoAnnuncio listing type
     * @param usernameProponente proposer username
     * @return true when the delete succeeds
     * @throws DatabaseException when the delete fails
     */
    public boolean eliminaProposta(int idAnnuncio, String tipoAnnuncio, String usernameProponente)
            throws DatabaseException {
        String tabella = resolveTabellaProposta(tipoAnnuncio);
        if (tabella == null) {
            throw new DatabaseException("Tipo annuncio non riconosciuto: " + tipoAnnuncio);
        }
        if (con == null) throw new DatabaseException("Connessione al database non disponibile.");

        String sql =
                "DELETE FROM " + tabella +
                " WHERE idannuncio = ? " +
                "   AND idutente = (SELECT idutente FROM utente WHERE nomeutente = ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idAnnuncio);
            ps.setString(2, usernameProponente);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Errore durante l'eliminazione della proposta", e);
        }
    }

    /**
     * Resolves the table name for the listing type.
     *
     * @param tipoAnnuncio listing type
     * @return table name or null when unknown
     */
    private String resolveTabellaProposta(String tipoAnnuncio) {
        if (tipoAnnuncio == null) {
            return null;
        }
        String normalizzato = tipoAnnuncio.trim().toUpperCase();
        if (normalizzato.contains("VENDITA")) {
            return "vendita";
        }
        if (normalizzato.contains("SCAMBIO")) {
            return "scambio";
        }
        if (normalizzato.contains("REGALO")) {
            return "regalo";
        }
        return null;
    }
}
