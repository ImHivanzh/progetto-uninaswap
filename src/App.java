import com.formdev.flatlaf.FlatLightLaf;
import controller.ProfiloController;
import dao.UtenteDAO;
import gui.Profilo;
import gui.PubblicaAnnuncio;
import model.Utente;
import utils.SessionManager;

import javax.swing.SwingUtilities;

public class App {
    /**
     * Starts the application and shows the profile window on the EDT.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {

        FlatLightLaf.setup();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                try {
                    UtenteDAO utenteDAO = new UtenteDAO();
                    Utente utente = utenteDAO.autenticaUtente("a", "a");

                    if (utente != null) {
                        SessionManager.getInstance().login(utente);
                        System.out.println("Login simulato effettuato come: " + utente.getUsername());
                    } else {
                        System.err.println("Utente non trovato nel DB! La pubblicazione fallira.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Profilo frame = new Profilo();
                ProfiloController controller = new ProfiloController(frame);
                frame.setVisible(true);
            }
        });
    }
}
