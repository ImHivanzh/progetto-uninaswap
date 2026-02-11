package utils;

import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Utility per gestione finestre modali e ripristino stato.
 */
public final class WindowManager {
  /**
   * Utility class, non istanziabile.
   */
  private WindowManager() {}

  /**
   * Apre nuova finestra e disabilita quella corrente.
   *
   * @param current finestra corrente
   * @param next finestra successiva
   */
  public static void open(Window current, Window next) {
    if (next == null) {
      return;
    }
    attachRestoreListener(current, next);
    if (current != null) {
      current.setEnabled(false);
    }
    next.setVisible(true);
    next.toFront();
  }
si
  /**
   * Aggancia listener per ripristinare finestra precedente alla chiusura.
   *
   * @param previous finestra precedente
   * @param next finestra successiva
   */
  private static void attachRestoreListener(final Window previous, Window next) {
    if (previous == null) {
      return;
    }
    next.addWindowListener(new WindowAdapter() {
      /**
       * Ripristina finestra precedente se ancora disponibile.
       */
      private void restorePrevious() {
        if (previous.isDisplayable()) {
          previous.setEnabled(true);
          previous.toFront();
        }
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public void windowClosed(WindowEvent e) {
        restorePrevious();
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public void windowClosing(WindowEvent e) {
        restorePrevious();
      }
    });
  }
}
