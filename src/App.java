import com.formdev.flatlaf.FlatLightLaf;
import controller.ProfiloController;
import dao.UtenteDAO;
import gui.Profilo;
import gui.PubblicaAnnuncio;
import model.Utente;
import utils.SessionManager;

import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) { // Ricorda "public"

        FlatLightLaf.setup();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                // --- 1. LOGIN SIMULATO (Necessario) ---
                try {
                    UtenteDAO utenteDAO = new UtenteDAO();
                    // Assicurati che l'utente "a" con password "a" esista nel tuo DB
                    // Altrimenti l'app si aprirà ma non potrai pubblicare
                    Utente utente = utenteDAO.autenticaUtente("a", "a");

                    if (utente != null) {
                        SessionManager.getInstance().login(utente);
                        System.out.println("Login simulato effettuato come: " + utente.getUsername());
                    } else {
                        System.err.println("Utente non trovato nel DB! La pubblicazione fallirà.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // --- 2. APERTURA PUBBLICA ANNUNCIO ---
                Profilo frame = new Profilo();
                ProfiloController controller = new ProfiloController(frame);
                frame.setVisible(true);
            }
        });
    }
}