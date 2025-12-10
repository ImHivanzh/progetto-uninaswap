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
      // Tentativo di connessione gestendo l'eccezione personalizzata DatabaseException
      this.con = dbConnection.getInstance().getConnection();
    } catch (DatabaseException e) {
      System.err.println("Errore di connessione al database: " + e.getMessage());
      // In caso di errore la connessione resta null
    }
  }

  /**
   * Inserisce un nuovo annuncio nel database.
   * Gestisce i campi specifici (prezzo, oggetto_richiesto) tramite controlli instanceof.
   * * @throws DatabaseException se si verifica un errore SQL o di connessione.
   */
  public boolean pubblicaAnnuncio(Annuncio annuncio) throws DatabaseException {
    if (con == null) {
      throw new DatabaseException("Connessione al database non disponibile.");
    }

    // Query generica che include tutti i possibili campi della tabella
    String sql = "INSERT INTO annuncio (titolo, descrizione, categoria, utente_id, tipo_annuncio, prezzo, oggetto_richiesto) VALUES (?, ?, ?, ?, ?, ?, ?)";

    try (PreparedStatement ps = con.prepareStatement(sql)) {
      ps.setString(1, annuncio.getTitolo());
      ps.setString(2, annuncio.getDescrizione());
      ps.setString(3, annuncio.getCategoria().toString());
      ps.setInt(4, annuncio.getUtenteID());
      ps.setString(5, annuncio.getTipoAnnuncio().toString());

      // Gestione PREZZO (Solo per Vendita)
      if (annuncio instanceof Vendita) {
        ps.setDouble(6, ((Vendita) annuncio).getPrezzo());
      } else {
        ps.setNull(6, java.sql.Types.DOUBLE);
      }

      // Gestione OGGETTO RICHIESTO (Solo per Scambio)
      if (annuncio instanceof Scambio) {
        ps.setString(7, ((Scambio) annuncio).getOggettoRichiesto());
      } else {
        ps.setNull(7, java.sql.Types.VARCHAR);
      }

      int rows = ps.executeUpdate();
      return rows > 0;

    } catch (SQLException e) {
      // Lancia l'eccezione personalizzata invece di stampare e ritornare false
      throw new DatabaseException("Errore durante l'inserimento dell'annuncio: " + e.getMessage());
    }
  }

  /**
   * Recupera tutti gli annunci dal database.
   * Ricostruisce l'oggetto corretto (Vendita, Scambio o Regalo) in base alla colonna 'tipo_annuncio'.
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
   * Recupera un singolo annuncio per ID.
   */
  public Annuncio findById(int id) {
    if (con == null) return null;

    String sql = "SELECT * FROM annuncio WHERE id = ?";
    try (PreparedStatement ps = con.prepareStatement(sql)) {
      ps.setInt(1, id);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return mapResultSetToAnnuncio(rs);
        }
      }
    } catch (SQLException e) {
      System.err.println("Errore durante la ricerca dell'annuncio per ID: " + e.getMessage());
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Metodo helper privato per mappare una riga del ResultSet nell'oggetto Java corretto.
   */
  private Annuncio mapResultSetToAnnuncio(ResultSet rs) throws SQLException {
    int id = rs.getInt("id");
    String titolo = rs.getString("titolo");
    String descrizione = rs.getString("descrizione");

    // Gestione Enum Categoria con fallback
    Categoria categoria;
    try {
      categoria = Categoria.valueOf(rs.getString("categoria").toUpperCase());
    } catch (IllegalArgumentException | NullPointerException e) {
      categoria = Categoria.ALTRO;
    }

    int utenteId = rs.getInt("utente_id");
    String tipoString = rs.getString("tipo_annuncio");
    TipoAnnuncio tipo;
    try {
      tipo = TipoAnnuncio.valueOf(tipoString.toUpperCase());
    } catch (IllegalArgumentException | NullPointerException e) {
      tipo = TipoAnnuncio.VENDITA; // Default fallback
    }

    // Creazione dell'istanza specifica in base al tipo
    switch (tipo) {
      case VENDITA:
        double prezzo = rs.getDouble("prezzo");
        return new Vendita(id, titolo, descrizione, categoria, utenteId, prezzo);

      case SCAMBIO:
        String oggettoRichiesto = rs.getString("oggetto_richiesto");
        return new Scambio(id, titolo, descrizione, categoria, utenteId, oggettoRichiesto);

      case REGALO:
        return new Regalo(id, titolo, descrizione, categoria, utenteId);

      default:
        // Fallback di sicurezza
        return new Vendita(id, titolo, descrizione, categoria, utenteId, 0.0);
    }
  }
}