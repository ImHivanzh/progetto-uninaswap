package controller;

import gui.LoginForm;
import gui.MainApp;
import gui.PassDimenticataForm;
import gui.RegistrazioneForm;
import dao.UtenteDAO;
import model.Utente;
import utils.SessionManager;
import utils.WindowManager;
import exception.DatabaseException;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Controller per autenticazione utente.
 */
public class LoginController {

  /**
   * Vista login.
   */
  private final LoginForm view;
  /**
   * DAO utenti.
   */
  private final UtenteDAO utenteDAO;
  /**
   * Callback per login avvenuto.
   */
  private final Runnable onLoginSuccess;

  /**
   * Crea il controller e registra i listener.
   *
   * @param view vista del login
   */
  public LoginController(LoginForm view) {
    this(view, null);
  }

  /**
   * Crea il controller e registra i listener.
   *
   * @param view vista del login
   * @param onLoginSuccess callback eseguita dopo un login riuscito
   */
  public LoginController(LoginForm view, Runnable onLoginSuccess) {
    this.view = view;
    this.utenteDAO = new UtenteDAO();
    this.onLoginSuccess = onLoginSuccess;
    initListeners();
  }

  /**
   * Registra i listener dell'interfaccia per la vista di login.
   */
  private void initListeners() {
    this.view.addLoginListener(e -> controllaLogin());

    this.view.addRegisterListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        RegistrazioneForm regForm = new RegistrazioneForm();
        new RegistrazioneController(regForm);
        WindowManager.open(view, regForm);
      }
    });

    this.view.addForgotPassListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        PassDimenticataForm passForm = new PassDimenticataForm();
        new PassDimenticataController(passForm);
        WindowManager.open(view, passForm);
      }
    });
  }

  /**
   * Valida l'input dell'utente ed esegue l'autenticazione.
   */
  private void controllaLogin() {
    String user = view.getUsername().trim();
    String password = view.getPassword().trim();

    if (user.isEmpty() || password.isEmpty()) {
      view.mostraErrore("Inserire user e password!");
      return;
    }

    try {
      Utente utente = utenteDAO.autenticaUtente(user, password);

      if (utente != null) {
        SessionManager.getInstance().login(utente);
        view.dispose();
        avviaMainApp();
      } else {
        view.mostraErrore("Username o Password errati");
      }
    } catch (DatabaseException ex) {
      view.mostraErrore("Errore di connessione al Database: " + ex.getMessage());
    }
  }

  /**
   * Apre l'applicazione principale dopo un login riuscito.
   */
  private void avviaMainApp() {
    if (onLoginSuccess != null) {
      onLoginSuccess.run();
      return;
    }
    MainApp mainApp = new MainApp();
    MainController controller = new MainController(mainApp);
    controller.avvia();
  }
}
