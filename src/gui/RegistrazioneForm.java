package gui;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * Form per registrazione utente.
 */
public class RegistrazioneForm extends BaseFrame {
    /**
     * Pannello principale.
     */
    private JPanel mainPanel;
    /**
     * Campo username.
     */
    private JTextField txtUsername;
    /**
     * Campo email.
     */
    private JTextField txtMail;
    /**
     * Campo password.
     */
    private JPasswordField txtPassword;
    /**
     * Campo telefono.
     */
    private JTextField txtTelefono;
    /**
     * Pulsante registra.
     */
    private JButton btnRegistra;

    /**
     * Crea form registrazione.
     */
    public RegistrazioneForm() {
        super("Registrazione");
        
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        
        centraFinestra();
    }

    /**
     * Restituisce username input.
     *
     * @return username
     */
    public String getUsername() {
        return txtUsername.getText();
    }

    /**
     * Restituisce input email.
     *
     * @return email
     */
    public String getMail() {
        return txtMail.getText();
    }

    /**
     * Restituisce input password.
     *
     * @return password
     */
    public String getPassword() {
        return new String(txtPassword.getPassword());
    }

    /**
     * Restituisce numero telefono input.
     *
     * @return numero telefono
     */
    public String getTelefono() {
        return txtTelefono.getText();
    }

    /**
     * Aggiunge registrazione azione listener.
     *
     * @param listener azione listener
     */
    public void addRegistraListener(ActionListener listener) {
        btnRegistra.addActionListener(listener);
    }
}
