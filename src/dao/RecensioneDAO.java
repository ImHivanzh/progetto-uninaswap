package dao;

import db.DbConnection;
import exception.DatabaseException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Recensione;
import model.Utente;
import utils.Logger;
import utils.Validator;

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
      this.con = DbConnection.getInstance().getConnection();
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
    Validator.requireNonNull(recensione, "recensione");
    Validator.requireNonNull(recensione.getUtenteRecensore(), "recensione.utenteRecensore");
    Validator.requireNonNull(recensione.getUtenteRecensito(), "recensione.utenteRecensito");

    if (con == null) throw new DatabaseException("Connessione DB non disponibile.");

    String sql = "INSERT INTO recensione (idutente, idutenterecensito, voto, descrizione, idannuncio) VALUES (?, ?, ?, ?, ?)";

    try (PreparedStatement ps = con.prepareStatement(sql)) {

      ps.setInt(1, recensione.getUtenteRecensore().getIdUtente());
      ps.setInt(2, recensione.getUtenteRecensito().getIdUtente());
      ps.setInt(3, recensione.getVoto());
      ps.setString(4, recensione.getDescrizione());

      if (recensione.getIdAnnuncio() != null) {
        ps.setInt(5, recensione.getIdAnnuncio());
      } else {
        ps.setNull(5, java.sql.Types.INTEGER);
      }

      int rowsAffected = ps.executeUpdate();
      return rowsAffected > 0;
    } catch (SQLException e) {
      throw new DatabaseException("Errore durante l'inserimento della recensione", e);
    }
  }

  /**
   * Restituisce le recensioni ricevute da uno specifico utente.
   *
   * @param utenteRecensito utente recensito
   * @return lista delle recensioni
   * @throws DatabaseException se la query fallisce
   */
  public List<Recensione> getRecensioniRicevute(Utente utenteRecensito) throws DatabaseException {
    Validator.requireNonNull(utenteRecensito, "utenteRecensito");
    Validator.requirePositive(utenteRecensito.getIdUtente(), "utenteRecensito.idUtente");

    String sql = "SELECT r.idutente, r.idutenterecensito, r.voto, r.descrizione, "
            + "u.nomeutente, u.mail, u.numerotelefono "
            + "FROM recensione r "
            + "JOIN utente u ON r.idutente = u.idutente "
            + "WHERE r.idutenterecensito = ?";
    List<Recensione> recensioni = new ArrayList<>();

    try (PreparedStatement ps = con.prepareStatement(sql)) {

      ps.setInt(1, utenteRecensito.getIdUtente());

      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          // Costruisce oggetto Utente direttamente dal ResultSet
          Utente utenteRecensore = new Utente();
          utenteRecensore.setIdUtente(rs.getInt("idutente"));
          utenteRecensore.setUsername(rs.getString("nomeutente"));
          utenteRecensore.setEmail(rs.getString("mail"));
          utenteRecensore.setNumeroTelefono(rs.getString("numerotelefono"));

          Recensione r = new Recensione();
          r.setUtenteRecensore(utenteRecensore);
          r.setUtenteRecensito(utenteRecensito);
          r.setVoto(rs.getInt("voto"));
          r.setDescrizione(rs.getString("descrizione"));
          recensioni.add(r);
        }
      }
    } catch (SQLException e) {
      throw new DatabaseException("Errore durante il recupero delle recensioni", e);
    }
    return recensioni;
  }

  /**
   * Verifica se esiste già una recensione per uno specifico annuncio.
   *
   * @param recensore utente che ha scritto la recensione
   * @param idAnnuncio ID dell'annuncio
   * @return true se esiste già una recensione per questo annuncio
   * @throws DatabaseException se la query fallisce
   */
  public boolean esisteRecensionePerAnnuncio(Utente recensore, int idAnnuncio) throws DatabaseException {
    Validator.requireNonNull(recensore, "recensore");
    Validator.requirePositive(recensore.getIdUtente(), "recensore.idUtente");
    Validator.requirePositive(idAnnuncio, "idAnnuncio");

    String sql = "SELECT 1 FROM recensione WHERE idutente = ? AND idannuncio = ? LIMIT 1";

    try (PreparedStatement ps = con.prepareStatement(sql)) {
      ps.setInt(1, recensore.getIdUtente());
      ps.setInt(2, idAnnuncio);

      try (ResultSet rs = ps.executeQuery()) {
        return rs.next();
      }
    } catch (SQLException e) {
      throw new DatabaseException("Errore durante la verifica della recensione per annuncio", e);
    }
  }

  /**
   * Verifica se un annuncio specifico è stato completato (proposta accettata).
   * Controlla sia se l'utente è l'acquirente (ha fatto la proposta) sia se è il venditore (proprietario dell'annuncio).
   *
   * @param idAnnuncio ID dell'annuncio
   * @param idUtenteRecensore ID dell'utente che vuole lasciare la recensione
   * @return true se l'annuncio è stato completato
   * @throws DatabaseException se la query fallisce
   */
  public boolean annuncioCompletato(int idAnnuncio, int idUtenteRecensore) throws DatabaseException {
    Validator.requirePositive(idAnnuncio, "idAnnuncio");
    Validator.requirePositive(idUtenteRecensore, "idUtenteRecensore");

    String sql = "SELECT 1 FROM ("
            + "SELECT 1 FROM vendita v "
            + "WHERE v.idannuncio = ? AND v.idutente = ? AND v.accettato = TRUE "
            + "UNION ALL "
            + "SELECT 1 FROM scambio s "
            + "WHERE s.idannuncio = ? AND s.idutente = ? AND s.accettato = TRUE "
            + "UNION ALL "
            + "SELECT 1 FROM regalo r "
            + "WHERE r.idannuncio = ? AND r.idutente = ? AND r.accettato = TRUE "
            + "UNION ALL "
            + "SELECT 1 FROM vendita v "
            + "JOIN annuncio a ON v.idannuncio = a.idannuncio "
            + "WHERE v.idannuncio = ? AND a.idutente = ? AND v.accettato = TRUE "
            + "UNION ALL "
            + "SELECT 1 FROM scambio s "
            + "JOIN annuncio a ON s.idannuncio = a.idannuncio "
            + "WHERE s.idannuncio = ? AND a.idutente = ? AND s.accettato = TRUE "
            + "UNION ALL "
            + "SELECT 1 FROM regalo r "
            + "JOIN annuncio a ON r.idannuncio = a.idannuncio "
            + "WHERE r.idannuncio = ? AND a.idutente = ? AND r.accettato = TRUE"
            + ") AS completato LIMIT 1";

    try (PreparedStatement ps = con.prepareStatement(sql)) {
      // Caso 1: utente è l'acquirente (nelle tabelle vendita/scambio/regalo)
      ps.setInt(1, idAnnuncio);
      ps.setInt(2, idUtenteRecensore);
      ps.setInt(3, idAnnuncio);
      ps.setInt(4, idUtenteRecensore);
      ps.setInt(5, idAnnuncio);
      ps.setInt(6, idUtenteRecensore);
      // Caso 2: utente è il venditore (proprietario dell'annuncio)
      ps.setInt(7, idAnnuncio);
      ps.setInt(8, idUtenteRecensore);
      ps.setInt(9, idAnnuncio);
      ps.setInt(10, idUtenteRecensore);
      ps.setInt(11, idAnnuncio);
      ps.setInt(12, idUtenteRecensore);

      try (ResultSet rs = ps.executeQuery()) {
        return rs.next();
      }
    } catch (SQLException e) {
      throw new DatabaseException("Errore durante la verifica del completamento annuncio", e);
    }
  }

  /**
   * Conta quante transazioni completate esistono tra due utenti.
   *
   * @param utenteA primo utente
   * @param utenteB secondo utente
   * @return numero di transazioni completate
   * @throws DatabaseException se la query fallisce
   */
  public int contaTransazioniCompletate(Utente utenteA, Utente utenteB) throws DatabaseException {
    Validator.requireNonNull(utenteA, "utenteA");
    Validator.requireNonNull(utenteB, "utenteB");
    Validator.requirePositive(utenteA.getIdUtente(), "utenteA.idUtente");
    Validator.requirePositive(utenteB.getIdUtente(), "utenteB.idUtente");

    String sql = "SELECT COUNT(*) FROM ("
            + "SELECT 1 FROM vendita v "
            + "JOIN annuncio a ON v.idannuncio = a.idannuncio "
            + "WHERE v.accettato = TRUE "
            + "AND ((v.idutente = ? AND a.idutente = ?) OR (v.idutente = ? AND a.idutente = ?)) "
            + "UNION ALL "
            + "SELECT 1 FROM scambio s "
            + "JOIN annuncio a ON s.idannuncio = a.idannuncio "
            + "WHERE s.accettato = TRUE "
            + "AND ((s.idutente = ? AND a.idutente = ?) OR (s.idutente = ? AND a.idutente = ?)) "
            + "UNION ALL "
            + "SELECT 1 FROM regalo r "
            + "JOIN annuncio a ON r.idannuncio = a.idannuncio "
            + "WHERE r.accettato = TRUE "
            + "AND ((r.idutente = ? AND a.idutente = ?) OR (r.idutente = ? AND a.idutente = ?))"
            + ") AS transazioni";

    try (PreparedStatement ps = con.prepareStatement(sql)) {
      int idA = utenteA.getIdUtente();
      int idB = utenteB.getIdUtente();

      // Vendita
      ps.setInt(1, idA);
      ps.setInt(2, idB);
      ps.setInt(3, idB);
      ps.setInt(4, idA);
      // Scambio
      ps.setInt(5, idA);
      ps.setInt(6, idB);
      ps.setInt(7, idB);
      ps.setInt(8, idA);
      // Regalo
      ps.setInt(9, idA);
      ps.setInt(10, idB);
      ps.setInt(11, idB);
      ps.setInt(12, idA);

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return rs.getInt(1);
        }
        return 0;
      }
    } catch (SQLException e) {
      throw new DatabaseException("Errore durante il conteggio delle transazioni completate", e);
    }
  }

  /**
   * Verifica se due utenti hanno completato una transazione (vendita o scambio).
   *
   * @param utenteA primo utente
   * @param utenteB secondo utente
   * @return true se esiste una transazione completata
   * @throws DatabaseException se la query fallisce
   */
  public boolean hannoTransazioneCompletata(Utente utenteA, Utente utenteB) throws DatabaseException {
    Validator.requireNonNull(utenteA, "utenteA");
    Validator.requireNonNull(utenteB, "utenteB");
    Validator.requirePositive(utenteA.getIdUtente(), "utenteA.idUtente");
    Validator.requirePositive(utenteB.getIdUtente(), "utenteB.idUtente");

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
      int idA = utenteA.getIdUtente();
      int idB = utenteB.getIdUtente();

      ps.setInt(1, idA);
      ps.setInt(2, idB);
      ps.setInt(3, idB);
      ps.setInt(4, idA);
      ps.setInt(5, idA);
      ps.setInt(6, idB);
      ps.setInt(7, idB);
      ps.setInt(8, idA);

      try (ResultSet rs = ps.executeQuery()) {
        return rs.next();
      }
    } catch (SQLException e) {
      throw new DatabaseException("Errore durante la verifica della transazione completata", e);
    }
  }
}
