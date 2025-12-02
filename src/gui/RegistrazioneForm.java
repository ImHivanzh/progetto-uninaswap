package gui;

import javax.swing.*;
import java.awt.event.ActionListener;

public class RegistrazioneForm extends JFrame {
  private JPanel mainPanel;
  private JTextField txtUsername;
  private JTextField txtMail;
  private JPasswordField txtPassword;
  private JTextField txtTelefono;
  private JButton btnRegistra;

  public RegistrazioneForm() {
    setContentPane(mainPanel);
    setTitle("Registrazione");
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Chiude solo questa finestra, non l'app
    pack();
    setLocationRelativeTo(null);
  }

  // --- GETTERS (Per il Controller) ---
  public String getUsername() {
    return txtUsername.getText();
  }

  public String getMail() {
    return txtMail.getText();
  }

  public String getPassword() {
    return new String(txtPassword.getPassword());
  }

  public String getTelefono() {
    return txtTelefono.getText();
  }

  // --- LISTENER (Per il Controller) ---
  public void addRegistraListener(ActionListener listener) {
    btnRegistra.addActionListener(listener);
  }

  // --- METODI DI UTILITÀ ---
  public void mostraMessaggio(String messaggio) {
    JOptionPane.showMessageDialog(this, messaggio, "Info", JOptionPane.INFORMATION_MESSAGE);
  }

  public void mostraErrore(String errore) {
    JOptionPane.showMessageDialog(this, errore, "Errore", JOptionPane.ERROR_MESSAGE);
  }
}