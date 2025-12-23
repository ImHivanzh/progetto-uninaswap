package gui;

import javax.swing.*;
import java.awt.*;

public class BaseFrame extends JFrame {

    /**
     * Creates a frame with a title and sets the icon.
     *
     * @param titolo frame title
     */
    public BaseFrame(String titolo) {
        super(titolo);
        setFrameIcon();
    }

    /**
     * Creates a frame and sets the icon.
     */
    public BaseFrame() {
        super();
        setFrameIcon();
    }

    /**
     * Loads the app icon and applies it to the window and taskbar when supported.
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
     * Shows an informational message dialog.
     *
     * @param messaggio message text
     */
    public void mostraMessaggio(String messaggio) {
        JOptionPane.showMessageDialog(this, messaggio, "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Shows an error message dialog.
     *
     * @param errore error text
     */
    public void mostraErrore(String errore) {
        JOptionPane.showMessageDialog(this, errore, "Errore", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Centers the window on screen.
     */
    public void centraFinestra() {
        setLocationRelativeTo(null);
    }
}
