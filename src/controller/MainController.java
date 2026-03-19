package controller;

import dao.AnnuncioDAO;
import dao.ImmaginiDAO;
import exception.DatabaseException;
import gui.DettaglioAnnuncio;
import gui.LoginForm;
import gui.MainApp;
import gui.Profilo;
import gui.PubblicaAnnuncio;
import model.Annuncio;
import utils.Logger;
import utils.SessionManager;
import utils.WindowManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.WindowConstants;

/**
 * Controller principale per la bacheca annunci.
 */
public class MainController implements ActionListener {

  /**
   * Numero massimo di annunci in evidenza.
   */
  private static final int MAX_ANNUNCI_EVIDENZA = 6;

  /**
   * Vista principale.
   */
  private final MainApp view;
  /**
   * DAO per gli annunci.
   */
  private final AnnuncioDAO annuncioDAO;
  /**
   * DAO per le immagini.
   */
  private final ImmaginiDAO immaginiDAO;

  /**
   * Crea il controller e registra i listener.
   *
   * @param view vista principale
   */
  public MainController(MainApp view) {
    this.view = view;
    this.annuncioDAO = new AnnuncioDAO();
    this.immaginiDAO = new ImmaginiDAO();
    registraListener();
  }

  /**
   * Avvia il flusso principale, mostrando il login se necessario.
   */
  public void avvia() {
    if (SessionManager.getInstance().getUtente() == null) {
      view.setNavigazioneAbilitata(false);
      mostraLogin();
      return;
    }

    view.setNavigazioneAbilitata(true);
    aggiornaTitoloUtente();
    view.mostra();
    caricaAnnunciInEvidenza();
  }

  /**
   * Registra i listener dell'interfaccia nella vista principale.
   */
  private void registraListener() {
    view.addProfiloListener(this);
    view.addLogoutListener(this);
    view.addPubblicaListener(this);
    view.addSearchListener(this);
    view.addResetListener(this);
    view.addAggiornaListener(this);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    String action = e.getActionCommand();
    if (MainApp.ACTION_PROFILO.equals(action)) {
      apriProfilo();
    } else if (MainApp.ACTION_LOGOUT.equals(action)) {
      eseguiLogout();
    } else if (MainApp.ACTION_PUBBLICA.equals(action)) {
      apriPubblicaAnnuncio();
    } else if (MainApp.ACTION_RICERCA.equals(action)) {
      eseguiRicerca();
    } else if (MainApp.ACTION_RESET.equals(action)) {
      resetRicerca();
    } else if (MainApp.ACTION_AGGIORNA.equals(action)) {
      aggiornaAnnunci();
    } else if (MainApp.ACTION_DETTAGLIO.equals(action)) {
      apriDettaglio(e);
    }
  }

  /**
   * Mostra il form di login e attende l'autenticazione.
   */
  private void mostraLogin() {
    LoginForm loginForm = new LoginForm();
    loginForm.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    new LoginController(loginForm, () -> {
      view.setNavigazioneAbilitata(true);
      aggiornaTitoloUtente();
      view.mostra();
      view.toFront();
      caricaAnnunciInEvidenza();
    });

    loginForm.addWindowListener(new WindowAdapter() {
      /**
       * {@inheritDoc}
       */
      @Override
      public void windowClosed(WindowEvent e) {
        if (SessionManager.getInstance().getUtente() == null) {
          view.dispose();
        }
      }
    });

    loginForm.setVisible(true);
  }

  /**
   * Apre la vista del profilo.
   */
  private void apriProfilo() {
    Profilo profilo = new Profilo();
    new ProfiloController(profilo);
    WindowManager.open(view, profilo);
  }

  /**
   * Apre il form per pubblicare un annuncio.
   */
  private void apriPubblicaAnnuncio() {
    PubblicaAnnuncio pubblicaAnnuncio = new PubblicaAnnuncio();
    WindowManager.open(view, pubblicaAnnuncio);
  }

  /**
   * Esegue la ricerca usando i filtri correnti.
   * Ottimizzato per filtrare direttamente nel database invece che in memoria.
   */
  private void eseguiRicerca() {
    String testo = view.getTestoRicerca().trim();
    String categoria = view.getCategoriaSelezionata();
    String tipo = view.getTipoSelezionato();
    String prezzoRaw = view.getPrezzoMax().trim();

    Double prezzoMax = parsePrezzoMax(prezzoRaw);
    if (!prezzoRaw.isEmpty() && prezzoMax == null) {
      view.mostraErrore("Inserisci un numero valido per il prezzo massimo.");
      return;
    }
    if (prezzoMax != null && prezzoMax < 0) {
      view.mostraErrore("Il prezzo massimo non può essere negativo.");
      return;
    }

    // Usa il metodo search ottimizzato che filtra nel database
    List<Annuncio> annunci = annuncioDAO.search(
            testo.isEmpty() ? null : testo,
            categoria,
            tipo,
            prezzoMax
    );

    List<MainApp.AnnuncioEvidenza> risultati = new ArrayList<>();
    for (Annuncio annuncio : annunci) {
      byte[] immagine = estraiPrimaImmagine(annuncio);
      risultati.add(new MainApp.AnnuncioEvidenza(annuncio, immagine));
    }

    view.mostraRisultatiRicerca(risultati, this);
  }

