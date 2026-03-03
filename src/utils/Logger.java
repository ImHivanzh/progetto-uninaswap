package utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility semplice per logging consistente.
 * Fornisce metodi per registrare messaggi di errore e informazioni.
 */
public class Logger {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Registra un messaggio di errore.
     *
     * @param message messaggio da registrare
     */
    public static void error(String message) {
        System.err.println("[ERROR] " + LocalDateTime.now().format(FORMATTER) + " - " + message);
    }

    /**
     * Registra un messaggio di errore con eccezione.
     *
     * @param message messaggio da registrare
     * @param throwable eccezione associata
     */
    public static void error(String message, Throwable throwable) {
        System.err.println("[ERROR] " + LocalDateTime.now().format(FORMATTER) + " - " + message);
        if (throwable != null) {
            throwable.printStackTrace();
        }
    }

    /**
     * Registra un messaggio informativo.
     *
     * @param message messaggio da registrare
     */
    public static void info(String message) {
        System.out.println("[INFO] " + LocalDateTime.now().format(FORMATTER) + " - " + message);
    }

    /**
     * Costruttore privato per impedire istanziazione.
     */
    private Logger() {
        throw new AssertionError("Classe di utilità non istanziabile");
    }
}
