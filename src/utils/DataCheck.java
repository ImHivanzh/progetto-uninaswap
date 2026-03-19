package utils;

/**
 * Utility per validazioni input.
 */
@SuppressWarnings("java:S3516") // I metodi di validazione restituiscono valori diversi in base all'input - falso positivo di analisi statica
public class DataCheck {
    /**
     * Lunghezza minima password.
     */
    public static final int MIN_PASSWORD_LENGTH = 8;

    /**
     * Costruttore privato per nascondere quello pubblico implicito.
     */
    private DataCheck() {
        throw new AssertionError("Utility class non deve essere istanziata");
    }

    /**
     * Valida l'indirizzo email contro un pattern regex di base.
     *
     * @param email indirizzo email da validare
     * @return true quando l'email corrisponde al formato atteso
     */
    public static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailRegex);
    }

    /**
     * Valida il numero di telefono come stringa numerica di 10 cifre.
     *
     * @param phoneNumber numero di telefono da validare
     * @return true quando il valore è un numero di 10 cifre
     */
    public static boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber == null) {
            return false;
        }
        String phoneRegex = "^\\d{10}$";
        return phoneNumber.matches(phoneRegex);
    }

    /**
     * Verifica che la password sia sufficientemente forte per la registrazione.
     *
     * @param password password da validare
     * @return true quando ha lunghezza >= 8, maiuscola, minuscola, cifra e carattere speciale
     */
    public static boolean isStrongPassword(String password) {
        if (password == null || password.length() < MIN_PASSWORD_LENGTH) {
            return false;
        }
        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;
        String specialChars = "!@#$%^&*()_+-=[]{}|;':\",./<>?";

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUpper = true;
            } else if (Character.isLowerCase(c)) {
                hasLower = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (specialChars.indexOf(c) >= 0) {
                hasSpecial = true;
            }
        }
        return hasUpper && hasLower && hasDigit && hasSpecial;
    }
}
