package model;

public class Recensione {
    private int voto;
    private String descrizione;
    private int idUtente;
    private String nomeUtente;
    private int idUtenteRecensito;

    /**
     * Crea vuoto recensione.
     */
    public Recensione() {
    }

    /**
     * Crea recensione con principali campi.
     *
     * @param voto valutazione valore
     * @param descrizione testo recensione
     * @param idUtente recensore id
     * @param idUtenteRecensito id utente recensito
     */
    public Recensione(int voto, String descrizione, int idUtente, int idUtenteRecensito) {
        this.voto = voto;
        this.descrizione = descrizione;
        this.idUtente = idUtente;
        this.idUtenteRecensito = idUtenteRecensito;
    }

    /**
     * Restituisce valutazione valore.
     *
     * @return valutazione valore
     */
    public int getVoto() {
        return voto;
    }

    /**
     * Imposta valutazione valore.
     *
     * @param voto valutazione valore
     */
    public void setVoto(int voto) {
        this.voto = voto;
    }

    /**
     * Restituisce testo recensione.
     *
     * @return testo recensione
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * Imposta testo recensione.
     *
     * @param descrizione testo recensione
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * Restituisce id utente recensore.
     *
     * @return recensore id
     */
    public int getIdUtente() {
        return idUtente;
    }

    /**
     * Imposta id utente recensore.
     *
     * @param idUtente recensore id
     */
    public void setIdUtente(int idUtente) {
        this.idUtente = idUtente;
    }

    /**
     * Restituisce username recensore quando disponibile.
     *
     * @return username recensore
     */
    public String getNomeUtente() {
        return nomeUtente;
    }

    /**
     * Imposta username recensore.
     *
     * @param nomeUtente username recensore
     */
    public void setNomeUtente(String nomeUtente) {
        this.nomeUtente = nomeUtente;
    }

    /**
     * Restituisce id utente recensito.
     *
     * @return id utente recensito
     */
    public int getIdUtenteRecensito() {
        return idUtenteRecensito;
    }

    /**
     * Imposta id utente recensito.
     *
     * @param idUtenteRecensito id utente recensito
     */
    public void setIdUtenteRecensito(int idUtenteRecensito) {
        this.idUtenteRecensito = idUtenteRecensito;
    }
}
