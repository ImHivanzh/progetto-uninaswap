package dao;

import db.dbConnection;
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
  private final AnnuncioDAO annuncioDAO;

  /**
   * Crea DAO e inizializza database connessione.
   *
   * @throws DatabaseException quando database e non disponibile
   */
  public SpedizioneDAO() throws DatabaseException {
    this.con = dbConnection.getInstance().getConnection();
    if (this.con == null) {
      throw new DatabaseException("Connessione al database non disponibile.");
    }
    this.annuncioDAO = new AnnuncioDAO();
  }

  /**
   * Verifica se spedizione esiste per annuncio.
   *
   * @param idAnnuncio id annuncio
   * @return true quando esiste gia una spedizione
   * @throws DatabaseException quando query fallisce
   */
  public boolean esistePerAnnuncio(int idAnnuncio) throws DatabaseException {
    if (con == null) throw new DatabaseException("Connessione al database non disponibile.");

    String sql = "SELECT 1 FROM spedizione WHERE idannuncio = ? LIMIT 1";
    try (PreparedStatement ps = con.prepareStatement(sql)) {
      ps.setInt(1, idAnnuncio);
      try (ResultSet rs = ps.executeQuery()) {
        return rs.next();
      }
    } catch (SQLException e) {
      throw new DatabaseException("Errore durante la verifica spedizione", e);
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
          Date dataInvio, Date dataArrivo, String indirizzo, String numeroTelefono, int idAnnuncio, int idSpedito)
          throws DatabaseException {
    if (con == null) throw new DatabaseException("Connessione al database non disponibile.");

    String sql = "INSERT INTO spedizione (datainvio, dataarrivo, indirizzo, numerotelefono, idannuncio, spedito, idspedito) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)";

    try (PreparedStatement ps = con.prepareStatement(sql)) {
      ps.setDate(1, dataInvio);
      ps.setDate(2, dataArrivo);
      ps.setString(3, indirizzo);
      ps.setString(4, numeroTelefono);
      ps.setInt(5, idAnnuncio);
      ps.setBoolean(6, false);
      ps.setInt(7, idSpedito);
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

    String sql = "SELECT * FROM spedizione WHERE idannuncio = ?";
    try (PreparedStatement ps = con.prepareStatement(sql)) {
      ps.setInt(1, idAnnuncio);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          model.Spedizione spedizione = new model.Spedizione();
          try {
            spedizione.setIdSpedizione(rs.getInt("idspedizione"));
          } catch (SQLException ignored) {
            // Colonna opzionale in alcuni schemi.
          }
          spedizione.setIndirizzo(rs.getString("indirizzo"));
          spedizione.setNumeroTelefono(rs.getString("numerotelefono"));
          spedizione.setDataInvio(rs.getDate("datainvio"));
          spedizione.setDataArrivo(rs.getDate("dataarrivo"));
          spedizione.setSpedito(rs.getBoolean("spedito"));
          spedizione.setAnnuncio(annuncioDAO.findById(rs.getInt("idannuncio")));
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
