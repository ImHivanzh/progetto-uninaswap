package controller;

import dao.ImmaginiDAO;
import dao.UtenteDAO;
import exception.DatabaseException;
import gui.DettaglioAnnuncio;
import gui.FaiPropostaDialog;
import model.*;
import model.enums.TipoAnnuncio;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DettaglioAnnuncioController {

  private final DettaglioAnnuncio view;
  private final Annuncio annuncio;
  private final ImmaginiDAO immaginiDAO;
  private List<Immagini> listaImmagini;
  private int currentImageIndex = 0;

  public DettaglioAnnuncioController(DettaglioAnnuncio view, Annuncio annuncio) {
    this.view = view;
    this.annuncio = annuncio;
    this.immaginiDAO = new ImmaginiDAO();
    this.listaImmagini = new ArrayList<>();

    caricaDati();
  }

  private void caricaDati() {
    // 1. Recupero Immagini dal DB
    try {
      this.listaImmagini = immaginiDAO.getImmaginiByAnnuncio(annuncio.getIdAnnuncio());
    } catch (Exception e) {
      System.err.println("Errore caricamento immagini: " + e.getMessage());
    }

    // 2. Imposta dati testuali base
    view.setTitolo(annuncio.getTitolo());
    view.setDescrizione(annuncio.getDescrizione());
    view.setCategoria("Categoria: " + annuncio.getCategoria());
    view.setTipo("Tipo: " + annuncio.getTipoAnnuncio().toString());
    view.setCondizione("Condizioni: Buone");

    // 3. Gestione logica Prezzo/Scambio/Regalo
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

    // 4. Recupero Utente
    view.setUtenteInfo("Pubblicato da: Utente ID " + annuncio.getIdUtente());

    UtenteDAO utenteDAO = new UtenteDAO();
    try {
      Utente pubblicatore = utenteDAO.getUserByID(annuncio.getIdUtente());
      view.setUtenteInfo("Pubblicato da: " + pubblicatore.getUsername());
    } catch (DatabaseException e) {
      System.err.println("Errore caricamento info utente: " + e.getMessage());
    }

    // 5. Aggiorna vista immagini
    aggiornaVistaImmagine();
  }

  public void azioneSuccessiva() {
    if (listaImmagini != null && !listaImmagini.isEmpty()) {
      currentImageIndex = (currentImageIndex + 1) % listaImmagini.size();
      aggiornaVistaImmagine();
    }
  }

  public void azionePrecedente() {
    if (listaImmagini != null && !listaImmagini.isEmpty()) {
      currentImageIndex = (currentImageIndex - 1 + listaImmagini.size()) % listaImmagini.size();
      aggiornaVistaImmagine();
    }
  }

  public void azioneContatta() {
    // 1. Crea e mostra la Dialog (View)
    // La Dialog al suo interno inizializzerà il suo Controller
    FaiPropostaDialog dialog = new FaiPropostaDialog(view, annuncio.getTipoAnnuncio());
    dialog.setVisible(true); // Blocca l'esecuzione finché la dialog non viene chiusa

    // 2. Recupera il controller della dialog per esaminare i risultati
    FaiPropostaController dialogController = dialog.getController();

    if (dialogController != null && dialogController.isConfermato()) {
      // 3. Estrai i dati validati
      Double prezzo = dialogController.getOffertaPrezzo();
      String descrizione = dialogController.getDescrizioneProposta();
      byte[] immagine = dialogController.getImmagineProposta();

      // 4. Logica di persistenza
      salvaProposta(annuncio.getIdAnnuncio(), prezzo, descrizione, immagine);

      JOptionPane.showMessageDialog(view,
              "Proposta inviata con successo!",
              "Conferma",
              JOptionPane.INFORMATION_MESSAGE);
    }
  }

  private void salvaProposta(int idAnnuncio, Double prezzo, String descrizione, byte[] immagine) {
    // TODO: Qui va inserita la chiamata al PropostaDAO.insert(...)
    System.out.println("--- SIMULAZIONE SALVATAGGIO PROPOSTA ---");
    System.out.println("Annuncio ID: " + idAnnuncio);
    System.out.println("Tipo Annuncio: " + annuncio.getTipoAnnuncio());
    if (prezzo != null) System.out.println("Offerta Economica: " + prezzo);
    System.out.println("Descrizione/Messaggio: " + descrizione);
    System.out.println("Immagine presente: " + (immagine != null));
    System.out.println("----------------------------------------");
  }

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
}