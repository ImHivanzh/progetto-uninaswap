package controller;

import dao.AnnuncioDAO;
import dao.PropostaDAO;
import dao.RecensioneDAO;
import dao.RitiroDAO;
import dao.SpedizioneDAO;
import dao.UtenteDAO;
import model.Recensione;
import model.Annuncio;
import model.PropostaRiepilogo;
import model.Utente;
import model.enums.StatoConsegna;
import utils.ConsegnaHelper;
import utils.DataCheck;
import utils.FormHelper;
import utils.ImmaginePropostaHelper;
import utils.Logger;
import utils.SessionManager;
import utils.WindowManager;
import exception.DatabaseException;
import gui.DettaglioAnnuncio;
import gui.ModificaPropostaDialog;
import gui.Profilo;
import gui.ScriviRecensione;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Controller per gestione profilo utente.
 */
public class ProfiloController {

  /**
   * Vista profilo.
   */
  private final Profilo view;
  /**
   * DAO recensioni.
   */
  private final RecensioneDAO recensioneDAO;
  /**
   * DAO annunci.
   */
  private final AnnuncioDAO annuncioDAO;
  /**
   * DAO proposte.
   */
  private final PropostaDAO propostaDAO;
  private final SpedizioneDAO spedizioneDAO;
  private final RitiroDAO ritiroDAO;
  private final ConsegnaHelper consegnaHelper;
  /**
   * Utente target del profilo.
   */
  private final Utente utenteTarget;
  /**
   * Flag per dati sensibili visibili.
   */
  private boolean mostraDatiSensibili = false;

  /**
   * Lista annunci caricati.
   */
  private List<Annuncio> listaAnnunci;
  /**
   * Lista proposte ricevute.
   */
  private List<PropostaRiepilogo> proposteRicevute;
  /**
   * Lista proposte inviate.
   */
  private List<PropostaRiepilogo> proposteInviate;

  /**
   * Crea controller per profilo utente corrente.
   *
   * @param view vista profilo
   */
  public ProfiloController(Profilo view) {
    this(view, null);
  }

  /**
   * Crea controller per specifico utente profilo.
   *
   * @param view vista profilo
   * @param utenteTarget profilo proprietario
   */
  public ProfiloController(Profilo view, Utente utenteTarget) {
    this.view = view;
    this.recensioneDAO = new RecensioneDAO();
    this.annuncioDAO = new AnnuncioDAO();
    this.propostaDAO = creaPropostaDAO();
    this.spedizioneDAO = creaSpedizioneDAO();
    this.ritiroDAO = creaRitiroDAO();
    this.consegnaHelper = new ConsegnaHelper(spedizioneDAO, ritiroDAO, view);
    this.proposteRicevute = Collections.emptyList();
    this.proposteInviate = Collections.emptyList();

    if (utenteTarget == null) {
      this.utenteTarget = SessionManager.getInstance().getUtente();
      this.view.setTitoloProfilo("Il Mio Profilo");
      this.mostraDatiSensibili = true;
    } else {
      this.utenteTarget = utenteTarget;
      this.view.setTitoloProfilo("Profilo di " + utenteTarget.getUsername());
      this.view.nascondiTabProposte();
    }

    caricaDati();
    setupInteraction();
  }

  /**
   * Crea proposte DAO, mostrando errore in caso di errore.
   *
   * @return DAO istanza o null
   */
  private PropostaDAO creaPropostaDAO() {
    try {
      return new PropostaDAO();
    } catch (DatabaseException e) {
      view.mostraErrore("Errore durante la connessione per le proposte: " + e.getMessage());
      Logger.error("Errore creazione PropostaDAO", e);
      return null;
    }
  }

  /**
   * Crea spedizione DAO, mostrando errore in caso di errore.
   *
   * @return DAO istanza o null
   */
  private SpedizioneDAO creaSpedizioneDAO() {
    try {
      return new SpedizioneDAO();
    } catch (DatabaseException e) {
      view.mostraErrore("Errore durante la connessione per la spedizione: " + e.getMessage());
      Logger.error("Errore creazione SpedizioneDAO", e);
      return null;
    }
  }

  /**
   * Crea ritiro DAO, mostrando errore in caso di errore.
   *
   * @return DAO istanza o null
   */
  private RitiroDAO creaRitiroDAO() {
    try {
      return new RitiroDAO();
    } catch (DatabaseException e) {
      view.mostraErrore("Errore durante la connessione per il ritiro: " + e.getMessage());
      Logger.error("Errore creazione RitiroDAO", e);
      return null;
    }
  }

