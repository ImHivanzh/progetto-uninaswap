package model.enums;

/**
 * Stati possibili di una proposta.
 * Rappresenta il ciclo di vita di una proposta (vendita, scambio, regalo).
 */
public enum StatoProposta {
    /**
     * Proposta in attesa di risposta.
     */
    IN_ATTESA("In attesa"),

    /**
     * Proposta accettata.
     */
    ACCETTATA("Accettata"),

    /**
     * Proposta rifiutata.
     */
    RIFIUTATA("Rifiutata");

    private final String descrizione;

    /**
     * Costruttore dell'enum.
     *
     * @param descrizione descrizione leggibile dello stato
     */
    StatoProposta(String descrizione) {
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

    /**
     * Converte i flag booleani legacy in StatoProposta.
     *
     * @param accettato flag accettazione
     * @param inAttesa flag attesa
     * @return stato corrispondente
     */
    public static StatoProposta fromFlags(boolean accettato, boolean inAttesa) {
        if (accettato) {
            return ACCETTATA;
        } else if (inAttesa) {
            return IN_ATTESA;
        } else {
            return RIFIUTATA;
        }
    }

    @Override
    public String toString() {
        return descrizione;
    }
}
