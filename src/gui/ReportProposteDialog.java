package gui;

import javax.swing.*;
import java.awt.*;

/**
 * Dialogo report proposte inviate.
 */
public class ReportProposteDialog extends JDialog {

  private static final String EURO_FORMAT = "%.2f €";

  /**
   * Pannello contenuto.
   */
  private JPanel contentPane;
  /**
   * Pannello grafico.
   */
  private JPanel chartPanel;
  /**
   * Etichetta totale vendita.
   */
  private JLabel lblTotaleVendita;
  /**
   * Etichetta accettate vendita.
   */
  private JLabel lblAccettateVendita;
  /**
   * Etichetta valore minimo vendita.
   */
  private JLabel lblValoreMinimo;
  /**
   * Etichetta valore massimo vendita.
   */
  private JLabel lblValoreMassimo;
  /**
   * Etichetta valore medio vendita.
   */
  private JLabel lblValoreMedio;
  /**
   * Etichetta totale scambio.
   */
  private JLabel lblTotaleScambio;
  /**
   * Etichetta accettate scambio.
   */
  private JLabel lblAccettateScambio;
  /**
   * Etichetta totale regalo.
   */
  private JLabel lblTotaleRegalo;
  /**
   * Etichetta accettate regalo.
   */
  private JLabel lblAccettateRegalo;

  /**
   * Crea dialogo report.
   *
   * @param owner frame proprietario
   */
  public ReportProposteDialog(Frame owner) {
    super(owner, "Report Proposte Inviate", true);
    setContentPane(contentPane);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    pack();
    setMinimumSize(new Dimension(800, 600));
    setLocationRelativeTo(owner);
  }

  public void setChart(JPanel chart) {
    chartPanel.removeAll();
    chartPanel.setLayout(new BorderLayout());
    chartPanel.add(chart, BorderLayout.CENTER);
    chartPanel.revalidate();
    chartPanel.repaint();
  }

  public void setTotaleVendita(int totale) {
    lblTotaleVendita.setText(String.valueOf(totale));
  }

  public void setAccettateVendita(int accettate) {
    lblAccettateVendita.setText(String.valueOf(accettate));
  }

  public void setValoreMinimo(double minimo) {
    lblValoreMinimo.setText(String.format(EURO_FORMAT, minimo));
  }

  public void setValoreMassimo(double massimo) {
    lblValoreMassimo.setText(String.format(EURO_FORMAT, massimo));
  }

  public void setValoreMedio(double medio) {
    lblValoreMedio.setText(String.format(EURO_FORMAT, medio));
  }

  public void setTotaleScambio(int totale) {
    lblTotaleScambio.setText(String.valueOf(totale));
  }

  public void setAccettateScambio(int accettate) {
    lblAccettateScambio.setText(String.valueOf(accettate));
  }

  public void setTotaleRegalo(int totale) {
    lblTotaleRegalo.setText(String.valueOf(totale));
  }

  public void setAccettateRegalo(int accettate) {
    lblAccettateRegalo.setText(String.valueOf(accettate));
  }
}