  /**
   * Registra tabella listener per profilo interazioni.
   */
    private void setupInteraction() {
      view.addTableAnnunciListener(new MouseAdapter() {
        /**
         * {@inheritDoc}
         */
        @Override
        public void mouseClicked(MouseEvent e) {
          if (e.getClickCount() == 2) {
            JTable table = (JTable) e.getSource();
            int selectedRow = table.getSelectedRow();

          if (selectedRow != -1 && listaAnnunci != null && selectedRow < listaAnnunci.size()) {
            Annuncio annuncioSelezionato = listaAnnunci.get(selectedRow);

            DettaglioAnnuncio dettaglioFrame = new DettaglioAnnuncio(annuncioSelezionato);
            WindowManager.open(view, dettaglioFrame);
          }
        }
      }
    });

      view.addTableProposteRicevuteListener(new MouseAdapter() {
        /**
         * {@inheritDoc}
         */
        @Override
        public void mouseClicked(MouseEvent e) {
          if (e.getClickCount() == 2) {
            JTable table = (JTable) e.getSource();
            int selectedRow = table.getSelectedRow();
          handlePropostaRicevuta(selectedRow);
        }
      }
    });

      view.addTableProposteInviateListener(new MouseAdapter() {
        /**
         * {@inheritDoc}
         */
        @Override
        public void mouseClicked(MouseEvent e) {
          if (e.getClickCount() == 2) {
            JTable table = (JTable) e.getSource();
            int selectedRow = table.getSelectedRow();
          handlePropostaInviata(selectedRow);
        }
      }
    });

    view.addRecensioneRicevutaListener(e -> handleRecensioneDaProposta(true));
    view.addRecensioneInviataListener(e -> handleRecensioneDaProposta(false));
    view.addModificaPropostaListener(e -> handleModificaProposta());
    view.addAnnullaPropostaListener(e -> handleAnnullaProposta());
  }

  /**
   * Carica profilo data e popola vista.
   */
  private void caricaDati() {
    if (utenteTarget == null) {
      view.mostraErrore("Utente non trovato!");
      view.dispose();
      return;
    }

    view.setUsername(utenteTarget.getUsername());
    if (mostraDatiSensibili) {
      view.setEmail(utenteTarget.getEmail());
      view.setTelefono(utenteTarget.getNumeroTelefono());
    } else {
      view.setEmail("Nascosto");
      view.setTelefono("Nascosto");
    }
    proposteRicevute = Collections.emptyList();
    proposteInviate = Collections.emptyList();
    view.pulisciTabelle();

    try {
      List<Recensione> recensioni = recensioneDAO.getRecensioniRicevute(utenteTarget);
      if (recensioni.isEmpty()) {
        view.setMediaVoto(0.0);
      } else {
        double sommaVoti = 0;
        for (Recensione r : recensioni) {
          String nomeUtente = "Sconosciuto";
          if (r.getUtenteRecensore() != null && r.getUtenteRecensore().getUsername() != null) {
            nomeUtente = r.getUtenteRecensore().getUsername();
          }
          view.aggiungiRecensione(nomeUtente, r.getVoto(), r.getDescrizione());
          sommaVoti += r.getVoto();
        }
        view.setMediaVoto(sommaVoti / recensioni.size());
      }

      this.listaAnnunci = annuncioDAO.findAllByUtente(utenteTarget);

      for (Annuncio a : listaAnnunci) {
        view.aggiungiAnnuncio(
                a.getTitolo(),
                a.getCategoria() != null ? a.getCategoria().toString() : "N/A",
                a.getTipoAnnuncio() != null ? a.getTipoAnnuncio().toString() : "N/A"
        );
      }

      if (mostraDatiSensibili && propostaDAO != null) {
        proposteRicevute = propostaDAO.getProposteRicevute(utenteTarget.getIdUtente());
        proposteInviate = propostaDAO.getProposteInviate(utenteTarget.getIdUtente());

        for (PropostaRiepilogo proposta : proposteRicevute) {
          if (proposta == null) continue;

          String nomeUtente = proposta.utenteCoinvolto() != null ? proposta.utenteCoinvolto().getUsername() : "Sconosciuto";
          String titoloAnnuncio = proposta.annuncio() != null ? proposta.annuncio().getTitolo() : "N/A";
          String tipoAnnuncio = proposta.annuncio() != null && proposta.annuncio().getTipoAnnuncio() != null
              ? proposta.annuncio().getTipoAnnuncio().toString() : "N/A";
          view.aggiungiPropostaRicevuta(
                  nomeUtente,
                  titoloAnnuncio,
                  tipoAnnuncio,
                  proposta.dettaglio(),
                  formatStato(proposta).getDescrizione()
          );
        }

        for (PropostaRiepilogo proposta : proposteInviate) {
          if (proposta == null) continue;

          String nomeUtente = proposta.utenteCoinvolto() != null ? proposta.utenteCoinvolto().getUsername() : "Sconosciuto";
          String titoloAnnuncio = proposta.annuncio() != null ? proposta.annuncio().getTitolo() : "N/A";
          String tipoAnnuncio = proposta.annuncio() != null && proposta.annuncio().getTipoAnnuncio() != null
              ? proposta.annuncio().getTipoAnnuncio().toString() : "N/A";
          view.aggiungiPropostaInviata(
                  nomeUtente,
                  titoloAnnuncio,
                  tipoAnnuncio,
                  proposta.dettaglio(),
                  formatStato(proposta).getDescrizione()
          );
        }
      }

    } catch (DatabaseException e) {
      view.mostraErrore("Errore nel caricamento dati: " + e.getMessage());
      Logger.error("Errore caricamento dati profilo", e);
    }
  }

