package controller;

import dao.AnnuncioDAO;
import dao.ImmaginiDAO;
import exception.DatabaseException;
import gui.PubblicaAnnuncio;
import model.Annuncio;
import model.Immagini;
import model.Regalo;
import model.Scambio;
import model.Utente;
import model.Vendita;
import model.enums.Categoria;
import model.enums.TipoAnnuncio;
import utils.SessionManager;
import utils.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * Controller per pubblicazione annunci.
 */
public class PubblicaAnnuncioController {

  private static final String ERRORE_TITLE = "Errore";

  /**
   * Vista pubblica annuncio.
   */
  private final PubblicaAnnuncio view;
  /**
   * DAO annunci.
   */
  private final AnnuncioDAO annuncioDAO;
  /**
   * DAO immagini.
   */
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

    String titolo = view.getTitolo().trim();
    String descrizione = view.getDescrizione().trim();
    Categoria categoria = view.getCategoriaSelezionata();
    TipoAnnuncio tipo = view.getTipoSelezionato();
    boolean spedizione = view.isSpedizioneSelezionata();

    List<File> immaginiFiles = view.getImmagini();

    if (titolo.isEmpty() || descrizione.isEmpty() || categoria == null || tipo == null) {
      JOptionPane.showMessageDialog(view, "Compila tutti i campi obbligatori.", ERRORE_TITLE, JOptionPane.ERROR_MESSAGE);
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
          vendita.setUtente(utente);
          vendita.setSpedizione(spedizione);

          String prezzoStr = view.getPrezzo().trim();
          if (prezzoStr.isEmpty()) {
            throw new IllegalArgumentException("Inserisci un prezzo valido per la vendita.");
          }
          vendita.setPrezzo(Double.parseDouble(prezzoStr));

          nuovoAnnuncio = vendita;
          break;

        case SCAMBIO:
          String oggettoRichiesto = view.getOggettoRichiesto().trim();
          if (oggettoRichiesto.isEmpty()) {
            throw new IllegalArgumentException("Specifica cosa vorresti ricevere in cambio.");
          }
          nuovoAnnuncio = new Scambio(titolo, descrizione, categoria, utente, oggettoRichiesto);
          nuovoAnnuncio.setSpedizione(spedizione);
          break;

        case REGALO:
          nuovoAnnuncio = new Regalo(titolo, descrizione, categoria, utente);
          nuovoAnnuncio.setSpedizione(spedizione);
          break;

        default:
          nuovoAnnuncio = new Annuncio(utente, titolo, descrizione, categoria, tipo);
          nuovoAnnuncio.setSpedizione(spedizione);
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
        JOptionPane.showMessageDialog(view, "Errore nel salvataggio dell'annuncio.", ERRORE_TITLE, JOptionPane.ERROR_MESSAGE);
      }

    } catch (NumberFormatException e) {
      JOptionPane.showMessageDialog(view, "Il prezzo deve essere un numero valido (usa il punto per i decimali).", ERRORE_TITLE, JOptionPane.ERROR_MESSAGE);
    } catch (DatabaseException e) {
      JOptionPane.showMessageDialog(view, "Errore database: " + e.getMessage(), ERRORE_TITLE, JOptionPane.ERROR_MESSAGE);
      Logger.error("Errore database pubblicazione annuncio", e);
    } catch (Exception e) {
      JOptionPane.showMessageDialog(view, "Errore generico: " + e.getMessage(), ERRORE_TITLE, JOptionPane.ERROR_MESSAGE);
      Logger.error("Errore generico pubblicazione annuncio", e);
    }
  }

  /**
   * Converte i file in array di byte e li memorizza per l'annuncio.
   *
   * @param annuncio annuncio a cui allegare le immagini
   * @param files file delle immagini
   * @throws DatabaseException quando la persistenza fallisce
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
      Logger.info("Salvate " + files.size() + " immagini per annuncio ID: " + annuncio.getIdAnnuncio());

    } catch (IOException e) {
      Logger.error("Errore durante la lettura del file immagine", e);
    }
  }
}
