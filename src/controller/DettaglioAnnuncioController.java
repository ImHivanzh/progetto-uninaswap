package controller;

import dao.UtenteDAO;
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
  private List<Immagini> listaImmagini;
  private int currentImageIndex = 0;

  public DettaglioAnnuncioController(DettaglioAnnuncio view, Annuncio annuncio) {
    this.view = view;
    this.annuncio = annuncio;
    this.listaImmagini = new ArrayList<>();

    // TODO: Recuperare immagini reali se disponibili
    // if (annuncio.getImmagini() != null) {
    //     this.listaImmagini = annuncio.getImmagini();
    // }

    inizializzaDati();
  }

  private void inizializzaDati() {
    // 1. Imposta dati testuali base
    view.setTitolo(annuncio.getTitolo());
    view.setDescrizione(annuncio.getDescrizione());
    view.setCategoria("Categoria: " + annuncio.getCategoria());
    view.setTipo("Tipo: " + annuncio.getTipoAnnuncio().toString());
    view.setCondizione("Condizioni: " + "Buone"); // Se hai un campo condizioni in Annuncio, usalo qui

    // 2. Gestione logica Prezzo/Scambio/Regalo
    String prezzoTesto;
    Color prezzoColore;

    if (annuncio instanceof Vendita) {
      prezzoTesto = String.format("€ %.2f", ((Vendita) annuncio).getPrezzo());
      prezzoColore = new Color(34, 139, 34); // Verde soldi
    } else if (annuncio instanceof Scambio) {
      prezzoTesto = "Scambia con: " + ((Scambio) annuncio).getOggettoRichiesto();
      prezzoColore = new Color(255, 140, 0); // Arancione
    } else if (annuncio instanceof Regalo) {
      prezzoTesto = "IN REGALO";
      prezzoColore = new Color(220, 20, 60); // Rosso/Fucsia
    } else {
      if (annuncio.getTipoAnnuncio() == TipoAnnuncio.VENDITA) {
        prezzoTesto = "Prezzo non disponibile";
      } else {
        prezzoTesto = annuncio.getTipoAnnuncio().toString();
      }
      prezzoColore = Color.BLACK;
    }
    view.setPrezzoInfo(prezzoTesto, prezzoColore);

    // 3. Recupero Utente (Logica DAO)
    UtenteDAO utenteDAO = new UtenteDAO();
    try {
      // Logica di recupero nome utente
      // Esempio: Utente u = utenteDAO.getUtenteById(annuncio.getUtenteID());
      // String nomePubblicatore = u.getNickname();
      String nomePubblicatore = "Utente ID " + annuncio.getUtenteID(); // Placeholder
      view.setUtenteInfo("Pubblicato da: " + nomePubblicatore);
    } catch (Exception e) {
      view.setUtenteInfo("Utente sconosciuto");
      e.printStackTrace();
    }

    // 4. Aggiorna prima immagine
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
    // Qui gestisci la logica di apertura chat o invio email
    JOptionPane.showMessageDialog(view, "Funzionalità chat in arrivo per l'annuncio: " + annuncio.getTitolo(), "Contatta Utente", JOptionPane.INFORMATION_MESSAGE);
  }

  private void aggiornaVistaImmagine() {
    if (listaImmagini == null || listaImmagini.isEmpty()) {
      view.mostraImmaginePlaceholder();
      view.setContatoreImmagini("0/0");
      return;
    }

    Immagini imgObj = listaImmagini.get(currentImageIndex);
    // Logica di conversione byte[] -> ImageIcon
        /*
        byte[] imgBytes = imgObj.getDati();
        ImageIcon icon = new ImageIcon(imgBytes);
        view.setImmagine(icon);
        */

    // Placeholder per ora
    view.setImmagineTesto("Immagine " + (currentImageIndex + 1));
    view.setContatoreImmagini((currentImageIndex + 1) + "/" + listaImmagini.size());
  }
}