package controller;

import dao.AnnuncioDAO;
import gui.PubblicaAnnuncio;
import model.Annuncio;
import model.Utente;
import model.Vendita;
import model.enums.Categoria;
import model.enums.TipoAnnuncio;
import utils.SessionManager;
import exception.DatabaseException;

import javax.swing.*;

public class PubblicaAnnuncioController {

  private final PubblicaAnnuncio view;
  private final AnnuncioDAO annuncioDAO;

  public PubblicaAnnuncioController(PubblicaAnnuncio view) {
    this.view = view;
    this.annuncioDAO = new AnnuncioDAO();
  }

  public void pubblica() {
    Utente utente = SessionManager.getInstance().getUtente();
    if (utente == null) {
      JOptionPane.showMessageDialog(view, "Devi essere loggato per pubblicare un annuncio!");
      return;
    }

    // 1. Recupero dati dalla View
    String titolo = view.getTitolo();
    String descrizione = view.getDescrizione();
    Categoria categoria = view.getCategoriaSelezionata();
    TipoAnnuncio tipo = view.getTipoSelezionato();
    String prezzoStr = view.getPrezzo();

    // 2. Validazione base
    if (titolo.isEmpty() || descrizione.isEmpty() || categoria == null || tipo == null) {
      JOptionPane.showMessageDialog(view, "Compila tutti i campi obbligatori.", "Errore", JOptionPane.ERROR_MESSAGE);
      return;
    }

    try {
      Annuncio nuovoAnnuncio;

      // 3. Creazione Oggetto (Gestione Polimorfismo CORRETTA)
      if (tipo == TipoAnnuncio.VENDITA) {
        // Se è vendita, creo specificamente una Vendita
        Vendita vendita = new Vendita();

        if (prezzoStr == null || prezzoStr.isEmpty()) {
          JOptionPane.showMessageDialog(view, "Inserisci un prezzo valido.", "Errore", JOptionPane.ERROR_MESSAGE);
          return;
        }

        // Parsing del prezzo
        try {
          double prezzo = Double.parseDouble(prezzoStr);
          vendita.setPrezzo(prezzo); // Qui funziona perché la variabile 'vendita' è di tipo Vendita
        } catch (NumberFormatException e) {
          JOptionPane.showMessageDialog(view, "Il prezzo deve essere un numero.", "Errore", JOptionPane.ERROR_MESSAGE);
          return;
        }

        nuovoAnnuncio = vendita; // Assegno al padre (Upcasting)
      } else {
        // Altrimenti creo un Annuncio generico (Scambio, Regalo, ecc.)
        nuovoAnnuncio = new Annuncio();
      }

      // 4. Setto i campi comuni (definiti nella classe padre Annuncio)
      nuovoAnnuncio.setIdUtente(utente.getIdUtente());
      nuovoAnnuncio.setTitolo(titolo);
      nuovoAnnuncio.setDescrizione(descrizione);
      nuovoAnnuncio.setCategoria(categoria);
      nuovoAnnuncio.setTipoAnnuncio(tipo);
      nuovoAnnuncio.setStato(true);

      // 5. Salvataggio nel DB
      boolean successo = annuncioDAO.pubblicaAnnuncio(nuovoAnnuncio);

      if (successo) {
        JOptionPane.showMessageDialog(view, "Annuncio pubblicato con successo!");
        view.dispose(); // Chiude la finestra
      } else {
        JOptionPane.showMessageDialog(view, "Errore nel salvataggio dell'annuncio.", "Errore", JOptionPane.ERROR_MESSAGE);
      }

    } catch (DatabaseException e) {
      JOptionPane.showMessageDialog(view, "Errore database: " + e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
      e.printStackTrace();
    } catch (Exception e) {
      JOptionPane.showMessageDialog(view, "Errore generico: " + e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
      e.printStackTrace();
    }
  }
}