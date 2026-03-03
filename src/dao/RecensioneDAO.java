package dao;

import model.Recensione;
import db.dbConnection;
import exception.DatabaseException;
import utils.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO per l'accesso ai dati delle recensioni.
 */
public class RecensioneDAO {

  /**
   * Connessione al database.
   */
  private Connection con;

  /**
   * Crea il DAO e inizializza la connessione al database.
   */
  public RecensioneDAO() {
    try {
      this.con = dbConnection.getInstance().getConnection();
    } catch (DatabaseException e) {
      Logger.error("Errore di connessione al database in RecensioneDAO", e);
    }
  }

  /**
   * Inserisce una recensione nel database.
   *
   * @param recensione recensione da inserire
   * @return true se l'inserimento ha successo
   * @throws DatabaseException se l'inserimento fallisce
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
   * Restituisce le recensioni ricevute da uno specifico utente.
   *
   * @param idUtenteRecensito ID dell'utente recensito
   * @return lista delle recensioni
   * @throws DatabaseException se la query fallisce
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
   * Verifica se due utenti hanno completato una transazione (vendita o scambio).
   *
   * @param idUtenteA ID del primo utente
   * @param idUtenteB ID del secondo utente
   * @return true se esiste una transazione completata
   * @throws DatabaseException se la query fallisce
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
