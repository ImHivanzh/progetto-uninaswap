package utils;

/**
 * Costanti condivise dell'applicazione.
 * Centralizza i valori hardcoded per facilitare la manutenzione.
 */
public class Constanti {

    /**
     * Tipi di annuncio.
     */
    public static final String TIPO_VENDITA = "VENDITA";
    public static final String TIPO_SCAMBIO = "SCAMBIO";
    public static final String TIPO_REGALO = "REGALO";

    /**
     * Valori filtri.
     */
    public static final String CATEGORIA_TUTTE = "Tutte";
    public static final String TIPO_TUTTI = "Tutti";

    /**
     * Nomi tabelle database.
     */
    public static final String TABELLA_VENDITA = "vendita";
    public static final String TABELLA_SCAMBIO = "scambio";
    public static final String TABELLA_REGALO = "regalo";

    /**
     * Costruttore privato per impedire istanziazione.
     */
    private Constanti() {
        throw new AssertionError("Classe di utilità non istanziabile");
    }
}
