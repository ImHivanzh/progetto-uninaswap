package gui;

import javax.swing.*;
import java.awt.event.ActionListener;

public class PassDimenticataForm extends JFrame {
    private JPanel mainPanel;
    private JTextField UserField;
    private JPasswordField NPassField;
    private JPasswordField CPassField;
    private JButton loginButton; // Questo è il tasto "Invio"

    public PassDimenticataForm() {
        setContentPane(mainPanel);
        setTitle("Recupero Password");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null); // Centra la finestra
    }

    // --- GETTERS ---
    public String getUsername() {
        return UserField.getText();
    }

    public String getNuovaPassword() {
        return new String(NPassField.getPassword());
    }

    public String getConfermaPassword() {
        return new String(CPassField.getPassword());
    }

    // --- LISTENERS ---
    public void addInvioListener(ActionListener listener) {
        loginButton.addActionListener(listener);
    }

    // --- METODI UTILI ---
    public void mostraMessaggio(String messaggio) {
        JOptionPane.showMessageDialog(this, messaggio, "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostraErrore(String errore) {
        JOptionPane.showMessageDialog(this, errore, "Errore", JOptionPane.ERROR_MESSAGE);
    }
}