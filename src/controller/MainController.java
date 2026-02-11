package controller;

import dao.AnnuncioDAO;
import dao.ImmaginiDAO;
import gui.DettaglioAnnuncio;
import gui.LoginForm;
import gui.MainApp;
import gui.Profilo;
import gui.PubblicaAnnuncio;
import model.Annuncio;
import model.Immagini;
import model.Vendita;
import utils.SessionManager;
import utils.WindowManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Controller principale per la bacheca annunci.
 */
public class MainController implements ActionListener {

  /**
   * Numero massimo annunci in evidenza.
   */
  private static final int MAX_ANNUNCI_EVIDENZA = 6;

  /**
   * Vista principale.
   */
  private final MainApp view;
  /**
   * DAO annunci.
   */
  private final AnnuncioDAO annuncioDAO;
  /**
   * DAO immagini.
   */
  private final ImmaginiDAO immaginiDAO;

  /**
   * Crea controller e registra listener.
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
   * Avvia principale flusso, mostrando login se necessario.
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
   * Registra UI listener in vista principale.
   */
  private void registraListener() {
    view.addProfiloListener(this);
    view.addLogoutListener(this);
    view.addPubblicaListener(this);
    view.addSearchListener(this);
    view.addResetListener(this);
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
    } else if (MainApp.ACTION_DETTAGLIO.equals(action)) {
      apriDettaglio(e);
    }
  }

  /**
   * Mostra form login e attende per autenticazione.
   */
  private void mostraLogin() {
    LoginForm loginForm = new LoginForm();
    loginForm.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
    new LoginController(loginForm, new Runnable() {
      /**
       * {@inheritDoc}
       */
      @Override
      public void run() {
        view.setNavigazioneAbilitata(true);
        aggiornaTitoloUtente();
        view.mostra();
        view.toFront();
        caricaAnnunciInEvidenza();
      }
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
   * Apre vista profilo.
   */
  private void apriProfilo() {
    Profilo profilo = new Profilo();
    new ProfiloController(profilo);
    WindowManager.open(view, profilo);
  }

  /**
   * Apre form pubblica annuncio.
   */
  private void apriPubblicaAnnuncio() {
    PubblicaAnnuncio pubblicaAnnuncio = new PubblicaAnnuncio();
    WindowManager.open(view, pubblicaAnnuncio);
  }

  /**
   * Esegue ricerca usando filtri correnti.
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
      view.mostraErrore("Il prezzo massimo non puo essere negativo.");
      return;
    }

    List<Annuncio> annunci = annuncioDAO.findAll();
    List<MainApp.AnnuncioEvidenza> risultati = new ArrayList<>();

    for (Annuncio annuncio : annunci) {
      if (annuncio == null || !annuncio.isStato()) {
        continue;
      }
      if (!matchesTesto(annuncio, testo)) {
        continue;
      }
      if (!matchesCategoria(annuncio, categoria)) {
        continue;
      }
      if (!matchesTipo(annuncio, tipo)) {
        continue;
      }
      if (!matchesPrezzo(annuncio, prezzoMax)) {
        continue;
      }

      byte[] immagine = estraiPrimaImmagine(annuncio);
      risultati.add(new MainApp.AnnuncioEvidenza(annuncio, immagine));
    }

    view.mostraRisultatiRicerca(risultati, this);
  }

  /**
   * Converte testo prezzo in valore numerico, supportando virgola come separatore.
   *
   * @param prezzoRaw testo prezzo
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
    } catch (NumberFormatException ex) {
      return null;
    }
  }

  /**
   * Verifica se annuncio contiene il testo in titolo o descrizione.
   *
   * @param annuncio annuncio da verificare
   * @param testo testo filtro
   * @return true se il testo corrisponde ai campi
   */
  private boolean matchesTesto(Annuncio annuncio, String testo) {
    if (testo == null || testo.isEmpty()) {
      return true;
    }
    String query = testo.toLowerCase();
    String titolo = annuncio.getTitolo() != null ? annuncio.getTitolo().toLowerCase() : "";
    String descrizione = annuncio.getDescrizione() != null ? annuncio.getDescrizione().toLowerCase() : "";
    return titolo.contains(query) || descrizione.contains(query);
  }

  /**
   * Verifica se annuncio appartiene alla categoria selezionata.
   *
   * @param annuncio annuncio da verificare
   * @param categoria categoria filtro
   * @return true se la categoria corrisponde
   */
  private boolean matchesCategoria(Annuncio annuncio, String categoria) {
    if (categoria == null || categoria.trim().isEmpty() || "Tutte".equalsIgnoreCase(categoria.trim())) {
      return true;
    }
    String annuncioCategoria = annuncio.getCategoria() != null ? annuncio.getCategoria().toString() : "";
    return annuncioCategoria.equalsIgnoreCase(categoria.trim());
  }

  /**
   * Verifica se annuncio appartiene al tipo selezionato.
   *
   * @param annuncio annuncio da verificare
   * @param tipo tipo filtro
   * @return true se il tipo corrisponde
   */
  private boolean matchesTipo(Annuncio annuncio, String tipo) {
    if (tipo == null || tipo.trim().isEmpty() || "Tutti".equalsIgnoreCase(tipo.trim())) {
      return true;
    }
    String annuncioTipo = annuncio.getTipoAnnuncio() != null ? annuncio.getTipoAnnuncio().toString() : "";
    return annuncioTipo.equalsIgnoreCase(tipo.trim());
  }

  /**
   * Verifica filtro prezzo massimo per annuncio.
   *
   * @param annuncio annuncio da verificare
   * @param prezzoMax prezzo massimo
   * @return true se compatibile con il filtro
   */
  private boolean matchesPrezzo(Annuncio annuncio, Double prezzoMax) {
    if (prezzoMax == null) {
      return true;
    }
    if (!(annuncio instanceof Vendita)) {
      return false;
    }
    return ((Vendita) annuncio).getPrezzo() <= prezzoMax;
  }

  /**
   * Ripristina filtri ricerca e ricarica annunci in evidenza.
   */
  private void resetRicerca() {
    view.resetFiltri();
    caricaAnnunciInEvidenza();
  }

  /**
   * Esegue logout e mostra login.
   */
  private void eseguiLogout() {
    SessionManager.getInstance().logout();
    view.setNavigazioneAbilitata(false);
    view.setTitoloUtente(null);
    mostraLogin();
  }

  /**
   * Carica annunci in evidenza dal database.
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
   * Restituisce primo byte immagine per annuncio, se presente.
   *
   * @param annuncio annuncio
   * @return byte immagine o null
   */
  private byte[] estraiPrimaImmagine(Annuncio annuncio) {
    if (annuncio == null) {
      return null;
    }
    List<Immagini> immagini = immaginiDAO.getImmaginiByAnnuncio(annuncio.getIdAnnuncio());
    if (immagini == null || immagini.isEmpty()) {
      return null;
    }
    for (Immagini immagine : immagini) {
      if (immagine != null && immagine.getImmagine() != null && immagine.getImmagine().length > 0) {
        return immagine.getImmagine();
      }
    }
    return null;
  }

  /**
   * Apre dettaglio vista per selezionato annuncio in evidenza.
   *
   * @param e azione evento
   */
  private void apriDettaglio(ActionEvent e) {
    if (!(e.getSource() instanceof javax.swing.JButton)) {
      return;
    }
    Object data = ((javax.swing.JButton) e.getSource()).getClientProperty(MainApp.KEY_ANNUNCIO);
    if (!(data instanceof Annuncio)) {
      return;
    }
    Annuncio annuncio = (Annuncio) data;
    DettaglioAnnuncio dettaglio = new DettaglioAnnuncio(annuncio);
    WindowManager.open(view, dettaglio);
  }

  /**
   * Aggiorna finestra titolo con utente corrente.
   */
  private void aggiornaTitoloUtente() {
    String username = SessionManager.getInstance().getUtente() != null
            ? SessionManager.getInstance().getUtente().getUsername()
            : null;
    view.setTitoloUtente(username);
  }
}
