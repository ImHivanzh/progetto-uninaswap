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

  public AnnuncioDAO() {
    try {
      this.con = dbConnection.getInstance().getConnection();
    } catch (DatabaseException e) {
      System.err.println("Errore di connessione al database: " + e.getMessage());
    }
  }

  /**
   * Inserisce un nuovo annuncio nel database.
   * Restituisce l'ID generato (o -1 in caso di errore).
   */
  public int pubblicaAnnuncio(Annuncio annuncio) throws DatabaseException {
    if (con == null) {
      throw new DatabaseException("Connessione al database non disponibile.");
    }

    // Query con RETURN_GENERATED_KEYS
    String sql = "INSERT INTO annuncio (titolo, descrizione, categoria, idutente, tipoannuncio, prezzo, oggetto_richiesto, stato) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    // NOTA: Qui usiamo 'con' direttamente. Chiudiamo solo il PreparedStatement 'ps'.
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
            return rs.getInt(1); // Ritorna il NUOVO ID
          }
        }
      }
      return -1; // Fallito

    } catch (SQLException e) {
      throw new DatabaseException("Errore durante l'inserimento dell'annuncio: " + e.getMessage());
    }
  }

  /**
   * Recupera tutti gli annunci dal database.
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
   * Recupera tutti gli annunci di un utente.
   * [CORRETTO] Usa la connessione condivisa senza chiuderla.
   */
  public List<Annuncio> findAllByUtente(int idUtente) throws DatabaseException {
    String sql = "SELECT * FROM annuncio WHERE idutente = ?";
    List<Annuncio> annunci = new ArrayList<>();

    // CORREZIONE IMPORTANTE: Non usiamo 'try (Connection conn = ...)' qui!
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
   * Recupera un annuncio per ID.
   * [CORRETTO] Usa la connessione condivisa senza chiuderla.
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
   * Elimina un annuncio.
   * [CORRETTO] Usa la connessione condivisa senza chiuderla.
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