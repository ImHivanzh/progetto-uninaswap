package model;

import model.enums.Categoria;
import model.enums.TipoAnnuncio;

public class Vendita extends Annuncio {
    private double prezzo;

    // Costruttore completo (dal DB)
    public Vendita(int id, String titolo, String descrizione, Categoria categoria, int utenteID, double prezzo) {
        super(id, titolo, descrizione, categoria, utenteID, TipoAnnuncio.VENDITA);
        this.prezzo = prezzo;
    }

    // Costruttore nuovo inserimento
    public Vendita(String titolo, String descrizione, Categoria categoria, int utenteID, double prezzo) {
        super(titolo, descrizione, categoria, utenteID, TipoAnnuncio.VENDITA);
        this.prezzo = prezzo;
    }

    public double getPrezzo() { return prezzo; }
    public void setPrezzo(double prezzo) { this.prezzo = prezzo; }

    @Override
    public String toString() {
        return "Vendita{" +
                "prezzo=" + prezzo +
                "} " + super.toString();
    }
}