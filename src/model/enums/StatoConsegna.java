package model.enums;

/**
 * Stati possibili di una consegna (spedizione o ritiro).
 */
public enum StatoConsegna {
    /**
     * In attesa di definire modalita consegna.
     */
    IN_ATTESA("In attesa"),

    /**
     * Proposta accettata, in attesa dei dati di spedizione.
     */
    ACCETTATA("Accettata, in attesa dei dati di spedizione"),

    /**
     * Da spedire.
     */
    DA_SPEDIRE("Da spedire"),

    /**
     * Da ritirare.
     */
    DA_RITIRARE("Da ritirare"),

    /**
     * Consegna conclusa.
     */
    CONCLUSO("Concluso"),

    /**
     * Proposta rifiutata.
     */
    RIFIUTATO("Rifiutato");

    private final String descrizione;

    /**
     * Costruttore dell'enum.
     *
     * @param descrizione descrizione leggibile dello stato
     */
    StatoConsegna(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * Restituisce la descrizione leggibile dello stato.
     *
     * @return descrizione
     */
    public String getDescrizione() {
        return descrizione;
    }

    @Override
    public String toString() {
        return descrizione;
    }
}
