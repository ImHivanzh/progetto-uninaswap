package model;

import model.enums.Categoria;
import model.enums.TipoAnnuncio;
import java.util.List;
import java.util.ArrayList;

public class Annuncio {
    private int id;
    private String titolo;
    private String descrizione;
    private Categoria categoria;
    private int utenteID;
    private TipoAnnuncio tipoAnnuncio;
    private List<Immagini> immagini;

    // Costruttore completo (es. dal DB)
    public Annuncio(int id, String titolo, String descrizione, Categoria categoria, int utenteID, TipoAnnuncio tipoAnnuncio) {
        this.id = id;
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.categoria = categoria;
        this.utenteID = utenteID;
        this.tipoAnnuncio = tipoAnnuncio;
        this.immagini = new ArrayList<>();
    }

    // Costruttore per nuovi inserimenti (senza ID)
    public Annuncio(String titolo, String descrizione, Categoria categoria, int utenteID, TipoAnnuncio tipoAnnuncio) {
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.categoria = categoria;
        this.utenteID = utenteID;
        this.tipoAnnuncio = tipoAnnuncio;
        this.immagini = new ArrayList<>();
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitolo() { return titolo; }
    public void setTitolo(String titolo) { this.titolo = titolo; }

    public String getDescrizione() { return descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    public int getUtenteID() { return utenteID; }
    public void setUtenteID(int utenteID) { this.utenteID = utenteID; }

    public TipoAnnuncio getTipoAnnuncio() { return tipoAnnuncio; }
    public void setTipoAnnuncio(TipoAnnuncio tipoAnnuncio) { this.tipoAnnuncio = tipoAnnuncio; }

    public List<Immagini> getImmagini() { return immagini; }
    public void setImmagini(List<Immagini> immagini) { this.immagini = immagini; }

    public void addImmagine(Immagini immagine) {
        if (this.immagini == null) this.immagini = new ArrayList<>();
        this.immagini.add(immagine);
    }

    @Override
    public String toString() {
        return "Annuncio{" +
                "id=" + id +
                ", titolo='" + titolo + '\'' +
                ", tipo=" + tipoAnnuncio +
                '}';
    }
}