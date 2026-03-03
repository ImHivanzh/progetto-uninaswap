package utils;

/**
 * Utility per validazione input centralizzata.
 * Fornisce metodi per validare parametri comuni.
 */
public class Validator {

    /**
     * Verifica che un oggetto non sia null.
     *
     * @param obj oggetto da verificare
     * @param message messaggio di errore
     * @throws IllegalArgumentException se l'oggetto è null
     */
    public static void requireNonNull(Object obj, String message) {
        if (obj == null) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Verifica che un valore sia positivo.
     *
     * @param value valore da verificare
     * @param fieldName nome del campo (per il messaggio di errore)
     * @throws IllegalArgumentException se il valore non è positivo
     */
    public static void requirePositive(int value, String fieldName) {
        if (value <= 0) {
            throw new IllegalArgumentException(fieldName + " deve essere positivo");
        }
    }

    /**
     * Verifica che un valore double sia positivo.
     *
     * @param value valore da verificare
     * @param fieldName nome del campo (per il messaggio di errore)
     * @throws IllegalArgumentException se il valore non è positivo
     */
    public static void requirePositive(double value, String fieldName) {
        if (value <= 0) {
            throw new IllegalArgumentException(fieldName + " deve essere positivo");
        }
    }

    /**
     * Verifica che una stringa non sia null o vuota.
     *
     * @param str stringa da verificare
     * @param fieldName nome del campo (per il messaggio di errore)
     * @throws IllegalArgumentException se la stringa è null o vuota
     */
    public static void requireNonEmpty(String str, String fieldName) {
        if (str == null || str.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " non può essere vuoto");
        }
    }

    /**
     * Costruttore privato per impedire istanziazione.
     */
    private Validator() {
        throw new AssertionError("Classe di utilità non istanziabile");
    }
}
