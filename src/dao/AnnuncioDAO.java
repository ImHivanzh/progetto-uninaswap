package dao;

import db.dbConnection;
import exception.DatabaseException;
import model.*;
import model.enums.Categoria;
import model.enums.TipoAnnuncio;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO per accesso dati annunci.
 */
public class AnnuncioDAO {

  /**
   * Connessione al database.
   */
  private Connection con;

  /**
   * Crea DAO e inizializza database connessione.
   */
  public AnnuncioDAO() {
    try {
      this.con = dbConnection.getInstance().getConnection();
    } catch (DatabaseException e) {
      System.err.println("Errore di connessione al database: " + e.getMessage());
    }
  }

  /**
   * Inserisce nuovo annuncio e restituisce id generato.
   *
   * @param annuncio annuncio a salva
   * @return generato id, o -1 in caso di errore
   * @throws DatabaseException quando database e non disponibile o inserimento fallisce
   */
  public int pubblicaAnnuncio(Annuncio annuncio) throws DatabaseException {
    if (con == null) {
      throw new DatabaseException("Connessione al database non disponibile.");
    }

    String sql = "INSERT INTO annuncio (titolo, descrizione, categoria, idutente, tipoannuncio, prezzo, oggetto_richiesto, stato) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      ps.setString(1, annuncio.getTitolo());
      ps.setString(2, annuncio.getDescrizione());
      ps.setString(3, annuncio.getCategoria().name());
      ps.setInt(4, annuncio.getIdUtente());
      ps.setString(5, annuncio.getTipoAnnuncio().name());

      if (annuncio instanceof Vendita) {
        ps.setDouble(6, ((Vendita) annuncio).getPrezzo());
      } else {
        ps.setNull(6, java.sql.Types.DOUBLE);
      }

      if (annuncio instanceof Scambio) {
        ps.setString(7, ((Scambio) annuncio).getOggettoRichiesto());
      } else {
        ps.setNull(7, java.sql.Types.VARCHAR);
      }

      ps.setBoolean(8, true);

      int rows = ps.executeUpdate();

      if (rows > 0) {
        try (ResultSet rs = ps.getGeneratedKeys()) {
          if (rs.next()) {
            return rs.getInt(1);
          }
        }
      }
      return -1;

    } catch (SQLException e) {
      throw new DatabaseException("Errore durante l'inserimento dell'annuncio: " + e.getMessage());
    }
  }

  /**
   * Restituisce tutti annunci.
   *
   * @return lista di annunci
   */
  public List<Annuncio> findAll() {
    List<Annuncio> annunci = new ArrayList<>();
    if (con == null) return annunci;

    String sql = "SELECT * FROM annuncio";

    try (Statement stmt = con.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {

      while (rs.next()) {
        Annuncio a = mapResultSetToAnnuncio(rs);
        if (a != null) {
          annunci.add(a);
        }
      }
    } catch (SQLException e) {
      System.err.println("Errore durante il recupero degli annunci: " + e.getMessage());
      e.printStackTrace();
    }
    return annunci;
  }

  /**
   * Restituisce tutti annunci per specifico utente.
   *
   * @param idUtente utente id
   * @return lista di annunci
   * @throws DatabaseException quando query fallisce
   */
  public List<Annuncio> findAllByUtente(int idUtente) throws DatabaseException {
    String sql = "SELECT * FROM annuncio WHERE idutente = ?";
    List<Annuncio> annunci = new ArrayList<>();

    try (PreparedStatement ps = con.prepareStatement(sql)) {
      ps.setInt(1, idUtente);

      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          Annuncio a = mapResultSetToAnnuncio(rs);
          if (a != null) annunci.add(a);
        }
      }
    } catch (SQLException e) {
      throw new DatabaseException("Errore nel recupero annunci utente", e);
    }
    return annunci;
  }

  /**
   * Restituisce annuncio da id.
   *
   * @param idAnnuncio id annuncio
   * @return annuncio o null
   * @throws DatabaseException quando query fallisce
   */
  public Annuncio findById(int idAnnuncio) throws DatabaseException {
    String sql = "SELECT * FROM annuncio WHERE idannuncio = ?";

    try (PreparedStatement ps = con.prepareStatement(sql)) {
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
   * Elimina annuncio da id.
   *
   * @param idAnnuncio id annuncio
   * @return true quando riga era eliminato
   * @throws DatabaseException quando elimina fallisce
   */
  public boolean deleteAnnuncio(int idAnnuncio) throws DatabaseException {
    String sql = "DELETE FROM annuncio WHERE idannuncio = ?";

    try (PreparedStatement ps = con.prepareStatement(sql)) {
      ps.setInt(1, idAnnuncio);
      return ps.executeUpdate() > 0;
    } catch (SQLException e) {
      throw new DatabaseException("Errore durante l'eliminazione dell'annuncio", e);
    }
  }

  /**
   * Mappa riga del set di risultati a annuncio istanza.
   *
   * @param rs set di risultati
   * @return mappato annuncio
   * @throws SQLException quando lettura valori fallisce
   */
  private Annuncio mapResultSetToAnnuncio(ResultSet rs) throws SQLException {
    int id = rs.getInt("idannuncio");
    String titolo = rs.getString("titolo");
    String descrizione = rs.getString("descrizione");
    int idUtente = rs.getInt("idutente");
    boolean stato = rs.getBoolean("stato");

    Categoria categoria;
    try {
      categoria = Categoria.valueOf(rs.getString("categoria").toUpperCase());
    } catch (Exception e) {
      categoria = Categoria.ALTRO;
    }

    TipoAnnuncio tipo;
    try {
      tipo = TipoAnnuncio.valueOf(rs.getString("tipoannuncio").toUpperCase());
    } catch (Exception e) {
      tipo = TipoAnnuncio.VENDITA;
    }

    Annuncio annuncio;

    switch (tipo) {
      case VENDITA:
        double prezzo = rs.getDouble("prezzo");
        Vendita v = new Vendita();
        v.setPrezzo(prezzo);
        annuncio = v;
        break;
      case SCAMBIO:
        String oggettoRichiesto = rs.getString("oggetto_richiesto");
        Scambio s = new Scambio(titolo, descrizione, categoria, idUtente, oggettoRichiesto);
        annuncio = s;
        break;
      case REGALO:
        annuncio = new Regalo(titolo, descrizione, categoria, idUtente);
        break;
      default:
        annuncio = new Annuncio();
    }

    annuncio.setIdAnnuncio(id);
    annuncio.setIdUtente(idUtente);
    annuncio.setTitolo(titolo);
    annuncio.setDescrizione(descrizione);
    annuncio.setCategoria(categoria);
    annuncio.setTipoAnnuncio(tipo);
    annuncio.setStato(stato);
    annuncio.setSpedizione(readSpedizione(rs));

    return annuncio;
  }

  private Boolean readSpedizione(ResultSet rs) {
    try {
      Object spedizioneObj = rs.getObject("spedizione");
      if (spedizioneObj == null) {
        return null;
      }
      if (spedizioneObj instanceof Boolean) {
        return (Boolean) spedizioneObj;
      }
      if (spedizioneObj instanceof Number) {
        return ((Number) spedizioneObj).intValue() == 1;
      }
      String valore = spedizioneObj.toString().trim().toLowerCase();
      if (valore.equals("1") || valore.equals("true") || valore.equals("t")) {
        return true;
      }
      if (valore.equals("0") || valore.equals("false") || valore.equals("f")) {
        return false;
      }
    } catch (SQLException ignored) {
      return null;
    }
    return null;
  }
}
