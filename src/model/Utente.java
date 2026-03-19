package model;
import utils.DataCheck;

/**
 * Modello dati utente.
 */
public class Utente {
    /**
     * Identificativo utente.
     */
    private int idUtente;
    /**
     * Username utente.
     */
    private String username;
    /**
     * Email utente.
     */
    private String email;
    /**
     * Password utente.
     */
    private String password;
    /**
     * Numero telefono utente.
     */
    private String numeroTelefono;

    /**
     * Crea un utente vuoto.
     */
    public Utente() {}

    /**
     * Crea un utente con i campi forniti.
     *
     * @param idUtente id utente
     * @param username username
     * @param password password
     * @param email indirizzo email
     * @param numeroTelefono numero telefono
     */
    public Utente(int idUtente, String username, String password, String email, String numeroTelefono) {
        this.idUtente = idUtente;
        this.username = username;
        this.email = email;
        this.password = password;
        this.numeroTelefono = numeroTelefono;
    }

    /**
     * Restituisce id utente.
     *
     * @return id utente
     */
    public int getIdUtente() { return idUtente; }

    /**
     * Imposta id utente.
     *
     * @param idUtente id utente
     */
    public void setIdUtente(int idUtente) { this.idUtente = idUtente; }

    /**
     * Restituisce username.
     *
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Imposta username.
     *
     * @param username username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Restituisce indirizzo email.
     *
     * @return indirizzo email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Imposta indirizzo email.
     *
     * @param email indirizzo email
     */
    public void setEmail(String email) { this.email = email; }

    /**
     * Restituisce password.
     *
     * @return password
     */
    public String getPassword() { return password; }

    /**
     * Imposta password dopo forza validazione.
     *
     * @param password password valore
     * @throws IllegalArgumentException quando la password non è sufficientemente forte
     */
    public void setPassword(String password) {
        if (DataCheck.isStrongPassword(password)) {
            this.password = password;
        } else {
            throw new IllegalArgumentException("Password non sufficientemente sicura.");
        }
    }

    /**
     * Restituisce numero telefono.
     *
     * @return numero telefono
     */
    public String getNumeroTelefono() {
        return numeroTelefono;
    }

    /**
     * Imposta numero telefono.
     *
     * @param numeroTelefono numero telefono
     */
    public void setNumeroTelefono(String numeroTelefono) {
        this.numeroTelefono = numeroTelefono;
    }

}
