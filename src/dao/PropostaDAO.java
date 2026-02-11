package dao;

import db.dbConnection;
import exception.DatabaseException;
import model.PropostaRiepilogo;
import model.ReportProposte;

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
 * DAO per accesso dati proposte.
 */
public class PropostaDAO {

    /**
     * Connessione al database.
     */
    private final Connection con;
    /**
     * Query per proposte ricevute.
     */
    private static final String SQL_PROPOSTE_RICEVUTE =
            "SELECT a.idannuncio, a.titolo, a.tipoannuncio, u.nomeutente AS utente, " +
            "       ('Offerta: ' || COALESCE(CAST(v.controofferta AS VARCHAR), 'N/A')) AS dettaglio, " +
            "       v.accettato, v.inattesa, CAST(NULL AS bytea) AS immagine " +
            "  FROM vendita v " +
            "  JOIN annuncio a ON v.idannuncio = a.idannuncio " +
            "  JOIN utente u ON v.idutente = u.idutente " +
            " WHERE a.idutente = ? " +
            "UNION ALL " +
            "SELECT a.idannuncio, a.titolo, a.tipoannuncio, u.nomeutente AS utente, " +
            "       ('Scambio proposto: ' || COALESCE(s.propscambio, 'N/A')) AS dettaglio, " +
            "       s.accettato, s.inattesa, s.immagine AS immagine " +
            "  FROM scambio s " +
            "  JOIN annuncio a ON s.idannuncio = a.idannuncio " +
            "  JOIN utente u ON s.idutente = u.idutente " +
            " WHERE a.idutente = ? " +
            "UNION ALL " +
            "SELECT a.idannuncio, a.titolo, a.tipoannuncio, u.nomeutente AS utente, " +
            "       ('Richiesta regalo' || COALESCE(' del ' || r.dataprenotazione, '')) AS dettaglio, " +
            "       r.accettato, r.inattesa, CAST(NULL AS bytea) AS immagine " +
            "  FROM regalo r " +
            "  JOIN annuncio a ON r.idannuncio = a.idannuncio " +
            "  JOIN utente u ON r.idutente = u.idutente " +
            " WHERE a.idutente = ? " +
            "ORDER BY titolo";

    /**
     * Query per proposte inviate.
     */
    private static final String SQL_PROPOSTE_INVIATE =
            "SELECT a.idannuncio, a.titolo, a.tipoannuncio, u.nomeutente AS utente, " +
            "       ('Offerta: ' || COALESCE(CAST(v.controofferta AS VARCHAR), 'N/A')) AS dettaglio, " +
            "       v.accettato, v.inattesa, CAST(NULL AS bytea) AS immagine " +
            "  FROM vendita v " +
            "  JOIN annuncio a ON v.idannuncio = a.idannuncio " +
            "  JOIN utente u ON a.idutente = u.idutente " +
            " WHERE v.idutente = ? " +
            "UNION ALL " +
            "SELECT a.idannuncio, a.titolo, a.tipoannuncio, u.nomeutente AS utente, " +
            "       ('Scambio proposto: ' || COALESCE(s.propscambio, 'N/A')) AS dettaglio, " +
            "       s.accettato, s.inattesa, s.immagine AS immagine " +
            "  FROM scambio s " +
            "  JOIN annuncio a ON s.idannuncio = a.idannuncio " +
            "  JOIN utente u ON a.idutente = u.idutente " +
            " WHERE s.idutente = ? " +
            "UNION ALL " +
            "SELECT a.idannuncio, a.titolo, a.tipoannuncio, u.nomeutente AS utente, " +
            "       ('Richiesta regalo' || COALESCE(' del ' || r.dataprenotazione, '')) AS dettaglio, " +
            "       r.accettato, r.inattesa, CAST(NULL AS bytea) AS immagine " +
            "  FROM regalo r " +
            "  JOIN annuncio a ON r.idannuncio = a.idannuncio " +
            "  JOIN utente u ON a.idutente = u.idutente " +
            " WHERE r.idutente = ? " +
            "ORDER BY titolo";

    /**
     * Crea DAO e inizializza database connessione.
     *
     * @throws DatabaseException quando database e non disponibile
     */
    public PropostaDAO() throws DatabaseException {
        this.con = dbConnection.getInstance().getConnection();
        if (this.con == null) {
            throw new DatabaseException("Connessione al database non disponibile.");
        }
    }

    /**
     * Inserisce vendite proposta per annuncio.
     *
     * @param idUtente proponente utente id
     * @param idAnnuncio id annuncio
     * @param controOfferta contatore offerta
     * @return true quando inserimento riesce
     * @throws DatabaseException quando inserimento fallisce
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
     * Inserisce scambio proposta per annuncio.
     *
     * @param idUtente proponente utente id
     * @param idAnnuncio id annuncio
     * @param propScambio scambio descrizione
     * @return true quando inserimento riesce
     * @throws DatabaseException quando inserimento fallisce
     */
    public boolean inserisciPropostaScambio(int idUtente, int idAnnuncio, String propScambio)
            throws DatabaseException {
        return inserisciPropostaScambio(idUtente, idAnnuncio, propScambio, null);
    }

