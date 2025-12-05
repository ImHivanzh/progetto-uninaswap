package model;
import java.util.Date;

public class Regalo {
    private int idRegalo;
    private Date dataPrenotazione;
    private boolean accettato;
    private Utente utente;
    private Annuncio annuncio;

    //costruttore di default
    public Regalo() {
    }

    //costruttore
    public Regalo(int idRegalo, Date dataPrenotazione, boolean accettato, Utente utente, Annuncio annuncio) {
        this.idRegalo = idRegalo;
        this.dataPrenotazione = dataPrenotazione;
        this.accettato = accettato;
        this.utente = utente;
        this.annuncio = annuncio;
    }

    // metodi getter e setter

    // idRegalo
    public int getIdRegalo() {
        return idRegalo;
    }

    public void setIdRegalo(int idRegalo) {
        this.idRegalo = idRegalo;
    }

    // dataPrenotazione
    public Date getDataPrenotazione() {
        return dataPrenotazione;
    }

    public void setDataPrenotazione(Date dataPrenotazione) {
        this.dataPrenotazione = dataPrenotazione;
    }

    // accettato (boolean -> isAccettato)
    public boolean isAccettato() {
        return accettato;
    }

    public void setAccettato(boolean accettato) {
        this.accettato = accettato;
    }

    // idUtente
    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    // idAnnuncio
    public Annuncio getAnnuncio() {
        return annuncio;
    }

    public void setAnnuncio(Annuncio annuncio) {
        this.annuncio = annuncio;
    }

}
