package gui;

import controller.DettaglioAnnuncioController;
import model.Annuncio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;

public class DettaglioAnnuncio extends JFrame {
  private JPanel mainPanel;
  private JLabel lblTitolo;
  private JLabel lblPrezzo;
  private JLabel lblCategoria;
  private JTextArea txtDescrizione;
  private JLabel lblImmagine;
  private JButton btnPrecedente;
  private JButton btnSuccessivo;
  private JLabel lblUtentePrefix;
  private JLabel lblUtenteNome;
  private JButton btnContatta;
  private JButton btnIndietro;
  private JLabel lblTipo;
  private JLabel lblCondizione;
  private JPanel imagePanel;
  private JLabel lblContatoreImmagini;

  private DettaglioAnnuncioController controller;

  /**
   * Crea finestra dettaglio annuncio.
   *
   * @param annuncio annuncio a visualizza
   */
  public DettaglioAnnuncio(Annuncio annuncio) {
    setContentPane(mainPanel);
    setTitle("UninaSwap - Dettaglio Annuncio");
    setSize(800, 600);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setLocationRelativeTo(null);

    this.controller = new DettaglioAnnuncioController(this, annuncio);

    setupListeners();
  }

  /**
   * Registra pulsante listener per vista.
   */
  private void setupListeners() {
    btnIndietro.addActionListener(e -> dispose());
    btnContatta.addActionListener(e -> controller.azioneContatta());
    btnPrecedente.addActionListener(e -> controller.azionePrecedente());
    btnSuccessivo.addActionListener(e -> controller.azioneSuccessiva());
  }

  /**
   * Imposta titolo etichetta testo.
   *
   * @param titolo titolo testo
   */
  public void setTitolo(String titolo) { lblTitolo.setText(titolo); }

  /**
   * Imposta descrizione testo.
   *
   * @param descrizione descrizione testo
   */
  public void setDescrizione(String descrizione) { txtDescrizione.setText(descrizione); }

  /**
   * Imposta categoria etichetta testo.
   *
   * @param testo categoria testo
   */
  public void setCategoria(String testo) { lblCategoria.setText(testo); }

  /**
   * Imposta tipo etichetta testo.
   *
   * @param testo tipo testo
   */
  public void setTipo(String testo) { lblTipo.setText(testo); }

  /**
   * Imposta condizione etichetta testo.
   *
   * @param testo condizione testo
   */
  public void setCondizione(String testo) { lblCondizione.setText(testo); }

  /**
   * Imposta prezzo etichetta testo e colore.
   *
   * @param testo prezzo testo
   * @param colore etichetta colore
   */
  public void setPrezzoInfo(String testo, Color colore) {
    lblPrezzo.setText(testo);
    lblPrezzo.setForeground(colore);
  }

  /**
   * Imposta pubblicatore username testo.
   *
   * @param nome username
   * @param cliccabile true quando etichetta dovrebbe aspetto cliccabile
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
   * Aggiunge mouse listener a pubblicatore etichetta.
   *
   * @param listener mouse listener
   */
  public void addUtenteListener(MouseListener listener) {
    lblUtenteNome.addMouseListener(listener);
  }

  /**
   * Imposta etichetta contatore immagini testo.
   *
   * @param testo contatore testo
   */
  public void setContatoreImmagini(String testo) { lblContatoreImmagini.setText(testo); }

  /**
   * Imposta icona immagine, ridimensionandola a etichetta dimensione.
   *
   * @param icon icona immagine
   */
  public void setImmagine(ImageIcon icon) {
    int w = lblImmagine.getWidth();
    int h = lblImmagine.getHeight();

    if (w <= 0 || h <= 0) {
      w = 400;
      h = 300;
    }

    Image img = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
    lblImmagine.setIcon(new ImageIcon(img));
    lblImmagine.setText("");
  }

  /**
   * Cancella icona immagine e imposta segnaposto testo.
   *
   * @param testo segnaposto testo
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

  /**
   * Inizializza personalizzati UI componenti.
   */
  private void createUIComponents() { }
}
