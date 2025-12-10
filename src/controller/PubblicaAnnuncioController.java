package controller;

import gui.PubblicaAnnuncio;
import dao.AnnuncioDAO;
import model.Annuncio;
import model.enums.Categoria;
import model.enums.TipoAnnuncio;
import model.Utente;
import utils.SessionManager;
import exception.DatabaseException;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
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
    this.view.addPubblicaListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        gestisciPubblicazione();
      }
    });
  }

  private void gestisciPubblicazione() {
    Utente utenteLoggato = SessionManager.getInstance().getUtente();
    if (utenteLoggato == null) {
      view.mostraErrore("Devi essere loggato per pubblicare un annuncio!");
      view.dispose();
      return;
    }

    String titolo = view.getTitolo();
    String descrizione = view.getDescrizione();
    String prezzoStr = view.getPrezzo();
    TipoAnnuncio tipo = view.getTipo();
    String categoriaStr = view.getCategoria();
    Categoria categoria;

    try {
      categoria = Categoria.valueOf(categoriaStr.toUpperCase());
    } catch (IllegalArgumentException ex) {
      view.mostraErrore("Categoria non valida selezionata.");
      return;
    }

    List<File> immagini = view.getImmagini();

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

    Annuncio nuovoAnnuncio = new Annuncio(titolo, descrizione, categoria, utenteLoggato.getIdUtente(), tipo);

    try {
      boolean successo = annuncioDAO.pubblicaAnnuncio(nuovoAnnuncio);

      if (successo) {
        if (!immagini.isEmpty()) {
          System.out.println("Devo salvare " + immagini.size() + " immagini per questo annuncio.");
        }

        view.mostraMessaggio("Annuncio pubblicato con successo!\nCategoria: " + categoria + "\nImmagini: " + immagini.size());
        view.dispose();
      } else {
        view.mostraErrore("Errore durante la pubblicazione.");
      }
    } catch (DatabaseException ex) {
      view.mostraErrore("Errore Database: " + ex.getMessage());
      ex.printStackTrace();
    }
  }
}