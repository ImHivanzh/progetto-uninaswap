package controller;

import dao.AnnuncioDAO;
import dao.PropostaDAO;
import dao.RecensioneDAO;
import exception.DatabaseException;
import gui.Profilo;
import model.Annuncio;
import model.PropostaRiepilogo;
import model.Recensione;
import model.Utente;
import utils.Logger;

import java.util.Collections;
import java.util.List;

/**
 * Gestisce il caricamento e la formattazione dei dati per il profilo utente.
 */
public class ProfiloDataLoader {

  private static final String UNKNOWN_USER = "Sconosciuto";

  private final Profilo view;
  private final RecensioneDAO recensioneDAO;
  private final AnnuncioDAO annuncioDAO;
  private final PropostaDAO propostaDAO;
  private final PropostaHandler propostaHandler;

  public ProfiloDataLoader(Profilo view, RecensioneDAO recensioneDAO,
                           AnnuncioDAO annuncioDAO, PropostaDAO propostaDAO,
                           PropostaHandler propostaHandler) {
    this.view = view;
    this.recensioneDAO = recensioneDAO;
    this.annuncioDAO = annuncioDAO;
    this.propostaDAO = propostaDAO;
    this.propostaHandler = propostaHandler;
  }

  /**
   * Carica tutti i dati del profilo utente e li inserisce nella vista.
   *
   * @param utenteTarget l'utente di cui caricare il profilo
   * @param mostraDatiSensibili true per mostrare email e telefono, false per nasconderli
   * @return oggetto DatiProfilo contenente annunci e proposte, null se l'utente non esiste
   */
  public DatiProfilo caricaDatiCompleti(Utente utenteTarget, boolean mostraDatiSensibili) {
    if (utenteTarget == null) {
      view.mostraErrore("Utente non trovato!");
      return null;
    }

    impostaDatiUtente(utenteTarget, mostraDatiSensibili);
    view.pulisciTabelle();

    DatiProfilo dati = new DatiProfilo();

    try {
      caricaRecensioni(utenteTarget);
      dati.setListaAnnunci(caricaAnnunci(utenteTarget));

      if (mostraDatiSensibili && propostaDAO != null) {
        dati.setProposteRicevute(caricaProposteRicevute(utenteTarget));
        dati.setProposteInviate(caricaProposteInviate(utenteTarget));
      } else {
        dati.setProposteRicevute(Collections.emptyList());
        dati.setProposteInviate(Collections.emptyList());
      }

    } catch (DatabaseException e) {
      view.mostraErrore("Errore nel caricamento dati: " + e.getMessage());
      Logger.error("Errore caricamento dati profilo", e);
      dati.setListaAnnunci(Collections.emptyList());
      dati.setProposteRicevute(Collections.emptyList());
      dati.setProposteInviate(Collections.emptyList());
    }

    return dati;
  }

  /**
   * Imposta i dati anagrafici dell'utente nella vista.
   *
   * @param utenteTarget l'utente di cui mostrare i dati
   * @param mostraDatiSensibili true per mostrare email e telefono, false per nasconderli
   */
  private void impostaDatiUtente(Utente utenteTarget, boolean mostraDatiSensibili) {
    view.setUsername(utenteTarget.getUsername());
    if (mostraDatiSensibili) {
      view.setEmail(utenteTarget.getEmail());
      view.setTelefono(utenteTarget.getNumeroTelefono());
    } else {
      view.setEmail("Nascosto");
      view.setTelefono("Nascosto");
    }
  }

  /**
   * Carica le recensioni ricevute dall'utente e calcola la media dei voti.
   *
   * @param utenteTarget l'utente di cui caricare le recensioni
   * @throws DatabaseException se si verifica un errore durante l'accesso al database
   */
  private void caricaRecensioni(Utente utenteTarget) throws DatabaseException {
    List<Recensione> recensioni = recensioneDAO.getRecensioniRicevute(utenteTarget);
    if (recensioni.isEmpty()) {
      view.setMediaVoto(0.0);
      return;
    }

    double sommaVoti = 0;
    for (Recensione r : recensioni) {
      String nomeUtente = r.getUtenteRecensore() != null && r.getUtenteRecensore().getUsername() != null
          ? r.getUtenteRecensore().getUsername() : UNKNOWN_USER;
      view.aggiungiRecensione(nomeUtente, r.getVoto(), r.getDescrizione());
      sommaVoti += r.getVoto();
    }
    view.setMediaVoto(sommaVoti / recensioni.size());
  }

