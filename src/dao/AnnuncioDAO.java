package dao;

import db.dbConnection;
import exception.DatabaseException;
import model.Annuncio;
import model.Regalo;
import model.Scambio;
import model.Utente;
import model.Vendita;
import model.enums.Categoria;
import model.enums.TipoAnnuncio;
import utils.Constanti;
import utils.Logger;
import utils.Validator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO per l'accesso ai dati degli annunci.
 */
public class AnnuncioDAO {

  /**
   * Connessione al database.
   */
  private Connection con;

  /**
   * Crea il DAO e inizializza la connessione al database.
   */
  public AnnuncioDAO() {
    try {
      this.con = dbConnection.getInstance().getConnection();
    } catch (DatabaseException e) {
      Logger.error("Errore di connessione al database in AnnuncioDAO", e);
    }
  }

  /**
   * Inserisce un nuovo annuncio e restituisce l'ID generato.
   *
   * @param annuncio annuncio da salvare
   * @return ID generato, o -1 in caso di errore
   * @throws DatabaseException se il database non è disponibile o l'inserimento fallisce
   */
  public int pubblicaAnnuncio(Annuncio annuncio) throws DatabaseException {
    Validator.requireNonNull(annuncio, "annuncio");
    Validator.requireNonEmpty(annuncio.getTitolo(), "titolo");
    Validator.requireNonEmpty(annuncio.getDescrizione(), "descrizione");
    Validator.requireNonNull(annuncio.getUtente(), "utente");
    Validator.requirePositive(annuncio.getUtente().getIdUtente(), "utente.idUtente");

    if (con == null) {
      throw new DatabaseException("Connessione al database non disponibile.");
    }

    String sql = "INSERT INTO annuncio (titolo, descrizione, categoria, idutente, tipoannuncio, prezzo, oggetto_richiesto, stato) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      ps.setString(1, annuncio.getTitolo());
      ps.setString(2, annuncio.getDescrizione());
      ps.setString(3, annuncio.getCategoria().name());
      ps.setInt(4, annuncio.getUtente().getIdUtente());
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
   * Restituisce tutti gli annunci.
   *
   * @return lista degli annunci
   */
  public List<Annuncio> findAll() {
    List<Annuncio> annunci = new ArrayList<>();

    String sql = "SELECT a.idannuncio, a.titolo, a.descrizione, a.categoria, a.tipoannuncio, a.prezzo, a.oggetto_richiesto, a.stato, a.spedizione, "
            + "u.idutente, u.nomeutente, u.email, u.password, u.numerotelefono "
            + "FROM annuncio a "
            + "JOIN utente u ON a.idutente = u.idutente";

    try (Statement stmt = con.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {

      while (rs.next()) {
        Annuncio a = mapResultSetToAnnuncio(rs);
        if (a != null) {
          annunci.add(a);
        }
      }
    } catch (SQLException e) {
      Logger.error("Errore durante il recupero degli annunci", e);
    }
    return annunci;
  }

  /**
   * Ricerca annunci con filtri applicati direttamente nel database (ottimizzato).
   * Evita di caricare tutti gli annunci e filtrarli in memoria.
   *
   * @param testo testo da cercare in titolo o descrizione (null per ignorare)
   * @param categoria categoria da filtrare (null per ignorare)
   * @param tipo tipo annuncio da filtrare (null per ignorare)
   * @param prezzoMax prezzo massimo per vendite (null per ignorare)
   * @return lista di annunci filtrati
   */
  public List<Annuncio> search(String testo, String categoria, String tipo, Double prezzoMax) {
    List<Annuncio> annunci = new ArrayList<>();

    // Costruisce query dinamica in base ai filtri
    StringBuilder sql = new StringBuilder(
            "SELECT a.idannuncio, a.titolo, a.descrizione, a.categoria, a.tipoannuncio, a.prezzo, a.oggetto_richiesto, a.stato, a.spedizione, " +
            "u.idutente, u.nomeutente, u.email, u.password, u.numerotelefono " +
            "FROM annuncio a " +
            "JOIN utente u ON a.idutente = u.idutente " +
            "WHERE a.stato = true");
    List<Object> parametri = new ArrayList<>();

    // Filtro testo (cerca in titolo e descrizione)
    if (testo != null && !testo.trim().isEmpty()) {
      sql.append(" AND (LOWER(titolo) LIKE ? OR LOWER(descrizione) LIKE ?)");
      String pattern = "%" + testo.toLowerCase() + "%";
      parametri.add(pattern);
      parametri.add(pattern);
    }

    // Filtro categoria
    if (categoria != null && !categoria.trim().isEmpty() && !Constanti.CATEGORIA_TUTTE.equalsIgnoreCase(categoria.trim())) {
      sql.append(" AND UPPER(categoria) = ?");
      parametri.add(categoria.toUpperCase());
    }

    // Filtro tipo annuncio
    if (tipo != null && !tipo.trim().isEmpty() && !Constanti.TIPO_TUTTI.equalsIgnoreCase(tipo.trim())) {
      sql.append(" AND UPPER(tipoannuncio) = ?");
      parametri.add(tipo.toUpperCase());
    }

    // Filtro prezzo massimo (solo per vendite)
    if (prezzoMax != null && prezzoMax >= 0) {
      sql.append(" AND (tipoannuncio != 'VENDITA' OR prezzo <= ?)");
      parametri.add(prezzoMax);
    }

    try (PreparedStatement ps = con.prepareStatement(sql.toString())) {
      // Imposta i parametri nella query
      for (int i = 0; i < parametri.size(); i++) {
        ps.setObject(i + 1, parametri.get(i));
      }

      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          Annuncio a = mapResultSetToAnnuncio(rs);
          if (a != null) {
            annunci.add(a);
          }
        }
      }
    } catch (SQLException e) {
      Logger.error("Errore durante la ricerca degli annunci", e);
    }
    return annunci;
  }

