package utils;

import model.PropostaRiepilogo;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * Helper per la gestione delle immagini delle proposte.
 */
public class ImmaginePropostaHelper {

  /**
   * Costruttore privato per nascondere quello pubblico implicito.
   */
  private ImmaginePropostaHelper() {
    throw new AssertionError("Utility class non deve essere istanziata");
  }

  /**
   * Verifica presenza immagine nella proposta.
   *
   * @param proposta proposta da verificare
   * @return true se immagine disponibile
   */
  public static boolean hasImmagine(PropostaRiepilogo proposta) {
    return proposta != null && proposta.immagine() != null && proposta.immagine().length > 0;
  }

  /**
   * Mostra immagine della proposta in un dialog.
   *
   * @param proposta proposta selezionata
   * @param parent componente parent per il dialog
   * @param onError callback per gestire errori
   */
  public static void mostraImmagine(PropostaRiepilogo proposta, JFrame parent, Runnable onError) {
    if (!hasImmagine(proposta)) {
      onError.run();
      return;
    }

    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    int maxWidth = (int) Math.round(screen.width * 0.6);
    int maxHeight = (int) Math.round(screen.height * 0.6);
    ImageIcon icon = creaIcona(proposta.immagine(), maxWidth, maxHeight);

    if (icon == null) {
      onError.run();
      return;
    }

    JLabel label = new JLabel(icon);
    label.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
    JOptionPane.showMessageDialog(parent, label, "Immagine proposta", JOptionPane.PLAIN_MESSAGE);
  }

  /**
   * Crea icona ridimensionata da bytes immagine.
   *
   * @param bytes dati immagine
   * @param maxWidth larghezza massima
   * @param maxHeight altezza massima
   * @return icona ridimensionata o null
   */
  private static ImageIcon creaIcona(byte[] bytes, int maxWidth, int maxHeight) {
    if (bytes == null || bytes.length == 0) {
      return null;
    }
    ImageIcon icon = new ImageIcon(bytes);
    int width = icon.getIconWidth();
    int height = icon.getIconHeight();
    if (width <= 0 || height <= 0) {
      return icon;
    }
    double scale = Math.min(1.0, Math.min((double) maxWidth / width, (double) maxHeight / height));
    if (scale == 1.0) {
      return icon;
    }
    int targetW = Math.max(1, (int) Math.round(width * scale));
    int targetH = Math.max(1, (int) Math.round(height * scale));
    Image img = icon.getImage().getScaledInstance(targetW, targetH, Image.SCALE_SMOOTH);
    return new ImageIcon(img);
  }
}
