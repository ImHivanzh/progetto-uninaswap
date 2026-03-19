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
     * ID annuncio associato alla recensione.
     */
    private Integer idAnnuncio;

    /**
     * Crea una recensione vuota.
     */
    public Recensione() {
    }

    /**
     * Crea una recensione con i campi principali.
     *
     * @param voto valutazione valore (deve essere tra 1 e 5)
     * @param descrizione testo recensione
     * @param utenteRecensore recensore
     * @param utenteRecensito utente recensito
     * @throws IllegalArgumentException se voto non è tra 1 e 5
     */
    public Recensione(int voto, String descrizione, Utente utenteRecensore, Utente utenteRecensito) {
        setVoto(voto);
        this.descrizione = descrizione;
        this.utenteRecensore = utenteRecensore;
        this.utenteRecensito = utenteRecensito;
    }

    /**
     * Crea recensione con annuncio associato.
     *
     * @param voto valutazione valore (deve essere tra 1 e 5)
     * @param descrizione testo recensione
     * @param utenteRecensore recensore
     * @param utenteRecensito utente recensito
     * @param idAnnuncio ID annuncio per cui si lascia la recensione
     * @throws IllegalArgumentException se voto non è tra 1 e 5
     */
    public Recensione(int voto, String descrizione, Utente utenteRecensore, Utente utenteRecensito, int idAnnuncio) {
        setVoto(voto);
        this.descrizione = descrizione;
        this.utenteRecensore = utenteRecensore;
        this.utenteRecensito = utenteRecensito;
        this.idAnnuncio = idAnnuncio;
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
     * @param voto valutazione valore (deve essere tra 1 e 5)
     * @throws IllegalArgumentException se voto non è tra 1 e 5
     */
    public void setVoto(int voto) {
        if (voto < 1 || voto > 5) {
            throw new IllegalArgumentException("Il voto deve essere compreso tra 1 e 5, ricevuto: " + voto);
        }
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

    /**
     * Restituisce ID annuncio associato.
     *
     * @return ID annuncio
     */
    public Integer getIdAnnuncio() {
        return idAnnuncio;
    }

    /**
     * Imposta ID annuncio associato.
     *
     * @param idAnnuncio ID annuncio
     */
    public void setIdAnnuncio(Integer idAnnuncio) {
        this.idAnnuncio = idAnnuncio;
    }
}
