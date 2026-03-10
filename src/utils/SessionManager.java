package utils;

import model.Utente;

/**
 * Gestisce la sessione dell'utente corrente.
 *
 * Implementa il pattern Singleton per garantire una singola istanza
 * della sessione utente condivisa in tutta l'applicazione.
 */
@SuppressWarnings("java:S6548") // Singleton pattern è richiesto per gestire la sessione globale
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
   * Impedisce l'istanziazione diretta del singleton.
   */
  private SessionManager() {}

  /**
   * Restituisce l'istanza singleton del session manager.
   *
   * @return istanza singleton
   */
  public static synchronized SessionManager getInstance() {
    if (instance == null) {
      instance = new SessionManager();
    }
    return instance;
  }

  /**
   * Memorizza l'utente attualmente autenticato.
   *
   * @param utente utente autenticato
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
   * Restituisce l'utente attualmente loggato.
   *
   * @return utente corrente, o null se non loggato
   */
  public Utente getUtente() {
    return utenteCorrente;
  }
}
