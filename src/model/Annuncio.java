package model;

public class Annuncio {

    private int idAnnuncio;
    private String titolo;
    private String descrizione;
    private boolean stato; // TRUE = disponibile
    private float prezzo;
    private TipoAnnuncio tipoAnnuncio;
    private int idUtente;


    //costruttore di default
    public Annuncio() {
    }

    //costruttore principale
    public Annuncio(int idAnnuncio, String titolo, String descrizione, boolean stato, float prezzo, TipoAnnuncio tipoAnnuncio, int idUtente) {
        this.idAnnuncio = idAnnuncio;
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.stato = stato;
        this.prezzo = prezzo;
        this.tipoAnnuncio = tipoAnnuncio;
        this.idUtente = idUtente;
    }

    //metodi getter e setter
    // idAnnuncio
    public int getIdAnnuncio() {
        return idAnnuncio;
    }

    public void setIdAnnuncio(int idAnnuncio) {
        this.idAnnuncio = idAnnuncio;
    }

    // titolo
    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    // descrizione
    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    // stato
    public boolean isStato() {
        return stato;
    }

    public void setStato(boolean stato) {
        this.stato = stato;
    }

    // prezzo
    public float getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(float prezzo) {
        this.prezzo = prezzo;
    }

    // tipoAnnuncio
    public TipoAnnuncio getTipoAnnuncio() {
        return tipoAnnuncio;
    }

    public void setTipoAnnuncio(TipoAnnuncio tipoAnnuncio) {
        this.tipoAnnuncio = tipoAnnuncio;
    }

    // idUtente
    public int getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(int idUtente) {
        this.idUtente = idUtente;
    }
}