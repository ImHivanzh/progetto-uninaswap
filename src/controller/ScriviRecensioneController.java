package controller;

import gui.ScriviRecensione;
import dao.RecensioneDAO;
import model.Recensione;
import model.Utente;
import utils.SessionManager;
import exception.DatabaseException;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ScriviRecensioneController {

  private final ScriviRecensione view;
  private final RecensioneDAO recensioneDAO;
  private final int idUtenteDestinatario;

  /**
   * Creates the controller and registers listeners.
   *
   * @param view review view
   * @param idUtenteDestinatario reviewed user id
   */
  public ScriviRecensioneController(ScriviRecensione view, int idUtenteDestinatario) {
    this.view = view;
    this.idUtenteDestinatario = idUtenteDestinatario;
    this.recensioneDAO = new RecensioneDAO();
    initListeners();
  }

  /**
   * Registers UI listeners for the review form.
   */
  private void initListeners() {
    this.view.addInviaListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        inviaRecensione();
      }
    });
  }

  /**
   * Validates input and sends the review.
   */
  private void inviaRecensione() {
    String descrizione = view.getDescrizione();
    int voto = view.getVoto();

    if (descrizione == null || descrizione.trim().isEmpty()) {
      view.mostraErrore("Per favore, inserisci una descrizione.");
      return;
    }

    Utente utenteLoggato = SessionManager.getInstance().getUtente();

    if (utenteLoggato != null && utenteLoggato.getIdUtente() == idUtenteDestinatario) {
      view.mostraErrore("Non puoi recensirti da solo!");
      return;
    }

    if (utenteLoggato == null) {
      view.mostraErrore("Utente non loggato.");
      return;
    }

    Recensione recensione = new Recensione(voto, descrizione, utenteLoggato.getIdUtente(), idUtenteDestinatario);

    try {
      boolean successo = recensioneDAO.inserisciRecensione(recensione);
      if (successo) {
        view.mostraMessaggio("Recensione inviata con successo!");
        view.dispose();
      } else {
        view.mostraErrore("Errore durante l'invio della recensione.");
      }
    } catch (DatabaseException ex) {
      view.mostraErrore("Errore Database: " + ex.getMessage());
      ex.printStackTrace();
    }
  }
}
