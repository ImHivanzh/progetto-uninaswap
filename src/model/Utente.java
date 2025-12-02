package model;
import utils.DataCheck;

public class Utente {
    private int idUtente;
    private String username;
    private String email;
    private String password;
    private String numeroTelefono;

    //costruttore di default
    public Utente() {}
    //costruttore
    public Utente(String username, String email, String password, String numeroTelefono) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.numeroTelefono = numeroTelefono;
    }

    //metodi getter e setter
    //idUtente
    public int getIdUtente() { return idUtente;}

    public int setIdUtente(int idUtente) { this.idUtente = idUtente; }

    //username
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    //email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) { this.email=email;}

    //password
    public String getPassword(){return password;}

    public void setPassword(String password) {
        if(DataCheck.isStrongPassword(password)) {
            this.password = password;
        } else {
            throw new IllegalArgumentException("Password non sufficientemente sicura.");
        }
    }

    //numeroTelefono
    public String getNumeroTelefono() {
        return numeroTelefono;
    }

    public void setNumeroTelefono(String numeroTelefono) {
        this.numeroTelefono = numeroTelefono;
    }

}