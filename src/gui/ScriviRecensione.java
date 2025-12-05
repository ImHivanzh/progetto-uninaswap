package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ScriviRecensione extends BaseFrame {
    private JPanel mainPanel;
    private JPanel pnlStelle; // Contenitore per le stelle
    private JTextArea txtDescrizione;
    private JButton btnInvia;

    // Array per gestire i pulsanti delle stelle
    private final JButton[] stelleButtons = new JButton[5];
    // Variabile per tenere traccia del voto (default 5)
    private int votoCorrente = 5;

    // Colori per le stelle
    private final Color COLORE_PIENA = new Color(255, 200, 0); // Oro/Arancione
    private final Color COLORE_VUOTA = Color.LIGHT_GRAY;

    public ScriviRecensione() {
        super("Scrivi Recensione");
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        inizializzaStelle(); // Crea i pulsanti a stella

        pack();
        centraFinestra();
    }

    private void inizializzaStelle() {
        pnlStelle.removeAll();

        for (int i = 0; i < 5; i++) {
            JButton stella = new JButton("★"); // Simbolo stella unicode
            stella.setFont(new Font("SansSerif", Font.BOLD, 24)); // Font grande
            stella.setForeground(COLORE_PIENA); // Colore iniziale

            stella.setBorderPainted(false);
            stella.setContentAreaFilled(false);
            stella.setFocusPainted(false);
            stella.setCursor(new Cursor(Cursor.HAND_CURSOR));

            final int votoStella = i + 1; // Voto da 1 a 5

            stella.addActionListener(e -> {
                setVoto(votoStella);
            });

            stelleButtons[i] = stella;
            pnlStelle.add(stella);
        }

        aggiornaGraficaStelle();
    }

    /**
     * Imposta il voto e aggiorna la visualizzazione
     */
    private void setVoto(int voto) {
        this.votoCorrente = voto;
        aggiornaGraficaStelle();
    }

    /**
     * Ricolora le stelle in base al voto corrente.
     * Es: se voto è 3, le prime 3 sono Oro, le altre 2 Grigie.
     */
    private void aggiornaGraficaStelle() {
        for (int i = 0; i < 5; i++) {
            if (i < votoCorrente) {
                stelleButtons[i].setText("★");
                stelleButtons[i].setForeground(COLORE_PIENA);
            } else {
                stelleButtons[i].setText("☆"); // O lascia ★ ma cambia solo colore
                stelleButtons[i].setForeground(COLORE_VUOTA);
            }
        }
        pnlStelle.repaint();
    }

    // --- GETTERS ---

    public int getVoto() {
        return votoCorrente;
    }

    public String getDescrizione() {
        return txtDescrizione.getText();
    }

    // --- LISTENERS ---

    public void addInviaListener(ActionListener listener) {
        btnInvia.addActionListener(listener);
    }
}