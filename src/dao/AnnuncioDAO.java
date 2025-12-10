package dao;

import model.Annuncio;
import model.Vendita;
import model.enums.Categoria;
import model.enums.TipoAnnuncio;
import db.dbConnection;
import exception.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class AnnuncioDAO {

  public AnnuncioDAO() {}

  /**
   * Pubblica un nuovo annuncio nel database.
   * Gestisce diversamente gli annunci di tipo VENDITA salvando il prezzo.
   * Imposta lo stato dell'annuncio a 'true' (attivo) di default.
   *
   * @param annuncio L'oggetto Annuncio da salvare.
   * @return true se l'inserimento va a buon fine, false altrimenti.
   * @throws DatabaseException in caso di errore SQL.
   */
  public boolean pubblicaAnnuncio(Annuncio annuncio) throws DatabaseException {
    String sql = "INSERT INTO annuncio(titolo, descrizione, prezzo, tipoannuncio, idutente, stato, categoria) VALUES (?, ?, ?, ?, ?, ?, ?)";

    try (Connection conn = dbConnection.getInstance().getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setString(1, annuncio.getTitolo());
      ps.setString(2, annuncio.getDescrizione());

      // Gestione del prezzo solo se è una Vendita
      if (annuncio.getTipoAnnuncio() == TipoAnnuncio.VENDITA) {
        // Controllo se l'oggetto è effettivamente istanza di Vendita per recuperare il prezzo
        if (annuncio instanceof Vendita) {
          ps.setDouble(3, ((Vendita) annuncio).getPrezzo());
        } else {
          ps.setNull(3, Types.NUMERIC);
        }
      } else {
        ps.setNull(3, Types.NUMERIC);
      }

      // Nota: nel DB usiamo la stringa dell'enum (es. "vendita", "scambio")
      ps.setString(4, annuncio.getTipoAnnuncio().name());

      // Uso getIdUtente()
      ps.setInt(5, annuncio.getIdUtente());

      // Stato attivo di default
      ps.setBoolean(6, true);

      // Categoria
      ps.setString(7, annuncio.getCategoria().name());

      int rowsAffected = ps.executeUpdate();
      return rowsAffected > 0;

    } catch (SQLException e) {
      throw new DatabaseException("Errore durante la pubblicazione dell'annuncio", e);
    }
  }

  /**
   * Recupera tutti gli annunci pubblicati da uno specifico utente.
   * Fondamentale per la scheda Profilo.
   *
   * @param idUtente L'ID dell'utente.
   * @return Lista di annunci.
   * @throws DatabaseException in caso di errore SQL.
   */
  public List<Annuncio> findAllByUtente(int idUtente) throws DatabaseException {
    String sql = "SELECT * FROM annuncio WHERE idutente = ?";
    List<Annuncio> annunci = new ArrayList<>();

    try (Connection conn = dbConnection.getInstance().getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setInt(1, idUtente);

      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          annunci.add(mapResultSetToAnnuncio(rs));
        }
      }
    } catch (SQLException e) {
      throw new DatabaseException("Errore nel recupero annunci utente", e);
    }
    return annunci;
  }

  /**
   * Recupera un singolo annuncio tramite il suo ID.
   *
   * @param idAnnuncio L'ID univoco dell'annuncio.
   * @return L'oggetto Annuncio trovato, o null se non esiste.
   * @throws DatabaseException in caso di errore SQL.
   */
  public Annuncio findById(int idAnnuncio) throws DatabaseException {
    String sql = "SELECT * FROM annuncio WHERE idannuncio = ?";

    try (Connection conn = dbConnection.getInstance().getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setInt(1, idAnnuncio);

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return mapResultSetToAnnuncio(rs);
        }
      }
    } catch (SQLException e) {
      throw new DatabaseException("Errore nel recupero dell'annuncio", e);
    }
    return null;
  }

  /**
   * Elimina un annuncio dal database.
   *
   * @param idAnnuncio L'ID dell'annuncio da eliminare.
   * @return true se l'eliminazione ha avuto successo.
   * @throws DatabaseException in caso di errore SQL.
   */
  public boolean deleteAnnuncio(int idAnnuncio) throws DatabaseException {
    String sql = "DELETE FROM annuncio WHERE idannuncio = ?";

    try (Connection conn = dbConnection.getInstance().getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setInt(1, idAnnuncio);

      int rowsAffected = ps.executeUpdate();
      return rowsAffected > 0;

    } catch (SQLException e) {
      throw new DatabaseException("Errore durante l'eliminazione dell'annuncio", e);
    }
  }

  /**
   * Metodo helper privato per mappare una riga del ResultSet in un oggetto Annuncio.
   * Gestisce il polimorfismo per la classe Vendita.
   */
  private Annuncio mapResultSetToAnnuncio(ResultSet rs) throws SQLException {
    Annuncio a;
    String tipoStr = rs.getString("tipoannuncio");
    TipoAnnuncio tipo;

    try {
      tipo = TipoAnnuncio.valueOf(tipoStr.toUpperCase());
    } catch (IllegalArgumentException | NullPointerException e) {
      tipo = TipoAnnuncio.VENDITA; // Default fallback
    }

    // Se è una vendita, istanzio la classe specifica e setto il prezzo
    if (tipo == TipoAnnuncio.VENDITA) {
      Vendita v = new Vendita();
      v.setPrezzo(rs.getDouble("prezzo"));
      a = v;
    } else {
      a = new Annuncio();
    }

    // Popolo i campi comuni
    a.setIdAnnuncio(rs.getInt("idannuncio"));
    a.setIdUtente(rs.getInt("idutente"));
    a.setTitolo(rs.getString("titolo"));
    a.setDescrizione(rs.getString("descrizione"));
    a.setTipoAnnuncio(tipo);
    a.setStato(rs.getBoolean("stato"));

    try {
      String cat = rs.getString("categoria");
      if (cat != null) a.setCategoria(Categoria.valueOf(cat.toUpperCase()));
    } catch (IllegalArgumentException e) {
      // Gestione errore categoria non valida
    }

    return a;
  }
}