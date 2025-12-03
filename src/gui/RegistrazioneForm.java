package gui;

import javax.swing.*;
import java.awt.event.ActionListener;

public class RegistrazioneForm extends BaseFrame {
    private JPanel mainPanel;
    private JTextField txtUsername;
    private JTextField txtMail;
    private JPasswordField txtPassword;
    private JTextField txtTelefono;
    private JButton btnRegistra;

    public RegistrazioneForm() {
        super("Registrazione");
        
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Chiude solo questa finestra
        pack();
        
        centraFinestra();
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
}