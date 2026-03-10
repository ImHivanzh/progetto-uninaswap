package model;

/**
 * Modello dati recensione.
 */
public class Recensione {
    /**
     * Valutazione numerica.
     */
    private int voto;
    /**
     * Testo recensione.
     */
    private String descrizione;
    /**
     * Utente recensore.
     */
    private Utente utenteRecensore;
    /**
     * Utente recensito.
     */
    private Utente utenteRecensito;

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
     * @param utenteRecensore recensore
     * @param utenteRecensito utente recensito
     */
    public Recensione(int voto, String descrizione, Utente utenteRecensore, Utente utenteRecensito) {
        this.voto = voto;
        this.descrizione = descrizione;
        this.utenteRecensore = utenteRecensore;
        this.utenteRecensito = utenteRecensito;
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
     * Restituisce utente recensore.
     *
     * @return recensore
     */
    public Utente getUtenteRecensore() {
        return utenteRecensore;
    }

    /**
     * Imposta utente recensore.
     *
     * @param utenteRecensore recensore
     */
    public void setUtenteRecensore(Utente utenteRecensore) {
        this.utenteRecensore = utenteRecensore;
    }

    /**
     * Restituisce utente recensito.
     *
     * @return utente recensito
     */
    public Utente getUtenteRecensito() {
        return utenteRecensito;
    }

    /**
     * Imposta utente recensito.
     *
     * @param utenteRecensito utente recensito
     */
    public void setUtenteRecensito(Utente utenteRecensito) {
        this.utenteRecensito = utenteRecensito;
    }
}
