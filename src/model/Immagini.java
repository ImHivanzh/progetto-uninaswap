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
     * Crea vuoto immagine contenitore.
     */
    public Immagini() {
    }

    /**
     * Crea immagine contenitore con annuncio collegamento.
     *
     * @param immagine byte immagine
     * @param annuncio collegato annuncio
     */
    public Immagini(byte[] immagine, Annuncio annuncio) {
        this.immagine = immagine;
        this.annuncio = annuncio;
    }

    /**
     * Restituisce byte immagine.
     *
     * @return byte immagine
     */
    public byte[] getImmagine() {
        return immagine;
    }

    /**
     * Imposta byte immagine.
     *
     * @param immagine byte immagine
     */
    public void setImmagine(byte[] immagine) {
        this.immagine = immagine;
    }

    /**
     * Restituisce collegato annuncio.
     *
     * @return collegato annuncio
     */
    public Annuncio getAnnuncio() {
        return annuncio;
    }

    /**
     * Imposta collegato annuncio.
     *
     * @param annuncio collegato annuncio
     */
    public void setAnnuncio(Annuncio annuncio) {
        this.annuncio = annuncio;
    }
}
