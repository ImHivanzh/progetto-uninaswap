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

  public DettaglioAnnuncio(Annuncio annuncio) {
    setContentPane(mainPanel);
    setTitle("UninaSwap - Dettaglio Annuncio");
    setSize(800, 600);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setLocationRelativeTo(null);

    this.controller = new DettaglioAnnuncioController(this, annuncio);

    setupListeners();
  }

  private void setupListeners() {
    btnIndietro.addActionListener(e -> dispose());
    btnContatta.addActionListener(e -> controller.azioneContatta());
    btnPrecedente.addActionListener(e -> controller.azionePrecedente());
    btnSuccessivo.addActionListener(e -> controller.azioneSuccessiva());
  }

  public void setTitolo(String titolo) { lblTitolo.setText(titolo); }
  public void setDescrizione(String descrizione) { txtDescrizione.setText(descrizione); }
  public void setCategoria(String testo) { lblCategoria.setText(testo); }
  public void setTipo(String testo) { lblTipo.setText(testo); }
  public void setCondizione(String testo) { lblCondizione.setText(testo); }

  public void setPrezzoInfo(String testo, Color colore) {
    lblPrezzo.setText(testo);
    lblPrezzo.setForeground(colore);
  }
  public void setUtenteInfo(String testo) { lblUtente.setText(testo); }
  public void setContatoreImmagini(String testo) { lblContatoreImmagini.setText(testo); }

  // [FIX WIDTH 0] Gestisce il ridimensionamento sicuro
  public void setImmagine(ImageIcon icon) {
    int w = lblImmagine.getWidth();
    int h = lblImmagine.getHeight();

    if (w <= 0 || h <= 0) {
      w = 400; // Dimensioni di default
      h = 300;
    }

    Image img = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
    lblImmagine.setIcon(new ImageIcon(img));
    lblImmagine.setText("");
  }

  public void setImmagineTesto(String testo) {
    lblImmagine.setIcon(null);
    lblImmagine.setText(testo);
  }

  // Metodi per gestire la visibilità del pannello immagini
  public void nascondiPannelloImmagini() {
    if (imagePanel != null) imagePanel.setVisible(false);
  }

  public void mostraPannelloImmagini() {
    if (imagePanel != null) imagePanel.setVisible(true);
  }

  private void createUIComponents() { }
}