package dao;

import db.dbConnection;
import exception.DatabaseException;
import model.Immagini;
import model.Annuncio;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO per accesso dati immagini.
 */
public class ImmaginiDAO {

  /**
   * Connessione al database.
   */
  private Connection con;

  /**
   * Crea DAO e inizializza database connessione.
   */
  public ImmaginiDAO() {
    try {
      this.con = dbConnection.getInstance().getConnection();
    } catch (DatabaseException e) {
      System.err.println("Errore connessione DB in ImmaginiDAO: " + e.getMessage());
    }
  }

  /**
   * Salva immagine collegato a annuncio.
   *
   * @param immagine immagine entita
   * @return true quando inserimento riesce
   * @throws DatabaseException quando inserimento fallisce
   */
  public boolean salvaImmagine(Immagini immagine) throws DatabaseException {
    String sql = "INSERT INTO immagini (immagine, idannuncio) VALUES (?, ?)";

    try (PreparedStatement ps = con.prepareStatement(sql)) {
      if (immagine.getImmagine() != null) {
        ps.setBytes(1, immagine.getImmagine());
      } else {
        ps.setNull(1, Types.BINARY);
      }

      if (immagine.getAnnuncio() != null) {
        ps.setInt(2, immagine.getAnnuncio().getIdAnnuncio());
      } else {
        throw new DatabaseException("Impossibile salvare: Annuncio mancante.");
      }

      return ps.executeUpdate() > 0;

    } catch (SQLException e) {
      throw new DatabaseException("Errore salvataggio immagine BLOB", e);
    }
  }

  /**
   * Restituisce tutti immagini per specifico annuncio.
   *
   * @param idAnnuncio id annuncio
   * @return lista di immagini
   */
  public List<Immagini> getImmaginiByAnnuncio(int idAnnuncio) {
    List<Immagini> lista = new ArrayList<>();
    String sql = "SELECT * FROM immagini WHERE idannuncio = ?";

    try (PreparedStatement ps = con.prepareStatement(sql)) {
      ps.setInt(1, idAnnuncio);

      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          Immagini img = new Immagini();
          img.setIdImmagine(rs.getInt("idimmagine"));
          img.setImmagine(rs.getBytes("immagine"));

          Annuncio a = new Annuncio();
          a.setIdAnnuncio(idAnnuncio);
          img.setAnnuncio(a);

          lista.add(img);
        }
      }
    } catch (SQLException e) {
      System.err.println("Errore recupero immagini: " + e.getMessage());
    }
    return lista;
  }
}
