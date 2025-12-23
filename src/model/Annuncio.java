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
     * Creates an empty listing with an initialized image list.
     */
    public Annuncio() {
        this.immagini = new ArrayList<>();
    }

    /**
     * Creates a listing with core fields.
     *
     * @param idUtente owner id
     * @param titolo title
     * @param descrizione description
     * @param categoria category
     * @param tipoAnnuncio listing type
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
     * Creates a listing with an explicit id, useful for DB reconstruction.
     *
     * @param idAnnuncio listing id
     * @param titolo title
     * @param descrizione description
     * @param categoria category
     * @param idUtente owner id
     * @param tipoAnnuncio listing type
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
     * Returns the listing id.
     *
     * @return listing id
     */
    public int getIdAnnuncio() {
        return idAnnuncio;
    }

    /**
     * Sets the listing id.
     *
     * @param idAnnuncio listing id
     */
    public void setIdAnnuncio(int idAnnuncio) {
        this.idAnnuncio = idAnnuncio;
    }

    /**
     * Returns the owner user id.
     *
     * @return owner id
     */
    public int getIdUtente() {
        return idUtente;
    }

    /**
     * Sets the owner user id.
     *
     * @param idUtente owner id
     */
    public void setIdUtente(int idUtente) {
        this.idUtente = idUtente;
    }

    /**
     * Returns the title.
     *
     * @return title
     */
    public String getTitolo() {
        return titolo;
    }

    /**
     * Sets the title.
     *
     * @param titolo title
     */
    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    /**
     * Returns the description.
     *
     * @return description
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * Sets the description.
     *
     * @param descrizione description
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * Returns the category.
     *
     * @return category
     */
    public Categoria getCategoria() {
        return categoria;
    }

    /**
     * Sets the category.
     *
     * @param categoria category
     */
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    /**
     * Returns the listing type.
     *
     * @return listing type
     */
    public TipoAnnuncio getTipoAnnuncio() {
        return tipoAnnuncio;
    }

    /**
     * Sets the listing type.
     *
     * @param tipoAnnuncio listing type
     */
    public void setTipoAnnuncio(TipoAnnuncio tipoAnnuncio) {
        this.tipoAnnuncio = tipoAnnuncio;
    }

    /**
     * Returns whether the listing is active.
     *
     * @return true when active
     */
    public boolean isStato() {
        return stato;
    }

    /**
     * Sets the active state.
     *
     * @param stato active state
     */
    public void setStato(boolean stato) {
        this.stato = stato;
    }

    /**
     * Returns the attached images.
     *
     * @return image list
     */
    public List<Immagini> getImmagini() {
        return immagini;
    }

    /**
     * Sets the image list.
     *
     * @param immagini image list
     */
    public void setImmagini(List<Immagini> immagini) {
        this.immagini = immagini;
    }

    /**
     * Adds an image to the listing.
     *
     * @param immagine image to add
     */
    public void addImmagine(Immagini immagine) {
        if (this.immagini == null) {
            this.immagini = new ArrayList<>();
        }
        this.immagini.add(immagine);
    }

    /**
     * Returns a string representation of the listing.
     *
     * @return string representation
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
