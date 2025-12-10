package controller;

import dao.AnnuncioDAO;
import exception.DatabaseException;
import gui.PubblicaAnnuncio;
import model.*;
import model.enums.Categoria;
import model.enums.TipoAnnuncio;
import utils.SessionManager;

import javax.swing.*;
import java.io.File;
import java.util.List;

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

    // Recupero immagini (ora il metodo esiste)
    List<File> immagini = view.getImmagini();

    // 2. Validazione base
    if (titolo.isEmpty() || descrizione.isEmpty() || categoria == null || tipo == null) {
      JOptionPane.showMessageDialog(view, "Compila tutti i campi obbligatori.", "Errore", JOptionPane.ERROR_MESSAGE);
      return;
    }

    Annuncio nuovoAnnuncio = null;

    try {
      // 3. Creazione dell'Oggetto Specifico (Polimorfismo)
      switch (tipo) {
        case VENDITA:
          Vendita vendita = new Vendita();
          // Campi comuni
          vendita.setTitolo(titolo);
          vendita.setDescrizione(descrizione);
          vendita.setCategoria(categoria);
          vendita.setTipoAnnuncio(TipoAnnuncio.VENDITA);
          vendita.setIdUtente(utente.getIdUtente());

          // Gestione Prezzo
          String prezzoStr = view.getPrezzo();
          if (prezzoStr == null || prezzoStr.isEmpty()) {
            throw new IllegalArgumentException("Inserisci un prezzo valido per la vendita.");
          }
          vendita.setPrezzo(Double.parseDouble(prezzoStr));

          nuovoAnnuncio = vendita;
          break;

        case SCAMBIO:
          // Se in futuro aggiungi un campo "Oggetto richiesto" nella GUI, recuperalo qui.
          String oggettoRichiesto = "";
          nuovoAnnuncio = new Scambio(titolo, descrizione, categoria, utente.getIdUtente(), oggettoRichiesto);
          break;

        case REGALO:
          nuovoAnnuncio = new Regalo(titolo, descrizione, categoria, utente.getIdUtente());
          break;

        default:
          nuovoAnnuncio = new Annuncio(utente.getIdUtente(), titolo, descrizione, categoria, tipo);
      }

      // TODO: Qui dovresti implementare la logica per salvare fisicamente le immagini (es. copia in una cartella)
      // e creare i record nella tabella Immagini, se previsto dal DB.
      if (!immagini.isEmpty()) {
        System.out.println("Immagini da caricare: " + immagini.size());
      }

      // 4. Salvataggio nel DB
      boolean successo = annuncioDAO.pubblicaAnnuncio(nuovoAnnuncio);

      if (successo) {
        JOptionPane.showMessageDialog(view, "Annuncio pubblicato con successo!");
        view.dispose(); // Chiude la finestra
      } else {
        JOptionPane.showMessageDialog(view, "Errore nel salvataggio dell'annuncio.", "Errore", JOptionPane.ERROR_MESSAGE);
      }

    } catch (NumberFormatException e) {
      JOptionPane.showMessageDialog(view, "Il prezzo deve essere un numero valido (usa il punto per i decimali).", "Errore", JOptionPane.ERROR_MESSAGE);
    } catch (DatabaseException e) {
      JOptionPane.showMessageDialog(view, "Errore database: " + e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
      e.printStackTrace();
    } catch (Exception e) {
      JOptionPane.showMessageDialog(view, "Errore generico: " + e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
      e.printStackTrace();
    }
  }
}