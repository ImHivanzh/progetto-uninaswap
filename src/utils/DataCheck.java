package utils;

/**
 * Utility per validazioni input.
 */
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
     * Valida email indirizzo contro base regex pattern.
     *
     * @param email email indirizzo a valida
     * @return true quando email corrisponde atteso formato
     */
    public static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailRegex);
    }

    /**
     * Valida numero telefono come 10-cifra numerico stringa.
     *
     * @param phoneNumber numero telefono a valida
     * @return true quando valore e numero 10 cifre
     */
    public static boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber == null) {
            return false;
        }
        String phoneRegex = "^\\d{10}$";
        return phoneNumber.matches(phoneRegex);
    }

    /**
     * Verifica che password e forte sufficiente per registrazione.
     *
     * @param password password a valida
     * @return true quando ha lunghezza >= 8, maiuscola, minuscola, cifra, e speciale carattere
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

            // Early exit se tutti i requisiti sono soddisfatti
            if (hasUpper && hasLower && hasDigit && hasSpecial) {
                return true;
            }
        }
        return hasUpper && hasLower && hasDigit && hasSpecial;
    }
}
