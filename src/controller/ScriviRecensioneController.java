package controller;

import gui.ScriviRecensione;
import dao.RecensioneDAO;
import dao.UtenteDAO;
import model.Recensione;
import model.Utente;
import utils.SessionManager;
import utils.Logger;
import exception.DatabaseException;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Controller per invio recensioni.
 */
public class ScriviRecensioneController {

  /**
   * Vista recensione.
   */
  private final ScriviRecensione view;
  /**
   * DAO recensioni.
   */
  private final RecensioneDAO recensioneDAO;
  /**
   * DAO utenti.
   */
  private final UtenteDAO utenteDAO;
  /**
   * Utente destinatario recensione.
   */
  private final Utente utenteDestinatario;

  /**
   * Crea controller e registra listener.
   *
   * @param view recensione vista
   * @param idUtenteDestinatario id utente recensito
   */
  public ScriviRecensioneController(ScriviRecensione view, int idUtenteDestinatario) {
    this.view = view;
    this.recensioneDAO = new RecensioneDAO();
    this.utenteDAO = new UtenteDAO();

    // Carica l'utente destinatario
    Utente utenteDest = null;
    try {
      utenteDest = utenteDAO.getUserByID(idUtenteDestinatario);
    } catch (DatabaseException e) {
      view.mostraErrore("Errore nel caricamento dell'utente destinatario: " + e.getMessage());
    }
    this.utenteDestinatario = utenteDest;

    initListeners();
  }

  /**
   * Registra UI listener per form recensione.
   */
  private void initListeners() {
    this.view.addInviaListener(new ActionListener() {
      /**
       * {@inheritDoc}
       */
      @Override
      public void actionPerformed(ActionEvent e) {
        inviaRecensione();
      }
    });
  }

  /**
   * Valida input e invia recensione.
   */
  private void inviaRecensione() {
    String descrizione = view.getDescrizione().trim();
    int voto = view.getVoto();

    if (descrizione.isEmpty()) {
      view.mostraErrore("Per favore, inserisci una descrizione.");
      return;
    }

    if (utenteDestinatario == null) {
      view.mostraErrore("Utente destinatario non trovato.");
      return;
    }

    Utente utenteLoggato = SessionManager.getInstance().getUtente();

    if (utenteLoggato == null) {
      view.mostraErrore("Utente non loggato.");
      return;
    }

    if (utenteLoggato.getIdUtente() == utenteDestinatario.getIdUtente()) {
      view.mostraErrore("Non puoi recensirti da solo!");
      return;
    }

    try {
      boolean transazioneOk = recensioneDAO.hannoTransazioneCompletata(utenteLoggato, utenteDestinatario);
      if (!transazioneOk) {
        view.mostraErrore("Puoi lasciare una recensione solo dopo una transazione completata.");
        return;
      }
    } catch (DatabaseException ex) {
      view.mostraErrore("Errore durante la verifica della transazione: " + ex.getMessage());
      return;
    }

    Recensione recensione = new Recensione(voto, descrizione, utenteLoggato, utenteDestinatario);

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
      Logger.error("Errore database scrittura recensione", ex);
    }
  }
}
