package gui;

import controller.FaiPropostaController;
import model.enums.TipoAnnuncio;

import javax.swing.*;
import java.awt.*;

/**
 * Dialogo per invio proposta.
 */
public class FaiPropostaDialog extends JDialog {
  /**
   * Pannello contenuto.
   */
  private JPanel contentPane;
  /**
   * Pannello prezzo.
   */
  private JPanel pnlPrezzo;
  /**
   * Campo prezzo.
   */
  private JTextField txtPrezzo;
  /**
   * Etichetta descrizione.
   */
  private JLabel lblDescrizione;
  /**
   * Area testo descrizione.
   */
  private JTextArea txtDescrizione;
  /**
   * Pannello immagine.
   */
  private JPanel pnlImmagine;
  /**
   * Pulsante carica immagine.
   */
  private JButton btnCaricaImmagine;
  /**
   * Etichetta anteprima immagine.
   */
  private JLabel lblImagePreview;
  /**
   * Pulsante invio.
   */
  private JButton btnInvia;
  /**
   * Pulsante annulla.
   */
  private JButton btnAnnulla;

  /**
   * Controller associato al dialogo.
   */
  private FaiPropostaController controller;

  /**
   * Crea dialogo proposta per fornito tipo annuncio.
   *
   * @param owner padre finestra
   * @param tipoAnnuncio tipo annuncio
   */
  public FaiPropostaDialog(Frame owner, TipoAnnuncio tipoAnnuncio) {
    super(owner, "Fai una Proposta", true);
    setContentPane(contentPane);
    setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    getRootPane().setDefaultButton(btnInvia);

    this.controller = new FaiPropostaController(this, tipoAnnuncio);
    configuraInterfaccia(tipoAnnuncio);
    setupListeners();

    pack();
    setLocationRelativeTo(owner);
  }

  /**
   * Configura UI basato in tipo annuncio.
   *
   * @param tipo tipo annuncio
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
   * Registra dialogo pulsante listener.
   */
  private void setupListeners() {
    btnInvia.addActionListener(e -> controller.azioneConferma());
    btnAnnulla.addActionListener(e -> controller.azioneAnnulla());
    btnCaricaImmagine.addActionListener(e -> controller.azioneCaricaImmagine());
  }

  /**
   * Restituisce input prezzo testo.
   *
   * @return input prezzo
   */
  public String getPrezzoInput() {
    return txtPrezzo.getText();
  }

  /**
   * Restituisce input descrizione testo.
   *
   * @return input descrizione
   */
  public String getDescrizioneInput() {
    return txtDescrizione.getText();
  }

  /**
   * Aggiorna area anteprima immagine.
   *
   * @param imgData byte immagine
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
   * Mostra errore dialogo.
   *
   * @param messaggio errore testo
   */
  public void mostraErrore(String messaggio) {
    JOptionPane.showMessageDialog(this, messaggio, "Errore", JOptionPane.ERROR_MESSAGE);
  }

  /**
   * Restituisce controller istanza.
   *
   * @return controller
   */
  public FaiPropostaController getController() {
    return controller;
  }
}
