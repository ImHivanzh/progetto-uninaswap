package controller;

import dao.AnnuncioDAO;
import dao.PropostaDAO;
import dao.RecensioneDAO;
import dao.RitiroDAO;
import dao.SpedizioneDAO;
import model.Annuncio;
import model.PropostaRiepilogo;
import model.Utente;
import model.enums.StatoConsegna;
import utils.Logger;
import utils.SessionManager;
import utils.WindowManager;
import exception.DatabaseException;
import gui.DettaglioAnnuncio;
import gui.Profilo;
import gui.ReportProposteDialog;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Controller per gestione profilo utente.
 * Coordina PropostaHandler e ProfiloDataLoader.
 */
public class ProfiloController {

  private final Profilo view;
  private final Utente utenteTarget;
  private final boolean mostraDatiSensibili;
  private final PropostaHandler propostaHandler;
  private final ProfiloDataLoader dataLoader;

  private List<Annuncio> listaAnnunci;
  private List<PropostaRiepilogo> proposteRicevute;
  private List<PropostaRiepilogo> proposteInviate;

  /**
   * Crea il controller per il profilo dell'utente corrente.
   */
  public ProfiloController(Profilo view) {
    this(view, null);
  }

  /**
   * Crea il controller per il profilo di uno specifico utente.
   */
  public ProfiloController(Profilo view, Utente utenteTarget) {
    this.view = view;

    if (utenteTarget == null) {
      this.utenteTarget = SessionManager.getInstance().getUtente();
      this.view.setTitoloProfilo("Il Mio Profilo");
      this.mostraDatiSensibili = true;
    } else {
      this.utenteTarget = utenteTarget;
      this.view.setTitoloProfilo("Profilo di " + utenteTarget.getUsername());
      this.view.nascondiTabProposte();
      this.mostraDatiSensibili = false;
    }

    RecensioneDAO recensioneDAO = new RecensioneDAO();
    AnnuncioDAO annuncioDAO = new AnnuncioDAO();
    PropostaDAO propostaDAO = creaDAO(PropostaDAO::new, "PropostaDAO");
    SpedizioneDAO spedizioneDAO = creaDAO(SpedizioneDAO::new, "SpedizioneDAO");
    RitiroDAO ritiroDAO = creaDAO(RitiroDAO::new, "RitiroDAO");

    this.propostaHandler = new PropostaHandler(view, propostaDAO, spedizioneDAO, ritiroDAO, this.utenteTarget);
    this.dataLoader = new ProfiloDataLoader(view, recensioneDAO, annuncioDAO, propostaDAO, propostaHandler);

    caricaDati();
    setupInteraction();
  }

  /**
   * Crea un DAO con gestione centralizzata degli errori.
   */
  private <T> T creaDAO(DAOSupplier<T> supplier, String nomeDAO) {
    try {
      return supplier.get();
    } catch (DatabaseException e) {
      view.mostraErrore("Errore connessione " + nomeDAO + ": " + e.getMessage());
      Logger.error("Errore creazione " + nomeDAO, e);
      return null;
    }
  }

  @FunctionalInterface
  private interface DAOSupplier<T> {
    T get() throws DatabaseException;
  }

  /**
   * Registra i listener per le interazioni dell'utente con le tabelle.
   */
  private void setupInteraction() {
    view.addTableAnnunciListener(new MouseAdapter() {
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
    view.addModificaPropostaListener(e -> propostaHandler.handleModificaProposta(validaPropostaPerModifica()));
    view.addAnnullaPropostaListener(e -> propostaHandler.handleAnnullaProposta(validaPropostaPerModifica()));
    view.addGeneraReportListener(e -> apriReportProposte());
  }

  /**
   * Carica i dati del profilo utente e aggiorna la vista.
   */
  private void caricaDati() {
    ProfiloDataLoader.DatiProfilo dati = dataLoader.caricaDatiCompleti(utenteTarget, mostraDatiSensibili);
    if (dati != null) {
      listaAnnunci = dati.listaAnnunci;
      proposteRicevute = dati.proposteRicevute;
      proposteInviate = dati.proposteInviate;
    }
  }

  /**
   * Gestisce il doppio clic dell'utente su una proposta ricevuta nella tabella.
   */
  private void handlePropostaRicevuta(int selectedRow) {
    PropostaRiepilogo proposta = validaSelezioneProposta(selectedRow, proposteRicevute);
    if (proposta == null) return;
    propostaHandler.handlePropostaRicevuta(proposta);
    caricaDati();
  }

  /**
   * Gestisce il doppio clic dell'utente su una proposta inviata nella tabella.
   */
  private void handlePropostaInviata(int selectedRow) {
    PropostaRiepilogo proposta = validaSelezioneProposta(selectedRow, proposteInviate);
    if (proposta == null) return;
    propostaHandler.handlePropostaInviata(proposta);
    caricaDati();
  }

  /**
   * Valida la proposta selezionata verificando permessi e dati.
   */
  private PropostaRiepilogo validaSelezioneProposta(int selectedRow, List<PropostaRiepilogo> lista) {
    if (!mostraDatiSensibili) {
      view.mostraErrore("Operazione disponibile solo nel tuo profilo.");
      return null;
    }
    if (selectedRow < 0 || lista == null || selectedRow >= lista.size()) {
      return null;
    }
    PropostaRiepilogo proposta = lista.get(selectedRow);
    if (proposta == null || proposta.annuncio() == null) {
      view.mostraErrore("Proposta non valida.");
      return null;
    }
    return proposta;
  }

  /**
   * Gestisce la richiesta di lasciare una recensione per una proposta selezionata.
   *
   * @param ricevuta true se la proposta è ricevuta, false se è inviata
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

    if (!StatoConsegna.CONCLUSO.equals(propostaHandler.formatStato(proposta))) {
      view.mostraErrore("Puoi lasciare una recensione solo per proposte concluse.");
      return;
    }

    if (proposta.utenteCoinvolto() == null) {
      view.mostraErrore("Utente coinvolto non disponibile.");
      return;
    }

    gui.ScriviRecensione recensioneView = new gui.ScriviRecensione();
    new ScriviRecensioneController(recensioneView, proposta.utenteCoinvolto().getIdUtente());
    WindowManager.open(view, recensioneView);
  }

  /**
   * Valida la proposta selezionata per operazioni di modifica o annullamento.
   *
   * @return la proposta selezionata se valida, null altrimenti
   */
  private PropostaRiepilogo validaPropostaPerModifica() {
    if (!mostraDatiSensibili) {
      view.mostraErrore("Operazione disponibile solo nel tuo profilo.");
      return null;
    }

    int selectedRow = view.getSelectedPropostaInviataRow();
    if (selectedRow < 0 || proposteInviate == null || selectedRow >= proposteInviate.size()) {
      view.mostraErrore("Seleziona una proposta.");
      return null;
    }

    return proposteInviate.get(selectedRow);
  }

  /**
   * Apre dialogo report proposte inviate.
   */
  private void apriReportProposte() {
    if (!mostraDatiSensibili) {
      view.mostraErrore("Operazione disponibile solo nel tuo profilo.");
      return;
    }

    ReportProposteDialog reportDialog = new ReportProposteDialog(view);
    new ReportProposteController(reportDialog);
    reportDialog.setVisible(true);
  }
}
