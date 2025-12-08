package controller;

import gui.RegistrazioneForm;
import dao.UtenteDAO;
import model.Utente;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegistrazioneController {

  private final RegistrazioneForm view;
  private final UtenteDAO utenteDAO;

  public RegistrazioneController(RegistrazioneForm view) {
    this.view = view;
    this.utenteDAO = new UtenteDAO();

    initListeners();
  }

  private void initListeners() {
    this.view.addRegistraListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        registraUtente();
      }
    });
  }

  private void registraUtente() {
    String username = view.getUsername();
    String mail = view.getMail();
    String password = view.getPassword();
    String telefono = view.getTelefono();

    if (username.isEmpty() || mail.isEmpty() || password.isEmpty() || telefono.isEmpty()) {
      view.mostraErrore("Compila i campi obbligatori (Username, Mail, Password, Numero di Telefono).");
      return;
    }

    // 2. Logica di Business / DB
    try {
      boolean successo = utenteDAO.registraUtente(new Utente(0, username, password, mail, telefono));

      if (successo) {
        view.mostraMessaggio("Registrazione completata per: " + username);
        view.dispose(); // Chiude la finestra di registrazione
      } else {
        view.mostraErrore("Errore durante la registrazione (es. utente già esistente)");
      }

    } catch (Exception ex) { // Cattura generica o SQLException
      view.mostraErrore("Errore tecnico: " + ex.getMessage());
      ex.printStackTrace();
    }
  }
}