package model;

/**
 * Modello dati immagine.
 */
public class Immagini {
    /**
     * Contenuto immagine in bytes.
     */
    private byte[] immagine;
    /**
     * Annuncio associato.
     */
    private Annuncio annuncio;

    /**
     * Crea un contenitore immagine vuoto.
     */
    public Immagini() {
    }

    /**
     * Crea immagine contenitore con annuncio collegamento.
     *
     * @param immagine array di byte dell'immagine
     * @param annuncio annuncio collegato
     */
    public Immagini(byte[] immagine, Annuncio annuncio) {
        this.immagine = immagine;
        this.annuncio = annuncio;
    }

    /**
     * Restituisce array di byte dell'immagine.
     *
     * @return array di byte dell'immagine
     */
    public byte[] getImmagine() {
        return immagine;
    }

    /**
     * Imposta array di byte dell'immagine.
     *
     * @param immagine array di byte dell'immagine
     */
    public void setImmagine(byte[] immagine) {
        this.immagine = immagine;
    }

    /**
     * Restituisce annuncio collegato.
     *
     * @return annuncio collegato
     */
    public Annuncio getAnnuncio() {
        return annuncio;
    }

    /**
     * Imposta annuncio collegato.
     *
     * @param annuncio annuncio collegato
     */
    public void setAnnuncio(Annuncio annuncio) {
        this.annuncio = annuncio;
    }
}
