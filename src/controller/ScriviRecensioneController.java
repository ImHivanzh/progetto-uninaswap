package controller;

import dao.RecensioneDAO;
import dao.UtenteDAO;
import exception.DatabaseException;
import gui.ScriviRecensione;
import model.Recensione;
import model.Utente;
import utils.Logger;
import utils.SessionManager;

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
   * ID annuncio per cui si lascia la recensione.
   */
  private final int idAnnuncio;

  /**
   * Crea controller e registra listener.
   *
   * @param view recensione vista
   * @param idUtenteDestinatario id utente recensito
   * @param idAnnuncio id annuncio per cui si lascia la recensione
   */
  public ScriviRecensioneController(ScriviRecensione view, int idUtenteDestinatario, int idAnnuncio) {
    this.view = view;
    this.recensioneDAO = new RecensioneDAO();
    this.utenteDAO = new UtenteDAO();
    this.idAnnuncio = idAnnuncio;

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
    this.view.addInviaListener(e -> inviaRecensione());
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
      // Verifica se l'annuncio è stato completato dall'utente loggato
      boolean annuncioOk = recensioneDAO.annuncioCompletato(idAnnuncio, utenteLoggato.getIdUtente());
      if (!annuncioOk) {
        view.mostraErrore("Puoi lasciare una recensione solo per annunci con transazione completata.");
        return;
      }

      // Verifica se esiste già una recensione per questo annuncio
      boolean recensioneEsistente = recensioneDAO.esisteRecensionePerAnnuncio(utenteLoggato, idAnnuncio);
      if (recensioneEsistente) {
        view.mostraErrore("Hai già lasciato una recensione per questo annuncio.");
        return;
      }
    } catch (DatabaseException ex) {
      view.mostraErrore("Errore durante la verifica: " + ex.getMessage());
      return;
    }

    Recensione recensione = new Recensione(voto, descrizione, utenteLoggato, utenteDestinatario, idAnnuncio);

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
