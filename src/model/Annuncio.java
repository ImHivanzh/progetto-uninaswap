package model;

import model.enums.Categoria;
import model.enums.TipoAnnuncio;

public class Annuncio {
    private int idAnnuncio;
    private int idUtente; // Usiamo l'ID per collegarlo all'utente senza caricare tutto l'oggetto
    private String titolo;
    private String descrizione;
    private Categoria categoria;
    private TipoAnnuncio tipoAnnuncio;
    private boolean stato; // true = attivo, false = concluso/eliminato

    public Annuncio() {
    }

    public Annuncio(int idUtente, String titolo, String descrizione, Categoria categoria, TipoAnnuncio tipoAnnuncio) {
        this.idUtente = idUtente;
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.categoria = categoria;
        this.tipoAnnuncio = tipoAnnuncio;
        this.stato = true; // Attivo di default
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

    @Override
    public String toString() {
        return "Annuncio{" +
                "idAnnuncio=" + idAnnuncio +
                ", idUtente=" + idUtente +
                ", titolo='" + titolo + '\'' +
                ", categoria=" + categoria +
                ", tipo=" + tipoAnnuncio +
                '}';
    }
}