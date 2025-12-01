import gui.LoginForm;

import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // 1. Crea l'oggetto della finestra di login
                LoginForm loginPage = new LoginForm();

                // 2. Rendilo visibile a schermo
                loginPage.setVisible(true);
            }
        });
    }
}