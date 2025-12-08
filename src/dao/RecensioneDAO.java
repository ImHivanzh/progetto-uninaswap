package dao;

import model.Recensione;
import db.dbConnection;
import exception.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RecensioneDAO {

  public RecensioneDAO() {}

  /**
   * Inserisce una nuova recensione nel database.
   * @param recensione Oggetto Recensione da salvare.
   * @return true se l'inserimento ha successo.
   * @throws DatabaseException in caso di errore SQL.
   */
  public boolean inserisciRecensione(Recensione recensione) throws DatabaseException {
    String sql = "INSERT INTO recensione (idutente, idutenterecens, voto, descrizione) VALUES (?, ?, ?, ?)";

    try (Connection conn = dbConnection.getInstance().getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setInt(1, recensione.getIdUtente());
      ps.setInt(2, recensione.getIdUtenteRecensito());
      ps.setInt(3, recensione.getVoto());
      ps.setString(4, recensione.getDescrizione());

      int rowsAffected = ps.executeUpdate();
      return rowsAffected > 0;
    } catch (SQLException e) {
      throw new DatabaseException("Errore durante l'inserimento della recensione", e);
    }
  }
}