package controller;

import gui.Profilo;
import dao.RecensioneDAO;
import model.Recensione;
import model.Utente;
import utils.SessionManager;
import exception.DatabaseException;

import java.util.List;

public class ProfiloController {

  private final Profilo view;
  private final RecensioneDAO recensioneDAO;
  private final Utente utenteTarget; // L'utente di cui mostriamo il profilo

  /**
   * Costruttore per visualizzare il PROPRIO profilo (utente loggato).
   */
  public ProfiloController(Profilo view) {
    this(view, null);
  }

  /**
   * Costruttore per visualizzare il profilo di UN ALTRO utente (o se null, il proprio).
   * @param view La vista Profilo.
   * @param utenteTarget L'utente da visualizzare. Se null, usa l'utente di sessione.
   */
  public ProfiloController(Profilo view, Utente utenteTarget) {
    this.view = view;
    this.recensioneDAO = new RecensioneDAO();

    // Se non viene passato un utente specifico, prendo quello loggato
    if (utenteTarget == null) {
      this.utenteTarget = SessionManager.getInstance().getUtente();
      // Titolo per il proprio profilo
      this.view.setTitoloProfilo("Il Mio Profilo");
    } else {
      this.utenteTarget = utenteTarget;
      // Titolo per il profilo altrui
      this.view.setTitoloProfilo("Profilo di " + utenteTarget.getUsername());
    }

    caricaDati();
  }

  private void caricaDati() {
    if (utenteTarget == null) {
      view.mostraErrore("Utente non trovato o non loggato!");
      view.dispose();
      return;
    }

    // 1. Popola Dati Personali
    view.setUsername(utenteTarget.getUsername());

    if(utenteTarget.getIdUtente() == SessionManager.getInstance().getUtente().getIdUtente()) {
      view.setEmail(utenteTarget.getEmail());
      view.setTelefono(utenteTarget.getNumeroTelefono());
    } else {
      view.setEmail("Nascosto");
      view.setTelefono("Nascosto");
    }

    // 2. Recupera Recensioni e Calcola Media
    try {
      List<Recensione> recensioni = recensioneDAO.getRecensioniRicevute(utenteTarget.getIdUtente());

      view.pulisciTabella();

      if (recensioni.isEmpty()) {
        view.setMediaVoto(0.0);
      } else {
        double sommaVoti = 0;
        for (Recensione r : recensioni) {
          view.aggiungiRecensione(r.getVoto(), r.getDescrizione());
          sommaVoti += r.getVoto();
        }
        double media = sommaVoti / recensioni.size();
        view.setMediaVoto(media);
      }

    } catch (DatabaseException e) {
      view.mostraErrore("Errore nel caricamento delle recensioni: " + e.getMessage());
      e.printStackTrace();
    }
  }
}