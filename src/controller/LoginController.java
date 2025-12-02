package controller;

import gui.LoginForm;
import gui.RegistrazioneForm;
import dbQuery.UtenteDB;
import model.Utente;
import utils.SessionManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

public class LoginController {

  private final LoginForm view;
  private final UtenteDB utenteDB;

  public LoginController(LoginForm view) {
    this.view = view;
    this.utenteDB = new UtenteDB();

    // Ho inizializzato qui i listener
    initListeners();
  }

  private void initListeners() {
    // Gestione Login
    this.view.addLoginListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          controllaLogin();
        } catch (SQLException ex) {
          view.mostraErrore("Errore DB: " + ex.getMessage());
        }
      }
    });

    // Gestione click su "Registrati"
    this.view.addRegisterListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        RegistrazioneForm regForm = new RegistrazioneForm();
        new RegistrazioneController(regForm); // Collega il cervello alla form
        regForm.setVisible(true);
        // view.dispose(); // Da decidere se lasciare aperta la login o no
      }
    });

    // Gestione click su "Password Dimenticata"
    this.view.addForgotPassListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        view.mostraMessaggio("Procedura recupero password avviata.");
      }
    });
  }

  private void controllaLogin() throws SQLException {
    String user = view.getUsername();
    String password = view.getPassword();

    if (user.isEmpty() || password.isEmpty()) {
      view.mostraErrore("Inserire user e password!");
      return;
    }

    Utente utente = utenteDB.autenticaUtente(user, password);

    if (utente != null) {
      SessionManager.getInstance().login(utente);
      // Login riuscito
      view.mostraMessaggio("Login effettuato con successo!");
      view.dispose();
      // new MainApp().setVisible(true);
    } else {
      view.mostraErrore("Username o Password errati");
    }
  }
}