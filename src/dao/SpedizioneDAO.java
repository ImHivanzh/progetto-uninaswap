package dao;

import db.DbConnection;
import exception.DatabaseException;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO per accesso dati spedizione.
 */
public class SpedizioneDAO {

  /**
   * Connessione al database.
   */
  private final Connection con;

  /**
   * Crea DAO e inizializza database connessione.
   *
   * @throws DatabaseException quando database è non disponibile
   */
  public SpedizioneDAO() throws DatabaseException {
    this.con = DbConnection.getInstance().getConnection();
    if (this.con == null) {
      throw new DatabaseException("Connessione al database non disponibile.");
    }
  }

  /**
   * Inserisce dati spedizione per annuncio.
   *
   * @param dataInvio data invio
   * @param dataArrivo data arrivo
   * @param indirizzo indirizzo spedizione
   * @param numeroTelefono telefono contatto
   * @param idAnnuncio id annuncio
   * @return true quando inserimento riesce
   * @throws DatabaseException quando inserimento fallisce
   */
  public boolean inserisciSpedizione(
          Date dataInvio, Date dataArrivo, String indirizzo, String numeroTelefono, int idAnnuncio)
          throws DatabaseException {
    if (con == null) throw new DatabaseException("Connessione al database non disponibile.");

    String sql = "INSERT INTO spedizione (datainvio, dataarrivo, indirizzo, numerotelefono, idannuncio, spedito) " +
            "VALUES (?, ?, ?, ?, ?, ?)";

    try (PreparedStatement ps = con.prepareStatement(sql)) {
      ps.setDate(1, dataInvio);
      ps.setDate(2, dataArrivo);
      ps.setString(3, indirizzo);
      ps.setString(4, numeroTelefono);
      ps.setInt(5, idAnnuncio);
      ps.setBoolean(6, false);
      return ps.executeUpdate() > 0;
    } catch (SQLException e) {
      throw new DatabaseException("Errore durante l'inserimento della spedizione", e);
    }
  }

  /**
   * Restituisce spedizione da id annuncio.
   *
   * @param idAnnuncio id annuncio
   * @return spedizione o null
   * @throws DatabaseException quando query fallisce
   */
  public model.Spedizione getSpedizioneByAnnuncio(int idAnnuncio) throws DatabaseException {
    if (con == null) throw new DatabaseException("Connessione al database non disponibile.");

    String sql = "SELECT idspedizione, datainvio, dataarrivo, indirizzo, numerotelefono, spedito, idannuncio FROM spedizione WHERE idannuncio = ?";
    try (PreparedStatement ps = con.prepareStatement(sql)) {
      ps.setInt(1, idAnnuncio);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          model.Spedizione spedizione = new model.Spedizione();
          spedizione.setIdSpedizione(rs.getInt("idspedizione"));
          spedizione.setIndirizzo(rs.getString("indirizzo"));
          spedizione.setNumeroTelefono(rs.getString("numerotelefono"));
          spedizione.setDataInvio(rs.getDate("datainvio"));
          spedizione.setDataArrivo(rs.getDate("dataarrivo"));
          spedizione.setSpedito(rs.getBoolean("spedito"));
          return spedizione;
        }
      }
    } catch (SQLException e) {
      throw new DatabaseException("Errore durante il recupero della spedizione", e);
    }
    return null;
  }

  /**
   * Aggiorna lo stato "spedito" di una spedizione.
   *
   * @param idAnnuncio l'ID dell'annuncio a cui è associata la spedizione.
   * @param isSpedito il nuovo stato di spedizione (true se spedito, false altrimenti).
   * @return true se l'aggiornamento è riuscito, false altrimenti.
   * @throws DatabaseException se si verifica un errore durante l'accesso al database.
   */
  public boolean aggiornaStatoSpedizione(int idAnnuncio, boolean isSpedito) throws DatabaseException {
    if (con == null) {
      throw new DatabaseException("Connessione al database non disponibile.");
    }
    String sql = "UPDATE spedizione SET spedito = ? WHERE idannuncio = ?";
    try (PreparedStatement ps = con.prepareStatement(sql)) {
      ps.setBoolean(1, isSpedito);
      ps.setInt(2, idAnnuncio);
      return ps.executeUpdate() > 0;
    } catch (SQLException e) {
      throw new DatabaseException("Errore durante l'aggiornamento dello stato della spedizione", e);
    }
  }
}
