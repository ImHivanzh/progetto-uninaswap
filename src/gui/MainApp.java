package gui;

import javax.swing.*;
import java.awt.*;

public class MainApp {
  private JPanel mainPanel;

  // Header Components
  private JButton btnBacheca;      // Ex btnAnn1
  private JButton btnPubblica;     // Ex btnAnn2
  private JButton btnMieiAnnunci;  // Nuovo bottone

  // Sidebar Components
  private JList categoryList;
  private JRadioButton radioTutti;
  private JRadioButton radioScambio;
  private JRadioButton radioVendita;
  private JRadioButton radioRegalo;
  private JTextField txtPrezzoMax; // Nuovo campo filtro

  // Content Components
  private JTextField searchField;
  private JButton searchButton;

  // Card Example Buttons
  private JButton btnView1;
  private JButton btnView2;

  public MainApp() {
    // Qui andrebbero i listener per gestire i filtri
    // Esempio: Click su "Pubblica" apre un dialog per creare un annuncio
  }

  public static void main(String[] args) {
    JFrame frame = new JFrame("EcoMarket - Compra, Vendi, Scambia");
    frame.setContentPane(new MainApp().mainPanel);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setSize(900, 650);
    frame.setVisible(true);
  }
}