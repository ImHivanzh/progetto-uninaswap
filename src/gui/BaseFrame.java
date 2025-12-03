package gui;

import javax.swing.*;
import java.awt.*;

public class BaseFrame extends JFrame {

    public BaseFrame(String titolo) {
        super(titolo);
        setFrameIcon();
    }

    public BaseFrame() {
        super();
        setFrameIcon();
    }

    /**
     * Carica l'immagine logo.png dal classpath e la imposta come icona della finestra.
     */
    private void setFrameIcon() {
        try {
            java.net.URL imageUrl = getClass().getResource("../img/logo.png");

            if (imageUrl != null) {
                Image icon = new ImageIcon(imageUrl).getImage();
                setIconImage(icon);
            } else {
                System.err.println("AVVISO: Risorsa icona non trovata. Controlla il percorso: /logo.png");
            }
        } catch (Exception e) {
            System.err.println("Errore durante il caricamento dell'icona: " + e.getMessage());
        }
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