  /**
   * Carica gli annunci pubblicati dall'utente e li aggiunge alla vista.
   *
   * @param utenteTarget l'utente di cui caricare gli annunci
   * @return lista degli annunci caricati
   * @throws DatabaseException se si verifica un errore durante l'accesso al database
   */
  private List<Annuncio> caricaAnnunci(Utente utenteTarget) throws DatabaseException {
    List<Annuncio> listaAnnunci = annuncioDAO.findAllByUtente(utenteTarget);
    for (Annuncio a : listaAnnunci) {
      view.aggiungiAnnuncio(
          a.getTitolo(),
          a.getCategoria() != null ? a.getCategoria().toString() : "N/A",
          a.getTipoAnnuncio() != null ? a.getTipoAnnuncio().toString() : "N/A"
      );
    }
    return listaAnnunci;
  }

  /**
   * Carica le proposte ricevute dall'utente e le aggiunge alla vista.
   *
   * @param utenteTarget l'utente di cui caricare le proposte ricevute
   * @return lista delle proposte ricevute
   * @throws DatabaseException se si verifica un errore durante l'accesso al database
   */
  private List<PropostaRiepilogo> caricaProposteRicevute(Utente utenteTarget) throws DatabaseException {
    List<PropostaRiepilogo> proposte = propostaDAO.getProposteRicevute(utenteTarget.getIdUtente());
    for (PropostaRiepilogo proposta : proposte) {
      if (proposta != null) {
        aggiungiPropostaRicevutaAllaVista(proposta);
      }
    }
    return proposte;
  }

  /**
   * Carica le proposte inviate dall'utente e le aggiunge alla vista.
   *
   * @param utenteTarget l'utente di cui caricare le proposte inviate
   * @return lista delle proposte inviate
   * @throws DatabaseException se si verifica un errore durante l'accesso al database
   */
  private List<PropostaRiepilogo> caricaProposteInviate(Utente utenteTarget) throws DatabaseException {
    List<PropostaRiepilogo> proposte = propostaDAO.getProposteInviate(utenteTarget.getIdUtente());
    for (PropostaRiepilogo proposta : proposte) {
      if (proposta != null) {
        aggiungiPropostaInviataAllaVista(proposta);
      }
    }
    return proposte;
  }

  /**
   * Aggiunge una proposta ricevuta alla tabella nella vista.
   *
   * @param proposta la proposta ricevuta da aggiungere
   */
  private void aggiungiPropostaRicevutaAllaVista(PropostaRiepilogo proposta) {
    String nomeUtente = proposta.utenteCoinvolto() != null
        ? proposta.utenteCoinvolto().getUsername() : UNKNOWN_USER;
    String titoloAnnuncio = proposta.annuncio() != null
        ? proposta.annuncio().getTitolo() : "N/A";
    String tipoAnnuncio = proposta.annuncio() != null && proposta.annuncio().getTipoAnnuncio() != null
        ? proposta.annuncio().getTipoAnnuncio().toString() : "N/A";

    view.aggiungiPropostaRicevuta(nomeUtente, titoloAnnuncio, tipoAnnuncio,
                                   proposta.dettaglio(), propostaHandler.formatStato(proposta).getDescrizione());
  }

  /**
   * Aggiunge una proposta inviata alla tabella nella vista.
   *
   * @param proposta la proposta inviata da aggiungere
   */
  private void aggiungiPropostaInviataAllaVista(PropostaRiepilogo proposta) {
    String nomeUtente = proposta.utenteCoinvolto() != null
        ? proposta.utenteCoinvolto().getUsername() : UNKNOWN_USER;
    String titoloAnnuncio = proposta.annuncio() != null
        ? proposta.annuncio().getTitolo() : "N/A";
    String tipoAnnuncio = proposta.annuncio() != null && proposta.annuncio().getTipoAnnuncio() != null
        ? proposta.annuncio().getTipoAnnuncio().toString() : "N/A";

    view.aggiungiPropostaInviata(nomeUtente, titoloAnnuncio, tipoAnnuncio,
                                  proposta.dettaglio(), propostaHandler.formatStato(proposta).getDescrizione());
  }

  /**
   * Classe contenitore per i dati caricati del profilo utente.
   * Contiene le liste di annunci e proposte (ricevute e inviate).
   */
  public static class DatiProfilo {
    private List<Annuncio> listaAnnunci = Collections.emptyList();
    private List<PropostaRiepilogo> proposteRicevute = Collections.emptyList();
    private List<PropostaRiepilogo> proposteInviate = Collections.emptyList();

    public List<Annuncio> getListaAnnunci() {
      return listaAnnunci;
    }

    public void setListaAnnunci(List<Annuncio> listaAnnunci) {
      this.listaAnnunci = listaAnnunci;
    }

    public List<PropostaRiepilogo> getProposteRicevute() {
      return proposteRicevute;
    }

    public void setProposteRicevute(List<PropostaRiepilogo> proposteRicevute) {
      this.proposteRicevute = proposteRicevute;
    }

    public List<PropostaRiepilogo> getProposteInviate() {
      return proposteInviate;
    }

    public void setProposteInviate(List<PropostaRiepilogo> proposteInviate) {
      this.proposteInviate = proposteInviate;
    }
  }
}
