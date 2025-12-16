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

  // Stato della proposta
  private boolean confermato = false;
  private Double offertaPrezzo = null;
  private String descrizioneProposta = "";
  private byte[] immagineProposta = null;

  public FaiPropostaController(FaiPropostaDialog view, TipoAnnuncio tipoAnnuncio) {
    this.view = view;
    this.tipoAnnuncio = tipoAnnuncio;
  }

  public void azioneCaricaImmagine() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setFileFilter(new FileNameExtensionFilter("Immagini (JPG, PNG)", "jpg", "png", "jpeg"));

    if (fileChooser.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
      File file = fileChooser.getSelectedFile();
      try (FileInputStream fis = new FileInputStream(file)) {
        this.immagineProposta = fis.readAllBytes();
        // Aggiorna la vista con l'anteprima
        view.aggiornaAnteprimaImmagine(this.immagineProposta);
      } catch (IOException ex) {
        view.mostraErrore("Errore durante il caricamento dell'immagine: " + ex.getMessage());
      }
    }
  }

  public void azioneConferma() {
    String testoDescrizione = view.getDescrizioneInput();
    String testoPrezzo = view.getPrezzoInput();

    // 1. Validazione VENDITA
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

    // 2. Validazione SCAMBIO
    if (tipoAnnuncio == TipoAnnuncio.SCAMBIO) {
      if (testoDescrizione.isEmpty()) {
        view.mostraErrore("La descrizione dell'oggetto di scambio è obbligatoria.");
        return;
      }
      if (immagineProposta == null) {
        view.mostraErrore("È obbligatorio caricare una foto dell'oggetto per lo scambio.");
        return;
      }
    }

    // 3. Validazione Generale (salvataggio descrizione)
    this.descrizioneProposta = testoDescrizione.trim();
    this.confermato = true;

    // Chiude la dialog
    view.dispose();
  }

  public void azioneAnnulla() {
    this.confermato = false;
    view.dispose();
  }

  // --- Getters per recuperare i dati dal chiamante ---
  public boolean isConfermato() { return confermato; }
  public Double getOffertaPrezzo() { return offertaPrezzo; }
  public String getDescrizioneProposta() { return descrizioneProposta; }
  public byte[] getImmagineProposta() { return immagineProposta; }
}