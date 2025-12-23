package model;

public class Recensione {
    private int voto;
    private String descrizione;
    private int idUtente;
    private String nomeUtente;
    private int idUtenteRecensito;

    /**
     * Creates an empty review.
     */
    public Recensione() {
    }

    /**
     * Creates a review with core fields.
     *
     * @param voto rating value
     * @param descrizione review text
     * @param idUtente reviewer id
     * @param idUtenteRecensito reviewed user id
     */
    public Recensione(int voto, String descrizione, int idUtente, int idUtenteRecensito) {
        this.voto = voto;
        this.descrizione = descrizione;
        this.idUtente = idUtente;
        this.idUtenteRecensito = idUtenteRecensito;
    }

    /**
     * Returns the rating value.
     *
     * @return rating value
     */
    public int getVoto() {
        return voto;
    }

    /**
     * Sets the rating value.
     *
     * @param voto rating value
     */
    public void setVoto(int voto) {
        this.voto = voto;
    }

    /**
     * Returns the review text.
     *
     * @return review text
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * Sets the review text.
     *
     * @param descrizione review text
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * Returns the reviewer user id.
     *
     * @return reviewer id
     */
    public int getIdUtente() {
        return idUtente;
    }

    /**
     * Sets the reviewer user id.
     *
     * @param idUtente reviewer id
     */
    public void setIdUtente(int idUtente) {
        this.idUtente = idUtente;
    }

    /**
     * Returns the reviewer username when available.
     *
     * @return reviewer username
     */
    public String getNomeUtente() {
        return nomeUtente;
    }

    /**
     * Sets the reviewer username.
     *
     * @param nomeUtente reviewer username
     */
    public void setNomeUtente(String nomeUtente) {
        this.nomeUtente = nomeUtente;
    }

    /**
     * Returns the reviewed user id.
     *
     * @return reviewed user id
     */
    public int getIdUtenteRecensito() {
        return idUtenteRecensito;
    }

    /**
     * Sets the reviewed user id.
     *
     * @param idUtenteRecensito reviewed user id
     */
    public void setIdUtenteRecensito(int idUtenteRecensito) {
        this.idUtenteRecensito = idUtenteRecensito;
    }
}
