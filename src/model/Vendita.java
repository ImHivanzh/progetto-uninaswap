package model;

public class Vendita extends Annuncio {
    private double prezzo;

    /**
     * Creates an empty sale listing.
     */
    public Vendita() {
        super();
    }

    /**
     * Creates a sale listing with the provided price.
     *
     * @param prezzo price value
     */
    public Vendita(double prezzo) {
        this.prezzo = prezzo;
    }

    /**
     * Returns the price.
     *
     * @return price value
     */
    public double getPrezzo() {
        return prezzo;
    }

    /**
     * Sets the price.
     *
     * @param prezzo price value
     */
    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }

    /**
     * Returns a string representation of the sale listing.
     *
     * @return string representation
     */
    @Override
    public String toString() {
        return "Vendita{" +
                "prezzo=" + prezzo +
                "} " + super.toString();
    }
}
