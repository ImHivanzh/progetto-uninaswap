package utils;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test completi per la classe FormHelper.
 */
class FormHelperTest {

    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "HH:mm";
    private static final String TEST_LABEL = "Test:";
    private static final String LABEL_TEXT = "Label:";
    private static final String TEST_LABEL_LONG = "Test Label:";
    private static final String NAME_LABEL = "Name:";
    private static final String EMAIL_LABEL = "Email:";
    private static final String PHONE_LABEL = "Phone:";
    private static final String TEXT_LABEL = "Text:";
    private static final String COMBO_LABEL = "Combo:";
    private static final String SPINNER_LABEL = "Spinner:";
    private static final String CHECK_LABEL = "Check:";

    @Test
    void createDateSpinnerShouldReturnNonNull() {
        JSpinner spinner = FormHelper.createDateSpinner();
        assertNotNull(spinner);
    }

    @Test
    void createDateSpinnerShouldHaveSpinnerDateModel() {
        JSpinner spinner = FormHelper.createDateSpinner();
        assertTrue(spinner.getModel() instanceof SpinnerDateModel);
    }

    @Test
    void createDateSpinnerShouldHaveDateEditor() {
        JSpinner spinner = FormHelper.createDateSpinner();
        assertTrue(spinner.getEditor() instanceof JSpinner.DateEditor);
    }

    @Test
    void createDateSpinnerShouldHaveCorrectFormat() {
        JSpinner spinner = FormHelper.createDateSpinner();
        JSpinner.DateEditor editor = (JSpinner.DateEditor) spinner.getEditor();

        // Verify format by checking the pattern
        String pattern = editor.getFormat().toPattern();
        assertEquals(DATE_FORMAT, pattern);
    }

    @Test
    void createDateSpinnerShouldHaveCurrentDate() {
        JSpinner spinner = FormHelper.createDateSpinner();
        Object value = spinner.getValue();

        assertNotNull(value);
        assertTrue(value instanceof Date);
    }

    @Test
    void createDateSpinnerShouldUseDayOfMonthCalendarField() {
        JSpinner spinner = FormHelper.createDateSpinner();
        SpinnerDateModel model = (SpinnerDateModel) spinner.getModel();

        assertEquals(Calendar.DAY_OF_MONTH, model.getCalendarField());
    }

    @Test
    void createTimeSpinnerShouldReturnNonNull() {
        JSpinner spinner = FormHelper.createTimeSpinner();
        assertNotNull(spinner);
    }

    @Test
    void createTimeSpinnerShouldHaveSpinnerDateModel() {
        JSpinner spinner = FormHelper.createTimeSpinner();
        assertTrue(spinner.getModel() instanceof SpinnerDateModel);
    }

    @Test
    void createTimeSpinnerShouldHaveDateEditor() {
        JSpinner spinner = FormHelper.createTimeSpinner();
        assertTrue(spinner.getEditor() instanceof JSpinner.DateEditor);
    }

    @Test
    void createTimeSpinnerShouldHaveCorrectFormat() {
        JSpinner spinner = FormHelper.createTimeSpinner();
        JSpinner.DateEditor editor = (JSpinner.DateEditor) spinner.getEditor();

        String pattern = editor.getFormat().toPattern();
        assertEquals(TIME_FORMAT, pattern);
    }

    @Test
    void createTimeSpinnerShouldHaveCurrentTime() {
        JSpinner spinner = FormHelper.createTimeSpinner();
        Object value = spinner.getValue();

        assertNotNull(value);
        assertTrue(value instanceof Date);
    }

    @Test
    void createTimeSpinnerShouldUseMinuteCalendarField() {
        JSpinner spinner = FormHelper.createTimeSpinner();
        SpinnerDateModel model = (SpinnerDateModel) spinner.getModel();

        assertEquals(Calendar.MINUTE, model.getCalendarField());
    }

    @Test
    void createFormConstraintsShouldReturnNonNull() {
        GridBagConstraints gbc = FormHelper.createFormConstraints();
        assertNotNull(gbc);
    }

