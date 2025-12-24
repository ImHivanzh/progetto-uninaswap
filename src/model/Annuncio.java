package model;

import model.enums.Categoria;
import model.enums.TipoAnnuncio;
import java.util.ArrayList;
import java.util.List;

public class Annuncio {
    private int idAnnuncio;
    private int idUtente;
    private String titolo;
    private String descrizione;
    private Categoria categoria;
    private TipoAnnuncio tipoAnnuncio;
    private boolean stato;
    private List<Immagini> immagini;

    /**
     * Crea vuoto annuncio con inizializzato lista immagini.
     */
    public Annuncio() {
        this.immagini = new ArrayList<>();
    }

    /**
     * Crea annuncio con principali campi.
     *
     * @param idUtente proprietario id
     * @param titolo titolo
     * @param descrizione descrizione
     * @param categoria categoria
     * @param tipoAnnuncio tipo annuncio
     */
    public Annuncio(int idUtente, String titolo, String descrizione, Categoria categoria, TipoAnnuncio tipoAnnuncio) {
        this.idUtente = idUtente;
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.categoria = categoria;
        this.tipoAnnuncio = tipoAnnuncio;
        this.stato = true;
        this.immagini = new ArrayList<>();
    }

    /**
     * Crea annuncio con esplicito id, utile per DB ricostruzione.
     *
     * @param idAnnuncio id annuncio
     * @param titolo titolo
     * @param descrizione descrizione
     * @param categoria categoria
     * @param idUtente proprietario id
     * @param tipoAnnuncio tipo annuncio
     */
    public Annuncio(int idAnnuncio, String titolo, String descrizione, Categoria categoria, int idUtente,
                    TipoAnnuncio tipoAnnuncio) {
        this.idAnnuncio = idAnnuncio;
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.categoria = categoria;
        this.idUtente = idUtente;
        this.tipoAnnuncio = tipoAnnuncio;
        this.stato = true;
        this.immagini = new ArrayList<>();
    }

    /**
     * Restituisce id annuncio.
     *
     * @return id annuncio
     */
    public int getIdAnnuncio() {
        return idAnnuncio;
    }

    /**
     * Imposta id annuncio.
     *
     * @param idAnnuncio id annuncio
     */
    public void setIdAnnuncio(int idAnnuncio) {
        this.idAnnuncio = idAnnuncio;
    }

    /**
     * Restituisce proprietario utente id.
     *
     * @return proprietario id
     */
    public int getIdUtente() {
        return idUtente;
    }

    /**
     * Imposta proprietario utente id.
     *
     * @param idUtente proprietario id
     */
    public void setIdUtente(int idUtente) {
        this.idUtente = idUtente;
    }

    /**
     * Restituisce titolo.
     *
     * @return titolo
     */
    public String getTitolo() {
        return titolo;
    }

    /**
     * Imposta titolo.
     *
     * @param titolo titolo
     */
    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    /**
     * Restituisce descrizione.
     *
     * @return descrizione
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * Imposta descrizione.
     *
     * @param descrizione descrizione
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * Restituisce categoria.
     *
     * @return categoria
     */
    public Categoria getCategoria() {
        return categoria;
    }

    /**
     * Imposta categoria.
     *
     * @param categoria categoria
     */
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    /**
     * Restituisce tipo annuncio.
     *
     * @return tipo annuncio
     */
    public TipoAnnuncio getTipoAnnuncio() {
        return tipoAnnuncio;
    }

    /**
     * Imposta tipo annuncio.
     *
     * @param tipoAnnuncio tipo annuncio
     */
    public void setTipoAnnuncio(TipoAnnuncio tipoAnnuncio) {
        this.tipoAnnuncio = tipoAnnuncio;
    }

    /**
     * Restituisce se annuncio e attivo.
     *
     * @return true quando attivo
     */
    public boolean isStato() {
        return stato;
    }

    /**
     * Imposta attivo stato.
     *
     * @param stato attivo stato
     */
    public void setStato(boolean stato) {
        this.stato = stato;
    }

    /**
     * Restituisce allegati immagini.
     *
     * @return lista immagini
     */
    public List<Immagini> getImmagini() {
        return immagini;
    }

    /**
     * Imposta lista immagini.
     *
     * @param immagini lista immagini
     */
    public void setImmagini(List<Immagini> immagini) {
        this.immagini = immagini;
    }

    /**
     * Aggiunge immagine a annuncio.
     *
     * @param immagine immagine da aggiungere
     */
    public void addImmagine(Immagini immagine) {
        if (this.immagini == null) {
            this.immagini = new ArrayList<>();
        }
        this.immagini.add(immagine);
    }

    /**
     * Restituisce rappresentazione stringa di annuncio.
     *
     * @return rappresentazione stringa
     */
    @Override
    public String toString() {
        return "Annuncio{" +
                "idAnnuncio=" + idAnnuncio +
                ", idUtente=" + idUtente +
                ", titolo='" + titolo + '\'' +
                ", categoria=" + categoria +
                ", tipo=" + tipoAnnuncio +
                ", stato=" + stato +
                '}';
    }
}
