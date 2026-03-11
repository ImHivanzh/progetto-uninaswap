package utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test completi per la classe DataCheck.
 */
class DataCheckTest {

    // ========== Email Validation Tests ==========

    @Test
    void isValidEmailWithValidEmailsShouldReturnTrue() {
        assertTrue(DataCheck.isValidEmail("user@example.com"));
        assertTrue(DataCheck.isValidEmail("test.user@domain.com"));
        assertTrue(DataCheck.isValidEmail("user+tag@example.co.uk"));
        assertTrue(DataCheck.isValidEmail("user_name@example.org"));
        assertTrue(DataCheck.isValidEmail("user123@test-domain.com"));
        assertTrue(DataCheck.isValidEmail("a@b.co"));
        assertTrue(DataCheck.isValidEmail("test.email.with.dots@example.com"));
        assertTrue(DataCheck.isValidEmail("user%test@example.com"));
        assertTrue(DataCheck.isValidEmail("user-name@example.com"));
    }

    @Test
    void isValidEmailWithInvalidEmailsShouldReturnFalse() {
        assertFalse(DataCheck.isValidEmail(null));
        assertFalse(DataCheck.isValidEmail(""));
        assertFalse(DataCheck.isValidEmail("notanemail"));
        assertFalse(DataCheck.isValidEmail("@example.com"));
        assertFalse(DataCheck.isValidEmail("user@"));
        assertFalse(DataCheck.isValidEmail("user@domain")); // No TLD
        assertFalse(DataCheck.isValidEmail("user @example.com")); // Space in local part
        assertFalse(DataCheck.isValidEmail("user@exam ple.com")); // Space in domain
        assertFalse(DataCheck.isValidEmail("user@@example.com")); // Double @
        // Note: "user@example..com" passes the simple regex - known limitation
    }

    // ========== Phone Number Validation Tests ==========

    @Test
    void isValidPhoneNumberWithValidNumbersShouldReturnTrue() {
        assertTrue(DataCheck.isValidPhoneNumber("1234567890"));
        assertTrue(DataCheck.isValidPhoneNumber("0000000000"));
        assertTrue(DataCheck.isValidPhoneNumber("9999999999"));
    }

    @Test
    void isValidPhoneNumberWithInvalidNumbersShouldReturnFalse() {
        assertFalse(DataCheck.isValidPhoneNumber(null));
        assertFalse(DataCheck.isValidPhoneNumber(""));
        assertFalse(DataCheck.isValidPhoneNumber("123456789")); // 9 digits
        assertFalse(DataCheck.isValidPhoneNumber("12345678901")); // 11 digits
        assertFalse(DataCheck.isValidPhoneNumber("123-456-7890")); // with dashes
        assertFalse(DataCheck.isValidPhoneNumber("(123)4567890")); // with parentheses
        assertFalse(DataCheck.isValidPhoneNumber("123 456 7890")); // with spaces
        assertFalse(DataCheck.isValidPhoneNumber("+1234567890")); // with plus
        assertFalse(DataCheck.isValidPhoneNumber("12345a7890")); // with letter
        assertFalse(DataCheck.isValidPhoneNumber("abcdefghij")); // all letters
    }

    // ========== Password Strength Tests ==========

    @Test
    void isStrongPasswordWithStrongPasswordsShouldReturnTrue() {
        assertTrue(DataCheck.isStrongPassword("Abcd123!"));
        assertTrue(DataCheck.isStrongPassword("Password1!"));
        assertTrue(DataCheck.isStrongPassword("MyP@ssw0rd"));
        assertTrue(DataCheck.isStrongPassword("Str0ng#Pass"));
        assertTrue(DataCheck.isStrongPassword("C0mpl3x!Pass"));
        assertTrue(DataCheck.isStrongPassword("Test123$Password"));
        assertTrue(DataCheck.isStrongPassword("aB1!cdef")); // exactly 8 chars
        assertTrue(DataCheck.isStrongPassword("P@ssw0rd123456789")); // long password
    }

    @Test
    void isStrongPasswordWithNullShouldReturnFalse() {
        assertFalse(DataCheck.isStrongPassword(null));
    }

    @Test
    void isStrongPasswordWithTooShortShouldReturnFalse() {
        assertFalse(DataCheck.isStrongPassword(""));
        assertFalse(DataCheck.isStrongPassword("Abc1!"));
        assertFalse(DataCheck.isStrongPassword("Ab1!cde")); // 7 chars
    }

    @Test
    void isStrongPasswordWithoutUppercaseShouldReturnFalse() {
        assertFalse(DataCheck.isStrongPassword("password1!"));
        assertFalse(DataCheck.isStrongPassword("test123!@#"));
        assertFalse(DataCheck.isStrongPassword("abcd1234!"));
    }

    @Test
    void isStrongPasswordWithoutLowercaseShouldReturnFalse() {
        assertFalse(DataCheck.isStrongPassword("PASSWORD1!"));
        assertFalse(DataCheck.isStrongPassword("TEST123!@#"));
        assertFalse(DataCheck.isStrongPassword("ABCD1234!"));
    }

    @Test
    void isStrongPasswordWithoutDigitShouldReturnFalse() {
        assertFalse(DataCheck.isStrongPassword("Password!"));
        assertFalse(DataCheck.isStrongPassword("TestPass!@#"));
        assertFalse(DataCheck.isStrongPassword("Abcdefgh!"));
    }

    @Test
    void isStrongPasswordWithoutSpecialCharShouldReturnFalse() {
        assertFalse(DataCheck.isStrongPassword("Password1"));
        assertFalse(DataCheck.isStrongPassword("TestPass123"));
        assertFalse(DataCheck.isStrongPassword("Abcd1234"));
    }

    @Test
    void isStrongPasswordWithAllSpecialCharsShouldReturnTrue() {
        // Test all special characters defined in the method
        String[] specialChars = {"!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "_", "+", "-", "=", "[", "]", "{", "}", "|", ";", "'", ":", "\"", ",", ".", "/", "<", ">", "?"};
        for (String special : specialChars) {
            assertTrue(DataCheck.isStrongPassword("Abcd123" + special),
                "Password with special char '" + special + "' should be valid");
        }
    }

    @Test
    void isStrongPasswordWithUnsupportedSpecialCharShouldReturnFalse() {
        assertFalse(DataCheck.isStrongPassword("Abcd123~")); // ~ not in list
        assertFalse(DataCheck.isStrongPassword("Abcd123`")); // ` not in list
    }

    @Test
    void isStrongPasswordEdgeCases() {
        // Exactly minimum length with all requirements
        assertTrue(DataCheck.isStrongPassword("Aa1!bcde"));

        // Multiple of each requirement
        assertTrue(DataCheck.isStrongPassword("AABBcc11!!"));

        // Mixed order
        assertTrue(DataCheck.isStrongPassword("1!aAbBcC"));
    }

    @Test
    void minPasswordLengthConstant() {
        assertEquals(8, DataCheck.MIN_PASSWORD_LENGTH);
    }
}
