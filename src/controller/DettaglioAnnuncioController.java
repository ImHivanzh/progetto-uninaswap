package controller;

import dao.ImmaginiDAO;
import dao.UtenteDAO;
import exception.DatabaseException;
import gui.DettaglioAnnuncio;
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
      this.view.setUtenteInfo("Caricamento info utente...");
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
    if(annuncio instanceof Vendita) {
      JOptionPane.showMessageDialog(view,
              "Contatta il venditore tramite il sistema di messaggistica interna.",
              "Contatta Venditore",
              JOptionPane.INFORMATION_MESSAGE);
    } else if(annuncio instanceof Scambio) {
      JOptionPane.showMessageDialog(view,
              "Proponi uno scambio al proprietario tramite il sistema di messaggistica interna.",
              "Proponi Scambio",
              JOptionPane.INFORMATION_MESSAGE);
    } else if(annuncio instanceof Regalo) {
      JOptionPane.showMessageDialog(view,
              "Contatta il donatore tramite il sistema di messaggistica interna.",
              "Contatta Donatore",
              JOptionPane.INFORMATION_MESSAGE);
    }
  }

  private void aggiornaVistaImmagine() {
    // [CORRETTO] Se non ci sono immagini, nascondiamo il pannello usando il metodo esistente
    if (listaImmagini == null || listaImmagini.isEmpty()) {
      view.nascondiPannelloImmagini();
      return;
    }

    // Se ci sono immagini, assicuriamoci che il pannello sia visibile
    view.mostraPannelloImmagini();

    Immagini imgObj = listaImmagini.get(currentImageIndex);

    if (imgObj.getImmagine() != null && imgObj.getImmagine().length > 0) {
      // Crea l'ImageIcon dai byte
      ImageIcon icon = new ImageIcon(imgObj.getImmagine());
      view.setImmagine(icon);
      view.setContatoreImmagini((currentImageIndex + 1) + "/" + listaImmagini.size());
    } else {
      view.setImmagineTesto("Errore img");
    }
  }
}