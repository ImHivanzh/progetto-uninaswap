package controller;

import dao.PropostaDAO;
import exception.DatabaseException;
import gui.ModificaPropostaDialog;
import model.PropostaRiepilogo;
import model.enums.TipoAnnuncio;
import utils.SessionManager;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Controller per modifica proposta.
 */
public class ModificaPropostaController {

  /**
   * Vista dialogo modifica proposta.
   */
  private final ModificaPropostaDialog view;
  /**
   * Proposta da modificare.
   */
  private final PropostaRiepilogo proposta;
  /**
   * Immagine proposta in bytes.
   */
  private byte[] immagineProposta;

  /**
   * Crea controller per modifica proposta.
   *
   * @param view dialogo vista
   * @param proposta proposta da modificare
   */
  public ModificaPropostaController(ModificaPropostaDialog view, PropostaRiepilogo proposta) {
    this.view = view;
    this.proposta = proposta;
    this.immagineProposta = proposta.immagine();
  }

  /**
   * Popola campi iniziali dialogo.
   */
  public void popolaDati() {
    TipoAnnuncio tipo = TipoAnnuncio.valueOf(proposta.tipoAnnuncio().toUpperCase());
    if (tipo == TipoAnnuncio.VENDITA) {
      view.setPrezzoInput(proposta.dettaglio().replaceAll("[^0-9,.]", ""));
      return;
    }
    if (tipo == TipoAnnuncio.SCAMBIO) {
      String dettaglio = proposta.dettaglio();
      int delimiter = dettaglio.indexOf(':');
      String descrizione = delimiter >= 0 ? dettaglio.substring(delimiter + 1).trim() : dettaglio.trim();
      view.setDescrizioneInput(descrizione);
      if (proposta.immagine() != null) {
        view.aggiornaAnteprimaImmagine(proposta.immagine());
      }
    }
  }

  /**
   * Gestisce selezione immagine.
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
   * Salva modifica proposta.
   */
  public void azioneSalva() {
    TipoAnnuncio tipo = TipoAnnuncio.valueOf(proposta.tipoAnnuncio().toUpperCase());
    try {
      PropostaDAO propostaDAO = new PropostaDAO();
      int idUtente = SessionManager.getInstance().getUtente().getIdUtente();
      boolean success = false;

      if (tipo == TipoAnnuncio.VENDITA) {
        String testoPrezzo = view.getPrezzoInput();
        double nuovaOfferta;
        try {
          nuovaOfferta = Double.parseDouble(testoPrezzo.replace(",", "."));
          if (nuovaOfferta <= 0) {
            throw new NumberFormatException();
          }
        } catch (NumberFormatException e) {
          view.mostraErrore("Inserisci un prezzo valido maggiore di 0.");
          return;
        }
        success = propostaDAO.modificaPropostaVendita(proposta.idAnnuncio(), idUtente, nuovaOfferta);
      } else if (tipo == TipoAnnuncio.SCAMBIO) {
        String nuovaDescrizione = view.getDescrizioneInput();
        if (nuovaDescrizione.isEmpty()) {
          view.mostraErrore("La descrizione dell'oggetto di scambio e obbligatoria.");
          return;
        }
        success = propostaDAO.modificaPropostaScambio(
                proposta.idAnnuncio(), idUtente, nuovaDescrizione, immagineProposta);
      } else {
        view.mostraErrore("Tipo proposta non supportato.");
        return;
      }

      if (success) {
        JOptionPane.showMessageDialog(view, "Proposta modificata con successo!");
        view.dispose();
      } else {
        view.mostraErrore("Impossibile modificare la proposta.");
      }
    } catch (DatabaseException e) {
      view.mostraErrore("Errore durante la modifica della proposta: " + e.getMessage());
    }
  }

  /**
   * Annulla modifica proposta.
   */
  public void azioneAnnulla() {
    view.dispose();
  }
}
