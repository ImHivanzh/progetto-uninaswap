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

/**
 * DAO per la gestione delle proposte sugli annunci di tipo vendita, scambio e regalo.
 * Le query sono allineate ai nomi di colonna mostrati nello schema di riferimento.
 */
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

    public PropostaDAO() throws DatabaseException {
        this.con = dbConnection.getInstance().getConnection();
        if (this.con == null) {
            throw new DatabaseException("Connessione al database non disponibile.");
        }
    }

    /**
     * Inserisce una proposta per un annuncio di vendita utilizzando le colonne
     * (IdUtente, IdAnnuncio, controOfferta, accettato).
     */
    public boolean inserisciPropostaVendita(int idUtente, int idAnnuncio, double controOfferta)
            throws DatabaseException {
        String sql = "INSERT INTO vendita(idutente, idannuncio, controofferta, accettato) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUtente);
            ps.setInt(2, idAnnuncio);
            ps.setDouble(3, controOfferta);
            ps.setBoolean(4, false); // una nuova proposta parte come non accettata
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Errore durante l'inserimento della proposta di vendita", e);
        }
    }

    /**
     * Inserisce una proposta di scambio utilizzando le colonne
     * (IdUtente, IdAnnuncio, propScambio, accettato).
     */
    public boolean inserisciPropostaScambio(int idUtente, int idAnnuncio, String propScambio)
            throws DatabaseException {
        return inserisciPropostaScambio(idUtente, idAnnuncio, propScambio, null);
    }

    /**
     * Inserisce una proposta di scambio con descrizione e immagine opzionale
     * utilizzando le colonne (IdUtente, IdAnnuncio, propScambio, immagine, accettato).
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
     * Inserisce una richiesta su un annuncio di regalo registrando la data di prenotazione.
     * Colonne utilizzate: (dataPrenotazione, accettato, IdUtente, IdAnnuncio).
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
     * Restituisce le proposte ricevute sugli annunci pubblicati dall'utente.
     */
    public List<PropostaRiepilogo> getProposteRicevute(int idUtente) throws DatabaseException {
        return getProposte(idUtente, SQL_PROPOSTE_RICEVUTE);
    }

    /**
     * Restituisce le proposte inviate dall'utente verso altri annunci.
     */
    public List<PropostaRiepilogo> getProposteInviate(int idUtente) throws DatabaseException {
        return getProposte(idUtente, SQL_PROPOSTE_INVIATE);
    }

    private List<PropostaRiepilogo> getProposte(int idUtente, String query) throws DatabaseException {
        List<PropostaRiepilogo> proposte = new ArrayList<>();

        if (con == null) throw new DatabaseException("Connessione al database non disponibile.");

        try (PreparedStatement ps = con.prepareStatement(query)) {
            // Il parametro compare tre volte nella query unificata
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
