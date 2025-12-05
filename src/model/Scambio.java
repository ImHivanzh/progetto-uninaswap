package model;

public class Scambio {
    private int idScambio;
    private Annuncio annuncio;
    private Utente utente;
    private String propScambio;
    private boolean accettato;

    //costruttore di default
    public Scambio() {
    }

    //costruttore
    public Scambio(int idScambio, Annuncio annuncio, Utente utente, String propScambio, boolean accettato) {
        this.idScambio = idScambio;
        this.annuncio = annuncio;
        this.utente = utente;
        this.propScambio = propScambio;
        this.accettato = accettato;
    }

    // metodi getter e setter

    // idScambio
    public int getIdScambio() {
        return idScambio;
    }

    public void setIdScambio(int idScambio) {
        this.idScambio = idScambio;
    }

    // idAnnuncio
    public Annuncio getAnnuncio() {
        return annuncio;
    }

    public void setAnnuncio(Annuncio annuncio) { this.annuncio = annuncio;}

    // idUtente
    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    // propScambio
    public String getPropScambio() {
        return propScambio;
    }

    public void setPropScambio(String propScambio) {
        this.propScambio = propScambio;
    }

    // accettato (boolean -> isAccettato)
    public boolean isAccettato() {
        return accettato;
    }

    public void setAccettato(boolean accettato) {
        this.accettato = accettato;
    }
}