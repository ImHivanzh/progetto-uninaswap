package dao;

import db.dbConnection;
import exception.DatabaseException;
import model.Immagini;
import model.Annuncio;
import utils.Logger;
import utils.Validator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO per l'accesso ai dati delle immagini.
 */
public class ImmaginiDAO {

  private static final String COLUMN_IMMAGINE = "immagine";

  /**
   * Connessione al database.
   */
  private Connection con;

  /**
   * Crea il DAO e inizializza la connessione al database.
   */
  public ImmaginiDAO() {
    try {
      this.con = dbConnection.getInstance().getConnection();
    } catch (DatabaseException e) {
      Logger.error("Errore connessione DB in ImmaginiDAO", e);
    }
  }

  /**
   * Salva un'immagine collegata a un annuncio.
   *
   * @param immagine oggetto immagine da salvare
   * @return true se l'inserimento ha successo
   * @throws DatabaseException se l'inserimento fallisce
   */
  public boolean salvaImmagine(Immagini immagine) throws DatabaseException {
    Validator.requireNonNull(immagine, COLUMN_IMMAGINE);

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
   * Restituisce tutte le immagini di uno specifico annuncio.
   *
   * @param annuncio annuncio di cui recuperare le immagini
   * @return lista delle immagini
   * @throws DatabaseException se il recupero fallisce
   */
  public List<Immagini> getImmaginiByAnnuncio(Annuncio annuncio) throws DatabaseException {
    Validator.requireNonNull(annuncio, "annuncio");
    Validator.requirePositive(annuncio.getIdAnnuncio(), "annuncio.idAnnuncio");

    List<Immagini> lista = new ArrayList<>();
    String sql = "SELECT idimmagine, immagine, idannuncio FROM immagini WHERE idannuncio = ?";

    try (PreparedStatement ps = con.prepareStatement(sql)) {
      ps.setInt(1, annuncio.getIdAnnuncio());

      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          Immagini img = new Immagini();
          img.setImmagine(rs.getBytes(COLUMN_IMMAGINE));
          img.setAnnuncio(annuncio);

          lista.add(img);
        }
      }
    } catch (SQLException e) {
      throw new DatabaseException("Errore recupero immagini per annuncio " + annuncio.getIdAnnuncio(), e);
    }
    return lista;
  }

  /**
   * Restituisce solo la prima immagine di un annuncio (ottimizzato).
   * Evita di caricare tutte le immagini quando serve solo la prima.
   *
   * @param annuncio annuncio di cui recuperare la prima immagine
   * @return byte array della prima immagine, o null se non presente
   * @throws DatabaseException se il recupero fallisce
   */
  public byte[] getPrimaImmagine(Annuncio annuncio) throws DatabaseException {
    Validator.requireNonNull(annuncio, "annuncio");
    Validator.requirePositive(annuncio.getIdAnnuncio(), "annuncio.idAnnuncio");

    String sql = "SELECT immagine FROM immagini WHERE idannuncio = ? LIMIT 1";

    try (PreparedStatement ps = con.prepareStatement(sql)) {
      ps.setInt(1, annuncio.getIdAnnuncio());

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return rs.getBytes(COLUMN_IMMAGINE);
        }
      }
    } catch (SQLException e) {
      throw new DatabaseException("Errore recupero prima immagine per annuncio " + annuncio.getIdAnnuncio(), e);
    }
    return new byte[0];
  }
}
