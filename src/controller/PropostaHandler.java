package controller;

import dao.PropostaDAO;
import dao.RitiroDAO;
import dao.SpedizioneDAO;
import exception.DatabaseException;
import gui.ModificaPropostaDialog;
import gui.Profilo;
import gui.ScriviRecensione;
import model.PropostaRiepilogo;
import model.Utente;
import model.enums.StatoConsegna;
import utils.ConsegnaHelper;
import utils.ImmaginePropostaHelper;
import utils.Logger;
import utils.WindowManager;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * Gestisce tutte le operazioni relative alle proposte nel profilo utente.
 */
public class PropostaHandler {

  private static final String TESTO_CHIUDI = "Chiudi";
  private static final String TESTO_MOSTRA_IMMAGINE = "Mostra immagine";
  private static final String TESTO_DETTAGLI_CONSEGNA = "Dettagli consegna";

  private final Profilo view;
  private final PropostaDAO propostaDAO;
  private final SpedizioneDAO spedizioneDAO;
  private final RitiroDAO ritiroDAO;
  private final ConsegnaHelper consegnaHelper;
  private final Utente utenteTarget;

  public PropostaHandler(Profilo view, PropostaDAO propostaDAO,
                         SpedizioneDAO spedizioneDAO, RitiroDAO ritiroDAO,
                         Utente utenteTarget) {
    this.view = view;
    this.propostaDAO = propostaDAO;
    this.spedizioneDAO = spedizioneDAO;
    this.ritiroDAO = ritiroDAO;
    this.consegnaHelper = new ConsegnaHelper(spedizioneDAO, ritiroDAO, view);
    this.utenteTarget = utenteTarget;
  }

  /**
   * Gestisce il clic dell'utente su una proposta ricevuta.
   *
   * @param proposta la proposta ricevuta da gestire
   */
  public void handlePropostaRicevuta(PropostaRiepilogo proposta) {
    if (proposta == null) return;
    mostraDialogProposta(proposta, true);
  }

  /**
   * Gestisce il clic dell'utente su una proposta inviata.
   *
   * @param proposta la proposta inviata da gestire
   */
  public void handlePropostaInviata(PropostaRiepilogo proposta) {
    if (proposta == null) return;
    mostraDialogProposta(proposta, false);
  }

  /**
   * Mostra il dialog appropriato per la gestione della proposta in base al suo stato.
   *
   * @param proposta la proposta da visualizzare
   * @param isRicevuta true se è una proposta ricevuta, false se inviata
   */
  private void mostraDialogProposta(PropostaRiepilogo proposta, boolean isRicevuta) {
    if (proposta == null) return;

    boolean mostraImmagine = isPropostaScambio(proposta);
    String labelUtente = isRicevuta ? "Da" : "A";
    String dettaglio = buildDettaglioProposta(proposta, labelUtente);

    if (proposta.accettata()) {
      handlePropostaAccettata(proposta, mostraImmagine, dettaglio, isRicevuta);
    } else if (!proposta.inattesa()) {
      handlePropostaRifiutata(proposta, mostraImmagine, dettaglio);
    } else {
      handlePropostaInAttesa(proposta, mostraImmagine, dettaglio, isRicevuta);
    }
  }

  /**
   * Gestisce una proposta che è stata accettata, mostrando le opzioni disponibili.
   *
   * @param proposta la proposta accettata
   * @param mostraImmagine true se la proposta ha un'immagine da mostrare
   * @param dettaglio stringa con i dettagli della proposta
   * @param isRicevuta true se è una proposta ricevuta, false se inviata
   */
  private void handlePropostaAccettata(PropostaRiepilogo proposta, boolean mostraImmagine,
                                       String dettaglio, boolean isRicevuta) {
    if (proposta.annuncio() == null) {
      view.mostraErrore("Annuncio non disponibile.");
      return;
    }

    int idAnnuncio = proposta.annuncio().getIdAnnuncio();
    StatoConsegna statoConsegna = formatStato(proposta);

    while (true) {
      List<String> opzioni = buildOpzioniAccettata(statoConsegna, mostraImmagine, isRicevuta, idAnnuncio);
      String messaggio = dettaglio + "\n\n" + (isRicevuta ? "Questa proposta è stata accettata." : "La proposta è stata accettata.");
      String titolo = isRicevuta ? "Proposta ricevuta" : "Proposta inviata";

      int scelta = JOptionPane.showOptionDialog(view, messaggio, titolo,
              JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
              null, opzioni.toArray(), opzioni.get(0));

      if (scelta < 0 || scelta >= opzioni.size()) return;

      if (!handleAzionePropostaAccettata(opzioni.get(scelta), proposta, idAnnuncio, isRicevuta)) {
        return;
      }
    }
  }

