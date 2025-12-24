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
