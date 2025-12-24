package controller;

import dao.ImmaginiDAO;
import dao.PropostaDAO;
import dao.UtenteDAO;
import exception.DatabaseException;
import gui.DettaglioAnnuncio;
import gui.FaiPropostaDialog;
import gui.Profilo;
import model.*;
import model.enums.TipoAnnuncio;
import utils.SessionManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class DettaglioAnnuncioController {

  private final DettaglioAnnuncio view;
  private final Annuncio annuncio;
  private final ImmaginiDAO immaginiDAO;
  private List<Immagini> listaImmagini;
  private int currentImageIndex = 0;
  private Utente pubblicatore;

  /**
   * Crea controller per vista dettaglio annuncio.
   *
   * @param view dettaglio vista
   * @param annuncio annuncio a visualizza
   */
  public DettaglioAnnuncioController(DettaglioAnnuncio view, Annuncio annuncio) {
    this.view = view;
    this.annuncio = annuncio;
    this.immaginiDAO = new ImmaginiDAO();
    this.listaImmagini = new ArrayList<>();

    caricaDati();
  }

  /**
   * Carica dati annuncio e popola vista.
   */
  private void caricaDati() {
    try {
      this.listaImmagini = immaginiDAO.getImmaginiByAnnuncio(annuncio.getIdAnnuncio());
    } catch (Exception e) {
      System.err.println("Errore caricamento immagini: " + e.getMessage());
    }

    view.setTitolo(annuncio.getTitolo());
    view.setDescrizione(annuncio.getDescrizione());
    view.setCategoria("Categoria: " + annuncio.getCategoria());
    view.setTipo("Tipo: " + annuncio.getTipoAnnuncio().toString());
    view.setCondizione("Condizioni: Buone");

    String prezzoTesto;
    Color prezzoColore;

    if (annuncio instanceof Vendita) {
      prezzoTesto = String.format("€ %.2f", ((Vendita) annuncio).getPrezzo());
      prezzoColore = new Color(34, 139, 34);
    } else if (annuncio instanceof Scambio) {
      prezzoTesto = "Scambia con: " + ((Scambio) annuncio).getOggettoRichiesto();
      prezzoColore = new Color(255, 140, 0);
    } else if (annuncio instanceof Regalo) {
      prezzoTesto = "IN REGALO";
      prezzoColore = new Color(220, 20, 60);
    } else {
      if (annuncio.getTipoAnnuncio() == TipoAnnuncio.VENDITA) {
        prezzoTesto = "Prezzo non disponibile";
      } else {
        prezzoTesto = annuncio.getTipoAnnuncio().toString();
      }
      prezzoColore = Color.BLACK;
    }
    view.setPrezzoInfo(prezzoTesto, prezzoColore);

    UtenteDAO utenteDAO = new UtenteDAO();
    try {
      pubblicatore = utenteDAO.getUserByID(annuncio.getIdUtente());
      if (pubblicatore != null) {
        view.setUtenteNome(pubblicatore.getUsername(), true);
        view.addUtenteListener(new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent e) {
            apriProfiloPubblicatore();
          }
        });
      } else {
        view.setUtenteNome("Utente ID " + annuncio.getIdUtente(), false);
      }
    } catch (DatabaseException e) {
      System.err.println("Errore caricamento info utente: " + e.getMessage());
      view.setUtenteNome("Utente ID " + annuncio.getIdUtente(), false);
    }

    aggiornaVistaImmagine();
  }

  /**
   * Avanza a successiva immagine in carosello.
   */
  public void azioneSuccessiva() {
    if (listaImmagini != null && !listaImmagini.isEmpty()) {
      currentImageIndex = (currentImageIndex + 1) % listaImmagini.size();
      aggiornaVistaImmagine();
    }
  }

  /**
   * Sposta a precedente immagine in carosello.
   */
  public void azionePrecedente() {
    if (listaImmagini != null && !listaImmagini.isEmpty()) {
      currentImageIndex = (currentImageIndex - 1 + listaImmagini.size()) % listaImmagini.size();
      aggiornaVistaImmagine();
    }
  }

  /**
   * Apre dialogo proposta e salva proposta se confermata.
   */
  public void azioneContatta() {
    FaiPropostaDialog dialog = new FaiPropostaDialog(view, annuncio.getTipoAnnuncio());
    dialog.setVisible(true);

    FaiPropostaController dialogController = dialog.getController();

    if (dialogController != null && dialogController.isConfermato()) {
      Double prezzo = dialogController.getOffertaPrezzo();
      String descrizione = dialogController.getDescrizioneProposta();
      byte[] immagine = dialogController.getImmagineProposta();

      salvaProposta(annuncio.getIdAnnuncio(), prezzo, descrizione, immagine);
    }
  }

  /**
   * Salva proposta per corrente annuncio.
   *
   * @param idAnnuncio id annuncio
   * @param prezzo proposto prezzo
   * @param descrizione descrizione proposta
   * @param immagine proposta immagine
   */
  private void salvaProposta(int idAnnuncio, Double prezzo, String descrizione, byte[] immagine) {
    Utente utenteCorrente = SessionManager.getInstance().getUtente();

    if (utenteCorrente == null) {
      JOptionPane.showMessageDialog(view,
              "Effettua il login prima di inviare una proposta.",
              "Accesso richiesto",
              JOptionPane.WARNING_MESSAGE);
      return;
    }

    if (utenteCorrente.getIdUtente() == annuncio.getIdUtente()) {
      JOptionPane.showMessageDialog(view,
              "Non puoi fare una proposta al tuo stesso annuncio!",
              "Operazione non consentita",
              JOptionPane.WARNING_MESSAGE);
      return;
    }

    try {
      PropostaDAO propostaDAO = new PropostaDAO();
      boolean successo = false;
      int idUtente = utenteCorrente.getIdUtente();

      switch (annuncio.getTipoAnnuncio()) {
        case VENDITA:
          if (prezzo == null) {
            JOptionPane.showMessageDialog(view,
                    "Inserisci un'offerta economica valida prima di procedere.",
                    "Dati mancanti",
                    JOptionPane.WARNING_MESSAGE);
            return;
          }
          successo = propostaDAO.inserisciPropostaVendita(idUtente, idAnnuncio, prezzo);
          break;
        case SCAMBIO:
          String propostaScambio = descrizione != null ? descrizione.trim() : "";
          successo = propostaDAO.inserisciPropostaScambio(idUtente, idAnnuncio, propostaScambio, immagine);
          break;
        case REGALO:
          successo = propostaDAO.inserisciPropostaRegalo(idUtente, idAnnuncio);
          break;
      }

      if (successo) {
        JOptionPane.showMessageDialog(view,
                "Proposta inviata con successo!",
                "Conferma",
                JOptionPane.INFORMATION_MESSAGE);
      } else {
        JOptionPane.showMessageDialog(view,
                "Impossibile inviare la proposta al momento.",
                "Errore",
                JOptionPane.ERROR_MESSAGE);
      }
    } catch (DatabaseException e) {
      JOptionPane.showMessageDialog(view,
              "Errore durante il salvataggio della proposta: " + e.getMessage(),
              "Errore database",
              JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * Aggiorna vista immagine basato in indice corrente.
   */
  private void aggiornaVistaImmagine() {
    if (listaImmagini == null || listaImmagini.isEmpty()) {
      view.nascondiPannelloImmagini();
      return;
    }

    view.mostraPannelloImmagini();

    Immagini imgObj = listaImmagini.get(currentImageIndex);

    if (imgObj.getImmagine() != null && imgObj.getImmagine().length > 0) {
      ImageIcon icon = new ImageIcon(imgObj.getImmagine());
      view.setImmagine(icon);
      view.setContatoreImmagini((currentImageIndex + 1) + "/" + listaImmagini.size());
    } else {
      view.setImmagineTesto("Errore img");
    }
  }

  /**
   * Apre pubblicatore vista profilo.
   */
  private void apriProfiloPubblicatore() {
    if (pubblicatore == null) {
      return;
    }
    Profilo profilo = new Profilo();
    new ProfiloController(profilo, pubblicatore);
    profilo.setVisible(true);
  }
}
