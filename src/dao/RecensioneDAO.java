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

  private Connection con;

  /**
   * Creates the DAO and initializes the database connection.
   */
  public RecensioneDAO() {
    try {
      this.con = dbConnection.getInstance().getConnection();
    } catch (DatabaseException e) {
      System.err.println("Errore di connessione al database in RecensioneDAO: " + e.getMessage());
    }
  }

  /**
   * Inserts a review into the database.
   *
   * @param recensione review to insert
   * @return true when the insert succeeds
   * @throws DatabaseException when the insert fails
   */
  public boolean inserisciRecensione(Recensione recensione) throws DatabaseException {
    if (con == null) throw new DatabaseException("Connessione DB non disponibile.");

    String sql = "INSERT INTO recensione (idutente, idutenterecensito, voto, descrizione) VALUES (?, ?, ?, ?)";

    try (PreparedStatement ps = con.prepareStatement(sql)) {

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
   * Returns the reviews received by a specific user.
   *
   * @param idUtenteRecensito reviewed user id
   * @return list of reviews
   * @throws DatabaseException when the query fails
   */
  public List<Recensione> getRecensioniRicevute(int idUtenteRecensito) throws DatabaseException {
    String sql = "SELECT r.idutente, r.idutenterecensito, r.voto, r.descrizione, u.nomeutente "
            + "FROM recensione r LEFT JOIN utente u ON u.idutente = r.idutente "
            + "WHERE r.idutenterecensito = ?";
    List<Recensione> recensioni = new ArrayList<>();

    if (con == null) return recensioni;

    try (PreparedStatement ps = con.prepareStatement(sql)) {

      ps.setInt(1, idUtenteRecensito);

      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          Recensione r = new Recensione();
          r.setIdUtente(rs.getInt("idutente"));
          r.setIdUtenteRecensito(rs.getInt("idutenterecensito"));
          r.setVoto(rs.getInt("voto"));
          r.setDescrizione(rs.getString("descrizione"));
          r.setNomeUtente(rs.getString("nomeutente"));
          recensioni.add(r);
        }
      }
    } catch (SQLException e) {
      throw new DatabaseException("Errore durante il recupero delle recensioni", e);
    }
    return recensioni;
  }
}
