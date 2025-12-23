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
     * Creates the login form frame.
     */
    public LoginForm() {
        super("Login");

        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();

        centraFinestra();
    }

    /**
     * Returns the username input.
     *
     * @return username
     */
    public String getUsername() {
        return userField.getText();
    }

    /**
     * Returns the password input.
     *
     * @return password text
     */
    public String getPassword() {
        return new String(passField.getPassword());
    }

    /**
     * Adds the login action listener.
     *
     * @param listener action listener
     */
    public void addLoginListener(ActionListener listener) {
        loginButton.addActionListener(listener);
    }

    /**
     * Adds the registration mouse listener.
     *
     * @param listener mouse listener
     */
    public void addRegisterListener(MouseListener listener) {
        registerLabel.addMouseListener(listener);
    }

    /**
     * Adds the forgot password mouse listener.
     *
     * @param listener mouse listener
     */
    public void addForgotPassListener(MouseListener listener) {
        forgotPassLabel.addMouseListener(listener);
    }
}
