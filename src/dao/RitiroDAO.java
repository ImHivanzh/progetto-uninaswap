package dao;

import db.dbConnection;
import exception.DatabaseException;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;

/**
 * DAO per accesso dati ritiro.
 */
public class RitiroDAO {

  /**
   * Connessione al database.
   */
  private final Connection con;

  /**
   * Crea DAO e inizializza database connessione.
   *
   * @throws DatabaseException quando database e non disponibile
   */
  public RitiroDAO() throws DatabaseException {
    this.con = dbConnection.getInstance().getConnection();
    if (this.con == null) {
      throw new DatabaseException("Connessione al database non disponibile.");
    }
  }

  /**
   * Verifica se ritiro esiste per annuncio.
   *
   * @param idAnnuncio id annuncio
   * @return true quando esiste gia un ritiro
   * @throws DatabaseException quando query fallisce
   */
  public boolean esistePerAnnuncio(int idAnnuncio) throws DatabaseException {
    if (con == null) throw new DatabaseException("Connessione al database non disponibile.");

    String sql = "SELECT 1 FROM ritiro WHERE idannuncio = ? LIMIT 1";
    try (PreparedStatement ps = con.prepareStatement(sql)) {
      ps.setInt(1, idAnnuncio);
      try (ResultSet rs = ps.executeQuery()) {
        return rs.next();
      }
    } catch (SQLException e) {
      throw new DatabaseException("Errore durante la verifica ritiro", e);
    }
  }

  /**
   * Inserisce dati ritiro per annuncio.
   *
   * @param sede sede ritiro
   * @param orario orario ritiro
   * @param data data ritiro
   * @param numeroTelefono telefono contatto
   * @param idAnnuncio id annuncio
   * @return true quando inserimento riesce
   * @throws DatabaseException quando inserimento fallisce
   */
  public boolean inserisciRitiro(
          String sede, Time orario, Date data, String numeroTelefono, int idAnnuncio)
          throws DatabaseException {
    if (con == null) throw new DatabaseException("Connessione al database non disponibile.");

    String sql = "INSERT INTO ritiro (sede, orario, data, numerotelefono, ritirato, idannuncio) " +
            "VALUES (?, ?, ?, ?, ?, ?)";

    try (PreparedStatement ps = con.prepareStatement(sql)) {
      ps.setString(1, sede);
      ps.setTime(2, orario);
      ps.setDate(3, data);
      ps.setString(4, numeroTelefono);
      ps.setBoolean(5, false);
      ps.setInt(6, idAnnuncio);
      return ps.executeUpdate() > 0;
    } catch (SQLException e) {
      throw new DatabaseException("Errore durante l'inserimento del ritiro", e);
    }
  }
}