  /**
   * Restituisce etichetta stato per proposta.
   *
   * @param proposta riepilogo proposta
   * @return stato consegna
   */
  private StatoConsegna formatStato(PropostaRiepilogo proposta) {
    if (!proposta.accettata()) {
      return proposta.inattesa() ? StatoConsegna.IN_ATTESA : StatoConsegna.RIFIUTATO;
    }

    if (spedizioneDAO == null || ritiroDAO == null || proposta.annuncio() == null) {
      return StatoConsegna.IN_ATTESA;
    }

    try {
      int idAnnuncio = proposta.annuncio().getIdAnnuncio();
      model.Spedizione spedizione = spedizioneDAO.getSpedizioneByAnnuncio(idAnnuncio);
      model.Ritiro ritiro = ritiroDAO.getRitiroByAnnuncio(idAnnuncio);

      if (spedizione != null) {
        return spedizione.isSpedito() ? StatoConsegna.CONCLUSO : StatoConsegna.DA_SPEDIRE;
      }
      if (ritiro != null) {
        return ritiro.isRitirato() ? StatoConsegna.CONCLUSO : StatoConsegna.DA_RITIRARE;
      }
      return StatoConsegna.IN_ATTESA;
    } catch (DatabaseException e) {
      view.mostraErrore("Errore nel controllo stato proposta: " + e.getMessage());
      Logger.error("Errore controllo stato proposta", e);
      return StatoConsegna.IN_ATTESA;
    }
  }

  /**
   * Gestisce doppio-clic in ricevute proposta.
   *
   * @param selectedRow riga indice
   */
  private void handlePropostaRicevuta(int selectedRow) {
    if (!mostraDatiSensibili) {
      view.mostraErrore("Operazione disponibile solo nel tuo profilo.");
      return;
    }
    if (selectedRow < 0 || proposteRicevute == null || selectedRow >= proposteRicevute.size()) {
      return;
    }
    PropostaRiepilogo proposta = proposteRicevute.get(selectedRow);
    if (proposta == null || proposta.annuncio() == null) {
      view.mostraErrore("Proposta non valida.");
      return;
    }

    boolean mostraImmagine = isPropostaScambio(proposta);
    String dettaglio = buildDettaglioProposta(proposta, "Da");

    if (proposta.accettata()) {
      handlePropostaRicevutaAccettata(proposta, mostraImmagine, dettaglio);
    } else if (!proposta.inattesa()) {
      handlePropostaRicevutaRifiutata(proposta, mostraImmagine, dettaglio);
    } else {
      handlePropostaRicevutaInAttesa(proposta, mostraImmagine, dettaglio);
    }
  }

