package gui;

import javax.swing.*;
import java.awt.*;

public class BaseFrame extends JFrame {

    /**
     * Crea finestra con titolo e imposta icona.
     *
     * @param titolo finestra titolo
     */
    public BaseFrame(String titolo) {
        super(titolo);
        setFrameIcon();
    }

    /**
     * Crea finestra e imposta icona.
     */
    public BaseFrame() {
        super();
        setFrameIcon();
    }

    /**
     * Carica app icona e applica a finestra e taskbar quando supportato.
     */
    private void setFrameIcon() {
        try {
            java.net.URL imageUrl = getClass().getResource("../img/logo.png");

            if (imageUrl != null) {
                Image icon = new ImageIcon(imageUrl).getImage();

                setIconImage(icon);

                if (Taskbar.isTaskbarSupported()) {
                    Taskbar taskbar = Taskbar.getTaskbar();
                    if (taskbar.isSupported(Taskbar.Feature.ICON_IMAGE)) {
                        taskbar.setIconImage(icon);
                    }
                }

            } else {
                System.err.println("AVVISO: Risorsa icona non trovata. Controlla il percorso: ../img/logo.png");
            }
        } catch (Exception e) {
            System.err.println("Errore durante il caricamento dell'icona: " + e.getMessage());
        }
    }

    /**
     * Mostra informativo messaggio dialogo.
     *
     * @param messaggio messaggio testo
     */
    public void mostraMessaggio(String messaggio) {
        JOptionPane.showMessageDialog(this, messaggio, "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Mostra errore messaggio dialogo.
     *
     * @param errore errore testo
     */
    public void mostraErrore(String errore) {
        JOptionPane.showMessageDialog(this, errore, "Errore", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Centra finestra in schermo.
     */
    public void centraFinestra() {
        setLocationRelativeTo(null);
    }
}
