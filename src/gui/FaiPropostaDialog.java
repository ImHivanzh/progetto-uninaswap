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

  /**
   * Creates the proposal dialog for a given listing type.
   *
   * @param owner parent frame
   * @param tipoAnnuncio listing type
   */
  public FaiPropostaDialog(Frame owner, TipoAnnuncio tipoAnnuncio) {
    super(owner, "Fai una Proposta", true);
    setContentPane(contentPane);
    getRootPane().setDefaultButton(btnInvia);

    this.controller = new FaiPropostaController(this, tipoAnnuncio);
    configuraInterfaccia(tipoAnnuncio);
    setupListeners();

    pack();
    setLocationRelativeTo(owner);
  }

  /**
   * Configures the UI based on the listing type.
   *
   * @param tipo listing type
   */
  private void configuraInterfaccia(TipoAnnuncio tipo) {
    pnlPrezzo.setVisible(tipo == TipoAnnuncio.VENDITA);

    if (tipo == TipoAnnuncio.SCAMBIO) {
      lblDescrizione.setText("Descrizione oggetto (Obbligatorio):");
    } else {
      lblDescrizione.setText("Messaggio per l'utente:");
    }

    pnlImmagine.setVisible(tipo == TipoAnnuncio.SCAMBIO);
  }

  /**
   * Registers dialog button listeners.
   */
  private void setupListeners() {
    btnInvia.addActionListener(e -> controller.azioneConferma());
    btnAnnulla.addActionListener(e -> controller.azioneAnnulla());
    btnCaricaImmagine.addActionListener(e -> controller.azioneCaricaImmagine());
  }

  /**
   * Returns the price input text.
   *
   * @return price input
   */
  public String getPrezzoInput() {
    return txtPrezzo.getText();
  }

  /**
   * Returns the description input text.
   *
   * @return description input
   */
  public String getDescrizioneInput() {
    return txtDescrizione.getText();
  }

  /**
   * Updates the image preview area.
   *
   * @param imgData image bytes
   */
  public void aggiornaAnteprimaImmagine(byte[] imgData) {
    if (imgData != null) {
      ImageIcon icon = new ImageIcon(imgData);
      Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
      lblImagePreview.setIcon(new ImageIcon(img));
      lblImagePreview.setText("");
    }
  }

  /**
   * Shows an error dialog.
   *
   * @param messaggio error text
   */
  public void mostraErrore(String messaggio) {
    JOptionPane.showMessageDialog(this, messaggio, "Errore", JOptionPane.ERROR_MESSAGE);
  }

  /**
   * Returns the controller instance.
   *
   * @return controller
   */
  public FaiPropostaController getController() {
    return controller;
  }
}
