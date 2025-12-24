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

  /**
   * Crea controller per vista pubblica annuncio.
   *
   * @param view pubblica vista
   */
  public PubblicaAnnuncioController(PubblicaAnnuncio view) {
    this.view = view;
    this.annuncioDAO = new AnnuncioDAO();
    this.immaginiDAO = new ImmaginiDAO();
  }

  /**
   * Valida input e pubblica annuncio.
   */
  public void pubblica() {
    Utente utente = SessionManager.getInstance().getUtente();
    if (utente == null) {
      JOptionPane.showMessageDialog(view, "Devi essere loggato per pubblicare un annuncio!");
      return;
    }

    String titolo = view.getTitolo();
    String descrizione = view.getDescrizione();
    Categoria categoria = view.getCategoriaSelezionata();
    TipoAnnuncio tipo = view.getTipoSelezionato();

    List<File> immaginiFiles = view.getImmagini();

    if (titolo.isEmpty() || descrizione.isEmpty() || categoria == null || tipo == null) {
      JOptionPane.showMessageDialog(view, "Compila tutti i campi obbligatori.", "Errore", JOptionPane.ERROR_MESSAGE);
      return;
    }

    Annuncio nuovoAnnuncio = null;

    try {
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
          String oggettoRichiesto = "";
          nuovoAnnuncio = new Scambio(titolo, descrizione, categoria, utente.getIdUtente(), oggettoRichiesto);
          break;

        case REGALO:
          nuovoAnnuncio = new Regalo(titolo, descrizione, categoria, utente.getIdUtente());
          break;

        default:
          nuovoAnnuncio = new Annuncio(utente.getIdUtente(), titolo, descrizione, categoria, tipo);
      }

      int idAnnuncioCreato = annuncioDAO.pubblicaAnnuncio(nuovoAnnuncio);

      if (idAnnuncioCreato > 0) {
        nuovoAnnuncio.setIdAnnuncio(idAnnuncioCreato);

        if (immaginiFiles != null && !immaginiFiles.isEmpty()) {
          salvaImmaginiPerAnnuncio(nuovoAnnuncio, immaginiFiles);
        }

        JOptionPane.showMessageDialog(view, "Annuncio pubblicato con successo!");
        view.dispose();
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
   * Converte file a byte array e memorizza loro per annuncio.
   *
   * @param annuncio annuncio a allega immagini a
   * @param files file immagini
   * @throws DatabaseException quando persistenza fallisce
   */
  private void salvaImmaginiPerAnnuncio(Annuncio annuncio, List<File> files) throws DatabaseException {
    try {
      for (File file : files) {
        byte[] contenutoFile = Files.readAllBytes(file.toPath());

        Immagini imgModel = new Immagini();
        imgModel.setImmagine(contenutoFile);
        imgModel.setAnnuncio(annuncio);

        immaginiDAO.salvaImmagine(imgModel);
      }
      System.out.println("Salvate " + files.size() + " immagini per annuncio ID: " + annuncio.getIdAnnuncio());

    } catch (IOException e) {
      System.err.println("Errore durante la lettura del file immagine: " + e.getMessage());
      e.printStackTrace();
    }
  }
}
