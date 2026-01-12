package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Form per scrivere una recensione.
 */
public class ScriviRecensione extends BaseFrame {
    /**
     * Pannello principale.
     */
    private JPanel mainPanel;
    /**
     * Pannello stelle.
     */
    private JPanel pnlStelle;
    /**
     * Area testo descrizione.
     */
    private JTextArea txtDescrizione;
    /**
     * Pulsante invio.
     */
    private JButton btnInvia;

    /**
     * Pulsanti stelle voto.
     */
    private final JButton[] stelleButtons = new JButton[5];
    /**
     * Voto corrente selezionato.
     */
    private int votoCorrente = 5;

    /**
     * Colore stella selezionata.
     */
    private final Color COLORE_PIENA = new Color(255, 200, 0);
    /**
     * Colore stella non selezionata.
     */
    private final Color COLORE_VUOTA = Color.LIGHT_GRAY;

    /**
     * Crea form recensione.
     */
    public ScriviRecensione() {
        super("Scrivi Recensione");
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        inizializzaStelle();

        pack();
        centraFinestra();
    }

    /**
     * Inizializza stella pulsanti e listener.
     */
    private void inizializzaStelle() {
        pnlStelle.removeAll();

        for (int i = 0; i < 5; i++) {
            JButton stella = new JButton("★");
            stella.setFont(new Font("SansSerif", Font.BOLD, 24));
            stella.setForeground(COLORE_PIENA);

            stella.setBorderPainted(false);
            stella.setContentAreaFilled(false);
            stella.setFocusPainted(false);
            stella.setCursor(new Cursor(Cursor.HAND_CURSOR));

            final int votoStella = i + 1;

            stella.addActionListener(e -> {
                setVoto(votoStella);
            });

            stelleButtons[i] = stella;
            pnlStelle.add(stella);
        }

        aggiornaGraficaStelle();
    }

    /**
     * Imposta valutazione e aggiorna UI.
     *
     * @param voto valutazione valore
     */
    private void setVoto(int voto) {
        this.votoCorrente = voto;
        aggiornaGraficaStelle();
    }

    /**
     * Aggiorna stella colori basato in valutazione corrente.
     */
    private void aggiornaGraficaStelle() {
        for (int i = 0; i < 5; i++) {
            if (i < votoCorrente) {
                stelleButtons[i].setText("★");
                stelleButtons[i].setForeground(COLORE_PIENA);
            } else {
                stelleButtons[i].setText("☆");
                stelleButtons[i].setForeground(COLORE_VUOTA);
            }
        }
        pnlStelle.repaint();
    }

    /**
     * Restituisce valutazione corrente.
     *
     * @return valutazione valore
     */
    public int getVoto() {
        return votoCorrente;
    }

    /**
     * Restituisce testo recensione.
     *
     * @return testo recensione
     */
    public String getDescrizione() {
        return txtDescrizione.getText();
    }

    /**
     * Aggiunge invia azione listener.
     *
     * @param listener azione listener
     */
    public void addInviaListener(ActionListener listener) {
        btnInvia.addActionListener(listener);
    }
}
