package gui;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

/**
 * Form di login applicazione.
 */
public class LoginForm extends BaseFrame {
    /**
     * Pannello principale.
     */
    private JPanel mainPanel;
    /**
     * Campo username.
     */
    private JTextField userField;
    /**
     * Campo password.
     */
    private JPasswordField passField;
    /**
     * Pulsante login.
     */
    private JButton loginButton;
    /**
     * Etichetta registrazione.
     */
    private JLabel registerLabel;
    /**
     * Etichetta password dimenticata.
     */
    private JLabel forgotPassLabel;

    /**
     * Crea la finestra del form di login.
     */
    public LoginForm() {
        super("Login");

        setContentPane(mainPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();

        centraFinestra();
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
     * Restituisce la password inserita.
     *
     * @return password
     */
    public String getPassword() {
        return new String(passField.getPassword());
    }

    /**
     * Aggiunge un listener per l'azione di login.
     *
     * @param listener listener dell'azione
     */
    public void addLoginListener(ActionListener listener) {
        loginButton.addActionListener(listener);
    }

    /**
     * Aggiunge un mouse listener per la registrazione.
     *
     * @param listener mouse listener
     */
    public void addRegisterListener(MouseListener listener) {
        registerLabel.addMouseListener(listener);
    }

    /**
     * Aggiunge un mouse listener per il recupero password.
     *
     * @param listener mouse listener
     */
    public void addForgotPassListener(MouseListener listener) {
        forgotPassLabel.addMouseListener(listener);
    }
}
