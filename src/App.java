import com.formdev.flatlaf.FlatLightLaf;
import controller.ProfiloController;
import controller.PubblicaAnnuncioController;
import controller.ScriviRecensioneController;
import dao.UtenteDAO;
import gui.*;
import controller.LoginController; // Ho aggiunto l'import del controller
import model.Utente;
import utils.SessionManager;

import javax.swing.SwingUtilities;

public class App {
    static void main(String[] args) {

        FlatLightLaf.setup();
        // FlatDarkLaf.setup();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                //LoginForm loginPage = new LoginForm();
                //new LoginController(loginPage);

                //loginPage.setVisible(true);
                try {
                    UtenteDAO utenteDAO = new UtenteDAO();
                    Utente utente = utenteDAO.autenticaUtente("a", "a");
                    SessionManager.getInstance().login(utente);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Utente a = new Utente(1, "a", "a", "a@a.a", "1234567890");

                Profilo profilo = new Profilo();
                new ProfiloController(profilo, a);

                profilo.setVisible(true);
            }
        });
    }
}