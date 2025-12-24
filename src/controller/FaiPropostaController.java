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
   * Crea controller per dialogo proposta.
   *
   * @param view dialogo vista
   * @param tipoAnnuncio tipo annuncio
   */
  public FaiPropostaController(FaiPropostaDialog view, TipoAnnuncio tipoAnnuncio) {
    this.view = view;
    this.tipoAnnuncio = tipoAnnuncio;
  }

  /**
   * Gestisce immagine selezione e anteprima aggiorna.
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
   * Valida input e conferma proposta.
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
   * Annulla proposta e chiude dialogo.
   */
  public void azioneAnnulla() {
    this.confermato = false;
    view.dispose();
  }

  /**
   * Restituisce se proposta era confermata.
   *
   * @return true quando confermata
   */
  public boolean isConfermato() { return confermato; }

  /**
   * Restituisce proposto prezzo.
   *
   * @return proposto prezzo o null
   */
  public Double getOffertaPrezzo() { return offertaPrezzo; }

  /**
   * Restituisce descrizione proposta.
   *
   * @return descrizione proposta
   */
  public String getDescrizioneProposta() { return descrizioneProposta; }

  /**
   * Restituisce byte immagine proposta.
   *
   * @return byte immagine o null
   */
  public byte[] getImmagineProposta() { return immagineProposta; }
}
