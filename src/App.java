import com.formdev.flatlaf.FlatLightLaf;
import controller.PubblicaAnnuncioController;
import controller.ScriviRecensioneController;
import gui.LoginForm;
import controller.LoginController; // Ho aggiunto l'import del controller
import gui.MainApp;
import gui.PubblicaAnnuncio;
import gui.ScriviRecensione;

import javax.swing.SwingUtilities;

public class App {
    static void main(String[] args) {

        FlatLightLaf.setup();
        // FlatDarkLaf.setup();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                LoginForm loginPage = new LoginForm();
                new LoginController(loginPage);

                loginPage.setVisible(true);

                PubblicaAnnuncio pubblicaAnnuncio = new PubblicaAnnuncio();
                new PubblicaAnnuncioController(pubblicaAnnuncio);

                pubblicaAnnuncio.setVisible(true);
            }
        });
    }
}