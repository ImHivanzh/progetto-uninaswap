package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

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
    private static final Color COLORE_PIENA = new Color(255, 200, 0);
    /**
     * Colore stella non selezionata.
     */
    private static final Color COLORE_VUOTA = Color.LIGHT_GRAY;

    /**
     * Crea il form per la recensione.
     */
    public ScriviRecensione() {
        super("Scrivi Recensione");
        setContentPane(mainPanel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        inizializzaStelle();

        pack();
        centraFinestra();
    }

    /**
     * Inizializza i pulsanti delle stelle e i listener.
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

            stella.addActionListener(e -> setVoto(votoStella));

            stelleButtons[i] = stella;
            pnlStelle.add(stella);
        }

        aggiornaGraficaStelle();
    }

    /**
     * Imposta la valutazione e aggiorna l'interfaccia.
     *
     * @param voto valore della valutazione
     */
    private void setVoto(int voto) {
        this.votoCorrente = voto;
        aggiornaGraficaStelle();
    }

    /**
     * Aggiorna i colori delle stelle in base alla valutazione corrente.
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
     * Restituisce la valutazione corrente.
     *
     * @return valore della valutazione
     */
    public int getVoto() {
        return votoCorrente;
    }

    /**
     * Restituisce il testo della recensione.
     *
     * @return testo della recensione
     */
    public String getDescrizione() {
        return txtDescrizione.getText();
    }

    /**
     * Aggiunge un listener per l'azione di invio.
     *
     * @param listener listener dell'azione
     */
    public void addInviaListener(ActionListener listener) {
        btnInvia.addActionListener(listener);
    }
}
