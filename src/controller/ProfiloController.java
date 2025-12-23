package controller;

import dao.AnnuncioDAO;
import dao.PropostaDAO;
import dao.RecensioneDAO;
import model.Recensione;
import model.Annuncio;
import model.PropostaRiepilogo;
import model.Utente;
import utils.SessionManager;
import exception.DatabaseException;
import gui.DettaglioAnnuncio;
import gui.Profilo;

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
   * Creates a controller for the current user's profile.
   *
   * @param view profile view
   */
  public ProfiloController(Profilo view) {
    this(view, null);
  }

  /**
   * Creates a controller for a specific user profile.
   *
   * @param view profile view
   * @param utenteTarget profile owner
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
    }

    caricaDati();
    setupInteraction();
  }

  /**
   * Creates a proposals DAO, showing an error on failure.
   *
   * @return DAO instance or null
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
   * Registers table listeners for profile interactions.
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
   * Loads profile data and populates the view.
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

      if (propostaDAO != null) {
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
   * Returns the localized status label for a proposal.
   *
   * @param accettata accepted flag
   * @param inattesa pending flag
   * @return status label
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
   * Handles a double-click on a received proposal.
   *
   * @param selectedRow row index
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
      JOptionPane.showMessageDialog(
              view,
              dettaglio + "\n\nQuesta proposta è gia stata accettata.",
              "Proposta ricevuta",
              JOptionPane.INFORMATION_MESSAGE);
      return;
    }
    if (!proposta.inattesa()) {
      JOptionPane.showMessageDialog(
              view,
              dettaglio + "\n\nQuesta proposta è stata rifiutata.",
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
   * Handles a double-click on a sent proposal.
   *
   * @param selectedRow row index
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
      JOptionPane.showMessageDialog(
              view,
              dettaglio + "\n\nLa proposta e stata accettata e non puo essere annullata.",
              "Proposta inviata",
              JOptionPane.INFORMATION_MESSAGE);
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
   * Updates the proposal status and refreshes data on success.
   *
   * @param proposta proposal summary
   * @param usernameProponente proposer username
   * @param accettata accepted flag
   * @param inattesa pending flag
   * @param messaggioOk success message
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
   * Deletes a proposal and refreshes data on success.
   *
   * @param proposta proposal summary
   * @param usernameProponente proposer username
   * @param messaggioOk success message
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
   * Builds a detail string for a proposal row.
   *
   * @param proposta proposal summary
   * @param labelUtente label prefix
   * @return detail string
   */
  private String buildDettaglioProposta(PropostaRiepilogo proposta, String labelUtente) {
    return labelUtente + ": " + proposta.utenteCoinvolto()
            + "\nAnnuncio: " + proposta.titoloAnnuncio()
            + "\nTipo: " + proposta.tipoAnnuncio()
            + "\nDettaglio: " + proposta.dettaglio()
            + "\nStato: " + formatStato(proposta.accettata(), proposta.inattesa());
  }
}
