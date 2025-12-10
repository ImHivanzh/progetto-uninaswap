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

  // Riferimento al controller
  private DettaglioAnnuncioController controller;

  public DettaglioAnnuncio(Annuncio annuncio) {
    // Configurazione Frame
    setContentPane(mainPanel);
    setTitle("UninaSwap - Dettaglio Annuncio");
    setSize(800, 600);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setLocationRelativeTo(null);

    // Inizializza il controller passando questa vista e il modello
    this.controller = new DettaglioAnnuncioController(this, annuncio);

    setupListeners();
  }

  private void setupListeners() {
    btnIndietro.addActionListener(e -> dispose());

    btnContatta.addActionListener(e -> controller.azioneContatta());

    btnPrecedente.addActionListener(e -> controller.azionePrecedente());

    btnSuccessivo.addActionListener(e -> controller.azioneSuccessiva());
  }

  // --- Metodi Setter per la Vista (usati dal Controller) ---

  public void setTitolo(String titolo) {
    lblTitolo.setText(titolo);
  }

  public void setDescrizione(String descrizione) {
    txtDescrizione.setText(descrizione);
  }

  public void setCategoria(String testo) {
    lblCategoria.setText(testo);
  }

  public void setTipo(String testo) {
    lblTipo.setText(testo);
  }

  public void setCondizione(String testo) {
    lblCondizione.setText(testo);
  }

  public void setPrezzoInfo(String testo, Color colore) {
    lblPrezzo.setText(testo);
    lblPrezzo.setForeground(colore);
  }

  public void setUtenteInfo(String testo) {
    lblUtente.setText(testo);
  }

  public void setContatoreImmagini(String testo) {
    lblContatoreImmagini.setText(testo);
  }

  public void setImmagine(ImageIcon icon) {
    // Ridimensiona e setta l'icona
    Image img = icon.getImage().getScaledInstance(lblImmagine.getWidth(), lblImmagine.getHeight(), Image.SCALE_SMOOTH);
    lblImmagine.setIcon(new ImageIcon(img));
    lblImmagine.setText(""); // Rimuove testo se c'è immagine
  }

  public void setImmagineTesto(String testo) {
    lblImmagine.setIcon(null);
    lblImmagine.setText(testo);
  }

  public void mostraImmaginePlaceholder() {
    lblImmagine.setIcon(null);
    lblImmagine.setText("Nessuna immagine disponibile");
  }

  // Metodo necessario per il GUI Designer
  private void createUIComponents() {
    // TODO: place custom component creation code here
  }
}