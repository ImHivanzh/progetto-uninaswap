package controller;

import gui.Profilo;
import dao.RecensioneDAO;
import dao.AnnuncioDAO;
import model.Recensione;
import model.Annuncio;
import model.Utente;
import utils.SessionManager;
import exception.DatabaseException;

import java.util.List;

public class ProfiloController {

  private final Profilo view;
  private final RecensioneDAO recensioneDAO;
  private final AnnuncioDAO annuncioDAO;
  private final Utente utenteTarget;
  private boolean mostraDatiSensibili = false;

  public ProfiloController(Profilo view) {
    this(view, null);
  }

  public ProfiloController(Profilo view, Utente utenteTarget) {
    this.view = view;
    this.recensioneDAO = new RecensioneDAO();
    this.annuncioDAO = new AnnuncioDAO();

    if (utenteTarget == null) {
      this.utenteTarget = SessionManager.getInstance().getUtente();
      this.view.setTitoloProfilo("Il Mio Profilo");
      this.mostraDatiSensibili = true;
    } else {
      this.utenteTarget = utenteTarget;
      this.view.setTitoloProfilo("Profilo di " + utenteTarget.getUsername());
    }

    caricaDati();
  }

  private void caricaDati() {
    if (utenteTarget == null) {
      view.mostraErrore("Utente non trovato!");
      view.dispose();
      return;
    }

    view.setUsername(utenteTarget.getUsername());
    if(mostraDatiSensibili) {
      view.setEmail(utenteTarget.getEmail());
      view.setTelefono(utenteTarget.getNumeroTelefono());
    } else {
      view.setEmail("Nascosto");
      view.setTelefono("Nascosto");
    }
    view.pulisciTabelle();

    try {
      // 1. Carica Recensioni
      List<Recensione> recensioni = recensioneDAO.getRecensioniRicevute(utenteTarget.getIdUtente());
      if (recensioni.isEmpty()) {
        view.setMediaVoto(0.0);
      } else {
        double sommaVoti = 0;
        for (Recensione r : recensioni) {
          view.aggiungiRecensione(r.getVoto(), r.getDescrizione());
          sommaVoti += r.getVoto();
        }
        view.setMediaVoto(sommaVoti / recensioni.size());
      }

      // 2. Carica Annunci
      List<Annuncio> annunci = annuncioDAO.findAllByUtente(utenteTarget.getIdUtente());
      for (Annuncio a : annunci) {
        view.aggiungiAnnuncio(
                a.getTitolo(),
                a.getCategoria() != null ? a.getCategoria().toString() : "N/A",
                a.getTipoAnnuncio() != null ? a.getTipoAnnuncio().toString() : "N/A"
        );
      }

    } catch (DatabaseException e) {
      view.mostraErrore("Errore nel caricamento dati: " + e.getMessage());
      e.printStackTrace();
    }
  }
}