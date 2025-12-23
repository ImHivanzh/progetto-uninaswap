package controller;

import gui.RegistrazioneForm;
import dao.UtenteDAO;
import model.Utente;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegistrazioneController {

  private final RegistrazioneForm view;
  private final UtenteDAO utenteDAO;

  /**
   * Creates the controller and registers listeners.
   *
   * @param view registration view
   */
  public RegistrazioneController(RegistrazioneForm view) {
    this.view = view;
    this.utenteDAO = new UtenteDAO();

    initListeners();
  }

  /**
   * Registers UI listeners for the registration view.
   */
  private void initListeners() {
    this.view.addRegistraListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        registraUtente();
      }
    });
  }

  /**
   * Validates input and submits a new registration.
   */
  private void registraUtente() {
    String username = view.getUsername();
    String mail = view.getMail();
    String password = view.getPassword();
    String telefono = view.getTelefono();

    if (username.isEmpty() || mail.isEmpty() || password.isEmpty() || telefono.isEmpty()) {
      view.mostraErrore("Compila i campi obbligatori (Username, Mail, Password, Numero di Telefono).");
      return;
    }

    try {
      boolean successo = utenteDAO.registraUtente(new Utente(0, username, password, mail, telefono));

      if (successo) {
        view.mostraMessaggio("Registrazione completata per: " + username);
        view.dispose();
      } else {
        view.mostraErrore("Errore durante la registrazione (es. utente gia esistente)");
      }

    } catch (Exception ex) {
      view.mostraErrore("Errore tecnico: " + ex.getMessage());
      ex.printStackTrace();
    }
  }
}
