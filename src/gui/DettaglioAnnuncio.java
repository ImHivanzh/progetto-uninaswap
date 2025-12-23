package gui;

import controller.DettaglioAnnuncioController;
import model.Annuncio;

import javax.swing.*;
import java.awt.*;

public class DettaglioAnnuncio extends JFrame {
  private JPanel mainPanel;
  private JLabel lblTitolo;
  private JLabel lblPrezzo;
  private JLabel lblCategoria;
  private JTextArea txtDescrizione;
  private JLabel lblImmagine;
  private JButton btnPrecedente;
  private JButton btnSuccessivo;
  private JLabel lblUtente;
  private JButton btnContatta;
  private JButton btnIndietro;
  private JLabel lblTipo;
  private JLabel lblCondizione;
  private JPanel imagePanel;
  private JLabel lblContatoreImmagini;

  private DettaglioAnnuncioController controller;

  /**
   * Creates the listing detail frame.
   *
   * @param annuncio listing to display
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
   * Registers button listeners for the view.
   */
  private void setupListeners() {
    btnIndietro.addActionListener(e -> dispose());
    btnContatta.addActionListener(e -> controller.azioneContatta());
    btnPrecedente.addActionListener(e -> controller.azionePrecedente());
    btnSuccessivo.addActionListener(e -> controller.azioneSuccessiva());
  }

  /**
   * Sets the title label text.
   *
   * @param titolo title text
   */
  public void setTitolo(String titolo) { lblTitolo.setText(titolo); }

  /**
   * Sets the description text.
   *
   * @param descrizione description text
   */
  public void setDescrizione(String descrizione) { txtDescrizione.setText(descrizione); }

  /**
   * Sets the category label text.
   *
   * @param testo category text
   */
  public void setCategoria(String testo) { lblCategoria.setText(testo); }

  /**
   * Sets the type label text.
   *
   * @param testo type text
   */
  public void setTipo(String testo) { lblTipo.setText(testo); }

  /**
   * Sets the condition label text.
   *
   * @param testo condition text
   */
  public void setCondizione(String testo) { lblCondizione.setText(testo); }

  /**
   * Sets the price label text and color.
   *
   * @param testo price text
   * @param colore label color
   */
  public void setPrezzoInfo(String testo, Color colore) {
    lblPrezzo.setText(testo);
    lblPrezzo.setForeground(colore);
  }

  /**
   * Sets the user info label text.
   *
   * @param testo user info text
   */
  public void setUtenteInfo(String testo) { lblUtente.setText(testo); }

  /**
   * Sets the image counter label text.
   *
   * @param testo counter text
   */
  public void setContatoreImmagini(String testo) { lblContatoreImmagini.setText(testo); }

  /**
   * Sets the image icon, scaling it to the label size.
   *
   * @param icon image icon
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
   * Clears the image icon and sets placeholder text.
   *
   * @param testo placeholder text
   */
  public void setImmagineTesto(String testo) {
    lblImmagine.setIcon(null);
    lblImmagine.setText(testo);
  }

  /**
   * Hides the image panel.
   */
  public void nascondiPannelloImmagini() {
    if (imagePanel != null) imagePanel.setVisible(false);
  }

  /**
   * Shows the image panel.
   */
  public void mostraPannelloImmagini() {
    if (imagePanel != null) imagePanel.setVisible(true);
  }

  /**
   * Initializes custom UI components.
   */
  private void createUIComponents() { }
}
