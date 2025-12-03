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

    // CORRETTO: Rinominato da 'passDimenticataLabel' a 'forgotPassLabel'
    // per corrispondere al binding nel file LoginPage.form
    private JLabel forgotPassLabel;

    public LoginForm() {
        super("Login"); // Imposta il titolo usando il costruttore di BaseFrame

        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();

        centraFinestra(); // Usa il metodo di BaseFrame
    }

    // --- GETTERS ---
    public String getUsername() {
        return userField.getText();
    }

    public String getPassword() {
        return new String(passField.getPassword());
    }

    // --- LISTENERS ---
    public void addLoginListener(ActionListener listener) {
        loginButton.addActionListener(listener);
    }

    public void addRegisterListener(MouseListener listener) {
        registerLabel.addMouseListener(listener);
    }

    public void addForgotPassListener(MouseListener listener) {
        // Usa il nome corretto della variabile
        forgotPassLabel.addMouseListener(listener);
    }
}