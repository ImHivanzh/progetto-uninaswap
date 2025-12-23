package gui;

import javax.swing.*;
import java.awt.*;

public class MainApp {
  private JPanel mainPanel;

  private JButton btnBacheca;
  private JButton btnPubblica;
  private JButton btnMieiAnnunci;

  private JList categoryList;
  private JRadioButton radioTutti;
  private JRadioButton radioScambio;
  private JRadioButton radioVendita;
  private JRadioButton radioRegalo;
  private JTextField txtPrezzoMax;

  private JTextField searchField;
  private JButton searchButton;

  private JButton btnView1;
  private JButton btnView2;

  /**
   * Creates the main app UI container.
   */
  public MainApp() {
  }

  /**
   * Launches a standalone demo frame for the main app layout.
   *
   * @param args command-line arguments
   */
  public static void main(String[] args) {
    JFrame frame = new JFrame("EcoMarket - Compra, Vendi, Scambia");
    frame.setContentPane(new MainApp().mainPanel);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setSize(900, 650);
    frame.setVisible(true);
  }
}
