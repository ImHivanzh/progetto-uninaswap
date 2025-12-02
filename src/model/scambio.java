package model;

public class scambio {
    private int idScambio;
    private int idAnnuncio;
    private int idUtente;
    private String propScambio;
    private boolean accettato;

    //costruttore di default
    public Scambio() {
    }

    //costruttore
    public Scambio(int idScambio, int idAnnuncio, int idUtente, String propScambio, boolean accettato) {
        this.idScambio = idScambio;
        this.idAnnuncio = idAnnuncio;
        this.idUtente = idUtente;
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