package model;

public class vendita {
    private int idVendita;
    private float controOfferta;
    private boolean accettato;
    private int idAnnuncio;
    private int idUtente;

    private int idVendita;
    private float controOfferta;
    private boolean accettato; // TRUE = la controfferta è stata accettata
    private int idAnnuncio;
    private int idUtente; // L'utente che ha fatto la controfferta

    // costruttore di default
    public Vendita() {
    }

    //costruttore
    public Vendita(int idVendita, float controOfferta, boolean accettato, int idAnnuncio, int idUtente) {
        this.idVendita = idVendita;
        this.controOfferta = controOfferta;
        this.accettato = accettato;
        this.idAnnuncio = idAnnuncio;
        this.idUtente = idUtente;
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
    public int getIdAnnuncio() {
        return idAnnuncio;
    }

    public void setIdAnnuncio(int idAnnuncio) {
        this.idAnnuncio = idAnnuncio;
    }

    // idUtente
    public int getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(int idUtente) {
        this.idUtente = idUtente;
    }
}

}