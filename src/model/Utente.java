package model;
import utils.dataCheck;

public class Utente {
    private int idUtente;
    private String username;
    private String email;
    private String password;
    private String numeroTelefono;

    public Utente(String username, String email, String password, String numeroTelefono) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.numeroTelefono = numeroTelefono;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public void setPassword(String password) {
        if(utils.dataCheck.isStrongPassword(password)) {
            this.password = password;
        } else {
            throw new IllegalArgumentException("Password non sufficientemente sicura.");
        }
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNumeroTelefono() {
        return numeroTelefono;
    }

    public void setNumeroTelefono(String numeroTelefono) {
        this.numeroTelefono = numeroTelefono;
    }

}