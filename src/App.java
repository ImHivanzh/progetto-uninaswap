import com.formdev.flatlaf.FlatLightLaf;
import gui.LoginForm;
import controller.LoginController; // Ho aggiunto l'import del controller
import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {

        FlatLightLaf.setup();
        // FlatDarkLaf.setup();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                LoginForm loginPage = new LoginForm();
                new LoginController(loginPage);

                loginPage.setVisible(true);
            }
        });
    }
}