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
    private boolean stato; // true = attivo, false = concluso/eliminato
    private List<Immagini> immagini; // Lista per le immagini associate

    public Annuncio() {
        this.immagini = new ArrayList<>();
    }

    public Annuncio(int idUtente, String titolo, String descrizione, Categoria categoria, TipoAnnuncio tipoAnnuncio) {
        this.idUtente = idUtente;
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.categoria = categoria;
        this.tipoAnnuncio = tipoAnnuncio;
        this.stato = true; // Attivo di default
        this.immagini = new ArrayList<>();
    }

    // Costruttore completo (utile per ricostruzione dal DB se necessario, o per le sottoclassi)
    public Annuncio(int idAnnuncio, String titolo, String descrizione, Categoria categoria, int idUtente, TipoAnnuncio tipoAnnuncio) {
        this.idAnnuncio = idAnnuncio;
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.categoria = categoria;
        this.idUtente = idUtente;
        this.tipoAnnuncio = tipoAnnuncio;
        this.stato = true;
        this.immagini = new ArrayList<>();
    }

    // --- GETTERS & SETTERS ---

    public int getIdAnnuncio() {
        return idAnnuncio;
    }

    public void setIdAnnuncio(int idAnnuncio) {
        this.idAnnuncio = idAnnuncio;
    }

    public int getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(int idUtente) {
        this.idUtente = idUtente;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public TipoAnnuncio getTipoAnnuncio() {
        return tipoAnnuncio;
    }

    public void setTipoAnnuncio(TipoAnnuncio tipoAnnuncio) {
        this.tipoAnnuncio = tipoAnnuncio;
    }

    public boolean isStato() {
        return stato;
    }

    public void setStato(boolean stato) {
        this.stato = stato;
    }

    public List<Immagini> getImmagini() {
        return immagini;
    }

    public void setImmagini(List<Immagini> immagini) {
        this.immagini = immagini;
    }

    public void addImmagine(Immagini immagine) {
        if (this.immagini == null) {
            this.immagini = new ArrayList<>();
        }
        this.immagini.add(immagine);
    }

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