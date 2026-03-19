package gui;

import java.awt.*;
import javax.swing.*;

import utils.Logger;

/**
 * Finestra base con comportamenti comuni.
 */
public class BaseFrame extends JFrame {

    /**
     * Crea una finestra con titolo e imposta l'icona.
     *
     * @param titolo titolo della finestra
     */
    public BaseFrame(String titolo) {
        super(titolo);
        setFrameIcon();
    }

    /**
     * Crea una finestra e imposta l'icona.
     */
    public BaseFrame() {
        super();
        setFrameIcon();
    }

    /**
     * Carica l'icona dell'applicazione e la applica alla finestra e alla taskbar quando supportato.
     */
    private void setFrameIcon() {
        try {
            java.net.URL imageUrl = getClass().getResource("/img/logo.png");

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
                Logger.error("AVVISO: Risorsa icona non trovata. Controlla il percorso: /img/logo.png");
            }
        } catch (Exception e) {
            Logger.error("Errore durante il caricamento dell'icona", e);
        }
    }

    /**
     * Mostra un dialogo con un messaggio informativo.
     *
     * @param messaggio testo del messaggio
     */
    public void mostraMessaggio(String messaggio) {
        JOptionPane.showMessageDialog(this, messaggio, "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Mostra un dialogo con un messaggio di errore.
     *
     * @param errore testo dell'errore
     */
    public void mostraErrore(String errore) {
        JOptionPane.showMessageDialog(this, errore, "Errore", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Centra la finestra sullo schermo.
     */
    public void centraFinestra() {
        setLocationRelativeTo(null);
    }
}