  /**
   * Costruisce la lista delle opzioni disponibili per una proposta accettata.
   *
   * @param stato lo stato corrente della consegna
   * @param mostraImmagine true se c'è un'immagine da mostrare
   * @param isRicevuta true se è una proposta ricevuta
   * @param idAnnuncio l'ID dell'annuncio associato
   * @return lista delle opzioni disponibili
   */
  private List<String> buildOpzioniAccettata(StatoConsegna stato,
                                              boolean mostraImmagine, boolean isRicevuta, int idAnnuncio) {
    List<String> opzioni = new ArrayList<>();

    aggiungiOpzioneRecensione(opzioni, stato);
    aggiungiOpzioniPerTipoProposta(opzioni, isRicevuta, idAnnuncio);
    aggiungiOpzioneImmagine(opzioni, mostraImmagine);
    opzioni.add(TESTO_CHIUDI);
    return opzioni;
  }

  private void aggiungiOpzioneRecensione(List<String> opzioni, StatoConsegna stato) {
    if (StatoConsegna.CONCLUSO.equals(stato)) {
      opzioni.add("Lascia recensione");
    }
  }

  private void aggiungiOpzioniPerTipoProposta(List<String> opzioni, boolean isRicevuta, int idAnnuncio) {
    if (isRicevuta) {
      aggiungiOpzioniRicevuta(opzioni, idAnnuncio);
    } else {
      aggiungiOpzioniInviata(opzioni, idAnnuncio);
    }
  }

  private void aggiungiOpzioneImmagine(List<String> opzioni, boolean mostraImmagine) {
    if (mostraImmagine) {
      opzioni.add(TESTO_MOSTRA_IMMAGINE);
    }
  }

  private void aggiungiOpzioniRicevuta(List<String> opzioni, int idAnnuncio) {
    opzioni.add(TESTO_DETTAGLI_CONSEGNA);
    if (spedizioneDAO != null && ritiroDAO != null) {
      try {
        model.Spedizione spedizione = spedizioneDAO.getSpedizioneByAnnuncio(idAnnuncio);
        if (spedizione != null && !spedizione.isSpedito()) {
          opzioni.add("Ho spedito");
        }
        model.Ritiro ritiro = ritiroDAO.getRitiroByAnnuncio(idAnnuncio);
        if (ritiro != null && !ritiro.isRitirato()) {
          opzioni.add("Ritirato");
        }
      } catch (DatabaseException e) {
        Logger.error("Errore controllo stato consegna");
      }
    }
  }

  private void aggiungiOpzioniInviata(List<String> opzioni, int idAnnuncio) {
    boolean dettagliEsistono = verificaDettagliConsegna(idAnnuncio);
    opzioni.add(dettagliEsistono ? TESTO_DETTAGLI_CONSEGNA : "Scegli consegna");
  }

  /**
   * Gestisce l'azione selezionata dall'utente per una proposta accettata.
   *
   * @param azione l'azione scelta dall'utente
   * @param proposta la proposta su cui agire
   * @param idAnnuncio l'ID dell'annuncio associato
   * @param isRicevuta true se è una proposta ricevuta
   * @return false se il dialog deve chiudersi, true se deve rimanere aperto
   */
  private boolean handleAzionePropostaAccettata(String azione, PropostaRiepilogo proposta,
                                                 int idAnnuncio, boolean isRicevuta) {
    switch (azione) {
      case "Lascia recensione":
        apriScriviRecensione(proposta.utenteCoinvolto());
        return false;
      case TESTO_DETTAGLI_CONSEGNA:
        if (isRicevuta) {
          visualizzaDettagliConsegna(proposta);
        } else {
          inserisciDettagliConsegna(proposta);
        }
        return true;
      case "Scegli consegna":
        inserisciDettagliConsegna(proposta);
        return true;
      case "Ho spedito":
        aggiornaStatoConsegna(idAnnuncio, true, "Spedizione aggiornata a 'spedito'.");
        return false;
      case "Ritirato":
        aggiornaStatoConsegna(idAnnuncio, false, "Ritiro aggiornato a 'ritirato'.");
        return false;
      case TESTO_MOSTRA_IMMAGINE:
        mostraImmagineProposta(proposta);
        return true;
      default:
        return false;
    }
  }

