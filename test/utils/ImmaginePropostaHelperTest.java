package utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import model.Annuncio;
import model.PropostaRiepilogo;
import model.Utente;
import model.enums.Categoria;
import model.enums.TipoAnnuncio;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test completi per la classe ImmaginePropostaHelper.
 */
class ImmaginePropostaHelperTest {

    private static final String TEST_USERNAME = "test";
    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_PHONE = "1234567890";
    private static final String TEST_TITLE = "Test";
    private static final String TEST_DESC = "Desc";
    @SuppressWarnings("java:S2068") // Password fittizia per test, non credenziale reale
    private static final String DUMMY_PASSWORD = "dummyPass123!";

    // ========== hasImmagine Tests ==========

    @Test
    void hasImmagineWithNullShouldReturnFalse() {
        assertFalse(ImmaginePropostaHelper.hasImmagine(null));
    }

    @Test
    void hasImmagineWithNullImmaggineShouldReturnFalse() {
        Utente utente = new Utente(1, TEST_USERNAME, DUMMY_PASSWORD, TEST_EMAIL, TEST_PHONE);
        Annuncio annuncio = new Annuncio(1, TEST_TITLE, TEST_DESC, Categoria.LIBRI, utente, TipoAnnuncio.VENDITA);
        PropostaRiepilogo proposta = new PropostaRiepilogo(annuncio, null, TEST_TITLE, false, false, null);

        assertFalse(ImmaginePropostaHelper.hasImmagine(proposta));
    }

    @Test
    void hasImmagineWithEmptyByteArrayShouldReturnFalse() {
        Utente utente = new Utente(1, TEST_USERNAME, DUMMY_PASSWORD, TEST_EMAIL, TEST_PHONE);
        Annuncio annuncio = new Annuncio(1, TEST_TITLE, TEST_DESC, Categoria.LIBRI, utente, TipoAnnuncio.VENDITA);
        PropostaRiepilogo proposta = new PropostaRiepilogo(annuncio, null, TEST_TITLE, false, false, new byte[0]);

        assertFalse(ImmaginePropostaHelper.hasImmagine(proposta));
    }

    @Test
    void hasImmagineWithValidImageShouldReturnTrue() throws IOException {
        Utente utente = new Utente(1, TEST_USERNAME, DUMMY_PASSWORD, TEST_EMAIL, TEST_PHONE);
        Annuncio annuncio = new Annuncio(1, TEST_TITLE, TEST_DESC, Categoria.LIBRI, utente, TipoAnnuncio.VENDITA);
        byte[] imageBytes = createTestImageBytes();
        PropostaRiepilogo proposta = new PropostaRiepilogo(annuncio, null, TEST_TITLE, false, false, imageBytes);

        assertTrue(ImmaginePropostaHelper.hasImmagine(proposta));
    }

    @Test
    void hasImmagineWithSingleByteImageShouldReturnTrue() {
        Utente utente = new Utente(1, TEST_USERNAME, DUMMY_PASSWORD, TEST_EMAIL, TEST_PHONE);
        Annuncio annuncio = new Annuncio(1, TEST_TITLE, TEST_DESC, Categoria.LIBRI, utente, TipoAnnuncio.VENDITA);
        PropostaRiepilogo proposta = new PropostaRiepilogo(annuncio, null, TEST_TITLE, false, false, new byte[]{1});

        assertTrue(ImmaginePropostaHelper.hasImmagine(proposta));
    }

    @Test
    void hasImmagineWithNullAnnuncioShouldReturnFalse() {
        PropostaRiepilogo proposta = new PropostaRiepilogo(null, null, TEST_TITLE, false, false, new byte[]{1});

        // Should not crash, behavior depends on implementation
        // If it checks proposta != null first, it will check the image
        boolean result = ImmaginePropostaHelper.hasImmagine(proposta);
        assertTrue(result); // Has image bytes even without annuncio
    }

    // ========== mostraImmagine Tests ==========
    // Note: mostraImmagine tests are skipped as they require GUI components

    // ========== Helper Methods ==========

    /**
     * Creates a simple test image as byte array.
     */
    private byte[] createTestImageBytes() throws IOException {
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        return baos.toByteArray();
    }

    // ========== Edge Cases ==========

    @Test
    void hasImmagineWithLargeImageShouldReturnTrue() throws IOException {
        Utente utente = new Utente(1, TEST_USERNAME, DUMMY_PASSWORD, TEST_EMAIL, TEST_PHONE);
        Annuncio annuncio = new Annuncio(1, TEST_TITLE, TEST_DESC, Categoria.LIBRI, utente, TipoAnnuncio.VENDITA);
        BufferedImage largeImage = new BufferedImage(2000, 2000, BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(largeImage, "png", baos);
        byte[] imageBytes = baos.toByteArray();
        PropostaRiepilogo proposta = new PropostaRiepilogo(annuncio, null, TEST_TITLE, false, false, imageBytes);

        assertTrue(ImmaginePropostaHelper.hasImmagine(proposta));
    }

    @Test
    void hasImmagineMultipleCallsSameObjectShouldBeConsistent() throws IOException {
        Utente utente = new Utente(1, TEST_USERNAME, DUMMY_PASSWORD, TEST_EMAIL, TEST_PHONE);
        Annuncio annuncio = new Annuncio(1, TEST_TITLE, TEST_DESC, Categoria.LIBRI, utente, TipoAnnuncio.VENDITA);
        byte[] imageBytes = createTestImageBytes();
        PropostaRiepilogo proposta = new PropostaRiepilogo(annuncio, null, TEST_TITLE, false, false, imageBytes);

        boolean result1 = ImmaginePropostaHelper.hasImmagine(proposta);
        boolean result2 = ImmaginePropostaHelper.hasImmagine(proposta);
        boolean result3 = ImmaginePropostaHelper.hasImmagine(proposta);

        assertEquals(result1, result2);
        assertEquals(result2, result3);
        assertTrue(result1);
    }
}
