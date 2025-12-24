package model;
import utils.DataCheck;

public class Utente {
    private int idUtente;
    private String username;
    private String email;
    private String password;
    private String numeroTelefono;

    /**
     * Crea vuoto utente.
     */
    public Utente() {}

    /**
     * Crea utente con fornito campi.
     *
     * @param idUtente utente id
     * @param username username
     * @param password password
     * @param email email indirizzo
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
     * Restituisce utente id.
     *
     * @return utente id
     */
    public int getIdUtente() { return idUtente; }

    /**
     * Imposta utente id.
     *
     * @param idUtente utente id
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
     * Restituisce email indirizzo.
     *
     * @return email indirizzo
     */
    public String getEmail() {
        return email;
    }

    /**
     * Imposta email indirizzo.
     *
     * @param email email indirizzo
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
     * @throws IllegalArgumentException quando password e non forte sufficiente
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
