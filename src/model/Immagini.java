package model;

public class Immagini {
    private int idImmagine;
    private String immagine;
    private Annuncio annuncio;
    private Scambio scambio;

    //costruttore di default
    public Immagini() {
    }


    //costruttore
    public Immagini(int idImmagine, String immagine, Annuncio annuncio, Scambio scambio) {
        this.idImmagine = idImmagine;
        this.immagine = immagine;
        this.annuncio=annuncio;
        this.scambio= scambio;
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
    public Annuncio getAnnuncio() {
        return annuncio;
    }

    public void setIdAnnuncio(Annuncio annuncio) {
        this.annuncio = annuncio;
    }

    // idScambio
    public Scambio getScambio() {
        return scambio;
    }

    public void setScambio(Scambio scambio) {
        this.scambio = scambio;
    }
}