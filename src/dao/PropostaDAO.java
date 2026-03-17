package dao;

import db.DbConnection;
import exception.DatabaseException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import model.Annuncio;
import model.PropostaRiepilogo;
import model.ReportProposte;
import model.Utente;
import utils.Constanti;
import utils.Logger;
import utils.Validator;

/**
 * DAO per l'accesso ai dati delle proposte.
 */
public class PropostaDAO {

    private static final String COL_UTENTE = "utente";
    private static final String COL_ANNUNCIO = "annuncio";
    private static final String COL_ID_UTENTE = "utente.idUtente";
    private static final String COL_ID_ANNUNCIO = "annuncio.idAnnuncio";
    private static final String COL_NUMERO_TELEFONO = "numerotelefono";
    private static final String COL_ID_UTENTE_DB = "idutente";

    /**
     * Connessione al database.
     */
    private final Connection con;
    /**
     * Query per le proposte ricevute.
     */
    private static final String SQL_PROPOSTE_RICEVUTE =
            "SELECT a.idannuncio, a.titolo, a.tipoannuncio, a.descrizione, a.categoria, a.stato, a.spedizione, " +
            "       u.idutente, u.nomeutente AS utente, u.numerotelefono, " +
            "       ('Offerta: ' || COALESCE(CAST(v.controofferta AS VARCHAR), 'N/A')) AS dettaglio, " +
            "       v.accettato, v.inattesa, CAST(NULL AS bytea) AS immagine " +
            "  FROM vendita v " +
            "  JOIN annuncio a ON v.idannuncio = a.idannuncio " +
            "  JOIN utente u ON v.idutente = u.idutente " +
            " WHERE a.idutente = ? " +
            "UNION ALL " +
            "SELECT a.idannuncio, a.titolo, a.tipoannuncio, a.descrizione, a.categoria, a.stato, a.spedizione, " +
            "       u.idutente, u.nomeutente AS utente, u.numerotelefono, " +
            "       ('Scambio proposto: ' || COALESCE(s.propscambio, 'N/A')) AS dettaglio, " +
            "       s.accettato, s.inattesa, s.immagine AS immagine " +
            "  FROM scambio s " +
            "  JOIN annuncio a ON s.idannuncio = a.idannuncio " +
            "  JOIN utente u ON s.idutente = u.idutente " +
            " WHERE a.idutente = ? " +
            "UNION ALL " +
            "SELECT a.idannuncio, a.titolo, a.tipoannuncio, a.descrizione, a.categoria, a.stato, a.spedizione, " +
            "       u.idutente, u.nomeutente AS utente, u.numerotelefono, " +
            "       ('Richiesta regalo' || COALESCE(' del ' || r.dataprenotazione, '')) AS dettaglio, " +
            "       r.accettato, r.inattesa, CAST(NULL AS bytea) AS immagine " +
            "  FROM regalo r " +
            "  JOIN annuncio a ON r.idannuncio = a.idannuncio " +
            "  JOIN utente u ON r.idutente = u.idutente " +
            " WHERE a.idutente = ? " +
            "ORDER BY titolo";

