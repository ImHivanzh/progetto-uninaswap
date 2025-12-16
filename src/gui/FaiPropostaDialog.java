package gui;

import controller.FaiPropostaController;
import model.enums.TipoAnnuncio;

import javax.swing.*;
import java.awt.*;

public class FaiPropostaDialog extends JDialog {
  private JPanel contentPane;
  private JPanel pnlPrezzo;
  private JTextField txtPrezzo;
  private JLabel lblDescrizione;
  private JTextArea txtDescrizione;
  private JPanel pnlImmagine;
  private JButton btnCaricaImmagine;
  private JLabel lblImagePreview;
  private JButton btnInvia;
  private JButton btnAnnulla;

  private FaiPropostaController controller;

  public FaiPropostaDialog(Frame owner, TipoAnnuncio tipoAnnuncio) {
    super(owner, "Fai una Proposta", true);
    setContentPane(contentPane);
    getRootPane().setDefaultButton(btnInvia);

    // Inizializza il controller
    this.controller = new FaiPropostaController(this, tipoAnnuncio);

    // Configurazione UI in base al tipo
    configuraInterfaccia(tipoAnnuncio);

    // Setup Listener
    setupListeners();

    pack();
    setLocationRelativeTo(owner);
  }

  private void configuraInterfaccia(TipoAnnuncio tipo) {
    // Mostra/Nascondi campo Prezzo
    pnlPrezzo.setVisible(tipo == TipoAnnuncio.VENDITA);

    // Cambia testo etichetta
    if (tipo == TipoAnnuncio.SCAMBIO) {
      lblDescrizione.setText("Descrizione oggetto (Obbligatorio):");
    } else {
      lblDescrizione.setText("Messaggio per l'utente:");
    }

    // Mostra/Nascondi caricamento Immagine
    pnlImmagine.setVisible(tipo == TipoAnnuncio.SCAMBIO);
  }

  private void setupListeners() {
    btnInvia.addActionListener(e -> controller.azioneConferma());
    btnAnnulla.addActionListener(e -> controller.azioneAnnulla());
    btnCaricaImmagine.addActionListener(e -> controller.azioneCaricaImmagine());
  }

  // --- Metodi getter/setter per il Controller ---

  public String getPrezzoInput() {
    return txtPrezzo.getText();
  }

  public String getDescrizioneInput() {
    return txtDescrizione.getText();
  }

  public void aggiornaAnteprimaImmagine(byte[] imgData) {
    if (imgData != null) {
      ImageIcon icon = new ImageIcon(imgData);
      Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
      lblImagePreview.setIcon(new ImageIcon(img));
      lblImagePreview.setText("");
    }
  }

  public void mostraErrore(String messaggio) {
    JOptionPane.showMessageDialog(this, messaggio, "Errore", JOptionPane.ERROR_MESSAGE);
  }

  public FaiPropostaController getController() {
    return controller;
  }
}