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

/**
 * DAO per accesso dati recensioni.
 */
public class RecensioneDAO {

  /**
   * Connessione al database.
   */
  private Connection con;

  /**
   * Crea DAO e inizializza database connessione.
   */
  public RecensioneDAO() {
    try {
      this.con = dbConnection.getInstance().getConnection();
    } catch (DatabaseException e) {
      System.err.println("Errore di connessione al database in RecensioneDAO: " + e.getMessage());
    }
  }

  /**
   * Inserisce recensione in database.
   *
   * @param recensione recensione a inserimento
   * @return true quando inserimento riesce
   * @throws DatabaseException quando inserimento fallisce
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
   * Restituisce recensioni ricevute da specifico utente.
   *
   * @param idUtenteRecensito id utente recensito
   * @return lista di recensioni
   * @throws DatabaseException quando query fallisce
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

  /**
   * Verifica se due utenti hanno completato una transazione tra vendita o scambio.
   *
   * @param idUtenteA primo utente id
   * @param idUtenteB secondo utente id
   * @return true quando esiste transazione completata
   * @throws DatabaseException quando query fallisce
   */
  public boolean hannoTransazioneCompletata(int idUtenteA, int idUtenteB) throws DatabaseException {
    if (con == null) {
      throw new DatabaseException("Connessione DB non disponibile.");
    }

    String sql = "SELECT 1 FROM vendita v "
            + "JOIN annuncio a ON v.idannuncio = a.idannuncio "
            + "WHERE v.accettato = TRUE "
            + "AND ((v.idutente = ? AND a.idutente = ?) OR (v.idutente = ? AND a.idutente = ?)) "
            + "UNION ALL "
            + "SELECT 1 FROM scambio s "
            + "JOIN annuncio a ON s.idannuncio = a.idannuncio "
            + "WHERE s.accettato = TRUE "
            + "AND ((s.idutente = ? AND a.idutente = ?) OR (s.idutente = ? AND a.idutente = ?)) "
            + "LIMIT 1";

    try (PreparedStatement ps = con.prepareStatement(sql)) {
      ps.setInt(1, idUtenteA);
      ps.setInt(2, idUtenteB);
      ps.setInt(3, idUtenteB);
      ps.setInt(4, idUtenteA);
      ps.setInt(5, idUtenteA);
      ps.setInt(6, idUtenteB);
      ps.setInt(7, idUtenteB);
      ps.setInt(8, idUtenteA);

      try (ResultSet rs = ps.executeQuery()) {
        return rs.next();
      }
    } catch (SQLException e) {
      throw new DatabaseException("Errore durante la verifica della transazione completata", e);
    }
  }
}