  /**
   * Gestisce proposta ricevuta accettata.
   */
  private void handlePropostaRicevutaAccettata(PropostaRiepilogo proposta, boolean mostraImmagine, String dettaglio) {
    boolean isSpedizioneNonSpedita = false;
    boolean isRitiroNonRitirato = false;

    if (proposta.annuncio() == null) {
      view.mostraErrore("Annuncio non disponibile.");
      return;
    }

    int idAnnuncio = proposta.annuncio().getIdAnnuncio();

    if (spedizioneDAO != null && ritiroDAO != null) {
      try {
        if (spedizioneDAO != null) {
          model.Spedizione spedizione = spedizioneDAO.getSpedizioneByAnnuncio(idAnnuncio);
          if (spedizione != null && !spedizione.isSpedito()) {
            isSpedizioneNonSpedita = true;
          }
        }
        if (ritiroDAO != null) {
          model.Ritiro ritiro = ritiroDAO.getRitiroByAnnuncio(idAnnuncio);
          if (ritiro != null && !ritiro.isRitirato()) {
            isRitiroNonRitirato = true;
          }
        }
      } catch (DatabaseException e) {
        view.mostraErrore("Errore nel controllo stato consegna: " + e.getMessage());
        Logger.error("Errore controllo stato consegna", e);
      }
    }

    while (true) {
      List<String> opzioniList = new ArrayList<>();
      if (StatoConsegna.CONCLUSO.equals(formatStato(proposta))) {
        opzioniList.add("Lascia recensione");
      }
      opzioniList.add("Dettagli consegna");
      if (isSpedizioneNonSpedita) {
        opzioniList.add("Ho spedito");
      }
      if (isRitiroNonRitirato) {
        opzioniList.add("Ritirato");
      }
      if (mostraImmagine) {
        opzioniList.add("Mostra immagine");
      }
      opzioniList.add("Chiudi");

      String[] opzioni = opzioniList.toArray(new String[0]);

      int scelta = JOptionPane.showOptionDialog(
              view,
              dettaglio + "\n\nQuesta proposta è stata accettata.",
              "Proposta ricevuta",
              JOptionPane.DEFAULT_OPTION,
              JOptionPane.INFORMATION_MESSAGE,
              null,
              opzioni,
              opzioni[0]);

      if (scelta >= 0 && scelta < opzioni.length) {
        String opzioneScelta = opzioni[scelta];
        switch (opzioneScelta) {
          case "Lascia recensione":
            apriScriviRecensione(proposta.utenteCoinvolto());
            return;
          case "Dettagli consegna":
            visualizzaDettagliConsegna(proposta);
            break;
          case "Ho spedito":
            aggiornaStatoSpedizione(idAnnuncio);
            return;
          case "Ritirato":
            aggiornaStatoRitiro(idAnnuncio);
            return;
          case "Mostra immagine":
            mostraImmagineProposta(proposta);
            break;
          case "Chiudi":
          default:
            return;
        }
      } else {
        return;
      }
    }
  }

  /**
   * Gestisce proposta ricevuta rifiutata.
   */
  private void handlePropostaRicevutaRifiutata(PropostaRiepilogo proposta, boolean mostraImmagine, String dettaglio) {
    if (mostraImmagine) {
      while (true) {
        Object[] opzioni = {"Mostra immagine", "Chiudi"};
        int scelta = JOptionPane.showOptionDialog(
                view,
                dettaglio + "\n\nQuesta proposta è stata rifiutata.",
                "Proposta ricevuta",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                opzioni,
                opzioni[1]);
        if (scelta == 0) {
          mostraImmagineProposta(proposta);
          continue;
        }
        return;
      }
    } else {
      JOptionPane.showMessageDialog(
              view,
              dettaglio + "\n\nQuesta proposta è stata rifiutata.",
              "Proposta ricevuta",
              JOptionPane.INFORMATION_MESSAGE);
    }
  }

  /**
   * Gestisce proposta ricevuta in attesa.
   */
  private void handlePropostaRicevutaInAttesa(PropostaRiepilogo proposta, boolean mostraImmagine, String dettaglio) {
    while (true) {
      Object[] opzioni = mostraImmagine
              ? new Object[]{"Accetta", "Rifiuta", "Mostra immagine", "Chiudi"}
              : new Object[]{"Accetta", "Rifiuta", "Chiudi"};
      int scelta = JOptionPane.showOptionDialog(
              view,
              dettaglio,
              "Gestione proposta ricevuta",
              JOptionPane.DEFAULT_OPTION,
              JOptionPane.QUESTION_MESSAGE,
              null,
              opzioni,
              opzioni[0]);

      if (scelta == 0) {
        boolean ok = aggiornaEsitoProposta(
                proposta, proposta.utenteCoinvolto(), true, false, "Proposta accettata con successo.");
        if (ok) {
          caricaDati();
        }
        return;
      }
      if (scelta == 1) {
        aggiornaEsitoProposta(proposta, proposta.utenteCoinvolto(), false, false, "Proposta rifiutata.");
        return;
      }
      if (mostraImmagine && scelta == 2) {
        mostraImmagineProposta(proposta);
        continue;
      }
      return;
    }
  }

