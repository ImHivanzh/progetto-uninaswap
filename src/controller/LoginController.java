package controller;

import gui.LoginForm;
import gui.PassDimenticataForm;
import gui.RegistrazioneForm;
import dao.UtenteDAO;
import model.Utente;
import utils.SessionManager;
import exception.DatabaseException;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginController {

  private final LoginForm view;
  private final UtenteDAO utenteDAO;

  /**
   * Creates the controller and registers listeners.
   *
   * @param view login view
   */
  public LoginController(LoginForm view) {
    this.view = view;
    this.utenteDAO = new UtenteDAO();
    initListeners();
  }

  /**
   * Registers UI listeners for the login view.
   */
  private void initListeners() {
    this.view.addLoginListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        controllaLogin();
      }
    });

    this.view.addRegisterListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        RegistrazioneForm regForm = new RegistrazioneForm();
        new RegistrazioneController(regForm);
        regForm.setVisible(true);
      }
    });

    this.view.addForgotPassListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        PassDimenticataForm passForm = new PassDimenticataForm();
        new PassDimenticataController(passForm);
        passForm.setVisible(true);
      }
    });
  }

  /**
   * Validates user input and performs authentication.
   */
  private void controllaLogin() {
    String user = view.getUsername();
    String password = view.getPassword();

    if (user.isEmpty() || password.isEmpty()) {
      view.mostraErrore("Inserire user e password!");
      return;
    }

    try {
      Utente utente = utenteDAO.autenticaUtente(user, password);

      if (utente != null) {
        SessionManager.getInstance().login(utente);
        view.mostraMessaggio("Login effettuato con successo!");
        view.dispose();
      } else {
        view.mostraErrore("Username o Password errati");
      }
    } catch (DatabaseException ex) {
      view.mostraErrore("Errore di connessione al Database: " + ex.getMessage());
    }
  }
}
