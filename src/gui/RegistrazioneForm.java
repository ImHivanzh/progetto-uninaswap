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
     * Creates the registration form.
     */
    public RegistrazioneForm() {
        super("Registrazione");
        
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        
        centraFinestra();
    }

    /**
     * Returns the username input.
     *
     * @return username
     */
    public String getUsername() {
        return txtUsername.getText();
    }

    /**
     * Returns the email input.
     *
     * @return email
     */
    public String getMail() {
        return txtMail.getText();
    }

    /**
     * Returns the password input.
     *
     * @return password
     */
    public String getPassword() {
        return new String(txtPassword.getPassword());
    }

    /**
     * Returns the phone number input.
     *
     * @return phone number
     */
    public String getTelefono() {
        return txtTelefono.getText();
    }

    /**
     * Adds the registration action listener.
     *
     * @param listener action listener
     */
    public void addRegistraListener(ActionListener listener) {
        btnRegistra.addActionListener(listener);
    }
}