  /**
   * Aggiorna stato spedizione a spedito.
   */
  private void aggiornaStatoSpedizione(int idAnnuncio) {
    try {
      if (spedizioneDAO.aggiornaStatoSpedizione(idAnnuncio, true)) {
        view.mostraMessaggio("Spedizione aggiornata a 'spedito'.");
        caricaDati();
      } else {
        view.mostraErrore("Impossibile aggiornare lo stato della spedizione.");
      }
    } catch (DatabaseException e) {
      view.mostraErrore("Errore nell'aggiornamento stato spedizione: " + e.getMessage());
      Logger.error("Errore aggiornamento stato spedizione", e);
    }
  }

  /**
   * Aggiorna stato ritiro a ritirato.
   */
  private void aggiornaStatoRitiro(int idAnnuncio) {
    try {
      if (ritiroDAO.aggiornaStatoRitiro(idAnnuncio, true)) {
        view.mostraMessaggio("Ritiro aggiornato a 'ritirato'.");
        caricaDati();
      } else {
        view.mostraErrore("Impossibile aggiornare lo stato del ritiro.");
      }
    } catch (DatabaseException e) {
      view.mostraErrore("Errore nell'aggiornamento stato ritiro: " + e.getMessage());
      Logger.error("Errore aggiornamento stato ritiro", e);
    }
  }

  /**
   * Gestisce doppio-clic in inviate proposta.
   *
   * @param selectedRow riga indice
   */
  private void handlePropostaInviata(int selectedRow) {
    if (!mostraDatiSensibili) {
      view.mostraErrore("Operazione disponibile solo nel tuo profilo.");
      return;
    }
    if (selectedRow < 0 || proposteInviate == null || selectedRow >= proposteInviate.size()) {
      return;
    }
    PropostaRiepilogo proposta = proposteInviate.get(selectedRow);
    if (proposta == null || proposta.annuncio() == null) {
      view.mostraErrore("Proposta non valida.");
      return;
    }

    boolean mostraImmagine = isPropostaScambio(proposta);
    String dettaglio = buildDettaglioProposta(proposta, "A");

    if (proposta.accettata()) {
      handlePropostaInviataAccettata(proposta, mostraImmagine, dettaglio);
    } else if (!proposta.inattesa()) {
      handlePropostaInviataRifiutata(proposta, mostraImmagine, dettaglio);
    } else {
      handlePropostaInviataInAttesa(proposta, mostraImmagine, dettaglio);
    }
  }

  /**
   * Gestisce proposta inviata accettata.
   */
  private void handlePropostaInviataAccettata(PropostaRiepilogo proposta, boolean mostraImmagine, String dettaglio) {
    if (proposta.annuncio() == null) {
      view.mostraErrore("Annuncio non disponibile.");
      return;
    }

    while (true) {
      boolean deliveryDetailsExist = false;
      if (spedizioneDAO != null && ritiroDAO != null) {
        try {
          int idAnnuncio = proposta.annuncio().getIdAnnuncio();
          model.Spedizione spedizione = spedizioneDAO.getSpedizioneByAnnuncio(idAnnuncio);
          model.Ritiro ritiro = ritiroDAO.getRitiroByAnnuncio(idAnnuncio);
          deliveryDetailsExist = (spedizione != null || ritiro != null);
        } catch (DatabaseException e) {
          view.mostraErrore("Errore nel controllo stato consegna: " + e.getMessage());
          Logger.error("Errore controllo stato consegna proposta inviata", e);
        }
      }

      List<String> opzioniList = new ArrayList<>();
      if (StatoConsegna.CONCLUSO.equals(formatStato(proposta))) {
        opzioniList.add("Lascia recensione");
      }
      if (!deliveryDetailsExist) {
        opzioniList.add("Scegli consegna");
      } else {
        opzioniList.add("Dettagli consegna");
      }
      if (mostraImmagine) {
        opzioniList.add("Mostra immagine");
      }
      opzioniList.add("Chiudi");
      String[] opzioni = opzioniList.toArray(new String[0]);

      int scelta = JOptionPane.showOptionDialog(
              view,
              dettaglio + "\n\nLa proposta è stata accettata.",
              "Proposta inviata",
              JOptionPane.DEFAULT_OPTION,
              JOptionPane.INFORMATION_MESSAGE,
              null,
              opzioni,
              opzioni[0]);

      if (scelta >= 0 && scelta < opzioni.length) {
        String opzioneScelta = opzioni[scelta];
        switch (opzioneScelta) {
          case "Lascia recensione":
            apriScriviRecensione(proposta.utenteCoinvolto());
            return;
          case "Scegli consegna":
          case "Dettagli consegna":
            inserisciDettagliConsegna(proposta);
            break;
          case "Mostra immagine":
            mostraImmagineProposta(proposta);
            break;
          case "Chiudi":
          default:
            return;
        }
      } else {
        return;
      }
    }
  }

