package gui;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

public class LoginForm extends BaseFrame {
    private JPanel mainPanel;
    private JTextField userField;
    private JPasswordField passField;
    private JButton loginButton;
    private JLabel registerLabel;
    private JLabel forgotPassLabel;

    /**
     * Crea form login finestra.
     */
    public LoginForm() {
        super("Login");

        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();

        centraFinestra();
    }

    /**
     * Restituisce username input.
     *
     * @return username
     */
    public String getUsername() {
        return userField.getText();
    }

    /**
     * Restituisce input password.
     *
     * @return password testo
     */
    public String getPassword() {
        return new String(passField.getPassword());
    }

    /**
     * Aggiunge login azione listener.
     *
     * @param listener azione listener
     */
    public void addLoginListener(ActionListener listener) {
        loginButton.addActionListener(listener);
    }

    /**
     * Aggiunge registrazione mouse listener.
     *
     * @param listener mouse listener
     */
    public void addRegisterListener(MouseListener listener) {
        registerLabel.addMouseListener(listener);
    }

    /**
     * Aggiunge dimenticata password mouse listener.
     *
     * @param listener mouse listener
     */
    public void addForgotPassListener(MouseListener listener) {
        forgotPassLabel.addMouseListener(listener);
    }
}
