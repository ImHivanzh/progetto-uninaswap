package controller;

import gui.RegistrazioneForm;
import dao.UtenteDAO;
import model.Utente;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Controller per registrazione utente.
 */
public class RegistrazioneController {

  /**
   * Vista registrazione.
   */
  private final RegistrazioneForm view;
  /**
   * DAO utenti.
   */
  private final UtenteDAO utenteDAO;

  /**
   * Crea controller e registra listener.
   *
   * @param view vista registrazione
   */
  public RegistrazioneController(RegistrazioneForm view) {
    this.view = view;
    this.utenteDAO = new UtenteDAO();

    initListeners();
  }

  /**
   * Registra UI listener per vista registrazione.
   */
  private void initListeners() {
    this.view.addRegistraListener(new ActionListener() {
      /**
       * {@inheritDoc}
       */
      @Override
      public void actionPerformed(ActionEvent e) {
        registraUtente();
      }
    });
  }

  /**
   * Valida input e invia nuovo registrazione.
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
        view.mostraErrore("Errore durante la registrazione (es. utente già esistente)");
      }

    } catch (Exception ex) {
      view.mostraErrore("Errore tecnico: " + ex.getMessage());
      ex.printStackTrace();
    }
  }
}
