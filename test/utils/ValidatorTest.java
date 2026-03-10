package utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test completi per la classe Validator.
 */
class ValidatorTest {

    private static final String TEST_FIELD = "field";
    private static final String TEST_VALUE = "value";

    @Test
    void requireNonNullWithValidObjectShouldNotThrow() {
        assertDoesNotThrow(() -> Validator.requireNonNull("test", "Object should not be null"));
        assertDoesNotThrow(() -> Validator.requireNonNull(123, "Number should not be null"));
        assertDoesNotThrow(() -> Validator.requireNonNull(new Object(), "Object should not be null"));
    }

    @Test
    void requireNonNullWithNullShouldThrowException() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> Validator.requireNonNull(null, "Custom error message")
        );
        assertEquals("Custom error message", exception.getMessage());
    }

    @Test
    void requireNonNullWithNullAndEmptyMessageShouldThrowWithEmptyMessage() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> Validator.requireNonNull(null, "")
        );
        assertEquals("", exception.getMessage());
    }

    @Test
    void requirePositiveIntWithPositiveValueShouldNotThrow() {
        assertDoesNotThrow(() -> Validator.requirePositive(1, TEST_VALUE));
        assertDoesNotThrow(() -> Validator.requirePositive(100, TEST_VALUE));
        assertDoesNotThrow(() -> Validator.requirePositive(Integer.MAX_VALUE, TEST_VALUE));
    }

    @Test
    void requirePositiveIntWithZeroShouldThrowException() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> Validator.requirePositive(0, "amount")
        );
        assertEquals("amount deve essere positivo", exception.getMessage());
    }

    @Test
    void requirePositiveIntWithNegativeValueShouldThrowException() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> Validator.requirePositive(-1, "price")
        );
        assertEquals("price deve essere positivo", exception.getMessage());
    }

    @Test
    void requirePositiveIntWithMinValueShouldThrowException() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> Validator.requirePositive(Integer.MIN_VALUE, TEST_VALUE)
        );
        assertEquals("value deve essere positivo", exception.getMessage());
    }

    @Test
    void requirePositiveDoubleWithPositiveValueShouldNotThrow() {
        assertDoesNotThrow(() -> Validator.requirePositive(0.1, TEST_VALUE));
        assertDoesNotThrow(() -> Validator.requirePositive(1.0, TEST_VALUE));
        assertDoesNotThrow(() -> Validator.requirePositive(100.5, TEST_VALUE));
        assertDoesNotThrow(() -> Validator.requirePositive(Double.MAX_VALUE, TEST_VALUE));
    }

    @Test
    void requirePositiveDoubleWithZeroShouldThrowException() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> Validator.requirePositive(0.0, "amount")
        );
        assertEquals("amount deve essere positivo", exception.getMessage());
    }

    @Test
    void requirePositiveDoubleWithNegativeValueShouldThrowException() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> Validator.requirePositive(-0.1, "price")
        );
        assertEquals("price deve essere positivo", exception.getMessage());
    }

    @Test
    void requirePositiveDoubleWithNegativeZeroShouldThrowException() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> Validator.requirePositive(-0.0, TEST_VALUE)
        );
        assertEquals("value deve essere positivo", exception.getMessage());
    }

    @Test
    void requireNonEmptyWithValidStringShouldNotThrow() {
        assertDoesNotThrow(() -> Validator.requireNonEmpty("test", TEST_FIELD));
        assertDoesNotThrow(() -> Validator.requireNonEmpty("a", TEST_FIELD));
        assertDoesNotThrow(() -> Validator.requireNonEmpty("  text  ", TEST_FIELD));
    }

    @Test
    void requireNonEmptyWithNullShouldThrowException() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> Validator.requireNonEmpty(null, "username")
        );
        assertEquals("username non può essere vuoto", exception.getMessage());
    }

    @Test
    void requireNonEmptyWithEmptyStringShouldThrowException() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> Validator.requireNonEmpty("", "email")
        );
        assertEquals("email non può essere vuoto", exception.getMessage());
    }

    @Test
    void requireNonEmptyWithWhitespaceOnlyShouldThrowException() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> Validator.requireNonEmpty("   ", "password")
        );
        assertEquals("password non può essere vuoto", exception.getMessage());
    }

    @Test
    void requireNonEmptyWithTabsAndNewlinesShouldThrowException() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> Validator.requireNonEmpty("\t\n\r", TEST_FIELD)
        );
        assertEquals("field non può essere vuoto", exception.getMessage());
    }

    @Test
    void constructorShouldThrowAssertionError() {
        Exception exception = assertThrows(Exception.class, () -> {
            java.lang.reflect.Constructor<Validator> constructor = Validator.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
        });

        // The constructor throws AssertionError, but reflection wraps it in InvocationTargetException
        assertTrue(exception instanceof java.lang.reflect.InvocationTargetException);
        assertTrue(((java.lang.reflect.InvocationTargetException) exception).getCause() instanceof AssertionError);
    }
}
