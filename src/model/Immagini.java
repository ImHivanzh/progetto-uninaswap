package model;

public class Immagini {
    private int idImmagine;
    private byte[] immagine;
    private Annuncio annuncio;
    private Scambio scambio;

    /**
     * Creates an empty image container.
     */
    public Immagini() {
    }

    /**
     * Creates an image container with all fields.
     *
     * @param idImmagine image id
     * @param immagine image bytes
     * @param annuncio related listing
     * @param scambio related exchange
     */
    public Immagini(int idImmagine, byte[] immagine, Annuncio annuncio, Scambio scambio) {
        this.idImmagine = idImmagine;
        this.immagine = immagine;
        this.annuncio = annuncio;
        this.scambio = scambio;
    }

    /**
     * Creates an image container with listing linkage.
     *
     * @param immagine image bytes
     * @param annuncio related listing
     */
    public Immagini(byte[] immagine, Annuncio annuncio) {
        this.immagine = immagine;
        this.annuncio = annuncio;
    }

    /**
     * Returns the image id.
     *
     * @return image id
     */
    public int getIdImmagine() {
        return idImmagine;
    }

    /**
     * Sets the image id.
     *
     * @param idImmagine image id
     */
    public void setIdImmagine(int idImmagine) {
        this.idImmagine = idImmagine;
    }

    /**
     * Returns the image bytes.
     *
     * @return image bytes
     */
    public byte[] getImmagine() {
        return immagine;
    }

    /**
     * Sets the image bytes.
     *
     * @param immagine image bytes
     */
    public void setImmagine(byte[] immagine) {
        this.immagine = immagine;
    }

    /**
     * Returns the related listing.
     *
     * @return related listing
     */
    public Annuncio getAnnuncio() {
        return annuncio;
    }

    /**
     * Sets the related listing.
     *
     * @param annuncio related listing
     */
    public void setAnnuncio(Annuncio annuncio) {
        this.annuncio = annuncio;
    }

    /**
     * Returns the related exchange.
     *
     * @return related exchange
     */
    public Scambio getScambio() {
        return scambio;
    }

    /**
     * Sets the related exchange.
     *
     * @param scambio related exchange
     */
    public void setScambio(Scambio scambio) {
        this.scambio = scambio;
    }
}
