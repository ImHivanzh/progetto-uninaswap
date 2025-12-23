package controller;

import gui.FaiPropostaDialog;
import model.enums.TipoAnnuncio;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FaiPropostaController {

  private final FaiPropostaDialog view;
  private final TipoAnnuncio tipoAnnuncio;

  private boolean confermato = false;
  private Double offertaPrezzo = null;
  private String descrizioneProposta = "";
  private byte[] immagineProposta = null;

  /**
   * Creates the controller for the proposal dialog.
   *
   * @param view dialog view
   * @param tipoAnnuncio listing type
   */
  public FaiPropostaController(FaiPropostaDialog view, TipoAnnuncio tipoAnnuncio) {
    this.view = view;
    this.tipoAnnuncio = tipoAnnuncio;
  }

  /**
   * Handles image selection and preview update.
   */
  public void azioneCaricaImmagine() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setFileFilter(new FileNameExtensionFilter("Immagini (JPG, PNG)", "jpg", "png", "jpeg"));

    if (fileChooser.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
      File file = fileChooser.getSelectedFile();
      try (FileInputStream fis = new FileInputStream(file)) {
        this.immagineProposta = fis.readAllBytes();
        view.aggiornaAnteprimaImmagine(this.immagineProposta);
      } catch (IOException ex) {
        view.mostraErrore("Errore durante il caricamento dell'immagine: " + ex.getMessage());
      }
    }
  }

  /**
   * Validates input and confirms the proposal.
   */
  public void azioneConferma() {
    String testoDescrizione = view.getDescrizioneInput();
    String testoPrezzo = view.getPrezzoInput();

    if (tipoAnnuncio == TipoAnnuncio.VENDITA) {
      try {
        if (testoPrezzo == null || testoPrezzo.isEmpty()) throw new NumberFormatException();
        offertaPrezzo = Double.parseDouble(testoPrezzo.replace(",", "."));
        if (offertaPrezzo <= 0) throw new NumberFormatException();
      } catch (NumberFormatException e) {
        view.mostraErrore("Inserisci un prezzo valido maggiore di 0.");
        return;
      }
    }

    if (tipoAnnuncio == TipoAnnuncio.SCAMBIO) {
      if (testoDescrizione.isEmpty()) {
        view.mostraErrore("La descrizione dell'oggetto di scambio e obbligatoria.");
        return;
      }
      if (immagineProposta == null) {
        view.mostraErrore("E obbligatorio caricare una foto dell'oggetto per lo scambio.");
        return;
      }
    }

    this.descrizioneProposta = testoDescrizione.trim();
    this.confermato = true;

    view.dispose();
  }

  /**
   * Cancels the proposal and closes the dialog.
   */
  public void azioneAnnulla() {
    this.confermato = false;
    view.dispose();
  }

  /**
   * Returns whether the proposal was confirmed.
   *
   * @return true when confirmed
   */
  public boolean isConfermato() { return confermato; }

  /**
   * Returns the proposed price.
   *
   * @return proposed price or null
   */
  public Double getOffertaPrezzo() { return offertaPrezzo; }

  /**
   * Returns the proposal description.
   *
   * @return proposal description
   */
  public String getDescrizioneProposta() { return descrizioneProposta; }

  /**
   * Returns the proposal image bytes.
   *
   * @return image bytes or null
   */
  public byte[] getImmagineProposta() { return immagineProposta; }
}
