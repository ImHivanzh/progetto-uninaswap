package model.enums;

/**
 * Stati disponibili per offerte.
 */
public enum StatoOfferta {
    /**
     * Stato in attesa.
     */
    IN_ATTESA("In Attesa"),
    /**
     * Stato accettata.
     */
    ACCETTATA("Accettata"),
    /**
     * Stato rifiutata.
     */
    RIFIUTATA("Rifiutata");

    /**
     * Descrizione leggibile.
     */
    private final String descrizione;

    /**
     * Crea stato offerta con descrizione testuale.
     *
     * @param descrizione descrizione
     */
    StatoOfferta(String descrizione) {
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