    @Test
    void createFormConstraintsShouldHaveCorrectInsets() {
        GridBagConstraints gbc = FormHelper.createFormConstraints();

        assertEquals(4, gbc.insets.top);
        assertEquals(4, gbc.insets.left);
        assertEquals(4, gbc.insets.bottom);
        assertEquals(4, gbc.insets.right);
    }

    @Test
    void createFormConstraintsShouldHaveWestAnchor() {
        GridBagConstraints gbc = FormHelper.createFormConstraints();
        assertEquals(GridBagConstraints.WEST, gbc.anchor);
    }

    @Test
    void addFormRowShouldAddLabelAndField() {
        JPanel panel = new JPanel();
        GridBagConstraints gbc = FormHelper.createFormConstraints();
        JTextField field = new JTextField();

        FormHelper.addFormRow(panel, gbc, 0, TEST_LABEL_LONG, field);

        assertEquals(2, panel.getComponentCount());
    }

    @Test
    void addFormRowShouldAddLabelAtCorrectPosition() {
        JPanel panel = new JPanel();
        GridBagConstraints gbc = FormHelper.createFormConstraints();
        JTextField field = new JTextField();

        FormHelper.addFormRow(panel, gbc, 2, LABEL_TEXT, field);

        // After adding, gbc reflects the last component added (the field)
        assertEquals(1, gbc.gridx);
        assertEquals(2, gbc.gridy);
    }

    @Test
    void addFormRowShouldSetLabelConstraints() {
        JPanel panel = new JPanel();
        GridBagConstraints gbc = FormHelper.createFormConstraints();
        JTextField field = new JTextField();

        FormHelper.addFormRow(panel, gbc, 0, LABEL_TEXT, field);

        // After adding, gbc should have field constraints (last set)
        assertEquals(1, gbc.gridx);
        assertEquals(1.0, gbc.weightx);
        assertEquals(GridBagConstraints.HORIZONTAL, gbc.fill);
    }

    @Test
    void addFormRowWithMultipleRowsShouldAddAll() {
        JPanel panel = new JPanel();
        GridBagConstraints gbc = FormHelper.createFormConstraints();

        FormHelper.addFormRow(panel, gbc, 0, NAME_LABEL, new JTextField());
        FormHelper.addFormRow(panel, gbc, 1, EMAIL_LABEL, new JTextField());
        FormHelper.addFormRow(panel, gbc, 2, PHONE_LABEL, new JTextField());

        assertEquals(6, panel.getComponentCount()); // 3 labels + 3 fields
    }

    @Test
    void addFormRowWithDifferentComponentsShouldWork() {
        JPanel panel = new JPanel();
        GridBagConstraints gbc = FormHelper.createFormConstraints();

        FormHelper.addFormRow(panel, gbc, 0, TEXT_LABEL, new JTextField());
        FormHelper.addFormRow(panel, gbc, 1, COMBO_LABEL, new JComboBox<>());
        FormHelper.addFormRow(panel, gbc, 2, SPINNER_LABEL, new JSpinner());
        FormHelper.addFormRow(panel, gbc, 3, CHECK_LABEL, new JCheckBox());

        assertEquals(8, panel.getComponentCount());
    }

    @Test
    void addFormRowLabelShouldBeJLabel() {
        JPanel panel = new JPanel();
        GridBagConstraints gbc = FormHelper.createFormConstraints();

        FormHelper.addFormRow(panel, gbc, 0, TEST_LABEL, new JTextField());

        assertTrue(panel.getComponent(0) instanceof JLabel);
        assertEquals(TEST_LABEL, ((JLabel) panel.getComponent(0)).getText());
    }

    @Test
    void addFormRowFieldShouldBeAddedSecond() {
        JPanel panel = new JPanel();
        GridBagConstraints gbc = FormHelper.createFormConstraints();
        JTextField field = new JTextField();

        FormHelper.addFormRow(panel, gbc, 0, TEST_LABEL, field);

        assertSame(field, panel.getComponent(1));
    }
}