    /**
     * Query per le proposte inviate.
     */
    private static final String SQL_PROPOSTE_INVIATE =
            "SELECT a.idannuncio, a.titolo, a.tipoannuncio, a.descrizione, a.categoria, a.stato, a.spedizione, " +
            "       u.idutente, u.nomeutente AS utente, u.numerotelefono, " +
            "       ('Offerta: ' || COALESCE(CAST(v.controofferta AS VARCHAR), 'N/A')) AS dettaglio, " +
            "       v.accettato, v.inattesa, CAST(NULL AS bytea) AS immagine " +
            "  FROM vendita v " +
            "  JOIN annuncio a ON v.idannuncio = a.idannuncio " +
            "  JOIN utente u ON a.idutente = u.idutente " +
            " WHERE v.idutente = ? " +
            "UNION ALL " +
            "SELECT a.idannuncio, a.titolo, a.tipoannuncio, a.descrizione, a.categoria, a.stato, a.spedizione, " +
            "       u.idutente, u.nomeutente AS utente, u.numerotelefono, " +
            "       ('Scambio proposto: ' || COALESCE(s.propscambio, 'N/A')) AS dettaglio, " +
            "       s.accettato, s.inattesa, s.immagine AS immagine " +
            "  FROM scambio s " +
            "  JOIN annuncio a ON s.idannuncio = a.idannuncio " +
            "  JOIN utente u ON a.idutente = u.idutente " +
            " WHERE s.idutente = ? " +
            "UNION ALL " +
            "SELECT a.idannuncio, a.titolo, a.tipoannuncio, a.descrizione, a.categoria, a.stato, a.spedizione, " +
            "       u.idutente, u.nomeutente AS utente, u.numerotelefono, " +
            "       ('Richiesta regalo' || COALESCE(' del ' || r.dataprenotazione, '')) AS dettaglio, " +
            "       r.accettato, r.inattesa, CAST(NULL AS bytea) AS immagine " +
            "  FROM regalo r " +
            "  JOIN annuncio a ON r.idannuncio = a.idannuncio " +
            "  JOIN utente u ON a.idutente = u.idutente " +
            " WHERE r.idutente = ? " +
            "ORDER BY titolo";

    /**
     * Crea il DAO e inizializza la connessione al database.
     *
     * @throws DatabaseException se il database non è disponibile
     */
    public PropostaDAO() throws DatabaseException {
        this.con = DbConnection.getInstance().getConnection();
        if (this.con == null) {
            throw new DatabaseException("Connessione al database non disponibile.");
        }
    }