  /**
   * Gestisce proposta inviata rifiutata.
   */
  private void handlePropostaInviataRifiutata(PropostaRiepilogo proposta, boolean mostraImmagine, String dettaglio) {
    if (mostraImmagine) {
      while (true) {
        Object[] opzioni = {"Mostra immagine", "Chiudi"};
        int scelta = JOptionPane.showOptionDialog(
                view,
                dettaglio + "\n\nLa proposta è stata rifiutata.",
                "Proposta inviata",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                opzioni,
                opzioni[1]);
        if (scelta == 0) {
          mostraImmagineProposta(proposta);
          continue;
        }
        return;
      }
    } else {
      JOptionPane.showMessageDialog(
              view,
              dettaglio + "\n\nLa proposta è stata rifiutata.",
              "Proposta inviata",
              JOptionPane.INFORMATION_MESSAGE);
    }
  }

  /**
   * Gestisce proposta inviata in attesa.
   */
  private void handlePropostaInviataInAttesa(PropostaRiepilogo proposta, boolean mostraImmagine, String dettaglio) {
    while (true) {
      Object[] opzioni = mostraImmagine
              ? new Object[]{"Mostra immagine", "Chiudi"}
              : new Object[]{"Chiudi"};
      Object defaultOption = mostraImmagine ? opzioni[1] : opzioni[0];
      int scelta = JOptionPane.showOptionDialog(
              view,
              dettaglio,
              "Proposta inviata",
              JOptionPane.DEFAULT_OPTION,
              JOptionPane.INFORMATION_MESSAGE,
              null,
              opzioni,
              defaultOption);

      if (mostraImmagine && scelta == 0) {
        mostraImmagineProposta(proposta);
        continue;
      }
      return;
    }
  }

  /**
   * Gestisce azione recensione da proposta selezionata.
   *
   * @param ricevuta true per proposte ricevute, false per inviate
   */
  private void handleRecensioneDaProposta(boolean ricevuta) {
    if (!mostraDatiSensibili) {
      view.mostraErrore("Operazione disponibile solo nel tuo profilo.");
      return;
    }

    int selectedRow = ricevuta ? view.getSelectedPropostaRicevutaRow() : view.getSelectedPropostaInviataRow();
    List<PropostaRiepilogo> lista = ricevuta ? proposteRicevute : proposteInviate;

    if (selectedRow < 0 || lista == null || selectedRow >= lista.size()) {
      view.mostraErrore("Seleziona una proposta per lasciare una recensione.");
      return;
    }

    PropostaRiepilogo proposta = lista.get(selectedRow);
    if (proposta == null) {
      view.mostraErrore("Proposta non valida.");
      return;
    }

    if (!StatoConsegna.CONCLUSO.equals(formatStato(proposta))) {
      view.mostraErrore("Puoi lasciare una recensione solo per proposte concluse.");
      return;
    }

    if (proposta.utenteCoinvolto() == null) {
      view.mostraErrore("Utente coinvolto non disponibile.");
      return;
    }

    apriScriviRecensione(proposta.utenteCoinvolto());
  }

  /**
   * Aggiorna stato proposta e aggiorna data in caso di successo.
   *
   * @param proposta riepilogo proposta
   * @param utenteProponente proponente
   * @param accettata accettata flag
   * @param inattesa in attesa flag
   * @param messaggioOk successo messaggio
   */
  private boolean aggiornaEsitoProposta(
          PropostaRiepilogo proposta, Utente utenteProponente, boolean accettata, boolean inattesa,
          String messaggioOk) {
    if (propostaDAO == null) {
      view.mostraErrore("Connessione per le proposte non disponibile.");
      return false;
    }
    if (utenteProponente == null) {
      view.mostraErrore("Utente proponente non valido.");
      return false;
    }
    if (proposta.annuncio() == null) {
      view.mostraErrore("Annuncio non disponibile.");
      return false;
    }
    try {
      String tipoAnnuncio = proposta.annuncio().getTipoAnnuncio() != null
          ? proposta.annuncio().getTipoAnnuncio().toString() : "";
      boolean ok = propostaDAO.aggiornaEsitoProposta(
              proposta.annuncio().getIdAnnuncio(),
              tipoAnnuncio,
              utenteProponente.getUsername(),
              accettata,
              inattesa);
      if (ok) {
        view.mostraMessaggio(messaggioOk);
        caricaDati();
      } else {
        view.mostraErrore("Operazione non riuscita sulla proposta.");
      }
      return ok;
    } catch (DatabaseException e) {
      view.mostraErrore("Errore durante l'aggiornamento della proposta: " + e.getMessage());
      Logger.error("Errore aggiornamento esito proposta", e);
      return false;
    }
  }

