package dao;

import model.Annuncio;
import db.dbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AnnuncioDAO {

  public AnnuncioDAO() {}

  /**
   * Inserisce un nuovo annuncio nel database.
   * @param annuncio L'oggetto Annuncio da salvare.
   * @return true se l'inserimento va a buon fine.
   * @throws SQLException in caso di errore.
   */
  public boolean pubblicaAnnuncio(Annuncio annuncio) throws SQLException {
    // Assumiamo che la tabella si chiami 'annuncio' e le colonne siano:
    // titolo, descrizione, prezzo, tipo_annuncio (stringa o enum), id_utente (proprietario), stato (boolean)

    // Nota: Adatta i nomi delle colonne se nel tuo DB sono diversi (es: idutente invece di id_utente)
    String sql = "INSERT INTO annuncio(titolo, descrizione, prezzo, tipo_annuncio, id_utente, stato) VALUES (?, ?, ?, ?, ?, ?)";

    try (Connection conn = dbConnection.getInstance().getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setString(1, annuncio.getTitolo());
      ps.setString(2, annuncio.getDescrizione());
      ps.setFloat(3, annuncio.getPrezzo());
      // Salviamo il tipo come stringa (VENDITA, SCAMBIO, REGALO)
      ps.setString(4, annuncio.getTipoAnnuncio().toString());
      ps.setInt(5, annuncio.getUtente().getIdUtente());
      ps.setBoolean(6, true); // Stato = true (Disponibile/Attivo di default)

      int rowsAffected = ps.executeUpdate();
      return rowsAffected > 0;
    }
  }
}