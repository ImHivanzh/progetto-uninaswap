package dao;

import db.dbConnection;
import exception.DatabaseException;
import model.enums.StatoOfferta;
import model.enums.TipoAnnuncio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public class PropostaDAO {

    private Connection con;

    public PropostaDAO() {
        try {
            this.con = dbConnection.getInstance().getConnection();
        } catch (DatabaseException e) {
            System.err.println("Errore di connessione al database in PropostaDAO: " + e.getMessage());
        }
    }

    /**
     * Inserisce una proposta di VENDITA (denaro in cambio dell'oggetto).
     */
    public boolean inserisciPropostaVendita(int idAnnuncio, int idUtente, double offertaEconomica) throws DatabaseException {
        if (con == null) throw new DatabaseException("Connessione non disponibile.");

        String sql = "INSERT INTO proposta (idannuncio, idutente, tipo_proposta, prezzo, stato) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idAnnuncio);
            ps.setInt(2, idUtente);
            ps.setString(3, TipoAnnuncio.VENDITA.name());
            ps.setDouble(4, offertaEconomica);
            ps.setString(5, StatoOfferta.IN_ATTESA.name());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Errore inserimento proposta vendita", e);
        }
    }

    /**
     * Inserisce una proposta di SCAMBIO (oggetto in cambio dell'oggetto).
     */
    public boolean inserisciPropostaScambio(int idAnnuncio, int idUtente, String descrizioneOggetto, byte[] immagineOggetto) throws DatabaseException {
        if (con == null) throw new DatabaseException("Connessione non disponibile.");

        String sql = "INSERT INTO proposta (idannuncio, idutente, tipo_proposta, descrizione, immagine, stato) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idAnnuncio);
            ps.setInt(2, idUtente);
            ps.setString(3, TipoAnnuncio.SCAMBIO.name());
            ps.setString(4, descrizioneOggetto);

            if (immagineOggetto != null) {
                ps.setBytes(5, immagineOggetto);
            } else {
                ps.setNull(5, Types.BINARY);
            }

            ps.setString(6, StatoOfferta.IN_ATTESA.name());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Errore inserimento proposta scambio", e);
        }
    }
    // IMPLEMENTARE QUERY CORRETTE
    /**
     * Inserisce una richiesta di REGALO (messaggio al proprietario).
     */
    public boolean inserisciRichiestaRegalo(int idAnnuncio, int idUtente, String messaggio) throws DatabaseException {
        if (con == null) throw new DatabaseException("Connessione non disponibile.");

        String sql = "INSERT INTO proposta (idannuncio, idutente, tipo_proposta, descrizione, stato) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idAnnuncio);
            ps.setInt(2, idUtente);
            ps.setString(3, TipoAnnuncio.REGALO.name());
            ps.setString(4, messaggio); // Il messaggio viene salvato nel campo descrizione
            ps.setString(5, StatoOfferta.IN_ATTESA.name());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Errore inserimento richiesta regalo", e);
        }
    }
}