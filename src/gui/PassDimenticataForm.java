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
    private JTextField userField;
    /**
     * Campo nuova password.
     */
    private JPasswordField nPassField;
    /**
     * Campo conferma password.
     */
    private JPasswordField cPassField;
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
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }

    /**
     * Restituisce l'username inserito.
     *
     * @return username
     */
    public String getUsername() {
        return userField.getText();
    }

    /**
     * Restituisce la nuova password inserita.
     *
     * @return nuova password
     */
    public String getNuovaPassword() {
        return new String(nPassField.getPassword());
    }

    /**
     * Restituisce la password di conferma inserita.
     *
     * @return password di conferma
     */
    public String getConfermaPassword() {
        return new String(cPassField.getPassword());
    }

    /**
     * Aggiunge un listener per l'azione di invio.
     *
     * @param listener listener dell'azione
     */
    public void addInvioListener(ActionListener listener) {
        loginButton.addActionListener(listener);
    }

    /**
     * Mostra un dialogo con un messaggio informativo.
     *
     * @param messaggio testo del messaggio
     */
    public void mostraMessaggio(String messaggio) {
        JOptionPane.showMessageDialog(this, messaggio, "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Mostra un dialogo con un messaggio di errore.
     *
     * @param errore testo dell'errore
     */
    public void mostraErrore(String errore) {
        JOptionPane.showMessageDialog(this, errore, "Errore", JOptionPane.ERROR_MESSAGE);
    }
}