    /**
     * Inserisce una proposta di vendita per un annuncio.
     *
     * @param utente utente proponente
     * @param annuncio annuncio target
     * @param controOfferta controofferta proposta
     * @return true se l'inserimento ha successo
     * @throws DatabaseException se l'inserimento fallisce
     */
    public boolean inserisciPropostaVendita(Utente utente, Annuncio annuncio, double controOfferta)
            throws DatabaseException {
        Validator.requireNonNull(utente, COL_UTENTE);
        Validator.requireNonNull(annuncio, COL_ANNUNCIO);
        Validator.requirePositive(utente.getIdUtente(), COL_ID_UTENTE);
        Validator.requirePositive(annuncio.getIdAnnuncio(), COL_ID_ANNUNCIO);

        String sql = "INSERT INTO vendita(idutente, idannuncio, controofferta, accettato) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, utente.getIdUtente());
            ps.setInt(2, annuncio.getIdAnnuncio());
            ps.setDouble(3, controOfferta);
            ps.setBoolean(4, false);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Errore durante l'inserimento della proposta di vendita", e);
        }
    }

    /**
     * Inserisce una proposta di scambio per un annuncio.
     *
     * @param utente utente proponente
     * @param annuncio annuncio target
     * @param propScambio descrizione dello scambio proposto
     * @return true se l'inserimento ha successo
     * @throws DatabaseException se l'inserimento fallisce
     */
    public boolean inserisciPropostaScambio(Utente utente, Annuncio annuncio, String propScambio)
            throws DatabaseException {
        return inserisciPropostaScambio(utente, annuncio, propScambio, null);
    }

    /**
     * Inserisce una proposta di scambio con immagine opzionale.
     *
     * @param utente utente proponente
     * @param annuncio annuncio target
     * @param propScambio descrizione dello scambio proposto
     * @param immagine byte array dell'immagine opzionale
     * @return true se l'inserimento ha successo
     * @throws DatabaseException se l'inserimento fallisce
     */
    public boolean inserisciPropostaScambio(
            Utente utente, Annuncio annuncio, String propScambio, byte[] immagine)
            throws DatabaseException {
        Validator.requireNonNull(utente, COL_UTENTE);
        Validator.requireNonNull(annuncio, COL_ANNUNCIO);
        Validator.requirePositive(utente.getIdUtente(), COL_ID_UTENTE);
        Validator.requirePositive(annuncio.getIdAnnuncio(), COL_ID_ANNUNCIO);
        Validator.requireNonEmpty(propScambio, "propScambio");

        String sql =
                "INSERT INTO scambio(idutente, idannuncio, propscambio, immagine, accettato) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, utente.getIdUtente());
            ps.setInt(2, annuncio.getIdAnnuncio());
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
     * Inserisce una richiesta di regalo per un annuncio.
     *
     * @param utente utente richiedente
     * @param annuncio annuncio target
     * @return true se l'inserimento ha successo
     * @throws DatabaseException se l'inserimento fallisce
     */
    public boolean inserisciPropostaRegalo(Utente utente, Annuncio annuncio) throws DatabaseException {
        Validator.requireNonNull(utente, COL_UTENTE);
        Validator.requireNonNull(annuncio, COL_ANNUNCIO);
        Validator.requirePositive(utente.getIdUtente(), COL_ID_UTENTE);
        Validator.requirePositive(annuncio.getIdAnnuncio(), COL_ID_ANNUNCIO);

        String sql = "INSERT INTO regalo(dataprenotazione, accettato, idutente, idannuncio) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            ps.setBoolean(2, false);
            ps.setInt(3, utente.getIdUtente());
            ps.setInt(4, annuncio.getIdAnnuncio());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Errore durante l'inserimento della proposta di regalo", e);
        }
    }

    /**
     * Restituisce le proposte ricevute per gli annunci posseduti dall'utente.
     *
     * @param idUtente ID dell'utente
     * @return lista delle proposte
     * @throws DatabaseException se la query fallisce
     */
    public List<PropostaRiepilogo> getProposteRicevute(int idUtente) throws DatabaseException {
        return getProposte(idUtente, SQL_PROPOSTE_RICEVUTE);
    }

    /**
     * Restituisce le proposte inviate dall'utente.
     *
     * @param idUtente ID dell'utente
     * @return lista delle proposte
     * @throws DatabaseException se la query fallisce
     */
    public List<PropostaRiepilogo> getProposteInviate(int idUtente) throws DatabaseException {
        return getProposte(idUtente, SQL_PROPOSTE_INVIATE);
    }

    /**
     * Esegue la query per ottenere la lista delle proposte e mappa i risultati.
     *
     * @param idUtente ID dell'utente
     * @param query query SQL da eseguire
     * @return lista delle proposte
     * @throws DatabaseException se la query fallisce
     */
    private List<PropostaRiepilogo> getProposte(int idUtente, String query) throws DatabaseException {
        List<PropostaRiepilogo> proposte = new ArrayList<>();

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, idUtente);
            ps.setInt(2, idUtente);
            ps.setInt(3, idUtente);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Costruisci oggetto Utente direttamente dal ResultSet
                    Utente utenteCoinvolto = new Utente();
                    utenteCoinvolto.setIdUtente(rs.getInt(COL_ID_UTENTE_DB));
                    utenteCoinvolto.setUsername(rs.getString(COL_UTENTE));
                    utenteCoinvolto.setNumeroTelefono(rs.getString(COL_NUMERO_TELEFONO));

                    // Costruisci oggetto Annuncio direttamente dal ResultSet
                    Annuncio annuncio = new Annuncio();
                    annuncio.setIdAnnuncio(rs.getInt("idannuncio"));
                    annuncio.setTitolo(rs.getString("titolo"));
                    annuncio.setDescrizione(rs.getString("descrizione"));
                    annuncio.setStato(rs.getBoolean("stato"));

                    // Parse enum values
                    parseCategoria(rs, annuncio);
                    parseTipoAnnuncio(rs, annuncio);

                    Boolean spedizione = rs.getObject("spedizione", Boolean.class);
                    annuncio.setSpedizione(spedizione);

                    PropostaRiepilogo proposta = new PropostaRiepilogo(
                            annuncio,
                            utenteCoinvolto,
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
     * Estrae e imposta la categoria dall'ResultSet.
     */
    private void parseCategoria(ResultSet rs, Annuncio annuncio) throws SQLException {
        String categoriaStr = rs.getString("categoria");
        if (categoriaStr != null) {
            try {
                annuncio.setCategoria(model.enums.Categoria.valueOf(categoriaStr.toUpperCase()));
            } catch (IllegalArgumentException e) {
                Logger.error("Categoria non valida: " + categoriaStr);
            }
        }
    }

    /**
     * Estrae e imposta il tipo annuncio dall'ResultSet.
     */
    private void parseTipoAnnuncio(ResultSet rs, Annuncio annuncio) throws SQLException {
        String tipoStr = rs.getString("tipoannuncio");
        if (tipoStr != null) {
            try {
                annuncio.setTipoAnnuncio(model.enums.TipoAnnuncio.valueOf(tipoStr.toUpperCase()));
            } catch (IllegalArgumentException e) {
                Logger.error("Tipo annuncio non valido: " + tipoStr);
            }
        }
    }

    /**
     * Aggiorna lo stato di una proposta per un annuncio.
     *
     * @param idAnnuncio ID dell'annuncio
     * @param tipoAnnuncio tipo di annuncio
     * @param usernameProponente username del proponente
     * @param accettata flag di accettazione
     * @param inattesa flag di attesa
     * @return true se l'aggiornamento ha successo
     * @throws DatabaseException se l'aggiornamento fallisce
     */
    public boolean aggiornaEsitoProposta(
            int idAnnuncio, String tipoAnnuncio, String usernameProponente, boolean accettata, boolean inattesa)
            throws DatabaseException {
        String tabella = resolveTabellaProposta(tipoAnnuncio);
        if (tabella == null) {
            throw new DatabaseException("Tipo annuncio non riconosciuto: " + tipoAnnuncio);
        }

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
            throw new DatabaseException("Errore durante l'aggiornamento dello stato proposta", e);
        }
    }

    /**
     * Elimina una proposta per un annuncio.
     *
     * @param idAnnuncio ID dell'annuncio
     * @param tipoAnnuncio tipo di annuncio
     * @param usernameProponente username del proponente
     * @return true se l'eliminazione ha successo
     * @throws DatabaseException se l'eliminazione fallisce
     */
    public boolean eliminaProposta(int idAnnuncio, String tipoAnnuncio, String usernameProponente)
            throws DatabaseException {
        String tabella = resolveTabellaProposta(tipoAnnuncio);
        if (tabella == null) {
            throw new DatabaseException("Tipo annuncio non riconosciuto: " + tipoAnnuncio);
        }

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
     * Risolve il nome della tabella dato il tipo di annuncio.
     *
     * @param tipoAnnuncio tipo di annuncio
     * @return nome della tabella, o null se sconosciuto
     */
    private String resolveTabellaProposta(String tipoAnnuncio) {
        if (tipoAnnuncio == null) {
            return null;
        }
        String normalizzato = tipoAnnuncio.trim().toUpperCase();

        if (normalizzato.contains(Constanti.TIPO_VENDITA)) {
            return Constanti.TABELLA_VENDITA;
        } else if (normalizzato.contains(Constanti.TIPO_SCAMBIO)) {
            return Constanti.TABELLA_SCAMBIO;
        } else if (normalizzato.contains(Constanti.TIPO_REGALO)) {
            return Constanti.TABELLA_REGALO;
        } else {
            return null;
        }
    }
    
    /**
     * Aggiorna la controofferta per una proposta di vendita.
     *
     * @param idAnnuncio ID dell'annuncio
     * @param idUtente ID dell'utente proponente
     * @param nuovaOfferta nuova offerta
     * @return true se l'aggiornamento ha successo
     * @throws DatabaseException se l'aggiornamento fallisce
     */
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

    /**
     * Aggiorna la proposta di scambio per annuncio.
     *
     * @param idAnnuncio id annuncio
     * @param idUtente id utente proponente
     * @param nuovaDescrizione descrizione proposta
     * @param nuovaImmagine immagine proposta (può essere null)
     * @return true se aggiornamento riuscito
     * @throws DatabaseException quando update fallisce
     */
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

    /**
     * Genera report statistico delle proposte per utente.
     *
     * @param idUtente id utente
     * @return report proposte o null se nessun dato
     * @throws DatabaseException quando query fallisce
     */
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
