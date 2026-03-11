package utils;

import model.Utente;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test completi per la classe SessionManager.
 */
class SessionManagerTest {

    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_PASSWORD = "password";
    private static final String TEST_USER = "Test User";
    private static final String TEST_PHONE = "1234567890";

    @BeforeEach
    @AfterEach
    @SuppressWarnings("java:S3011") // Reflection necessaria per reset singleton nei test
    void resetSingleton() throws ReflectiveOperationException {
        // Reset singleton instance between tests
        java.lang.reflect.Field instance = SessionManager.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);
    }

    @Test
    void getInstanceShouldReturnNonNull() {
        SessionManager manager = SessionManager.getInstance();
        assertNotNull(manager);
    }

    @Test
    void getInstanceShouldReturnSameInstance() {
        SessionManager manager1 = SessionManager.getInstance();
        SessionManager manager2 = SessionManager.getInstance();
        assertSame(manager1, manager2);
    }

    @Test
    void getInstanceMultipleCallsShouldReturnSameInstance() {
        SessionManager manager1 = SessionManager.getInstance();
        SessionManager manager2 = SessionManager.getInstance();
        SessionManager manager3 = SessionManager.getInstance();

        assertSame(manager1, manager2);
        assertSame(manager2, manager3);
        assertSame(manager1, manager3);
    }

    @Test
    void getUtenteInitiallyNull() {
        SessionManager manager = SessionManager.getInstance();
        assertNull(manager.getUtente());
    }

    @Test
    void loginShouldSetUtente() {
        SessionManager manager = SessionManager.getInstance();
        Utente utente = new Utente(1, TEST_EMAIL, TEST_PASSWORD, TEST_USER, TEST_PHONE);

        manager.login(utente);

        assertEquals(utente, manager.getUtente());
    }

    @Test
    void loginWithNullShouldSetNull() {
        SessionManager manager = SessionManager.getInstance();
        Utente utente = new Utente(1, TEST_EMAIL, TEST_PASSWORD, TEST_USER, TEST_PHONE);
        manager.login(utente);

        manager.login(null);

        assertNull(manager.getUtente());
    }

    @Test
    void loginMultipleTimesShouldUpdateUtente() {
        SessionManager manager = SessionManager.getInstance();
        Utente utente1 = new Utente(1, "user1@example.com", "pass1", "User One", "1111111111");
        Utente utente2 = new Utente(2, "user2@example.com", "pass2", "User Two", "2222222222");

        manager.login(utente1);
        assertEquals(utente1, manager.getUtente());

        manager.login(utente2);
        assertEquals(utente2, manager.getUtente());
    }

    @Test
    void logoutShouldClearUtente() {
        SessionManager manager = SessionManager.getInstance();
        Utente utente = new Utente(1, TEST_EMAIL, TEST_PASSWORD, TEST_USER, TEST_PHONE);
        manager.login(utente);

        manager.logout();

        assertNull(manager.getUtente());
    }

    @Test
    void logoutWhenNoUserLoggedInShouldDoNothing() {
        SessionManager manager = SessionManager.getInstance();

        assertDoesNotThrow(() -> manager.logout());
        assertNull(manager.getUtente());
    }

    @Test
    void logoutMultipleTimesShouldRemainNull() {
        SessionManager manager = SessionManager.getInstance();
        Utente utente = new Utente(1, TEST_EMAIL, TEST_PASSWORD, TEST_USER, TEST_PHONE);
        manager.login(utente);

        manager.logout();
        manager.logout();
        manager.logout();

        assertNull(manager.getUtente());
    }

    @Test
    void loginLogoutCycleShouldWorkCorrectly() {
        SessionManager manager = SessionManager.getInstance();
        Utente utente1 = new Utente(1, "user1@example.com", "pass1", "User One", "1111111111");
        Utente utente2 = new Utente(2, "user2@example.com", "pass2", "User Two", "2222222222");

        manager.login(utente1);
        assertEquals(utente1, manager.getUtente());

        manager.logout();
        assertNull(manager.getUtente());

        manager.login(utente2);
        assertEquals(utente2, manager.getUtente());

        manager.logout();
        assertNull(manager.getUtente());
    }

    @Test
    void singletonPersistenceAcrossLoginLogout() {
        SessionManager manager1 = SessionManager.getInstance();
        Utente utente = new Utente(1, TEST_EMAIL, TEST_PASSWORD, TEST_USER, TEST_PHONE);
        manager1.login(utente);

        SessionManager manager2 = SessionManager.getInstance();

        assertSame(manager1, manager2);
        assertEquals(utente, manager2.getUtente());
    }
}
