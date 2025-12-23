package model;
import utils.DataCheck;

public class Utente {
    private int idUtente;
    private String username;
    private String email;
    private String password;
    private String numeroTelefono;

    /**
     * Creates an empty user.
     */
    public Utente() {}

    /**
     * Creates a user with the provided fields.
     *
     * @param idUtente user id
     * @param username username
     * @param password password
     * @param email email address
     * @param numeroTelefono phone number
     */
    public Utente(int idUtente, String username, String password, String email, String numeroTelefono) {
        this.idUtente = idUtente;
        this.username = username;
        this.email = email;
        this.password = password;
        this.numeroTelefono = numeroTelefono;
    }

    /**
     * Returns the user id.
     *
     * @return user id
     */
    public int getIdUtente() { return idUtente; }

    /**
     * Sets the user id.
     *
     * @param idUtente user id
     */
    public void setIdUtente(int idUtente) { this.idUtente = idUtente; }

    /**
     * Returns the username.
     *
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username.
     *
     * @param username username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the email address.
     *
     * @return email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address.
     *
     * @param email email address
     */
    public void setEmail(String email) { this.email = email; }

    /**
     * Returns the password.
     *
     * @return password
     */
    public String getPassword() { return password; }

    /**
     * Sets the password after strength validation.
     *
     * @param password password value
     * @throws IllegalArgumentException when the password is not strong enough
     */
    public void setPassword(String password) {
        if (DataCheck.isStrongPassword(password)) {
            this.password = password;
        } else {
            throw new IllegalArgumentException("Password non sufficientemente sicura.");
        }
    }

    /**
     * Returns the phone number.
     *
     * @return phone number
     */
    public String getNumeroTelefono() {
        return numeroTelefono;
    }

    /**
     * Sets the phone number.
     *
     * @param numeroTelefono phone number
     */
    public void setNumeroTelefono(String numeroTelefono) {
        this.numeroTelefono = numeroTelefono;
    }

}
