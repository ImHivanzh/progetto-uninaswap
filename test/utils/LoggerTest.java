package utils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test completi per la classe Logger.
 */
class LoggerTest {

    private static final String ERROR_TAG = "[ERROR]";
    private static final String INFO_TAG = "[INFO]";

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    @SuppressWarnings("java:S106")
    private static final PrintStream ORIGINAL_OUT = System.out;
    @SuppressWarnings("java:S106")
    private static final PrintStream ORIGINAL_ERR = System.err;

    @BeforeEach
    void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(ORIGINAL_OUT);
        System.setErr(ORIGINAL_ERR);
    }

    @Test
    void errorWithMessageShouldPrintToStderr() {
        Logger.error("Test error message");

        String output = errContent.toString();
        assertTrue(output.contains(ERROR_TAG), "Should contain [ERROR] tag");
        assertTrue(output.contains("Test error message"), "Should contain the message");
        // Check for date-time pattern (more flexible)
        assertTrue(output.matches("(?s).*\\d{4}-\\d{2}-\\d{2}.*\\d{2}:\\d{2}:\\d{2}.*"), "Should contain timestamp");
    }

    @Test
    void errorWithEmptyMessageShouldPrintEmptyError() {
        Logger.error("");

        String output = errContent.toString();
        assertTrue(output.contains(ERROR_TAG));
    }

    @Test
    void errorWithMessageAndThrowableShouldPrintBoth() {
        Exception testException = new RuntimeException("Test exception");
        Logger.error("Error occurred", testException);

        String output = errContent.toString();
        assertTrue(output.contains(ERROR_TAG));
        assertTrue(output.contains("Error occurred"));
        assertTrue(output.contains("RuntimeException"));
        assertTrue(output.contains("Test exception"));
    }

    @Test
    void errorWithMessageAndNullThrowableShouldPrintMessageOnly() {
        Logger.error("Error without exception", null);

        String output = errContent.toString();
        assertTrue(output.contains(ERROR_TAG));
        assertTrue(output.contains("Error without exception"));
    }

    @Test
    void infoWithMessageShouldPrintToStdout() {
        Logger.info("Test info message");

        String output = outContent.toString();
        assertTrue(output.contains(INFO_TAG), "Should contain [INFO] tag");
        assertTrue(output.contains("Test info message"), "Should contain the message");
        // Check for date-time pattern (more flexible)
        assertTrue(output.matches("(?s).*\\d{4}-\\d{2}-\\d{2}.*\\d{2}:\\d{2}:\\d{2}.*"), "Should contain timestamp");
    }

    @Test
    void infoWithEmptyMessageShouldPrintEmptyInfo() {
        Logger.info("");

        String output = outContent.toString();
        assertTrue(output.contains(INFO_TAG));
    }

    @Test
    void infoWithSpecialCharactersShouldPrintCorrectly() {
        Logger.info("Special chars: !@#$%^&*()");

        String output = outContent.toString();
        assertTrue(output.contains("Special chars: !@#$%^&*()"));
    }

    @Test
    void multipleLogCallsShouldPrintAll() {
        Logger.info("First message");
        Logger.error("Second message");
        Logger.info("Third message");

        String outOutput = outContent.toString();
        String errOutput = errContent.toString();

        assertTrue(outOutput.contains("First message"));
        assertTrue(outOutput.contains("Third message"));
        assertTrue(errOutput.contains("Second message"));
    }

    @Test
    void constructorShouldThrowAssertionError() {
        Exception exception = assertThrows(Exception.class, () -> {
            java.lang.reflect.Constructor<Logger> constructor = Logger.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
        });

        assertTrue(exception instanceof java.lang.reflect.InvocationTargetException);
        assertTrue(((java.lang.reflect.InvocationTargetException) exception).getCause() instanceof AssertionError);
    }
}
