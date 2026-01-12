package utils;

/**
 * Utility per validazioni input.
 */
public class DataCheck {
    /**
     * Valida email indirizzo contro base regex pattern.
     *
     * @param email email indirizzo a valida
     * @return true quando email corrisponde atteso formato
     */
    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email != null && email.matches(emailRegex);
    }

    /**
     * Valida numero telefono come 10-cifra numerico stringa.
     *
     * @param phoneNumber numero telefono a valida
     * @return true quando valore e numero 10 cifre
     */
    public static boolean isValidPhoneNumber(String phoneNumber) {
        String phoneRegex = "^[0-9]{10}$";
        return phoneNumber != null && phoneNumber.matches(phoneRegex);
    }

    /**
     * Verifica che password e forte sufficiente per registrazione.
     *
     * @param password password a valida
     * @return true quando ha lunghezza >= 8, maiuscola, minuscola, cifra, e speciale carattere
     */
    public static boolean isStrongPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        boolean hasUpper = false, hasLower = false, hasDigit = false, hasSpecial = false;
        String specialChars = "!@#$%^&*()_+-=[]{}|;':\",./<>?";

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else hasSpecial = true;
        }
        return hasUpper && hasLower && hasDigit && hasSpecial;
    }
}