  /**
   * Gestisce una proposta che è stata rifiutata.
   *
   * @param proposta la proposta rifiutata
   * @param mostraImmagine true se c'è un'immagine da mostrare
   * @param dettaglio stringa con i dettagli della proposta
   */
  private void handlePropostaRifiutata(PropostaRiepilogo proposta, boolean mostraImmagine, String dettaglio) {
    String messaggio = dettaglio + "\n\nQuesta proposta è stata rifiutata.";

    if (!mostraImmagine) {
      JOptionPane.showMessageDialog(view, messaggio, "Proposta", JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    while (true) {
      Object[] opzioni = {TESTO_MOSTRA_IMMAGINE, TESTO_CHIUDI};
      int scelta = JOptionPane.showOptionDialog(view, messaggio, "Proposta",
              JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
              null, opzioni, opzioni[1]);

      if (scelta == 0) {
        mostraImmagineProposta(proposta);
      } else {
        return;
      }
    }
  }

  /**
   * Gestisce una proposta che è ancora in attesa di risposta.
   *
   * @param proposta la proposta in attesa
   * @param mostraImmagine true se c'è un'immagine da mostrare
   * @param dettaglio stringa con i dettagli della proposta
   * @param isRicevuta true se è una proposta ricevuta, false se inviata
   */
  private void handlePropostaInAttesa(PropostaRiepilogo proposta, boolean mostraImmagine,
                                      String dettaglio, boolean isRicevuta) {
    while (true) {
      Object[] opzioni = buildOpzioniInAttesa(mostraImmagine, isRicevuta);
      String titolo = isRicevuta ? "Gestione proposta ricevuta" : "Proposta inviata";

      int scelta = JOptionPane.showOptionDialog(view, dettaglio, titolo,
              JOptionPane.DEFAULT_OPTION,
              isRicevuta ? JOptionPane.QUESTION_MESSAGE : JOptionPane.INFORMATION_MESSAGE,
              null, opzioni, opzioni[0]);

      if (handleSceltaPropostaInAttesa(scelta, proposta, mostraImmagine, isRicevuta)) {
        return;
      }
    }
  }

  /**
   * Gestisce la scelta dell'utente per una proposta in attesa.
   *
   * @return true se il dialog deve chiudersi, false se deve rimanere aperto
   */
  private boolean handleSceltaPropostaInAttesa(int scelta, PropostaRiepilogo proposta,
                                                 boolean mostraImmagine, boolean isRicevuta) {
    if (isRicevuta && scelta == 0) {
      aggiornaEsitoProposta(proposta, proposta.utenteCoinvolto(),
                           true, false, "Proposta accettata con successo.");
      return true;
    }
    if (isRicevuta && scelta == 1) {
      aggiornaEsitoProposta(proposta, proposta.utenteCoinvolto(),
                           false, false, "Proposta rifiutata.");
      return true;
    }

    int imgIndex = isRicevuta ? 2 : 0;
    if (mostraImmagine && scelta == imgIndex) {
      mostraImmagineProposta(proposta);
      return false;
    }
    return true;
  }

  /**
   * Costruisce le opzioni disponibili per una proposta in attesa.
   *
   * @param mostraImmagine true se c'è un'immagine da mostrare
   * @param isRicevuta true se è una proposta ricevuta
   * @return array delle opzioni disponibili
   */
  private Object[] buildOpzioniInAttesa(boolean mostraImmagine, boolean isRicevuta) {
    if (isRicevuta) {
      return mostraImmagine
          ? new Object[]{"Accetta", "Rifiuta", TESTO_MOSTRA_IMMAGINE, TESTO_CHIUDI}
          : new Object[]{"Accetta", "Rifiuta", TESTO_CHIUDI};
    } else {
      return mostraImmagine
          ? new Object[]{TESTO_MOSTRA_IMMAGINE, TESTO_CHIUDI}
          : new Object[]{TESTO_CHIUDI};
    }
  }

  /**
   * Aggiorna l'esito di una proposta (accettata o rifiutata).
   *
   * @param proposta la proposta da aggiornare
   * @param utenteProponente l'utente che ha fatto la proposta
   * @param accettata true se la proposta è accettata, false se rifiutata
   * @param inattesa true se la proposta è ancora in attesa
   * @param messaggioOk messaggio da mostrare in caso di successo
   * @return true se l'operazione è riuscita, false altrimenti
   */
  public boolean aggiornaEsitoProposta(PropostaRiepilogo proposta, Utente utenteProponente,
                                        boolean accettata, boolean inattesa, String messaggioOk) {
    if (!validaDatiProposta(proposta, utenteProponente)) return false;

    try {
      String tipoAnnuncio = proposta.annuncio().getTipoAnnuncio() != null
          ? proposta.annuncio().getTipoAnnuncio().toString() : "";
      boolean ok = propostaDAO.aggiornaEsitoProposta(
          proposta.annuncio().getIdAnnuncio(), tipoAnnuncio,
          utenteProponente.getUsername(), accettata, inattesa);

      if (ok) {
        view.mostraMessaggio(messaggioOk);
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
   * Elimina una proposta dal database.
   *
   * @param proposta la proposta da eliminare
   * @param utenteProponente l'utente che ha fatto la proposta
   * @param messaggioOk messaggio da mostrare in caso di successo
   */
  public void eliminaProposta(PropostaRiepilogo proposta, Utente utenteProponente, String messaggioOk) {
    if (!validaDatiProposta(proposta, utenteProponente)) return;

    try {
      String tipoAnnuncio = proposta.annuncio().getTipoAnnuncio() != null
          ? proposta.annuncio().getTipoAnnuncio().toString() : "";
      boolean ok = propostaDAO.eliminaProposta(
          proposta.annuncio().getIdAnnuncio(), tipoAnnuncio, utenteProponente.getUsername());

      if (ok) {
        view.mostraMessaggio(messaggioOk);
      } else {
        view.mostraErrore("Operazione non riuscita sulla proposta.");
      }
    } catch (DatabaseException e) {
      view.mostraErrore("Errore durante l'operazione sulla proposta: " + e.getMessage());
      Logger.error("Errore eliminazione proposta", e);
    }
  }

  /**
   * Gestisce la richiesta di modifica di una proposta inviata.
   *
   * @param proposta la proposta da modificare
   */
  public void handleModificaProposta(PropostaRiepilogo proposta) {
    if (proposta == null || !proposta.inattesa()) {
      view.mostraErrore("Puoi modificare solo le proposte in attesa.");
      return;
    }

    ModificaPropostaDialog dialog = new ModificaPropostaDialog(view, proposta);
    dialog.setVisible(true);
  }

  /**
   * Gestisce la richiesta di annullamento di una proposta inviata.
   *
   * @param proposta la proposta da annullare
   */
  public void handleAnnullaProposta(PropostaRiepilogo proposta) {
    if (proposta == null || !proposta.inattesa()) {
      view.mostraErrore("Puoi annullare solo le proposte in attesa.");
      return;
    }

    Object[] opzioni = {"No", "Sì"};
    int confirm = JOptionPane.showOptionDialog(view,
        "Sei sicuro di voler annullare questa proposta?",
        "Conferma annullamento",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE,
        null,
        opzioni,
        opzioni[0]);

    if (confirm == 1) {
      eliminaProposta(proposta, utenteTarget, "Proposta annullata con successo.");
    }
  }

  /**
   * Determina e restituisce lo stato di consegna di una proposta.
   *
   * @param proposta la proposta di cui determinare lo stato
   * @return lo stato di consegna della proposta
   */
  public StatoConsegna formatStato(PropostaRiepilogo proposta) {
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
   * Valida i dati della proposta e dell'utente proponente prima di operazioni sul database.
   *
   * @param proposta la proposta da validare
   * @param utenteProponente l'utente che ha fatto la proposta
   * @return true se i dati sono validi, false altrimenti
   */
  private boolean validaDatiProposta(PropostaRiepilogo proposta, Utente utenteProponente) {
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
    return true;
  }

  /**
   * Aggiorna lo stato di consegna (spedizione o ritiro) di un annuncio.
   *
   * @param idAnnuncio l'ID dell'annuncio
   * @param isSpedizione true per spedizione, false per ritiro
   * @param messaggioSuccesso messaggio da mostrare in caso di successo
   */
  private void aggiornaStatoConsegna(int idAnnuncio, boolean isSpedizione, String messaggioSuccesso) {
    try {
      boolean success = isSpedizione
          ? spedizioneDAO.aggiornaStatoSpedizione(idAnnuncio, true)
          : ritiroDAO.aggiornaStatoRitiro(idAnnuncio, true);

      if (success) {
        view.mostraMessaggio(messaggioSuccesso);
      } else {
        view.mostraErrore("Impossibile aggiornare lo stato della " +
                         (isSpedizione ? "spedizione" : "ritiro") + ".");
      }
    } catch (DatabaseException e) {
      view.mostraErrore("Errore nell'aggiornamento stato: " + e.getMessage());
      Logger.error("Errore aggiornamento stato " + (isSpedizione ? "spedizione" : "ritiro"), e);
    }
  }

  /**
   * Verifica se esistono già dettagli di consegna per un annuncio.
   *
   * @param idAnnuncio l'ID dell'annuncio da verificare
   * @return true se esistono dettagli di consegna, false altrimenti
   */
  private boolean verificaDettagliConsegna(int idAnnuncio) {
    if (spedizioneDAO == null || ritiroDAO == null) return false;
    try {
      model.Spedizione spedizione = spedizioneDAO.getSpedizioneByAnnuncio(idAnnuncio);
      model.Ritiro ritiro = ritiroDAO.getRitiroByAnnuncio(idAnnuncio);
      return spedizione != null || ritiro != null;
    } catch (DatabaseException e) {
      Logger.error("Errore verifica dettagli consegna", e);
      return false;
    }
  }

  /**
   * Gestisce l'inserimento o la visualizzazione dei dettagli di consegna per una proposta.
   *
   * @param proposta la proposta per cui gestire i dettagli di consegna
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
    int scelta = JOptionPane.showOptionDialog(view,
        "Seleziona il metodo di consegna per la proposta accettata.",
        TESTO_DETTAGLI_CONSEGNA, JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
        null, opzioni, opzioni[0]);

    if (scelta == 0) {
      consegnaHelper.salvaSpedizione(idAnnuncio, utenteTarget.getIdUtente(),
          view::mostraMessaggio, view::mostraErrore);
    } else if (scelta == 1) {
      consegnaHelper.salvaRitiro(idAnnuncio,
          view::mostraMessaggio, view::mostraErrore);
    }
  }

  /**
   * Visualizza i dettagli di consegna per una proposta (modalità sola lettura).
   *
   * @param proposta la proposta di cui visualizzare i dettagli
   */
  private void visualizzaDettagliConsegna(PropostaRiepilogo proposta) {
    consegnaHelper.visualizzaDettagli(proposta, view::mostraErrore,
        this::mostraMessaggioDettagliNonForniti);
  }

  private void mostraMessaggioDettagliNonForniti() {
    view.mostraMessaggio("Dettagli di consegna non ancora forniti dall'acquirente.");
  }

  /**
   * Costruisce una stringa con i dettagli completi della proposta.
   *
   * @param proposta la proposta di cui costruire i dettagli
   * @param labelUtente etichetta per l'utente ("Da" o "A")
   * @return stringa formattata con i dettagli della proposta
   */
  private String buildDettaglioProposta(PropostaRiepilogo proposta, String labelUtente) {
    if (proposta == null) return "Proposta non disponibile";

    String nomeUtente = proposta.utenteCoinvolto() != null
        ? proposta.utenteCoinvolto().getUsername() : "Sconosciuto";
    String titoloAnnuncio = proposta.annuncio() != null
        ? proposta.annuncio().getTitolo() : "N/A";
    String tipoAnnuncio = proposta.annuncio() != null && proposta.annuncio().getTipoAnnuncio() != null
        ? proposta.annuncio().getTipoAnnuncio().toString() : "N/A";

    return String.format("%s: %s%nAnnuncio: %s%nTipo: %s%nDettaglio: %s%nStato: %s",
        labelUtente, nomeUtente, titoloAnnuncio, tipoAnnuncio,
        proposta.dettaglio(), formatStato(proposta).getDescrizione());
  }

  /**
   * Verifica se una proposta è di tipo scambio.
   *
   * @param proposta la proposta da verificare
   * @return true se è una proposta di scambio, false altrimenti
   */
  private boolean isPropostaScambio(PropostaRiepilogo proposta) {
    return proposta != null && proposta.annuncio() != null
        && proposta.annuncio().getTipoAnnuncio() != null
        && proposta.annuncio().getTipoAnnuncio().toString().toUpperCase().contains("SCAMBIO");
  }

  /**
   * Mostra l'immagine associata a una proposta di scambio.
   *
   * @param proposta la proposta di cui mostrare l'immagine
   */
  private void mostraImmagineProposta(PropostaRiepilogo proposta) {
    ImmaginePropostaHelper.mostraImmagine(proposta, view,
        () -> view.mostraErrore("Immagine non disponibile."));
  }

  /**
   * Apre la finestra per scrivere una recensione per un utente.
   *
   * @param utenteDestinatario l'utente per cui scrivere la recensione
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
}
