package gui;

import javax.swing.*;
import java.awt.event.ActionListener;

public class PassDimenticataForm extends JFrame {
    private JPanel mainPanel;
    private JTextField UserField;
    private JPasswordField NPassField;
    private JPasswordField CPassField;
    private JButton loginButton;

    /**
     * Creates the password recovery form.
     */
    public PassDimenticataForm() {
        setContentPane(mainPanel);
        setTitle("Recupero Password");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }

    /**
     * Returns the username input.
     *
     * @return username
     */
    public String getUsername() {
        return UserField.getText();
    }

    /**
     * Returns the new password input.
     *
     * @return new password
     */
    public String getNuovaPassword() {
        return new String(NPassField.getPassword());
    }

    /**
     * Returns the confirmation password input.
     *
     * @return confirmation password
     */
    public String getConfermaPassword() {
        return new String(CPassField.getPassword());
    }

    /**
     * Adds the submit action listener.
     *
     * @param listener action listener
     */
    public void addInvioListener(ActionListener listener) {
        loginButton.addActionListener(listener);
    }

    /**
     * Shows an informational message dialog.
     *
     * @param messaggio message text
     */
    public void mostraMessaggio(String messaggio) {
        JOptionPane.showMessageDialog(this, messaggio, "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Shows an error message dialog.
     *
     * @param errore error text
     */
    public void mostraErrore(String errore) {
        JOptionPane.showMessageDialog(this, errore, "Errore", JOptionPane.ERROR_MESSAGE);
    }
}
