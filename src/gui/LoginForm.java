package gui;

import javax.swing.*;
import java.awt.*;             // Import necessario per il cursore e colori
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

public class LoginForm extends JFrame {
    private JPanel mainPanel;
    private JTextField userField;
    private JPasswordField passField;
    private JButton loginButton;

    // 1. NUOVE VARIABILI (Devono esserci perché sono nel file .form XML)
    private JLabel registerLabel;
    private JLabel forgotPassLabel;

    public LoginForm() {
        // Configurazioni base del JFrame
        setContentPane(mainPanel);
        setTitle("Login Form");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 450); // Aumentato un po' per far stare tutto comodo
        setLocationRelativeTo(null); // Centra la finestra

        // --- GESTIONE BOTTONE LOGIN ---
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkLogin();
            }
        });

        // Imposta il bottone di default (invio da tastiera)
        getRootPane().setDefaultButton(loginButton);

        registerLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(LoginForm.this, "Apertura pagina registrazione...");
                // Qui metterai: new RegistrazioneForm().setVisible(true);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // Sottolinea quando passi sopra col mouse
                registerLabel.setText("<html><u>Non sei registrato? Clicca qui</u></html>");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Toglie la sottolineatura quando esci
                registerLabel.setText("Non sei registrato? Clicca qui");
            }
        });

        forgotPassLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(LoginForm.this, "Procedura recupero password avviata.");
            }

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


    private void checkLogin() {
        String user = userField.getText();
        char[] pass = passField.getPassword(); // Ottieni la password come array di char

        // Logica di controllo (Esempio: admin / 123)
        if (true) {
            JOptionPane.showMessageDialog(this, "Login effettuato con successo!");

            // Qui chiudi il login e apri l'app principale
            // dispose();
            // new MainApp().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Username o Password errati", "Errore Accesso", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginForm form = new LoginForm();
            form.setVisible(true);
        });
    }
}