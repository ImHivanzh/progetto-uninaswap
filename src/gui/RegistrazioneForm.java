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
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        
        centraFinestra();
    }

    /**
     * Restituisce l'username inserito.
     *
     * @return username
     */
    public String getUsername() {
        return txtUsername.getText();
    }

    /**
     * Restituisce l'email inserita.
     *
     * @return email
     */
    public String getMail() {
        return txtMail.getText();
    }

    /**
     * Restituisce la password inserita.
     *
     * @return password
     */
    public String getPassword() {
        return new String(txtPassword.getPassword());
    }

    /**
     * Restituisce il numero di telefono inserito.
     *
     * @return numero telefono
     */
    public String getTelefono() {
        return txtTelefono.getText();
    }

    /**
     * Aggiunge un listener per l'azione di registrazione.
     *
     * @param listener listener dell'azione
     */
    public void addRegistraListener(ActionListener listener) {
        btnRegistra.addActionListener(listener);
    }
}
