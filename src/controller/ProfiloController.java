package controller;

import dao.AnnuncioDAO;
import dao.PropostaDAO;
import dao.RecensioneDAO;
import dao.UtenteDAO;
import model.Recensione;
import model.Annuncio;
import model.PropostaRiepilogo;
import model.Utente;
import utils.SessionManager;
import exception.DatabaseException;
import gui.DettaglioAnnuncio;
import gui.Profilo;
import gui.ScriviRecensione;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.List;

public class ProfiloController {

  private final Profilo view;
  private final RecensioneDAO recensioneDAO;
  private final AnnuncioDAO annuncioDAO;
  private final PropostaDAO propostaDAO;
  private final Utente utenteTarget;
  private boolean mostraDatiSensibili = false;

  private List<Annuncio> listaAnnunci;
  private List<PropostaRiepilogo> proposteRicevute;
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
      return null;
    }
  }

  /**
   * Registra tabella listener per profilo interazioni.
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
            dettaglioFrame.setVisible(true);
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
                  formatStato(proposta.accettata(), proposta.inattesa())
          );
        }

        for (PropostaRiepilogo proposta : proposteInviate) {
          view.aggiungiPropostaInviata(
                  proposta.utenteCoinvolto(),
                  proposta.titoloAnnuncio(),
                  proposta.tipoAnnuncio(),
                  proposta.dettaglio(),
                  formatStato(proposta.accettata(), proposta.inattesa())
          );
        }
      }

    } catch (DatabaseException e) {
      view.mostraErrore("Errore nel caricamento dati: " + e.getMessage());
      e.printStackTrace();
    }
  }

  /**
   * Restituisce localizzato etichetta stato per proposta.
   *
   * @param accettata accettata flag
   * @param inattesa in attesa flag
   * @return etichetta stato
   */
  private String formatStato(boolean accettata, boolean inattesa) {
    if (accettata) {
      return "Accettata";
    }
    if (inattesa) {
      return "In attesa";
    }
    return "Rifiutato";
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
    String dettaglio = buildDettaglioProposta(proposta, "Da");

    if (proposta.accettata()) {
      Object[] opzioni = {"Lascia recensione", "Chiudi"};
      int scelta = JOptionPane.showOptionDialog(
              view,
              dettaglio + "\n\nQuesta proposta e stata accettata.",
              "Proposta ricevuta",
              JOptionPane.DEFAULT_OPTION,
              JOptionPane.INFORMATION_MESSAGE,
              null,
              opzioni,
              opzioni[0]);
      if (scelta == 0) {
        apriScriviRecensione(proposta.utenteCoinvolto());
      }
      return;
    }
    if (!proposta.inattesa()) {
      JOptionPane.showMessageDialog(
              view,
              dettaglio + "\n\nQuesta proposta e stata rifiutata.",
              "Proposta ricevuta",
              JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    Object[] opzioni = {"Accetta", "Rifiuta", "Chiudi"};
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
      aggiornaEsitoProposta(proposta, proposta.utenteCoinvolto(), true, false,
              "Proposta accettata con successo.");
    } else if (scelta == 1) {
      aggiornaEsitoProposta(proposta, proposta.utenteCoinvolto(), false, false, "Proposta rifiutata.");
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
    String dettaglio = buildDettaglioProposta(proposta, "A");

    if (proposta.accettata()) {
      Object[] opzioni = {"Lascia recensione", "Chiudi"};
      int scelta = JOptionPane.showOptionDialog(
              view,
              dettaglio + "\n\nLa proposta e stata accettata.",
              "Proposta inviata",
              JOptionPane.DEFAULT_OPTION,
              JOptionPane.INFORMATION_MESSAGE,
              null,
              opzioni,
              opzioni[0]);
      if (scelta == 0) {
        apriScriviRecensione(proposta.utenteCoinvolto());
      }
      return;
    }
    if (!proposta.inattesa()) {
      JOptionPane.showMessageDialog(
              view,
              dettaglio + "\n\nLa proposta e stata rifiutata.",
              "Proposta inviata",
              JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    Object[] opzioni = {"Annulla proposta", "Chiudi"};
    int scelta = JOptionPane.showOptionDialog(
            view,
            dettaglio,
            "Proposta inviata",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            opzioni,
            opzioni[1]);

    if (scelta == 0) {
      eliminaProposta(proposta, utenteTarget.getUsername(), "Proposta annullata.");
    }
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
  private void aggiornaEsitoProposta(
          PropostaRiepilogo proposta, String usernameProponente, boolean accettata, boolean inattesa,
          String messaggioOk) {
    if (propostaDAO == null) {
      view.mostraErrore("Connessione per le proposte non disponibile.");
      return;
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
    } catch (DatabaseException e) {
      view.mostraErrore("Errore durante l'aggiornamento della proposta: " + e.getMessage());
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
    }
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
            + "\nStato: " + formatStato(proposta.accettata(), proposta.inattesa());
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
      return;
    }

    if (utenteDestinatario == null) {
      view.mostraErrore("Utente destinatario non trovato.");
      return;
    }

    ScriviRecensione recensioneView = new ScriviRecensione();
    new ScriviRecensioneController(recensioneView, utenteDestinatario.getIdUtente());
    recensioneView.setVisible(true);
  }
}
