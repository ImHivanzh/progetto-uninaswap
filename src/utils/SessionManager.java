package utils;

import model.Utente;

public class SessionManager {

  private static SessionManager instance;
  private Utente utenteCorrente;
  /**
   * Prevents direct instantiation of the singleton.
   */
  private SessionManager() {}

  /**
   * Returns the shared session manager instance.
   *
   * @return singleton instance
   */
  public static SessionManager getInstance() {
    if (instance == null) {
      instance = new SessionManager();
    }
    return instance;
  }

  /**
   * Stores the currently authenticated user.
   *
   * @param utente authenticated user
   */
  public void login(Utente utente) {
    this.utenteCorrente = utente;
  }

  /**
   * Clears the current session.
   */
  public void logout() {
    this.utenteCorrente = null;
  }

  /**
   * Returns the currently logged-in user.
   *
   * @return current user or null if not logged in
   */
  public Utente getUtente() {
    return utenteCorrente;
  }
}
