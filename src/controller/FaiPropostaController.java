package controller;

import gui.FaiPropostaDialog;
import model.enums.TipoAnnuncio;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Controller per dialogo proposta.
 */
public class FaiPropostaController {

  /**
   * Vista dialogo proposta.
   */
  private final FaiPropostaDialog view;
  /**
   * Tipo annuncio di riferimento.
   */
  private final TipoAnnuncio tipoAnnuncio;

  /**
   * Flag conferma proposta.
   */
  private boolean confermato = false;
  /**
   * Prezzo offerto.
   */
  private Double offertaPrezzo = null;
  /**
   * Testo descrizione proposta.
   */
  private String descrizioneProposta = "";
  /**
   * Immagine proposta in bytes.
   */
  private byte[] immagineProposta = null;

  /**
   * Crea controller per dialogo proposta.
   *
   * @param view vista del dialogo
   * @param tipoAnnuncio tipo annuncio
   */
  public FaiPropostaController(FaiPropostaDialog view, TipoAnnuncio tipoAnnuncio) {
    this.view = view;
    this.tipoAnnuncio = tipoAnnuncio;
  }

  /**
   * Gestisce la selezione dell'immagine e aggiorna l'anteprima.
   */
  public void azioneCaricaImmagine() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setFileFilter(new FileNameExtensionFilter("Immagini (JPG, PNG)", "jpg", "png", "jpeg"));

    if (fileChooser.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
      File file = fileChooser.getSelectedFile();
      caricaImmagineDaFile(file);
    }
  }

  /**
   * Carica un'immagine da file.
   *
   * @param file file dell'immagine
   */
  public void caricaImmagineDaFile(File file) {
    try (FileInputStream fis = new FileInputStream(file)) {
      this.immagineProposta = fis.readAllBytes();
      view.aggiornaAnteprimaImmagine(this.immagineProposta);
    } catch (IOException ex) {
      view.mostraErrore("Errore durante il caricamento dell'immagine: " + ex.getMessage());
    }
  }

  /**
   * Valida l'input e conferma la proposta.
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
        view.mostraErrore("La descrizione dell'oggetto di scambio è obbligatoria.");
        return;
      }
      if (immagineProposta == null) {
        view.mostraErrore("È obbligatorio caricare una foto dell'oggetto per lo scambio.");
        return;
      }
    }

    this.descrizioneProposta = testoDescrizione.trim();
    this.confermato = true;

    view.dispose();
  }

  /**
   * Annulla la proposta e chiude il dialogo.
   */
  public void azioneAnnulla() {
    this.confermato = false;
    view.dispose();
  }

  /**
   * Restituisce se la proposta è stata confermata.
   *
   * @return true quando confermata
   */
  public boolean isConfermato() { return confermato; }

  /**
   * Restituisce il prezzo proposto.
   *
   * @return prezzo proposto o null
   */
  public Double getOffertaPrezzo() { return offertaPrezzo; }

  /**
   * Restituisce la descrizione della proposta.
   *
   * @return descrizione della proposta
   */
  public String getDescrizioneProposta() { return descrizioneProposta; }

  /**
   * Restituisce l'array di byte dell'immagine della proposta.
   *
   * @return array di byte dell'immagine o null
   */
  public byte[] getImmagineProposta() { return immagineProposta; }
}
