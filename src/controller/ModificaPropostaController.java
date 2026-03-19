package controller;

import dao.PropostaDAO;
import exception.DatabaseException;
import gui.ModificaPropostaDialog;
import model.PropostaRiepilogo;
import model.enums.TipoAnnuncio;
import utils.SessionManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

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
   * DAO per operazioni su proposte.
   */
  private final PropostaDAO propostaDAO;

  /**
   * Crea il controller per la modifica della proposta.
   *
   * @param view vista del dialogo
   * @param proposta proposta da modificare
   * @throws DatabaseException se l'inizializzazione del DAO fallisce
   */
  public ModificaPropostaController(ModificaPropostaDialog view, PropostaRiepilogo proposta) throws DatabaseException {
    this.view = view;
    this.proposta = proposta;
    this.immagineProposta = proposta.immagine();
    this.propostaDAO = new PropostaDAO();
  }

  /**
   * Popola i campi iniziali del dialogo.
   */
  public void popolaDati() {
    if (proposta.annuncio() == null || proposta.annuncio().getTipoAnnuncio() == null) {
      view.mostraErrore("Tipo annuncio non disponibile.");
      return;
    }

    TipoAnnuncio tipo = proposta.annuncio().getTipoAnnuncio();
    switch (tipo) {
      case VENDITA -> {
        String dettaglio = proposta.dettaglio();
        // Estrai prezzo: "Offerta: €123.45" o "Offerta: €123.45 | Messaggio: testo"
        int messaggioIndex = dettaglio.indexOf(" | Messaggio: ");
        String partPrezzo = messaggioIndex >= 0 ? dettaglio.substring(0, messaggioIndex) : dettaglio;
        String prezzo = partPrezzo.replaceAll("[^0-9,.]", "");
        view.setPrezzoInput(prezzo);

        // Estrai messaggio se presente
        if (messaggioIndex >= 0) {
          String messaggio = dettaglio.substring(messaggioIndex + 14).trim();
          view.setMessaggioInput(messaggio);
        }
      }
      case SCAMBIO -> {
        String dettaglio = proposta.dettaglio();
        int delimiter = dettaglio.indexOf(':');
        String descrizione = delimiter >= 0 ? dettaglio.substring(delimiter + 1).trim() : dettaglio.trim();
        view.setDescrizioneInput(descrizione);
        if (proposta.immagine() != null) {
          view.aggiornaAnteprimaImmagine(proposta.immagine());
        }
      }
      case REGALO -> {
        String dettaglio = proposta.dettaglio();
        // Estrai messaggio: "Richiesta regalo | Messaggio: testo" o "Richiesta regalo"
        int messaggioIndex = dettaglio.indexOf(" | Messaggio: ");
        if (messaggioIndex >= 0) {
          String messaggio = dettaglio.substring(messaggioIndex + 14).trim();
          view.setMessaggioInput(messaggio);
        }
      }
      default -> {
        // No action needed for other types
      }
    }
  }

  /**
   * Gestisce la selezione dell'immagine.
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
   * Salva le modifiche alla proposta.
   */
  public void azioneSalva() {
    TipoAnnuncio tipo = proposta.annuncio().getTipoAnnuncio();
    try {
      int idUtente = SessionManager.getInstance().getUtente().getIdUtente();
      boolean success = switch (tipo) {
        case VENDITA -> salvaPropostaVendita(idUtente);
        case SCAMBIO -> salvaPropostaScambio(idUtente);
        case REGALO -> salvaPropostaRegalo(idUtente);
        default -> {
          view.mostraErrore("Tipo proposta non supportato.");
          yield false;
        }
      };

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
   * Salva la proposta di vendita.
   *
   * @param idUtente ID dell'utente
   * @return true se il salvataggio ha successo
   * @throws DatabaseException se si verifica un errore nel database
   */
  private boolean salvaPropostaVendita(int idUtente) throws DatabaseException {
    String testoPrezzo = view.getPrezzoInput();
    double nuovaOfferta;
    try {
      nuovaOfferta = Double.parseDouble(testoPrezzo.replace(",", "."));
      if (nuovaOfferta <= 0) {
        throw new NumberFormatException();
      }
    } catch (NumberFormatException e) {
      view.mostraErrore("Inserisci un prezzo valido maggiore di 0.");
      return false;
    }
    String messaggio = view.getMessaggioInput().trim();
    return propostaDAO.modificaPropostaVendita(proposta.annuncio().getIdAnnuncio(), idUtente, nuovaOfferta, messaggio);
  }

  /**
   * Salva la proposta di scambio.
   *
   * @param idUtente ID dell'utente
   * @return true se il salvataggio ha successo
   * @throws DatabaseException se si verifica un errore nel database
   */
  private boolean salvaPropostaScambio(int idUtente) throws DatabaseException {
    String nuovaDescrizione = view.getDescrizioneInput().trim();
    if (nuovaDescrizione.isEmpty()) {
      view.mostraErrore("La descrizione dell'oggetto di scambio è obbligatoria.");
      return false;
    }
    return propostaDAO.modificaPropostaScambio(
            proposta.annuncio().getIdAnnuncio(), idUtente, nuovaDescrizione, immagineProposta);
  }

  /**
   * Salva la proposta di regalo.
   *
   * @param idUtente ID dell'utente
   * @return true se il salvataggio ha successo
   * @throws DatabaseException se si verifica un errore nel database
   */
  private boolean salvaPropostaRegalo(int idUtente) throws DatabaseException {
    String messaggio = view.getMessaggioInput().trim();
    return propostaDAO.modificaPropostaRegalo(proposta.annuncio().getIdAnnuncio(), idUtente, messaggio);
  }

  /**
   * Annulla la modifica della proposta.
   */
  public void azioneAnnulla() {
    view.dispose();
  }
}