  /**
   * Converte il testo del prezzo in valore numerico, supportando la virgola come separatore.
   *
   * @param prezzoRaw testo del prezzo
   * @return valore numerico o null se non valido
   */
  private Double parsePrezzoMax(String prezzoRaw) {
    if (prezzoRaw == null) {
      return null;
    }
    String trimmed = prezzoRaw.trim();
    if (trimmed.isEmpty()) {
      return null;
    }
    String normalized = trimmed.replace(',', '.');
    try {
      return Double.parseDouble(normalized);
    } catch (NumberFormatException e) {
      return null;
    }
  }

  /**
   * Ripristina i filtri di ricerca e ricarica gli annunci in evidenza.
   */
  private void resetRicerca() {
    view.resetFiltri();
    caricaAnnunciInEvidenza();
  }

  /**
   * Aggiorna gli annunci in base ai filtri correnti.
   * Se almeno un filtro è attivo, esegue una ricerca.
   * Altrimenti, ricarica gli annunci in evidenza.
   */
  private void aggiornaAnnunci() {
    String testo = view.getTestoRicerca().trim();
    String categoria = view.getCategoriaSelezionata();
    String tipo = view.getTipoSelezionato();
    String prezzoMax = view.getPrezzoMax().trim();

    // Verifica se almeno un filtro è attivo
    boolean filtriAttivi = !testo.isEmpty()
        || !"Tutte".equals(categoria)
        || !"Tutti".equals(tipo)
        || !prezzoMax.isEmpty();

    if (filtriAttivi) {
      // Ricarica con filtri correnti
      eseguiRicerca();
    } else {
      // Ricarica annunci in evidenza
      caricaAnnunciInEvidenza();
    }
  }

  /**
   * Esegue il logout e mostra il login.
   */
  private void eseguiLogout() {
    SessionManager.getInstance().logout();
    view.setNavigazioneAbilitata(false);
    view.setTitoloUtente(null);
    mostraLogin();
  }

  /**
   * Carica gli annunci in evidenza dal database.
   */
  private void caricaAnnunciInEvidenza() {
    List<Annuncio> annunci = annuncioDAO.findAll();
    List<MainApp.AnnuncioEvidenza> evidenza = new ArrayList<>();

    for (Annuncio annuncio : annunci) {
      if (annuncio == null || !annuncio.isStato()) {
        continue;
      }
      byte[] immagine = estraiPrimaImmagine(annuncio);
      evidenza.add(new MainApp.AnnuncioEvidenza(annuncio, immagine));
    }

    if (!evidenza.isEmpty()) {
      Collections.shuffle(evidenza);
      if (evidenza.size() > MAX_ANNUNCI_EVIDENZA) {
        evidenza = evidenza.subList(0, MAX_ANNUNCI_EVIDENZA);
      }
    }

    view.mostraAnnunciInEvidenza(evidenza, this);
  }

  /**
   * Restituisce il primo array di byte dell'immagine per l'annuncio, se presente.
   * Ottimizzato per caricare solo la prima immagine invece di tutte.
   *
   * @param annuncio annuncio
   * @return array di byte dell'immagine o null
   */
  private byte[] estraiPrimaImmagine(Annuncio annuncio) {
    if (annuncio == null) {
      return new byte[0];
    }
    try {
      return immaginiDAO.getPrimaImmagine(annuncio);
    } catch (DatabaseException e) {
      Logger.error("Errore caricamento prima immagine per annuncio " + annuncio.getIdAnnuncio(), e);
      return new byte[0];
    }
  }

  /**
   * Apre la vista di dettaglio per l'annuncio selezionato in evidenza.
   *
   * @param e evento dell'azione
   */
  private void apriDettaglio(ActionEvent e) {
    if (!(e.getSource() instanceof javax.swing.JButton button)) {
      return;
    }
    Object data = button.getClientProperty(MainApp.KEY_ANNUNCIO);
    if (!(data instanceof Annuncio annuncio)) {
      return;
    }
    DettaglioAnnuncio dettaglio = new DettaglioAnnuncio(annuncio);
    WindowManager.open(view, dettaglio);
  }

  /**
   * Aggiorna il titolo della finestra con l'utente corrente.
   */
  private void aggiornaTitoloUtente() {
    String username = SessionManager.getInstance().getUtente() != null
            ? SessionManager.getInstance().getUtente().getUsername()
            : null;
    view.setTitoloUtente(username);
  }
}
