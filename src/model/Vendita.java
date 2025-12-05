package model;

public class Vendita {
    private int idVendita;
    private float controOfferta;
    private boolean accettato; // TRUE = la controfferta è stata accettata
    private Annuncio annuncio;
    private Utente utente; // L'utente che ha fatto la controfferta

    // costruttore di default
    public Vendita() {
    }

    //costruttore
    public Vendita(int idVendita, float controOfferta, boolean accettato, Annuncio annuncio, Utente utente) {
        this.idVendita = idVendita;
        this.controOfferta = controOfferta;
        this.accettato = accettato;
        this.annuncio = annuncio;
        this.utente = utente;
    }

    // metodi getter e setter

    // idVendita
    public int getIdVendita() {
        return idVendita;
    }

    public void setIdVendita(int idVendita) {
        this.idVendita = idVendita;
    }

    // controOfferta
    public float getControOfferta() {
        return controOfferta;
    }

    public void setControOfferta(float controOfferta) {
        this.controOfferta = controOfferta;
    }

    // accettato (boolean -> isAccettato)
    public boolean isAccettato() {
        return accettato;
    }

    public void setAccettato(boolean accettato) {
        this.accettato = accettato;
    }

    // idAnnuncio
    public Annuncio getAnnuncio() {
        return annuncio;
    }

    public void setAnnuncio(Annuncio annuncio) {this.annuncio = annuncio;}

    // idUtente
    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }
}