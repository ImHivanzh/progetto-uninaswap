package gui;

import controller.DettaglioAnnuncioController;
import exception.DatabaseException;
import model.Annuncio;
import utils.Logger;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;

/**
 * Finestra per dettaglio annuncio.
 */
public class DettaglioAnnuncio extends JFrame {
  /**
   * Pannello principale.
   */
  private JPanel mainPanel;
  /**
   * Etichetta titolo annuncio.
   */
  private JLabel lblTitolo;
  /**
   * Etichetta prezzo annuncio.
   */
  private JLabel lblPrezzo;
  /**
   * Etichetta categoria annuncio.
   */
  private JLabel lblCategoria;
  /**
   * Area testo descrizione.
   */
  private JTextArea txtDescrizione;
  /**
   * Etichetta immagine annuncio.
   */
  private JLabel lblImmagine;
  /**
   * Pulsante immagine precedente.
   */
  private JButton btnPrecedente;
  /**
   * Pulsante immagine successiva.
   */
  private JButton btnSuccessivo;
  /**
   * Etichetta nome utente.
   */
  private JLabel lblUtenteNome;
  /**
   * Pulsante contatta.
   */
  private JButton btnContatta;
  /**
   * Pulsante indietro.
   */
  private JButton btnIndietro;
  /**
   * Etichetta tipo annuncio.
   */
  private JLabel lblTipo;
  /**
   * Pannello immagini.
   */
  private JPanel imagePanel;
  /**
   * Etichetta contatore immagini.
   */
  private JLabel lblContatoreImmagini;

  /**
   * Controller associato alla vista.
   */
  private transient DettaglioAnnuncioController controller;

  /**
   * Crea la finestra di dettaglio dell'annuncio.
   *
   * @param annuncio annuncio da visualizzare
   */
  public DettaglioAnnuncio(Annuncio annuncio) {
    setContentPane(mainPanel);
    setTitle("UninaSwap - Dettaglio Annuncio");
    setSize(800, 600);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setLocationRelativeTo(null);

    try {
      this.controller = new DettaglioAnnuncioController(this, annuncio);
    } catch (DatabaseException e) {
      Logger.error("Errore inizializzazione controller dettaglio annuncio", e);
      JOptionPane.showMessageDialog(this,
        "Errore durante il caricamento del dettaglio annuncio: " + e.getMessage(),
        "Errore",
        JOptionPane.ERROR_MESSAGE);
      dispose();
      return;
    }

    setupListeners();
  }

  /**
   * Registra i listener dei pulsanti per la vista.
   */
  private void setupListeners() {
    btnIndietro.addActionListener(e -> dispose());
    btnContatta.addActionListener(e -> controller.azioneContatta());
    btnPrecedente.addActionListener(e -> controller.azionePrecedente());
    btnSuccessivo.addActionListener(e -> controller.azioneSuccessiva());
  }

  /**
   * Imposta il testo dell'etichetta del titolo.
   *
   * @param titolo testo del titolo
   */
  public void setTitolo(String titolo) { lblTitolo.setText(titolo); }

  /**
   * Imposta il testo della descrizione.
   *
   * @param descrizione testo della descrizione
   */
  public void setDescrizione(String descrizione) { txtDescrizione.setText(descrizione); }

  /**
   * Imposta il testo dell'etichetta della categoria.
   *
   * @param testo testo della categoria
   */
  public void setCategoria(String testo) { lblCategoria.setText(testo); }

  /**
   * Imposta il testo dell'etichetta del tipo.
   *
   * @param testo testo del tipo
   */
  public void setTipo(String testo) { lblTipo.setText(testo); }

  /**
   * Imposta il testo e il colore dell'etichetta del prezzo.
   *
   * @param testo testo del prezzo
   * @param colore colore dell'etichetta
   */
  public void setPrezzoInfo(String testo, Color colore) {
    lblPrezzo.setText(testo);
    lblPrezzo.setForeground(colore);
  }

  /**
   * Imposta il testo dell'username del pubblicatore.
   *
   * @param nome username
   * @param cliccabile true quando l'etichetta dovrebbe apparire cliccabile
   */
  public void setUtenteNome(String nome, boolean cliccabile) {
    String valore = nome != null ? nome : "";
    if (cliccabile) {
      lblUtenteNome.setText("<html><u>" + valore + "</u></html>");
      lblUtenteNome.setForeground(new Color(0, 102, 204));
      lblUtenteNome.setCursor(new Cursor(Cursor.HAND_CURSOR));
      lblUtenteNome.setToolTipText("Apri profilo");
    } else {
      lblUtenteNome.setText(valore);
      Color base = UIManager.getColor("Label.foreground");
      lblUtenteNome.setForeground(base != null ? base : Color.BLACK);
      lblUtenteNome.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
      lblUtenteNome.setToolTipText(null);
    }
  }

  /**
   * Aggiunge un mouse listener all'etichetta del pubblicatore.
   *
   * @param listener mouse listener
   */
  public void addUtenteListener(MouseListener listener) {
    lblUtenteNome.addMouseListener(listener);
  }

  /**
   * Imposta il testo dell'etichetta del contatore delle immagini.
   *
   * @param testo testo del contatore
   */
  public void setContatoreImmagini(String testo) { lblContatoreImmagini.setText(testo); }

  /**
   * Imposta l'icona dell'immagine, ridimensionandola alle dimensioni dell'etichetta.
   *
   * @param icon icona dell'immagine
   */
  public void setImmagine(ImageIcon icon) {
    int maxW = lblImmagine.getWidth();
    int maxH = lblImmagine.getHeight();

    if (maxW <= 0 || maxH <= 0) {
      maxW = 400;
      maxH = 300;
    }

    int origW = icon.getIconWidth();
    int origH = icon.getIconHeight();

    if (origW <= 0 || origH <= 0) {
      lblImmagine.setIcon(icon);
      lblImmagine.setText("");
      return;
    }

    // Calcola scala per preservare aspect ratio
    double scale = Math.min((double) maxW / origW, (double) maxH / origH);
    int targetW = (int) Math.round(origW * scale);
    int targetH = (int) Math.round(origH * scale);

    Image img = icon.getImage().getScaledInstance(targetW, targetH, Image.SCALE_SMOOTH);
    lblImmagine.setIcon(new ImageIcon(img));
    lblImmagine.setText("");
  }

  /**
   * Cancella l'icona dell'immagine e imposta un testo segnaposto.
   *
   * @param testo testo segnaposto
   */
  public void setImmagineTesto(String testo) {
    lblImmagine.setIcon(null);
    lblImmagine.setText(testo);
  }

  /**
   * Nasconde pannello immagini.
   */
  public void nascondiPannelloImmagini() {
    if (imagePanel != null) imagePanel.setVisible(false);
  }

  /**
   * Mostra pannello immagini.
   */
  public void mostraPannelloImmagini() {
    if (imagePanel != null) imagePanel.setVisible(true);
  }

}
