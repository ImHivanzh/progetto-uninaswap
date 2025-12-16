package model;

public class Immagini {
    private int idImmagine;
    private byte[] immagine; // [IMPORTANTE] Deve essere byte[], non String!
    private Annuncio annuncio;
    private Scambio scambio;

    public Immagini() {
    }

    public Immagini(int idImmagine, byte[] immagine, Annuncio annuncio, Scambio scambio) {
        this.idImmagine = idImmagine;
        this.immagine = immagine;
        this.annuncio = annuncio;
        this.scambio = scambio;
    }

    // Costruttore semplificato
    public Immagini(byte[] immagine, Annuncio annuncio) {
        this.immagine = immagine;
        this.annuncio = annuncio;
    }

    public int getIdImmagine() {
        return idImmagine;
    }

    public void setIdImmagine(int idImmagine) {
        this.idImmagine = idImmagine;
    }

    public byte[] getImmagine() {
        return immagine;
    }

    public void setImmagine(byte[] immagine) {
        this.immagine = immagine;
    }

    public Annuncio getAnnuncio() {
        return annuncio;
    }

    public void setAnnuncio(Annuncio annuncio) {
        this.annuncio = annuncio;
    }

    public Scambio getScambio() {
        return scambio;
    }

    public void setScambio(Scambio scambio) {
        this.scambio = scambio;
    }
}