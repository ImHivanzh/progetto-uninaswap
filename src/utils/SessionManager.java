package utils;

import model.Utente;

/**
 * Gestisce la sessione utente corrente.
 */
public class SessionManager {

  /**
   * Istanza singleton.
   */
  private static SessionManager instance;
  /**
   * Utente attualmente loggato.
   */
  private Utente utenteCorrente;
  /**
   * Impedisce diretto istanziazione di singleton.
   */
  private SessionManager() {}

  /**
   * Restituisce condiviso sessione manager istanza.
   *
   * @return singleton istanza
   */
  public static SessionManager getInstance() {
    if (instance == null) {
      instance = new SessionManager();
    }
    return instance;
  }

  /**
   * Memorizza attualmente autenticato utente.
   *
   * @param utente autenticato utente
   */
  public void login(Utente utente) {
    this.utenteCorrente = utente;
  }

  /**
   * Cancella sessione corrente.
   */
  public void logout() {
    this.utenteCorrente = null;
  }

  /**
   * Restituisce attualmente loggato-in utente.
   *
   * @return utente corrente o null se non loggato
   */
  public Utente getUtente() {
    return utenteCorrente;
  }
}