  /**
   * Elimina proposta e aggiorna data in caso di successo.
   *
   * @param proposta riepilogo proposta
   * @param utenteProponente proponente
   * @param messaggioOk successo messaggio
   */
  private void eliminaProposta(PropostaRiepilogo proposta, Utente utenteProponente, String messaggioOk) {
    if (propostaDAO == null) {
      view.mostraErrore("Connessione per le proposte non disponibile.");
      return;
    }
    if (utenteProponente == null) {
      view.mostraErrore("Utente proponente non valido.");
      return;
    }
    if (proposta.annuncio() == null) {
      view.mostraErrore("Annuncio non disponibile.");
      return;
    }
    try {
      String tipoAnnuncio = proposta.annuncio().getTipoAnnuncio() != null
          ? proposta.annuncio().getTipoAnnuncio().toString() : "";
      boolean ok = propostaDAO.eliminaProposta(
              proposta.annuncio().getIdAnnuncio(),
              tipoAnnuncio,
              utenteProponente.getUsername());
      if (ok) {
        view.mostraMessaggio(messaggioOk);
        caricaDati();
      } else {
        view.mostraErrore("Operazione non riuscita sulla proposta.");
      }
    } catch (DatabaseException e) {
      view.mostraErrore("Errore durante l'operazione sulla proposta: " + e.getMessage());
      Logger.error("Errore eliminazione proposta", e);
    }
  }

  /**
   * Gestisce inserimento dati consegna per proposta accettata.
   *
   * @param proposta riepilogo proposta
   */
  private void inserisciDettagliConsegna(PropostaRiepilogo proposta) {
    if (proposta == null || proposta.annuncio() == null) {
      view.mostraErrore("Proposta o annuncio non disponibile.");
      return;
    }

    if (spedizioneDAO == null || ritiroDAO == null) {
      view.mostraErrore("Connessione ai servizi di spedizione/ritiro non disponibile.");
      return;
    }

    int idAnnuncio = proposta.annuncio().getIdAnnuncio();

    try {
      model.Spedizione spedizione = spedizioneDAO.getSpedizioneByAnnuncio(idAnnuncio);
      if (spedizione != null) {
        consegnaHelper.mostraDettagliSpedizione(spedizione);
        return;
      }

      model.Ritiro ritiro = ritiroDAO.getRitiroByAnnuncio(idAnnuncio);
      if (ritiro != null) {
        consegnaHelper.mostraDettagliRitiro(ritiro);
        return;
      }
    } catch (DatabaseException e) {
      view.mostraErrore("Errore durante la verifica consegna: " + e.getMessage());
      Logger.error("Errore verifica dettagli consegna", e);
      return;
    }

    Object[] opzioni = {"Spedizione", "Ritiro", "Annulla"};
    int scelta = JOptionPane.showOptionDialog(
            view,
            "Seleziona il metodo di consegna per la proposta accettata.",
            "Dettagli consegna",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            opzioni,
            opzioni[0]);

    if (scelta == 0) {
      salvaSpedizione(idAnnuncio);
    } else if (scelta == 1) {
      salvaRitiro(idAnnuncio);
    }
  }

  /**
   * Richiede dati spedizione e salva nel database.
   *
   * @param idAnnuncio id annuncio
   */
  private void salvaSpedizione(int idAnnuncio) {
    consegnaHelper.salvaSpedizione(
            idAnnuncio,
            utenteTarget.getIdUtente(),
            msg -> {
              view.mostraMessaggio(msg);
              caricaDati();
            },
            view::mostraErrore
    );
  }

  /**
   * Richiede dati ritiro e salva nel database.
   *
   * @param idAnnuncio id annuncio
   */
  private void salvaRitiro(int idAnnuncio) {
    consegnaHelper.salvaRitiro(
            idAnnuncio,
            msg -> {
              view.mostraMessaggio(msg);
              caricaDati();
            },
            view::mostraErrore
    );
  }

