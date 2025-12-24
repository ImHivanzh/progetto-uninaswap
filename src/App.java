import com.formdev.flatlaf.FlatLightLaf;
import controller.LoginController;
import controller.MainController;
import controller.ProfiloController;
import dao.UtenteDAO;
import gui.LoginForm;
import gui.MainApp;
import gui.Profilo;
import gui.PubblicaAnnuncio;
import model.Utente;
import utils.SessionManager;

import javax.swing.SwingUtilities;

public class App {
    /**
     * Avvia applicazione e mostra finestra profilo in EDT.
     *
     * @param args argomenti da riga di comando
     */
    public static void main(String[] args) {

        FlatLightLaf.setup();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                LoginForm frame = new LoginForm();
                LoginController controller = new LoginController(frame);
                frame.setVisible(true);
            }
        });
    }
}
