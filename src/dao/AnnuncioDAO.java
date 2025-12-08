package dao;

import model.Annuncio;
import db.dbConnection;
import exception.DatabaseException;
import model.enums.TipoAnnuncio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public class AnnuncioDAO {

  public AnnuncioDAO() {}

  /**
   * Inserisce un nuovo annuncio nel database.
   * @param annuncio L'oggetto Annuncio da salvare.
   * @return true se l'inserimento va a buon fine.
   * @throws DatabaseException in caso di errore.
   */
  public boolean pubblicaAnnuncio(Annuncio annuncio) throws DatabaseException {
    String sql = "INSERT INTO annuncio(titolo, descrizione, prezzo, tipoannuncio, idutente, stato) VALUES (?, ?, ?, ?, ?, ?)";

    try (Connection conn = dbConnection.getInstance().getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setString(1, annuncio.getTitolo());
      ps.setString(2, annuncio.getDescrizione());

      if (annuncio.getTipoAnnuncio() == TipoAnnuncio.VENDITA) {
        ps.setFloat(3, annuncio.getPrezzo());
      } else {
        ps.setNull(3, Types.NUMERIC);
      }

      ps.setString(4, annuncio.getTipoAnnuncio().toString().toLowerCase());
      ps.setInt(5, annuncio.getUtente().getIdUtente());
      ps.setBoolean(6, true);

      int rowsAffected = ps.executeUpdate();
      return rowsAffected > 0;
    } catch (SQLException e) {
      throw new DatabaseException("Errore durante la pubblicazione dell'annuncio", e);
    }
  }
}