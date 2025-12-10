package model;

public class Vendita extends Annuncio { // Estende Annuncio
    private double prezzo;

    public Vendita() {
        super();
    }

    public Vendita(double prezzo) {
        this.prezzo = prezzo;
    }

    public double getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }

    @Override
    public String toString() {
        return "Vendita{" +
                "prezzo=" + prezzo +
                "} " + super.toString();
    }
}