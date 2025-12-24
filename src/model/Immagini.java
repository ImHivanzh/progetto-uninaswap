package model;

public class Immagini {
    private int idImmagine;
    private byte[] immagine;
    private Annuncio annuncio;
    private Scambio scambio;

    /**
     * Crea vuoto immagine contenitore.
     */
    public Immagini() {
    }

    /**
     * Crea immagine contenitore con tutti campi.
     *
     * @param idImmagine immagine id
     * @param immagine byte immagine
     * @param annuncio collegato annuncio
     * @param scambio collegato scambio
     */
    public Immagini(int idImmagine, byte[] immagine, Annuncio annuncio, Scambio scambio) {
        this.idImmagine = idImmagine;
        this.immagine = immagine;
        this.annuncio = annuncio;
        this.scambio = scambio;
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
     * Restituisce immagine id.
     *
     * @return immagine id
     */
    public int getIdImmagine() {
        return idImmagine;
    }

    /**
     * Imposta immagine id.
     *
     * @param idImmagine immagine id
     */
    public void setIdImmagine(int idImmagine) {
        this.idImmagine = idImmagine;
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

    /**
     * Restituisce collegato scambio.
     *
     * @return collegato scambio
     */
    public Scambio getScambio() {
        return scambio;
    }

    /**
     * Imposta collegato scambio.
     *
     * @param scambio collegato scambio
     */
    public void setScambio(Scambio scambio) {
        this.scambio = scambio;
    }
}
