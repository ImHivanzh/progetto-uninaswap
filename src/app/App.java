package app;

import com.formdev.flatlaf.FlatLightLaf;
import controller.LoginController;
import gui.LoginForm;

import javax.swing.SwingUtilities;

/**
 * Entry point per avvio applicazione.
 */
public class App {
    /**
     * Avvia applicazione e mostra finestra profilo in EDT.
     *
     * @param args argomenti linea comando (non utilizzati)
     */
    @SuppressWarnings("java:S1172")
    public static void main(String[] args) {

        FlatLightLaf.setup();

        SwingUtilities.invokeLater(() -> {
            LoginForm frame = new LoginForm();
            new LoginController(frame);
            frame.setVisible(true);
        });
    }
}
