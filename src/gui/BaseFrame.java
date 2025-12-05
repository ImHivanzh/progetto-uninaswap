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
     * Carica l'immagine e la imposta sia come icona della finestra (Windows/Linux)
     * che come icona del Dock (macOS).
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

    public void mostraMessaggio(String messaggio) {
        JOptionPane.showMessageDialog(this, messaggio, "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostraErrore(String errore) {
        JOptionPane.showMessageDialog(this, errore, "Errore", JOptionPane.ERROR_MESSAGE);
    }

    public void centraFinestra() {
        setLocationRelativeTo(null);
    }
}