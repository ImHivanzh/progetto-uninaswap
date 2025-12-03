package gui;

import javax.swing.*;

public class BaseFrame extends JFrame {

    public BaseFrame(String titolo) {
        super(titolo);
    }

    // Costruttore vuoto se necessario
    public BaseFrame() {
        super();
    }

    /**
     * Mostra un messaggio di informazione standard.
     */
    public void mostraMessaggio(String messaggio) {
        JOptionPane.showMessageDialog(this, messaggio, "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Mostra un messaggio di errore standard.
     */
    public void mostraErrore(String errore) {
        JOptionPane.showMessageDialog(this, errore, "Errore", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Metodo opzionale per centrare la finestra sullo schermo
     */
    public void centraFinestra() {
        setLocationRelativeTo(null);
    }
}