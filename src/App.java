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
     * @param args argomenti da riga di comando
     */
    public static void main(String[] args) {

        FlatLightLaf.setup();

        SwingUtilities.invokeLater(new Runnable() {
            /**
             * {@inheritDoc}
             */
            @Override
            public void run() {
                LoginForm frame = new LoginForm();
                LoginController controller = new LoginController(frame);
                frame.setVisible(true);
            }
        });
    }
}
