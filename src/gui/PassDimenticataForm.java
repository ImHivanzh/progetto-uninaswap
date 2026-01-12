package gui;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * Form per recupero password.
 */
public class PassDimenticataForm extends JFrame {
    /**
     * Pannello principale.
     */
    private JPanel mainPanel;
    /**
     * Campo username.
     */
    private JTextField UserField;
    /**
     * Campo nuova password.
     */
    private JPasswordField NPassField;
    /**
     * Campo conferma password.
     */
    private JPasswordField CPassField;
    /**
     * Pulsante invio.
     */
    private JButton loginButton;

    /**
     * Crea form recupero password.
     */
    public PassDimenticataForm() {
        setContentPane(mainPanel);
        setTitle("Recupero Password");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }

    /**
     * Restituisce username input.
     *
     * @return username
     */
    public String getUsername() {
        return UserField.getText();
    }

    /**
     * Restituisce nuovo input password.
     *
     * @return nuovo password
     */
    public String getNuovaPassword() {
        return new String(NPassField.getPassword());
    }

    /**
     * Restituisce conferma input password.
     *
     * @return conferma password
     */
    public String getConfermaPassword() {
        return new String(CPassField.getPassword());
    }

    /**
     * Aggiunge invia azione listener.
     *
     * @param listener azione listener
     */
    public void addInvioListener(ActionListener listener) {
        loginButton.addActionListener(listener);
    }

    /**
     * Mostra informativo messaggio dialogo.
     *
     * @param messaggio messaggio testo
     */
    public void mostraMessaggio(String messaggio) {
        JOptionPane.showMessageDialog(this, messaggio, "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Mostra errore messaggio dialogo.
     *
     * @param errore errore testo
     */
    public void mostraErrore(String errore) {
        JOptionPane.showMessageDialog(this, errore, "Errore", JOptionPane.ERROR_MESSAGE);
    }
}