    /**
     * Inserisce scambio proposta con facoltativo immagine.
     *
     * @param idUtente proponente utente id
     * @param idAnnuncio id annuncio
     * @param propScambio scambio descrizione
     * @param immagine facoltativo byte immagine
     * @return true quando inserimento riesce
     * @throws DatabaseException quando inserimento fallisce
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
            throw new DatabaseException("Errore durante l'inserimento della proposta di scambio" + e.getMessage(), e);
        }
    }

    /**
     * Inserisce regalo richiesta per annuncio.
     *
     * @param idUtente richiedente utente id
     * @param idAnnuncio id annuncio
     * @return true quando inserimento riesce
     * @throws DatabaseException quando inserimento fallisce
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
     * Restituisce proposte ricevute per annunci posseduto da utente.
     *
     * @param idUtente utente id
     * @return lista di proposte
     * @throws DatabaseException quando query fallisce
     */
    public List<PropostaRiepilogo> getProposteRicevute(int idUtente) throws DatabaseException {
        return getProposte(idUtente, SQL_PROPOSTE_RICEVUTE);
    }

    /**
     * Restituisce proposte inviate da utente.
     *
     * @param idUtente utente id
     * @return lista di proposte
     * @throws DatabaseException quando query fallisce
     */
    public List<PropostaRiepilogo> getProposteInviate(int idUtente) throws DatabaseException {
        return getProposte(idUtente, SQL_PROPOSTE_INVIATE);
    }

    /**
     * Esegue query lista proposte e mappa risultati.
     *
     * @param idUtente utente id
     * @param query SQL query
     * @return lista di proposte
     * @throws DatabaseException quando query fallisce
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
                            rs.getBoolean("inattesa"),
                            rs.getBytes("immagine")
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
     * Aggiorna stato proposta per annuncio.
     *
     * @param idAnnuncio id annuncio
     * @param tipoAnnuncio tipo annuncio
     * @param usernameProponente proponente username
     * @param accettata accettata flag
     * @param inattesa in attesa flag
     * @return true quando aggiorna riesce
     * @throws DatabaseException quando aggiorna fallisce
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
     * Elimina proposta per annuncio.
     *
     * @param idAnnuncio id annuncio
     * @param tipoAnnuncio tipo annuncio
     * @param usernameProponente proponente username
     * @return true quando elimina riesce
     * @throws DatabaseException quando elimina fallisce
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
     * Risolve nome tabella per tipo annuncio.
     *
     * @param tipoAnnuncio tipo annuncio
     * @return nome tabella o null quando sconosciuto
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
    
    public boolean modificaPropostaVendita(int idAnnuncio, int idUtente, double nuovaOfferta) throws DatabaseException {
        String sql = "UPDATE vendita SET controofferta = ? WHERE idannuncio = ? AND idutente = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, nuovaOfferta);
            ps.setInt(2, idAnnuncio);
            ps.setInt(3, idUtente);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Errore durante la modifica della proposta di vendita", e);
        }
    }

    public boolean modificaPropostaScambio(int idAnnuncio, int idUtente, String nuovaDescrizione, byte[] nuovaImmagine) throws DatabaseException {
        String sql = "UPDATE scambio SET propscambio = ?, immagine = ? WHERE idannuncio = ? AND idutente = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nuovaDescrizione);
            if (nuovaImmagine != null && nuovaImmagine.length > 0) {
                ps.setBytes(2, nuovaImmagine);
            } else {
                ps.setNull(2, Types.BINARY);
            }
            ps.setInt(3, idAnnuncio);
            ps.setInt(4, idUtente);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Errore durante la modifica della proposta di scambio", e);
        }
    }

    public ReportProposte getReportProposte(int idUtente) throws DatabaseException {
        String sql = "SELECT " +
                "    SUM(CASE WHEN tipo = 'VENDITA' THEN 1 ELSE 0 END) as totaleVendita, " +
                "    SUM(CASE WHEN tipo = 'VENDITA' AND accettato = TRUE THEN 1 ELSE 0 END) as accettateVendita, " +
                "    MIN(CASE WHEN tipo = 'VENDITA' AND accettato = TRUE THEN valore END) as valoreMinimoVendita, " +
                "    MAX(CASE WHEN tipo = 'VENDITA' AND accettato = TRUE THEN valore END) as valoreMassimoVendita, " +
                "    AVG(CASE WHEN tipo = 'VENDITA' AND accettato = TRUE THEN valore END) as valoreMedioVendita, " +
                "    SUM(CASE WHEN tipo = 'SCAMBIO' THEN 1 ELSE 0 END) as totaleScambio, " +
                "    SUM(CASE WHEN tipo = 'SCAMBIO' AND accettato = TRUE THEN 1 ELSE 0 END) as accettateScambio, " +
                "    SUM(CASE WHEN tipo = 'REGALO' THEN 1 ELSE 0 END) as totaleRegalo, " +
                "    SUM(CASE WHEN tipo = 'REGALO' AND accettato = TRUE THEN 1 ELSE 0 END) as accettateRegalo " +
                "FROM ( " +
                "    SELECT 'VENDITA' as tipo, accettato, controofferta as valore FROM vendita WHERE idutente = ? " +
                "    UNION ALL " +
                "    SELECT 'SCAMBIO' as tipo, accettato, NULL as valore FROM scambio WHERE idutente = ? " +
                "    UNION ALL " +
                "    SELECT 'REGALO' as tipo, accettato, NULL as valore FROM regalo WHERE idutente = ? " +
                ") as proposte";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUtente);
            ps.setInt(2, idUtente);
            ps.setInt(3, idUtente);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new ReportProposte(
                            rs.getInt("totaleVendita"),
                            rs.getInt("accettateVendita"),
                            rs.getInt("totaleScambio"),
                            rs.getInt("accettateScambio"),
                            rs.getInt("totaleRegalo"),
                            rs.getInt("accettateRegalo"),
                            rs.getDouble("valoreMinimoVendita"),
                            rs.getDouble("valoreMassimoVendita"),
                            rs.getDouble("valoreMedioVendita")
                    );
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Errore durante la generazione del report delle proposte", e);
        }
        return null;
    }
}
