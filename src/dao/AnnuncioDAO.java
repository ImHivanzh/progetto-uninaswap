package dao;

import db.dbConnection;
import exception.DatabaseException;
import model.*;
import model.enums.Categoria;
import model.enums.TipoAnnuncio;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AnnuncioDAO {

  private Connection con;

  /**
   * Creates the DAO and initializes the database connection.
   */
  public AnnuncioDAO() {
    try {
      this.con = dbConnection.getInstance().getConnection();
    } catch (DatabaseException e) {
      System.err.println("Errore di connessione al database: " + e.getMessage());
    }
  }

  /**
   * Inserts a new listing and returns its generated id.
   *
   * @param annuncio listing to persist
   * @return generated id, or -1 on failure
   * @throws DatabaseException when the database is unavailable or insert fails
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
   * Returns all listings.
   *
   * @return list of listings
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
   * Returns all listings for a specific user.
   *
   * @param idUtente user id
   * @return list of listings
   * @throws DatabaseException when the query fails
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
   * Returns a listing by id.
   *
   * @param idAnnuncio listing id
   * @return listing or null
   * @throws DatabaseException when the query fails
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
   * Deletes a listing by id.
   *
   * @param idAnnuncio listing id
   * @return true when a row was deleted
   * @throws DatabaseException when the delete fails
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
   * Maps a result set row to a listing instance.
   *
   * @param rs result set
   * @return mapped listing
   * @throws SQLException when reading values fails
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

    return annuncio;
  }
}
