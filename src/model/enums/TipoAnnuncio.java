package model.enums;

/**
 * Tipi di annuncio.
 */
public enum TipoAnnuncio {
    /**
     * Annuncio di scambio.
     */
    SCAMBIO("Scambio"),
    /**
     * Annuncio di vendita.
     */
    VENDITA("Vendita"),
    /**
     * Annuncio di regalo.
     */
    REGALO("Regalo");

    /**
     * Descrizione leggibile.
     */
    private final String descrizione;

    /**
     * Crea tipo annuncio con descrizione testuale.
     *
     * @param descrizione descrizione
     */
    TipoAnnuncio(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * Restituisce descrizione leggibile.
     *
     * @return descrizione
     */
    @Override
    public String toString() {
        return descrizione;
    }
}
