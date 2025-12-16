package controller;

import dao.AnnuncioDAO;
import dao.ImmaginiDAO;
import exception.DatabaseException;
import gui.PubblicaAnnuncio;
import model.*;
import model.enums.Categoria;
import model.enums.TipoAnnuncio;
import utils.SessionManager;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class PubblicaAnnuncioController {

  private final PubblicaAnnuncio view;
  private final AnnuncioDAO annuncioDAO;
  private final ImmaginiDAO immaginiDAO;

  public PubblicaAnnuncioController(PubblicaAnnuncio view) {
    this.view = view;
    this.annuncioDAO = new AnnuncioDAO();
    this.immaginiDAO = new ImmaginiDAO();
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

    // Recupero file immagini
    List<File> immaginiFiles = view.getImmagini();

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
          vendita.setTitolo(titolo);
          vendita.setDescrizione(descrizione);
          vendita.setCategoria(categoria);
          vendita.setTipoAnnuncio(TipoAnnuncio.VENDITA);
          vendita.setIdUtente(utente.getIdUtente());

          String prezzoStr = view.getPrezzo();
          if (prezzoStr == null || prezzoStr.isEmpty()) {
            throw new IllegalArgumentException("Inserisci un prezzo valido per la vendita.");
          }
          vendita.setPrezzo(Double.parseDouble(prezzoStr));

          nuovoAnnuncio = vendita;
          break;

        case SCAMBIO:
          // Se la GUI non ha ancora un campo per l'oggetto richiesto, mettiamo un placeholder o stringa vuota
          String oggettoRichiesto = "";
          nuovoAnnuncio = new Scambio(titolo, descrizione, categoria, utente.getIdUtente(), oggettoRichiesto);
          break;

        case REGALO:
          nuovoAnnuncio = new Regalo(titolo, descrizione, categoria, utente.getIdUtente());
          break;

        default:
          nuovoAnnuncio = new Annuncio(utente.getIdUtente(), titolo, descrizione, categoria, tipo);
      }

      // 4. Salvataggio nel DB (Ora riceviamo l'ID generato, non un boolean)
      int idAnnuncioCreato = annuncioDAO.pubblicaAnnuncio(nuovoAnnuncio);

      if (idAnnuncioCreato > 0) {
        // Impostiamo l'ID appena generato nell'oggetto annuncio
        nuovoAnnuncio.setIdAnnuncio(idAnnuncioCreato);

        // 5. Salvataggio Immagini (Se presenti)
        if (immaginiFiles != null && !immaginiFiles.isEmpty()) {
          salvaImmaginiPerAnnuncio(nuovoAnnuncio, immaginiFiles);
        }

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

  /**
   * Converte i file in byte[] e li salva nel DB collegandoli all'annuncio.
   */
  private void salvaImmaginiPerAnnuncio(Annuncio annuncio, List<File> files) throws DatabaseException {
    try {
      for (File file : files) {
        // Legge i byte del file
        byte[] contenutoFile = Files.readAllBytes(file.toPath());

        // Crea il model Immagini
        Immagini imgModel = new Immagini();
        imgModel.setImmagine(contenutoFile);
        imgModel.setAnnuncio(annuncio); // L'annuncio ora ha l'ID corretto setttato al punto 4

        // Chiama il DAO
        immaginiDAO.salvaImmagine(imgModel);
      }
      System.out.println("Salvate " + files.size() + " immagini per annuncio ID: " + annuncio.getIdAnnuncio());

    } catch (IOException e) {
      System.err.println("Errore durante la lettura del file immagine: " + e.getMessage());
      e.printStackTrace();
      // Opzionale: Lanciare un'eccezione o mostrare un warning, ma non blocchiamo la pubblicazione per questo
    }
  }
}