package controller;

import gui.ScriviRecensione;
import dao.RecensioneDAO;
import model.Recensione;
import model.Utente;
import utils.SessionManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class ScriviRecensioneController {

  private final ScriviRecensione view;
  private final RecensioneDAO recensioneDAO;
  private final int idUtenteDestinatario; // L'ID dell'utente che viene recensito

  /**
   * @param view La finestra GUI
   * @param idUtenteDestinatario L'ID dell'utente a cui stiamo lasciando la recensione
   */
  public ScriviRecensioneController(ScriviRecensione view, int idUtenteDestinatario) {
    this.view = view;
    this.idUtenteDestinatario = idUtenteDestinatario;
    this.recensioneDAO = new RecensioneDAO();

    initListeners();
  }

  private void initListeners() {
    this.view.addInviaListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        inviaRecensione();
      }
    });
  }

  private void inviaRecensione() {
    String descrizione = view.getDescrizione();
    int voto = view.getVoto();

    if (descrizione == null || descrizione.trim().isEmpty()) {
      view.mostraErrore("Per favore, inserisci una descrizione.");
      return;
    }

    Utente utenteLoggato = SessionManager.getInstance().getUtente();
//    if (utenteLoggato == null) {
//      view.mostraErrore("Devi essere loggato per lasciare una recensione.");
//      view.dispose();
//      return;
//    }

    // Controllo opzionale: non recensire se stessi
    if (utenteLoggato.getIdUtente() == idUtenteDestinatario) {
      view.mostraErrore("Non puoi recensirti da solo!");
      return;
    }

    // 3. Creazione Modello
    Recensione recensione = new Recensione(voto, descrizione, utenteLoggato.getIdUtente(), idUtenteDestinatario);

    // 4. Salvataggio su DB
    try {
      boolean successo = recensioneDAO.inserisciRecensione(recensione);
      if (successo) {
        view.mostraMessaggio("Recensione inviata con successo!");
        view.dispose(); // Chiude la finestra dopo l'invio
      } else {
        view.mostraErrore("Errore durante l'invio della recensione.");
      }
    } catch (SQLException ex) {
      view.mostraErrore("Errore Database: " + ex.getMessage());
      ex.printStackTrace();
    }
  }
}