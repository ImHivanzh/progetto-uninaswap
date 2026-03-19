package utils;

import javax.swing.*;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.Calendar;
import java.util.Date;

/**
 * Helper per la creazione di componenti form.
 */
public class FormHelper {

  /**
   * Costruttore privato per nascondere quello pubblico implicito.
   */
  private FormHelper() {
    throw new AssertionError("Utility class non deve essere istanziata");
  }

  /**
   * Crea uno spinner per la data con formato yyyy-MM-dd.
   *
   * @return spinner della data
   */
  public static JSpinner createDateSpinner() {
    JSpinner spinner = new JSpinner(new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH));
    spinner.setEditor(new JSpinner.DateEditor(spinner, "yyyy-MM-dd"));
    return spinner;
  }

  /**
   * Crea uno spinner per l'ora con formato HH:mm.
   *
   * @return spinner dell'ora
   */
  public static JSpinner createTimeSpinner() {
    JSpinner spinner = new JSpinner(new SpinnerDateModel(new Date(), null, null, Calendar.MINUTE));
    spinner.setEditor(new JSpinner.DateEditor(spinner, "HH:mm"));
    return spinner;
  }

  /**
   * Prepara i vincoli base per un form a griglia.
   *
   * @return vincoli della griglia
   */
  public static GridBagConstraints createFormConstraints() {
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(4, 4, 4, 4);
    gbc.anchor = GridBagConstraints.WEST;
    return gbc;
  }

  /**
   * Aggiunge una riga al form con etichetta e campo.
   *
   * @param panel pannello destinazione
   * @param gbc vincoli della griglia
   * @param row indice della riga
   * @param label testo dell'etichetta
   * @param field componente input
   */
  public static void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent field) {
    gbc.gridx = 0;
    gbc.gridy = row;
    gbc.weightx = 0;
    gbc.fill = GridBagConstraints.NONE;
    panel.add(new JLabel(label), gbc);

    gbc.gridx = 1;
    gbc.weightx = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    panel.add(field, gbc);
  }
}
