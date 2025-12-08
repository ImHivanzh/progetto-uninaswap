package dao;

import model.Recensione;
import db.dbConnection;
import exception.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RecensioneDAO {

  public RecensioneDAO() {}

  /**
   * Inserisce una nuova recensione nel database.
   */
  public boolean inserisciRecensione(Recensione recensione) throws DatabaseException {
    String sql = "INSERT INTO recensione (idutente, idutenterecensito, voto, descrizione) VALUES (?, ?, ?, ?)";

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

  /**
   * Recupera la lista delle recensioni ricevute da un determinato utente.
   * @param idUtenteRecensito L'ID dell'utente di cui visualizzare le recensioni.
   * @return Lista di oggetti Recensione.
   */
  public List<Recensione> getRecensioniRicevute(int idUtenteRecensito) throws DatabaseException {
    String sql = "SELECT * FROM recensione WHERE idutenterecensito = ?";
    List<Recensione> recensioni = new ArrayList<>();

    try (Connection conn = dbConnection.getInstance().getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setInt(1, idUtenteRecensito);

      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          Recensione r = new Recensione();
          // Nota: idutente qui è chi HA SCRITTO la recensione
          r.setIdUtente(rs.getInt("idutente"));
          r.setIdUtenteRecensito(rs.getInt("idutenterecensito"));
          r.setVoto(rs.getInt("voto"));
          r.setDescrizione(rs.getString("descrizione"));
          recensioni.add(r);
        }
      }
    } catch (SQLException e) {
      throw new DatabaseException("Errore durante il recupero delle recensioni", e);
    }
    return recensioni;
  }
}