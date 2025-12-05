import com.formdev.flatlaf.FlatLightLaf;
import controller.ScriviRecensioneController;
import gui.LoginForm;
import controller.LoginController; // Ho aggiunto l'import del controller
import gui.MainApp;
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

                ScriviRecensione scriviRecensione = new ScriviRecensione();
                new ScriviRecensioneController(scriviRecensione, 1);

                scriviRecensione.setVisible(true);
            }
        });
    }
}