  /**
   * Restituisce tutti gli annunci di uno specifico utente.
   *
   * @param utente utente proprietario degli annunci
   * @return lista degli annunci
   * @throws DatabaseException se la query fallisce
   */
  public List<Annuncio> findAllByUtente(Utente utente) throws DatabaseException {
    Validator.requireNonNull(utente, "utente");
    Validator.requirePositive(utente.getIdUtente(), "utente.idUtente");

    String sql = "SELECT a.idannuncio, a.titolo, a.descrizione, a.categoria, a.tipoannuncio, a.prezzo, a.oggetto_richiesto, a.stato, a.spedizione, " +
                 "u.idutente, u.nomeutente, u.email, u.password, u.numerotelefono " +
                 "FROM annuncio a " +
                 "JOIN utente u ON a.idutente = u.idutente " +
                 "WHERE a.idutente = ?";
    List<Annuncio> annunci = new ArrayList<>();

    try (PreparedStatement ps = con.prepareStatement(sql)) {
      ps.setInt(1, utente.getIdUtente());

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
   * Restituisce un annuncio dato l'ID.
   *
   * @param idAnnuncio ID dell'annuncio
   * @return annuncio trovato, o null se non esiste
   * @throws DatabaseException se la query fallisce
   */
  public Annuncio findById(int idAnnuncio) throws DatabaseException {
    Validator.requirePositive(idAnnuncio, "idAnnuncio");

    String sql = "SELECT a.idannuncio, a.titolo, a.descrizione, a.categoria, a.tipoannuncio, a.prezzo, a.oggetto_richiesto, a.stato, a.spedizione, "
            + "u.idutente, u.nomeutente, u.email, u.password, u.numerotelefono "
            + "FROM annuncio a "
            + "JOIN utente u ON a.idutente = u.idutente "
            + "WHERE a.idannuncio = ?";

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
   * Elimina un annuncio dato l'ID.
   *
   * @param idAnnuncio ID dell'annuncio
   * @return true se la riga è stata eliminata
   * @throws DatabaseException se l'eliminazione fallisce
   */
  public boolean deleteAnnuncio(int idAnnuncio) throws DatabaseException {
    Validator.requirePositive(idAnnuncio, "idAnnuncio");

    String sql = "DELETE FROM annuncio WHERE idannuncio = ?";

    try (PreparedStatement ps = con.prepareStatement(sql)) {
      ps.setInt(1, idAnnuncio);
      return ps.executeUpdate() > 0;
    } catch (SQLException e) {
      throw new DatabaseException("Errore durante l'eliminazione dell'annuncio", e);
    }
  }

  /**
   * Mappa una riga del ResultSet a un'istanza di Annuncio.
   *
   * @param rs ResultSet da cui leggere i dati
   * @return annuncio mappato
   * @throws SQLException se la lettura dei valori fallisce
   */
  private Annuncio mapResultSetToAnnuncio(ResultSet rs) throws SQLException {
    int id = rs.getInt("idannuncio");
    String titolo = rs.getString("titolo");
    String descrizione = rs.getString("descrizione");
    boolean stato = rs.getBoolean("stato");

    Categoria categoria = parseCategoria(rs.getString("categoria"));
    TipoAnnuncio tipo = parseTipoAnnuncio(rs.getString("tipoannuncio"));

    // Costruisce oggetto Utente direttamente dal ResultSet (già incluso nel JOIN)
    Utente utente = new Utente();
    utente.setIdUtente(rs.getInt("idutente"));
    utente.setUsername(rs.getString("nomeutente"));
    utente.setEmail(rs.getString("email"));
    utente.setPassword(rs.getString("password"));
    utente.setNumeroTelefono(rs.getString("numerotelefono"));

    Annuncio annuncio = creaAnnuncioPerTipo(tipo, titolo, descrizione, categoria, utente, rs);

    annuncio.setIdAnnuncio(id);
    annuncio.setUtente(utente);
    annuncio.setTitolo(titolo);
    annuncio.setDescrizione(descrizione);
    annuncio.setCategoria(categoria);
    annuncio.setTipoAnnuncio(tipo);
    annuncio.setStato(stato);
    annuncio.setSpedizione(readSpedizione(rs));

    return annuncio;
  }

  /**
   * Parsing sicuro della categoria da stringa.
   *
   * @param categoriaStr stringa categoria dal database
   * @return enum Categoria, o ALTRO se non riconosciuta
   */
  private Categoria parseCategoria(String categoriaStr) {
    try {
      return Categoria.valueOf(categoriaStr.toUpperCase());
    } catch (Exception e) {
      return Categoria.ALTRO;
    }
  }

  /**
   * Parsing sicuro del tipo annuncio da stringa.
   *
   * @param tipoStr stringa tipo dal database
   * @return enum TipoAnnuncio, o VENDITA se non riconosciuto
   */
  private TipoAnnuncio parseTipoAnnuncio(String tipoStr) {
    try {
      return TipoAnnuncio.valueOf(tipoStr.toUpperCase());
    } catch (Exception e) {
      return TipoAnnuncio.VENDITA;
    }
  }

  /**
   * Crea l'istanza specifica di Annuncio in base al tipo.
   *
   * @param tipo tipo di annuncio
   * @param titolo titolo annuncio
   * @param descrizione descrizione annuncio
   * @param categoria categoria annuncio
   * @param utente utente proprietario
   * @param rs ResultSet per leggere campi specifici
   * @return istanza di Annuncio (Vendita, Scambio, Regalo)
   * @throws SQLException se la lettura dei campi fallisce
   */
  private Annuncio creaAnnuncioPerTipo(TipoAnnuncio tipo, String titolo, String descrizione,
                                        Categoria categoria, Utente utente, ResultSet rs) throws SQLException {
    switch (tipo) {
      case VENDITA:
        double prezzo = rs.getDouble("prezzo");
        Vendita v = new Vendita();
        v.setPrezzo(prezzo);
        return v;
      case SCAMBIO:
        String oggettoRichiesto = rs.getString("oggetto_richiesto");
        return new Scambio(titolo, descrizione, categoria, utente, oggettoRichiesto);
      case REGALO:
        return new Regalo(titolo, descrizione, categoria, utente);
      default:
        return new Annuncio();
    }
  }

  /**
   * Legge il campo spedizione gestendo diversi formati dal database.
   *
   * @param rs ResultSet da cui leggere
   * @return Boolean (true/false/null)
   */

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
