package utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test completi per la classe Constanti.
 */
class ConstantiTest {

    // ========== Tipo Annuncio Tests ==========

    @Test
    void tipoVenditaShouldHaveCorrectValue() {
        assertEquals("VENDITA", Constanti.TIPO_VENDITA);
    }

    @Test
    void tipoScambioShouldHaveCorrectValue() {
        assertEquals("SCAMBIO", Constanti.TIPO_SCAMBIO);
    }

    @Test
    void tipoRegaloShouldHaveCorrectValue() {
        assertEquals("REGALO", Constanti.TIPO_REGALO);
    }

    @Test
    void tipoAnnuncioShouldBeDistinct() {
        assertNotEquals(Constanti.TIPO_VENDITA, Constanti.TIPO_SCAMBIO);
        assertNotEquals(Constanti.TIPO_VENDITA, Constanti.TIPO_REGALO);
        assertNotEquals(Constanti.TIPO_SCAMBIO, Constanti.TIPO_REGALO);
    }

    // ========== Filtri Tests ==========

    @Test
    void categoriaTutteShouldHaveCorrectValue() {
        assertEquals("Tutte", Constanti.CATEGORIA_TUTTE);
    }

    @Test
    void tipoTuttiShouldHaveCorrectValue() {
        assertEquals("Tutti", Constanti.TIPO_TUTTI);
    }

    // ========== Tabelle Database Tests ==========

    @Test
    void tabellaVenditaShouldHaveCorrectValue() {
        assertEquals("vendita", Constanti.TABELLA_VENDITA);
    }

    @Test
    void tabellaScambioShouldHaveCorrectValue() {
        assertEquals("scambio", Constanti.TABELLA_SCAMBIO);
    }

    @Test
    void tabellaRegaloShouldHaveCorrectValue() {
        assertEquals("regalo", Constanti.TABELLA_REGALO);
    }

    @Test
    void tabelleShouldBeDistinct() {
        assertNotEquals(Constanti.TABELLA_VENDITA, Constanti.TABELLA_SCAMBIO);
        assertNotEquals(Constanti.TABELLA_VENDITA, Constanti.TABELLA_REGALO);
        assertNotEquals(Constanti.TABELLA_SCAMBIO, Constanti.TABELLA_REGALO);
    }

    @Test
    void tabelleShouldBeLowercase() {
        assertEquals(Constanti.TABELLA_VENDITA.toLowerCase(), Constanti.TABELLA_VENDITA);
        assertEquals(Constanti.TABELLA_SCAMBIO.toLowerCase(), Constanti.TABELLA_SCAMBIO);
        assertEquals(Constanti.TABELLA_REGALO.toLowerCase(), Constanti.TABELLA_REGALO);
    }

    // ========== Constructor Tests ==========

    @Test
    void constructorShouldThrowAssertionError() {
        Exception exception = assertThrows(Exception.class, () -> {
            java.lang.reflect.Constructor<Constanti> constructor = Constanti.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
        });

        assertTrue(exception instanceof java.lang.reflect.InvocationTargetException);
        assertTrue(((java.lang.reflect.InvocationTargetException) exception).getCause() instanceof AssertionError);
    }

    // ========== Consistency Tests ==========

    @Test
    void tipoConstantsShouldMatchTabellaConstants() {
        // Verify naming consistency between TIPO and TABELLA constants
        assertEquals(Constanti.TIPO_VENDITA, Constanti.TABELLA_VENDITA.toUpperCase());
        assertEquals(Constanti.TIPO_SCAMBIO, Constanti.TABELLA_SCAMBIO.toUpperCase());
        assertEquals(Constanti.TIPO_REGALO, Constanti.TABELLA_REGALO.toUpperCase());
    }
}
