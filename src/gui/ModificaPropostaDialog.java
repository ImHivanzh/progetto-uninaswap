package gui;

import controller.ModificaPropostaController;
import model.PropostaRiepilogo;
import model.enums.TipoAnnuncio;

import javax.swing.*;
import java.awt.*;

/**
 * Dialogo per modifica proposta.
 */
public class ModificaPropostaDialog extends JDialog {
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
   * Anteprima immagine.
   */
  private JLabel lblImagePreview;
  /**
   * Pulsante salva.
   */
  private JButton btnSalva;
  /**
   * Pulsante annulla.
   */
  private JButton btnAnnulla;

  /**
   * Controller dialogo.
   */
  private final ModificaPropostaController controller;

  /**
   * Crea dialogo modifica proposta.
   *
   * @param owner frame proprietario
   * @param proposta proposta da modificare
   */
  public ModificaPropostaDialog(Frame owner, PropostaRiepilogo proposta) {
    super(owner, "Modifica Proposta", true);
    setContentPane(contentPane);
    setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    getRootPane().setDefaultButton(btnSalva);

    this.controller = new ModificaPropostaController(this, proposta);
    configuraInterfaccia(TipoAnnuncio.valueOf(proposta.tipoAnnuncio().toUpperCase()));
    setupListeners();
    popolaDati();

    pack();
    setLocationRelativeTo(owner);
  }

  private void configuraInterfaccia(TipoAnnuncio tipo) {
    pnlPrezzo.setVisible(tipo == TipoAnnuncio.VENDITA);
    pnlImmagine.setVisible(tipo == TipoAnnuncio.SCAMBIO);
  }

  private void setupListeners() {
    btnSalva.addActionListener(e -> controller.azioneSalva());
    btnAnnulla.addActionListener(e -> controller.azioneAnnulla());
    btnCaricaImmagine.addActionListener(e -> controller.azioneCaricaImmagine());
  }

  private void popolaDati() {
    controller.popolaDati();
  }

  public String getPrezzoInput() {
    return txtPrezzo.getText();
  }

  public void setPrezzoInput(String prezzo) {
    txtPrezzo.setText(prezzo);
  }

  public String getDescrizioneInput() {
    return txtDescrizione.getText();
  }

  public void setDescrizioneInput(String descrizione) {
    txtDescrizione.setText(descrizione);
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

  public ModificaPropostaController getController() {
    return controller;
  }
}
