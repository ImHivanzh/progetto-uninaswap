package model;

public class Immagini {
    private int idImmagine;
    private String immagine;
    private int idAnnuncio;
    private int idScambio;

    //costruttore di default
    public Immagini() {
    }


    //costruttore
    public Immagini(int idImmagine, String immagine, int idAnnuncio, int idScambio) {
        this.idImmagine = idImmagine;
        this.immagine = immagine;
        this.idAnnuncio = idAnnuncio;
        this.idScambio = idScambio;
    }

    // metodi getter e setter

    // idImmagine
    public int getIdImmagine() {
        return idImmagine;
    }

    public void setIdImmagine(int idImmagine) {
        this.idImmagine = idImmagine;
    }

    // immagine
    public String getImmagine() {
        return immagine;
    }

    public void setImmagine(String immagine) {
        this.immagine = immagine;
    }

    // idAnnuncio
    public int getIdAnnuncio() {
        return idAnnuncio;
    }

    public void setIdAnnuncio(int idAnnuncio) {
        this.idAnnuncio = idAnnuncio;
    }

    // idScambio
    public int getIdScambio() {
        return idScambio;
    }

    public void setIdScambio(int idScambio) {
        this.idScambio = idScambio;
    }
}