package utils;

import model.Utente;

public class SessionManager {

  private static SessionManager instance;
  private Utente utenteCorrente;
  private SessionManager() {}

  public static SessionManager getInstance() {
    if (instance == null) {
      instance = new SessionManager();
    }
    return instance;
  }

  public void login(Utente utente) {
    this.utenteCorrente = utente;
  }

  public void logout() {
    this.utenteCorrente = null;
  }

  public Utente getUtente() {
    return utenteCorrente;
  }
}