  /**
   * Costruisce dettaglio stringa per proposta riga.
   *
   * @param proposta riepilogo proposta
   * @param labelUtente etichetta prefisso
   * @return dettaglio stringa
   */
  private String buildDettaglioProposta(PropostaRiepilogo proposta, String labelUtente) {
    if (proposta == null) {
      return "Proposta non disponibile";
    }

    String nomeUtente = proposta.utenteCoinvolto() != null ? proposta.utenteCoinvolto().getUsername() : "Sconosciuto";
    String titoloAnnuncio = proposta.annuncio() != null ? proposta.annuncio().getTitolo() : "N/A";
    String tipoAnnuncio = proposta.annuncio() != null && proposta.annuncio().getTipoAnnuncio() != null
        ? proposta.annuncio().getTipoAnnuncio().toString() : "N/A";
    return labelUtente + ": " + nomeUtente
            + "\nAnnuncio: " + titoloAnnuncio
            + "\nTipo: " + tipoAnnuncio
            + "\nDettaglio: " + proposta.dettaglio()
            + "\nStato: " + formatStato(proposta).getDescrizione();
  }

  /**
   * Determina se la proposta e di tipo scambio.
   *
   * @param proposta proposta da verificare
   * @return true se e scambio
   */
  private boolean isPropostaScambio(PropostaRiepilogo proposta) {
    if (proposta == null || proposta.annuncio() == null || proposta.annuncio().getTipoAnnuncio() == null) {
      return false;
    }
    return proposta.annuncio().getTipoAnnuncio().toString().trim().toUpperCase().contains("SCAMBIO");
  }

  /**
   * Mostra immagine della proposta se disponibile.
   *
   * @param proposta proposta selezionata
   */
  private void mostraImmagineProposta(PropostaRiepilogo proposta) {
    ImmaginePropostaHelper.mostraImmagine(
            proposta,
            view,
            () -> view.mostraErrore("Immagine non disponibile.")
    );
  }

  /**
   * Apre form recensione per utente indicato.
   *
   * @param utenteDestinatario utente destinatario
   */
  private void apriScriviRecensione(Utente utenteDestinatario) {
    if (utenteDestinatario == null) {
      view.mostraErrore("Utente destinatario non valido.");
      return;
    }

    ScriviRecensione recensioneView = new ScriviRecensione();
    new ScriviRecensioneController(recensioneView, utenteDestinatario.getIdUtente());
    WindowManager.open(view, recensioneView);
  }

  /**
   * Gestisce modifica proposta inviata.
   */
  private void handleModificaProposta() {
    if (!mostraDatiSensibili) {
      view.mostraErrore("Operazione disponibile solo nel tuo profilo.");
      return;
    }

    int selectedRow = view.getSelectedPropostaInviataRow();
    if (selectedRow < 0 || proposteInviate == null || selectedRow >= proposteInviate.size()) {
      view.mostraErrore("Seleziona una proposta da modificare.");
      return;
    }

    PropostaRiepilogo proposta = proposteInviate.get(selectedRow);

    if (!proposta.inattesa()) {
      view.mostraErrore("Puoi modificare solo le proposte in attesa.");
      return;
    }

    if (proposta.annuncio() != null && proposta.annuncio().getTipoAnnuncio() != null
        && proposta.annuncio().getTipoAnnuncio().toString().toUpperCase().contains("REGALO")) {
      view.mostraErrore("Non puoi modificare una proposta per un regalo.");
      return;
    }

    ModificaPropostaDialog dialog = new ModificaPropostaDialog(view, proposta);
    dialog.setVisible(true);

    caricaDati();
  }

  /**
   * Gestisce annullamento proposta inviata.
   */
  private void handleAnnullaProposta() {
    if (!mostraDatiSensibili) {
      view.mostraErrore("Operazione disponibile solo nel tuo profilo.");
      return;
    }

    int selectedRow = view.getSelectedPropostaInviataRow();
    if (selectedRow < 0 || proposteInviate == null || selectedRow >= proposteInviate.size()) {
      view.mostraErrore("Seleziona una proposta da annullare.");
      return;
    }

    PropostaRiepilogo proposta = proposteInviate.get(selectedRow);

    if (!proposta.inattesa()) {
      view.mostraErrore("Puoi annullare solo le proposte in attesa.");
      return;
    }

    int confirm = JOptionPane.showConfirmDialog(
            view,
            "Sei sicuro di voler annullare questa proposta?",
            "Conferma annullamento",
            JOptionPane.YES_NO_OPTION
    );

    if (confirm == JOptionPane.YES_OPTION) {
      eliminaProposta(proposta, utenteTarget, "Proposta annullata con successo.");
    }
  }

  /**
   * Visualizza i dettagli della consegna per una proposta (sola lettura).
   *
   * @param proposta la proposta per cui visualizzare i dettagli.
   */
  private void visualizzaDettagliConsegna(PropostaRiepilogo proposta) {
    consegnaHelper.visualizzaDettagli(
            proposta,
            view::mostraErrore,
            () -> view.mostraMessaggio("Dettagli di consegna non ancora forniti dall'acquirente.")
    );
  }
}
