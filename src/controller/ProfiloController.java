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
import utils.DataCheck;
import utils.SessionManager;
import utils.WindowManager;
import exception.DatabaseException;
import gui.DettaglioAnnuncio;
import gui.ModificaPropostaDialog;
import gui.Profilo;
import gui.ScriviRecensione;

import javax.swing.*;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
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
      e.printStackTrace();
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
      e.printStackTrace();
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
      e.printStackTrace();
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
      List<Recensione> recensioni = recensioneDAO.getRecensioniRicevute(utenteTarget.getIdUtente());
      if (recensioni.isEmpty()) {
        view.setMediaVoto(0.0);
      } else {
        double sommaVoti = 0;
        for (Recensione r : recensioni) {
          String nomeUtente = r.getNomeUtente();
          if (nomeUtente == null || nomeUtente.trim().isEmpty()) {
            nomeUtente = "Sconosciuto";
          }
          view.aggiungiRecensione(nomeUtente, r.getVoto(), r.getDescrizione());
          sommaVoti += r.getVoto();
        }
        view.setMediaVoto(sommaVoti / recensioni.size());
      }

      this.listaAnnunci = annuncioDAO.findAllByUtente(utenteTarget.getIdUtente());

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
          view.aggiungiPropostaRicevuta(
                  proposta.utenteCoinvolto(),
                  proposta.titoloAnnuncio(),
                  proposta.tipoAnnuncio(),
                  proposta.dettaglio(),
                  formatStato(proposta)
          );
        }

        for (PropostaRiepilogo proposta : proposteInviate) {
          view.aggiungiPropostaInviata(
                  proposta.utenteCoinvolto(),
                  proposta.titoloAnnuncio(),
                  proposta.tipoAnnuncio(),
                  proposta.dettaglio(),
                  formatStato(proposta)
          );
        }
      }

    } catch (DatabaseException e) {
      view.mostraErrore("Errore nel caricamento dati: " + e.getMessage());
      e.printStackTrace();
    }
  }

  /**
   * Restituisce etichetta stato per proposta.
   *
   * @param proposta riepilogo proposta
   * @return etichetta stato
   */
  private String formatStato(PropostaRiepilogo proposta) {
    if (!proposta.accettata()) {
      return proposta.inattesa() ? "In attesa" : "Rifiutato";
    }

    if (spedizioneDAO == null || ritiroDAO == null) {
      return "In attesa";
    }

    try {
      model.Spedizione spedizione = spedizioneDAO.getSpedizioneByAnnuncio(proposta.idAnnuncio());
      model.Ritiro ritiro = ritiroDAO.getRitiroByAnnuncio(proposta.idAnnuncio());

      if (spedizione != null) {
        return spedizione.isSpedito() ? "Concluso" : "Da spedire";
      }
      if (ritiro != null) {
        return ritiro.isRitirato() ? "Concluso" : "Da ritirare";
      }
      return "In attesa";
    } catch (DatabaseException e) {
      view.mostraErrore("Errore nel controllo stato proposta: " + e.getMessage());
      e.printStackTrace();
      return "In attesa";
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
    boolean mostraImmagine = isPropostaScambio(proposta);
    String dettaglio = buildDettaglioProposta(proposta, "Da");

    if (proposta.accettata()) {
      boolean isSpedizioneNonSpedita = false;
      boolean isRitiroNonRitirato = false;
      int idAnnuncio = proposta.idAnnuncio();

      if (spedizioneDAO != null || ritiroDAO != null) {
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
          e.printStackTrace();
        }
      }

      while (true) {
        List<String> opzioniList = new ArrayList<>();
        if ("Concluso".equals(formatStato(proposta))) {
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

        // Handle button clicks by finding the chosen option in the list
        if (scelta >= 0 && scelta < opzioni.length) {
          String opzioneScelta = opzioni[scelta];
          switch (opzioneScelta) {
            case "Lascia recensione":
              apriScriviRecensione(proposta.utenteCoinvolto());
              return;
            case "Dettagli consegna":
              visualizzaDettagliConsegna(proposta);
              break; // continua ciclo
            case "Ho spedito":
              try {
                if (spedizioneDAO.aggiornaStatoSpedizione(idAnnuncio, true)) {
                  view.mostraMessaggio("Spedizione aggiornata a 'spedito'.");
                  caricaDati();
                }
                else {
                  view.mostraErrore("Impossibile aggiornare lo stato della spedizione.");
                }
              } catch (DatabaseException e) {
                view.mostraErrore("Errore nell'aggiornamento stato spedizione: " + e.getMessage());
                e.printStackTrace();
              }
              return;
            case "Ritirato":
              try {
                if (ritiroDAO.aggiornaStatoRitiro(idAnnuncio, true)) {
                  view.mostraMessaggio("Ritiro aggiornato a 'ritirato'.");
                  caricaDati();
                }
                else {
                  view.mostraErrore("Impossibile aggiornare lo stato del ritiro.");
                }
              } catch (DatabaseException e) {
                view.mostraErrore("Errore nell'aggiornamento stato ritiro: " + e.getMessage());
                e.printStackTrace();
              }
              return;
            case "Mostra immagine":
              mostraImmagineProposta(proposta);
              break; // continua ciclo
            case "Chiudi":
            default:
              return;
          }
        } else {
          return; // Utente ha chiuso la finestra
        }
      }
    }
    if (!proposta.inattesa()) {
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
      return;
    }

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
    boolean mostraImmagine = isPropostaScambio(proposta);
    String dettaglio = buildDettaglioProposta(proposta, "A");

    if (proposta.accettata()) {
      while (true) {
        boolean deliveryDetailsExist = false;
        if (spedizioneDAO != null && ritiroDAO != null) {
          try {
            deliveryDetailsExist = spedizioneDAO.esistePerAnnuncio(proposta.idAnnuncio())
                    || ritiroDAO.esistePerAnnuncio(proposta.idAnnuncio());
          } catch (DatabaseException e) {
            view.mostraErrore("Errore nel controllo stato consegna: " + e.getMessage());
            e.printStackTrace();
          }
        }

        List<String> opzioniList = new ArrayList<>();
        if ("Concluso".equals(formatStato(proposta))) {
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
              break; // continua ciclo
            case "Mostra immagine":
              mostraImmagineProposta(proposta);
              break; // continua ciclo
            case "Chiudi":
            default:
              return;
          }
        } else {
          return; // Utente ha chiuso la finestra
        }
      }
    }
    if (!proposta.inattesa()) {
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
      return;
    }

    // Quando la proposta è in attesa, consenti solo la visualizzazione dell'immagine o la chiusura del dialogo.
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
    if (!"Concluso".equals(formatStato(proposta))) {
      view.mostraErrore("Puoi lasciare una recensione solo per proposte concluse.");
      return;
    }

    apriScriviRecensione(proposta.utenteCoinvolto());
  }

  /**
   * Aggiorna stato proposta e aggiorna data in caso di successo.
   *
   * @param proposta riepilogo proposta
   * @param usernameProponente proponente username
   * @param accettata accettata flag
   * @param inattesa in attesa flag
   * @param messaggioOk successo messaggio
   */
  private boolean aggiornaEsitoProposta(
          PropostaRiepilogo proposta, String usernameProponente, boolean accettata, boolean inattesa,
          String messaggioOk) {
    if (propostaDAO == null) {
      view.mostraErrore("Connessione per le proposte non disponibile.");
      return false;
    }
    try {
      boolean ok = propostaDAO.aggiornaEsitoProposta(
              proposta.idAnnuncio(),
              proposta.tipoAnnuncio(),
              usernameProponente,
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
      e.printStackTrace();
      return false;
    }
  }

  /**
   * Elimina proposta e aggiorna data in caso di successo.
   *
   * @param proposta riepilogo proposta
   * @param usernameProponente proponente username
   * @param messaggioOk successo messaggio
   */
  private void eliminaProposta(PropostaRiepilogo proposta, String usernameProponente, String messaggioOk) {
    if (propostaDAO == null) {
      view.mostraErrore("Connessione per le proposte non disponibile.");
      return;
    }
    try {
      boolean ok = propostaDAO.eliminaProposta(
              proposta.idAnnuncio(),
              proposta.tipoAnnuncio(),
              usernameProponente);
      if (ok) {
        view.mostraMessaggio(messaggioOk);
        caricaDati();
      } else {
        view.mostraErrore("Operazione non riuscita sulla proposta.");
      }
    } catch (DatabaseException e) {
      view.mostraErrore("Errore durante l'operazione sulla proposta: " + e.getMessage());
      e.printStackTrace();
    }
  }

  /**
   * Gestisce inserimento dati consegna per proposta accettata.
   *
   * @param proposta riepilogo proposta
   */
  private void inserisciDettagliConsegna(PropostaRiepilogo proposta) {
    if (proposta == null) {
      return;
    }

    if (spedizioneDAO == null || ritiroDAO == null) {
      view.mostraErrore("Connessione ai servizi di spedizione/ritiro non disponibile.");
      return;
    }

    try {
      if (spedizioneDAO.esistePerAnnuncio(proposta.idAnnuncio())) {
        model.Spedizione spedizione = spedizioneDAO.getSpedizioneByAnnuncio(proposta.idAnnuncio());
        if (spedizione != null) {
          mostraDettagliSpedizione(spedizione);
          return;
        }
      }

      if (ritiroDAO.esistePerAnnuncio(proposta.idAnnuncio())) {
        model.Ritiro ritiro = ritiroDAO.getRitiroByAnnuncio(proposta.idAnnuncio());
        if (ritiro != null) {
          mostraDettagliRitiro(ritiro);
          return;
        }
      }
    } catch (DatabaseException e) {
      view.mostraErrore("Errore durante la verifica consegna: " + e.getMessage());
      e.printStackTrace();
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
      salvaSpedizione(proposta.idAnnuncio());
    } else if (scelta == 1) {
      salvaRitiro(proposta.idAnnuncio());
    }
  }

  /**
   * Richiede dati spedizione e salva nel database.
   *
   * @param spedizioneDAO dao spedizione
   * @param idAnnuncio id annuncio
   */
  private void salvaSpedizione(int idAnnuncio) {
    JTextField indirizzoField = new JTextField(24);
    JTextField telefonoField = new JTextField(12);
    JSpinner dataInvioSpinner = createDateSpinner();
    JSpinner dataArrivoSpinner = createDateSpinner();

    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = createFormConstraints();
    addFormRow(panel, gbc, 0, "Indirizzo:", indirizzoField);
    addFormRow(panel, gbc, 1, "Numero telefono:", telefonoField);
    addFormRow(panel, gbc, 2, "Data invio:", dataInvioSpinner);
    addFormRow(panel, gbc, 3, "Data arrivo:", dataArrivoSpinner);

    while (true) {
      int result = JOptionPane.showConfirmDialog(
              view,
              panel,
              "Dettagli spedizione",
              JOptionPane.OK_CANCEL_OPTION,
              JOptionPane.PLAIN_MESSAGE);
      if (result != JOptionPane.OK_OPTION) {
        return;
      }

      String indirizzo = indirizzoField.getText().trim();
      String telefono = telefonoField.getText().trim();
      Date dataInvio = (Date) dataInvioSpinner.getValue();
      Date dataArrivo = (Date) dataArrivoSpinner.getValue();

      if (indirizzo.isEmpty()) {
        view.mostraErrore("Indirizzo obbligatorio.");
        continue;
      }
      if (!DataCheck.isValidPhoneNumber(telefono)) {
        view.mostraErrore("Numero di telefono non valido (richieste 10 cifre).");
        continue;
      }
      if (dataInvio == null || dataArrivo == null) {
        view.mostraErrore("Inserisci date valide.");
        continue;
      }
      if (dataArrivo.before(dataInvio)) {
        view.mostraErrore("La data di arrivo non puo essere precedente alla data di invio.");
        continue;
      }

      try {
        boolean ok = spedizioneDAO.inserisciSpedizione(
                new java.sql.Date(dataInvio.getTime()),
                new java.sql.Date(dataArrivo.getTime()),
                indirizzo,
                telefono,
                idAnnuncio,
                utenteTarget.getIdUtente());
        if (ok) {
          view.mostraMessaggio("Dettagli spedizione salvati.");
          caricaDati();
        } else {
          view.mostraErrore("Salvataggio spedizione non riuscito.");
        }
      } catch (DatabaseException e) {
        view.mostraErrore("Errore durante il salvataggio della spedizione: " + e.getMessage());
        e.printStackTrace();
      }
      return;
    }
  }

  /**
   * Richiede dati ritiro e salva nel database.
   *
   * @param ritiroDAO dao ritiro
   * @param idAnnuncio id annuncio
   */
  private void salvaRitiro(int idAnnuncio) {
    JTextField sedeField = new JTextField(20);
    JTextField telefonoField = new JTextField(12);
    JSpinner dataSpinner = createDateSpinner();
    JSpinner orarioSpinner = createTimeSpinner();

    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = createFormConstraints();
    addFormRow(panel, gbc, 0, "Sede:", sedeField);
    addFormRow(panel, gbc, 1, "Numero telefono:", telefonoField);
    addFormRow(panel, gbc, 2, "Data:", dataSpinner);
    addFormRow(panel, gbc, 3, "Orario:", orarioSpinner);

    while (true) {
      int result = JOptionPane.showConfirmDialog(
              view,
              panel,
              "Dettagli ritiro",
              JOptionPane.OK_CANCEL_OPTION,
              JOptionPane.PLAIN_MESSAGE);
      if (result != JOptionPane.OK_OPTION) {
        return;
      }

      String sede = sedeField.getText().trim();
      String telefono = telefonoField.getText().trim();
      Date data = (Date) dataSpinner.getValue();
      Date orario = (Date) orarioSpinner.getValue();

      if (sede.isEmpty()) {
        view.mostraErrore("Sede obbligatoria.");
        continue;
      }
      if (!DataCheck.isValidPhoneNumber(telefono)) {
        view.mostraErrore("Numero di telefono non valido (richieste 10 cifre).");
        continue;
      }
      if (data == null || orario == null) {
        view.mostraErrore("Inserisci data e orario validi.");
        continue;
      }

      try {
        boolean ok = ritiroDAO.inserisciRitiro(
                sede,
                new java.sql.Time(orario.getTime()),
                new java.sql.Date(data.getTime()),
                telefono,
                idAnnuncio);
        if (ok) {
          view.mostraMessaggio("Dettagli ritiro salvati.");
          caricaDati();
        } else {
          view.mostraErrore("Salvataggio ritiro non riuscito.");
        }
      } catch (DatabaseException e) {
        view.mostraErrore("Errore durante il salvataggio del ritiro: " + e.getMessage());
        e.printStackTrace();
      }
      return;
    }
  }

  /**
   * Crea spinner data con formato yyyy-MM-dd.
   *
   * @return spinner data
   */
  private JSpinner createDateSpinner() {
    JSpinner spinner = new JSpinner(new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH));
    spinner.setEditor(new JSpinner.DateEditor(spinner, "yyyy-MM-dd"));
    return spinner;
  }

  /**
   * Crea spinner ora con formato HH:mm.
   *
   * @return spinner ora
   */
  private JSpinner createTimeSpinner() {
    JSpinner spinner = new JSpinner(new SpinnerDateModel(new Date(), null, null, Calendar.MINUTE));
    spinner.setEditor(new JSpinner.DateEditor(spinner, "HH:mm"));
    return spinner;
  }

  /**
   * Prepara vincoli base per form a griglia.
   *
   * @return vincoli griglia
   */
  private GridBagConstraints createFormConstraints() {
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(4, 4, 4, 4);
    gbc.anchor = GridBagConstraints.WEST;
    return gbc;
  }

  /**
   * Aggiunge una riga al form con etichetta e campo.
   *
   * @param panel pannello destinazione
   * @param gbc vincoli griglia
   * @param row indice riga
   * @param label testo etichetta
   * @param field componente input
   */
  private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent field) {
    gbc.gridx = 0;
    gbc.gridy = row;
    gbc.weightx = 0;
    gbc.fill = GridBagConstraints.NONE;
    panel.add(new JLabel(label), gbc);

    gbc.gridx = 1;
    gbc.weightx = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    panel.add(field, gbc);
  }

  /**
   * Costruisce dettaglio stringa per proposta riga.
   *
   * @param proposta riepilogo proposta
   * @param labelUtente etichetta prefisso
   * @return dettaglio stringa
   */
  private String buildDettaglioProposta(PropostaRiepilogo proposta, String labelUtente) {
    return labelUtente + ": " + proposta.utenteCoinvolto()
            + "\nAnnuncio: " + proposta.titoloAnnuncio()
            + "\nTipo: " + proposta.tipoAnnuncio()
            + "\nDettaglio: " + proposta.dettaglio()
            + "\nStato: " + formatStato(proposta);
  }

  /**
   * Determina se la proposta e di tipo scambio.
   *
   * @param proposta proposta da verificare
   * @return true se e scambio
   */
  private boolean isPropostaScambio(PropostaRiepilogo proposta) {
    if (proposta == null || proposta.tipoAnnuncio() == null) {
      return false;
    }
    return proposta.tipoAnnuncio().trim().toUpperCase().contains("SCAMBIO");
  }

  /**
   * Verifica presenza immagine nella proposta.
   *
   * @param proposta proposta da verificare
   * @return true se immagine disponibile
   */
  private boolean hasImmagineProposta(PropostaRiepilogo proposta) {
    return proposta != null && proposta.immagine() != null && proposta.immagine().length > 0;
  }

  /**
   * Mostra immagine della proposta se disponibile.
   *
   * @param proposta proposta selezionata
   */
  private void mostraImmagineProposta(PropostaRiepilogo proposta) {
    if (!hasImmagineProposta(proposta)) {
      view.mostraErrore("Immagine non disponibile.");
      return;
    }

    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    int maxWidth = (int) Math.round(screen.width * 0.6);
    int maxHeight = (int) Math.round(screen.height * 0.6);
    ImageIcon icon = creaIconaProposta(proposta.immagine(), maxWidth, maxHeight);
    if (icon == null) {
      view.mostraErrore("Immagine non disponibile.");
      return;
    }

    JLabel label = new JLabel(icon);
    label.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
    JOptionPane.showMessageDialog(view, label, "Immagine proposta", JOptionPane.PLAIN_MESSAGE);
  }

  /**
   * Crea icona ridimensionata da bytes immagine.
   *
   * @param bytes dati immagine
   * @param maxWidth larghezza massima
   * @param maxHeight altezza massima
   * @return icona ridimensionata o null
   */
  private ImageIcon creaIconaProposta(byte[] bytes, int maxWidth, int maxHeight) {
    if (bytes == null || bytes.length == 0) {
      return null;
    }
    ImageIcon icon = new ImageIcon(bytes);
    int width = icon.getIconWidth();
    int height = icon.getIconHeight();
    if (width <= 0 || height <= 0) {
      return icon;
    }
    double scale = Math.min(1.0, Math.min((double) maxWidth / width, (double) maxHeight / height));
    if (scale == 1.0) {
      return icon;
    }
    int targetW = Math.max(1, (int) Math.round(width * scale));
    int targetH = Math.max(1, (int) Math.round(height * scale));
    Image img = icon.getImage().getScaledInstance(targetW, targetH, Image.SCALE_SMOOTH);
    return new ImageIcon(img);
  }

  /**
   * Apre form recensione per utente indicato.
   *
   * @param usernameDestinatario username destinatario
   */
  private void apriScriviRecensione(String usernameDestinatario) {
    if (usernameDestinatario == null || usernameDestinatario.trim().isEmpty()) {
      view.mostraErrore("Utente destinatario non valido.");
      return;
    }

    String username = usernameDestinatario.trim();
    Utente utenteDestinatario;
    try {
      UtenteDAO utenteDAO = new UtenteDAO();
      utenteDestinatario = utenteDAO.getUserByUsername(username);
    } catch (DatabaseException e) {
      view.mostraErrore("Errore durante il recupero dell'utente: " + e.getMessage());
      e.printStackTrace();
      return;
    }

    if (utenteDestinatario == null) {
      view.mostraErrore("Utente destinatario non trovato.");
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

    if (proposta.tipoAnnuncio().toUpperCase().contains("REGALO")) {
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
      eliminaProposta(proposta, utenteTarget.getUsername(), "Proposta annullata con successo.");
    }
  }

  /**
   * Visualizza i dettagli della consegna per una proposta (sola lettura).
   *
   * @param proposta la proposta per cui visualizzare i dettagli.
   */
  private void visualizzaDettagliConsegna(PropostaRiepilogo proposta) {
    if (spedizioneDAO == null || ritiroDAO == null) {
      view.mostraErrore("Connessione ai servizi di spedizione/ritiro non disponibile.");
      return;
    }
    try {
      if (spedizioneDAO.esistePerAnnuncio(proposta.idAnnuncio())) {
        model.Spedizione spedizione = spedizioneDAO.getSpedizioneByAnnuncio(proposta.idAnnuncio());
        if (spedizione != null) {
          mostraDettagliSpedizione(spedizione);
        }
      } else if (ritiroDAO.esistePerAnnuncio(proposta.idAnnuncio())) {
        model.Ritiro ritiro = ritiroDAO.getRitiroByAnnuncio(proposta.idAnnuncio());
        if (ritiro != null) {
          mostraDettagliRitiro(ritiro);
        }
      } else {
        view.mostraMessaggio("Dettagli di consegna non ancora forniti dall'acquirente.");
      }
    } catch (DatabaseException e) {
      view.mostraErrore("Errore durante la visualizzazione dei dettagli di consegna: " + e.getMessage());
      e.printStackTrace();
    }
  }

  /**
   * Mostra dettagli di una spedizione in una finestra di dialogo.
   * @param spedizione l'oggetto Spedizione da visualizzare.
   */
  private void mostraDettagliSpedizione(model.Spedizione spedizione) {
    StringBuilder message = new StringBuilder("Dettagli Spedizione:\n");
    if (spedizione.getIdSpedizione() != 0) {
      message.append("ID Spedizione: ").append(spedizione.getIdSpedizione()).append("\n");
    }
    message.append("Indirizzo: ").append(spedizione.getIndirizzo()).append("\n");
    message.append("Numero di Telefono: ").append(spedizione.getNumeroTelefono()).append("\n");
    message.append("Data Invio: ").append(spedizione.getDataInvio().toString()).append("\n");
    message.append("Data Arrivo: ").append(spedizione.getDataArrivo().toString()).append("\n");
    message.append("Spedito: ").append(spedizione.isSpedito() ? "Si" : "No");

    JOptionPane.showMessageDialog(view, message.toString(), "Dettagli Spedizione", JOptionPane.INFORMATION_MESSAGE);
  }

  /**
   * Mostra dettagli di un ritiro in una finestra di dialogo.
   * @param ritiro l'oggetto Ritiro da visualizzare.
   */
  private void mostraDettagliRitiro(model.Ritiro ritiro) {
    String message = String.format(
            "Dettagli Ritiro:\n" +
            "ID Ritiro: %d\n" +
            "Sede: %s\n" +
            "Orario: %s\n" +
            "Data: %s\n" +
            "Numero di Telefono: %s\n" +
            "Ritirato: %s",
            ritiro.getIdRitiro(),
            ritiro.getSede(),
            ritiro.getOrario(),
            ritiro.getData().toString(),
            ritiro.getNumeroTelefono(),
            ritiro.isRitirato() ? "Si" : "No"
    );
    JOptionPane.showMessageDialog(view, message, "Dettagli Ritiro", JOptionPane.INFORMATION_MESSAGE);
  }
}
