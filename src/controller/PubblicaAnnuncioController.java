package controller;

import gui.PubblicaAnnuncio;
import dao.AnnuncioDAO;
import model.Annuncio;
import model.enums.TipoAnnuncio;
import model.Utente;
import utils.SessionManager;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.sql.SQLException;
import java.util.List;

public class PubblicaAnnuncioController {

  private final PubblicaAnnuncio view;
  private final AnnuncioDAO annuncioDAO;

  public PubblicaAnnuncioController(PubblicaAnnuncio view) {
    this.view = view;
    this.annuncioDAO = new AnnuncioDAO();

    initListeners();
  }

  private void initListeners() {
    // Ora usiamo un MouseAdapter perché il bottone è un JPanel custom
    this.view.addPubblicaListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        gestisciPubblicazione();
      }
    });
  }

  private void gestisciPubblicazione() {
    // 1. Controllo Sessione
    Utente utenteLoggato = SessionManager.getInstance().getUtente();
    if (utenteLoggato == null) {
      view.mostraErrore("Devi essere loggato per pubblicare un annuncio!");
      view.dispose();
      return;
    }

    // 2. Recupero Dati
    String titolo = view.getTitolo();
    String descrizione = view.getDescrizione();
    String prezzoStr = view.getPrezzo();
    TipoAnnuncio tipo = view.getTipo();
    String categoria = view.getCategoria(); // Nuovo campo
    List<File> immagini = view.getImmagini(); // Nuovo campo

    // 3. Validazione Input
    if (titolo.isEmpty() || descrizione.isEmpty()) {
      view.mostraErrore("Titolo e Descrizione sono obbligatori.");
      return;
    }

    float prezzo = 0.0f;
    if (tipo != TipoAnnuncio.REGALO) {
      try {
        prezzo = Float.parseFloat(prezzoStr);
        if (prezzo < 0) throw new NumberFormatException();
      } catch (NumberFormatException ex) {
        view.mostraErrore("Inserisci un prezzo valido (es. 10.50).");
        return;
      }
    }

    // 4. Creazione Oggetto
    Annuncio nuovoAnnuncio = new Annuncio();
    nuovoAnnuncio.setTitolo(titolo);
    nuovoAnnuncio.setDescrizione(descrizione);
    nuovoAnnuncio.setPrezzo(prezzo);
    nuovoAnnuncio.setTipoAnnuncio(tipo);
    nuovoAnnuncio.setUtente(utenteLoggato);
    nuovoAnnuncio.setStato(true);
    // nuovoAnnuncio.setCategoria(categoria); // TODO: Aggiungere campo categoria al Model e al DB se necessario

    // 5. Salvataggio su DB
    try {
      // Nota: Qui dovresti passare anche categoria e immagini se il DB è pronto
      // Per ora salvo l'annuncio base
      boolean successo = annuncioDAO.pubblicaAnnuncio(nuovoAnnuncio);

      if (successo) {
        // Qui andrebbe la logica per salvare le immagini
        if (!immagini.isEmpty()) {
          System.out.println("Devo salvare " + immagini.size() + " immagini per questo annuncio.");
          // ImmagineDB.salvaImmagini(annuncioID, immagini);
        }

        view.mostraMessaggio("Annuncio pubblicato con successo!\nCategoria: " + categoria + "\nImmagini: " + immagini.size());
        view.dispose();
      } else {
        view.mostraErrore("Errore durante la pubblicazione.");
      }
    } catch (SQLException ex) {
      view.mostraErrore("Errore Database: " + ex.getMessage());
      ex.printStackTrace();
    }
  }
}