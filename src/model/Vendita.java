package model;

/**
 * Modello annuncio vendita.
 */
public class Vendita extends Annuncio {
    /**
     * Prezzo vendita.
     */
    private double prezzo;

    /**
     * Crea vuoto vendita annuncio.
     */
    public Vendita() {
        super();
    }

    /**
     * Crea vendita annuncio con fornito prezzo.
     *
     * @param prezzo prezzo valore
     */
    public Vendita(double prezzo) {
        this.prezzo = prezzo;
    }

    /**
     * Restituisce prezzo.
     *
     * @return prezzo valore
     */
    public double getPrezzo() {
        return prezzo;
    }

    /**
     * Imposta prezzo.
     *
     * @param prezzo prezzo valore
     */
    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }

    /**
     * Restituisce rappresentazione stringa di vendita annuncio.
     *
     * @return rappresentazione stringa
     */
    @Override
    public String toString() {
        return "Vendita{" +
                "prezzo=" + prezzo +
                "} " + super.toString();
    }
}
