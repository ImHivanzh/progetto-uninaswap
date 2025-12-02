package model;
import java.util.Date;

public class Regalo {
    private int idRegalo;
    private Date dataPrenotazione;
    private boolean accettato;
    private int idUtente;
    private int idAnnuncio;

    //costruttore di default
    public Regalo() {
    }

    //costruttore
    public Regalo(int idRegalo, Date dataPrenotazione, boolean accettato, int idUtente, int idAnnuncio) {
        this.idRegalo = idRegalo;
        this.dataPrenotazione = dataPrenotazione;
        this.accettato = accettato;
        this.idUtente = idUtente;
        this.idAnnuncio = idAnnuncio;
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
    public int getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(int idUtente) {
        this.idUtente = idUtente;
    }

    // idAnnuncio
    public int getIdAnnuncio() {
        return idAnnuncio;
    }

    public void setIdAnnuncio(int idAnnuncio) {
        this.idAnnuncio = idAnnuncio;
    }

}
