package gui;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class LoginForm extends JFrame {
    private JPanel mainPanel;
    private JTextField userField;
    private JPasswordField passField;
    private JButton loginButton;

    // Componenti dal .form
    private JLabel registerLabel;
    private JLabel forgotPassLabel;

    public LoginForm() {
        setContentPane(mainPanel);
        setTitle("Login Form");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 450);
        setLocationRelativeTo(null);

        getRootPane().setDefaultButton(loginButton);

        // Ho mantenuto qui la logica estetica (UI)
        configuraEffettiGrafici();
    }

    // --- Metodi per il Controller ---

    public String getUsername() {
        return userField.getText();
    }

    public String getPassword() {
        return new String(passField.getPassword());
    }

    public void addLoginListener(ActionListener listener) {
        loginButton.addActionListener(listener);
    }

    public void addRegisterListener(MouseListener listener) {
        registerLabel.addMouseListener(listener);
    }

    public void addForgotPassListener(MouseListener listener) {
        forgotPassLabel.addMouseListener(listener);
    }

    public void mostraErrore(String messaggio) {
        JOptionPane.showMessageDialog(this, messaggio, "Errore", JOptionPane.ERROR_MESSAGE);
    }

    public void mostraMessaggio(String messaggio) {
        JOptionPane.showMessageDialog(this, messaggio);
    }

    // --- Effetti Grafici (UI pura) ---
    private void configuraEffettiGrafici() {
        registerLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                registerLabel.setText("<html><u>Non sei registrato? Clicca qui</u></html>");
            }
            @Override
            public void mouseExited(MouseEvent e) {
                registerLabel.setText("Non sei registrato? Clicca qui");
            }
        });

        forgotPassLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                forgotPassLabel.setText("<html><u>Hai dimenticato la password?</u></html>");
            }
            @Override
            public void mouseExited(MouseEvent e) {
                forgotPassLabel.setText("Hai dimenticato la password?");
            }
        });
    }
}