package utils;

public class DataCheck {
    /**
     * Validates an email address against a basic regex pattern.
     *
     * @param email email address to validate
     * @return true when the email matches the expected format
     */
    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email != null && email.matches(emailRegex);
    }

    /**
     * Validates a phone number as a 10-digit numeric string.
     *
     * @param phoneNumber phone number to validate
     * @return true when the value is a 10-digit number
     */
    public static boolean isValidPhoneNumber(String phoneNumber) {
        String phoneRegex = "^[0-9]{10}$";
        return phoneNumber != null && phoneNumber.matches(phoneRegex);
    }

    /**
     * Checks that a password is strong enough for registration.
     *
     * @param password password to validate
     * @return true when it has length >= 8, upper, lower, digit, and special char
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
