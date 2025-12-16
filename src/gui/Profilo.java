package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseListener; // [Nuovo Import]

public class Profilo extends BaseFrame {
  private JPanel mainPanel;
  private JLabel lblTitolo;
  private JLabel lblUsername;
  private JLabel lblEmail;
  private JLabel lblTelefono;
  private JLabel lblMediaVoto;
  private JTabbedPane tabbedPane;
  private JTable tableRecensioni;
  private JTable tableAnnunci;

  private DefaultTableModel modelRecensioni;
  private DefaultTableModel modelAnnunci;

  public Profilo() {
    super("Profilo Utente");
    setContentPane(mainPanel);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setMinimumSize(new Dimension(600, 550));

    setupTables();

    pack();
    centraFinestra();
  }

  private void setupTables() {
    // --- Tabella Recensioni ---
    modelRecensioni = new DefaultTableModel(new Object[]{"Voto", "Descrizione"}, 0) {
      @Override public boolean isCellEditable(int row, int col) { return false; }
    };
    tableRecensioni.setModel(modelRecensioni);
    tableRecensioni.getColumnModel().getColumn(0).setMaxWidth(80);
    tableRecensioni.setRowHeight(25);
    tableRecensioni.getTableHeader().setResizingAllowed(false);
    tableRecensioni.getTableHeader().setReorderingAllowed(false);

    // --- Tabella Annunci ---
    modelAnnunci = new DefaultTableModel(new Object[]{"Titolo", "Categoria", "Tipo"}, 0) {
      @Override public boolean isCellEditable(int row, int col) { return false; }
    };
    tableAnnunci.setModel(modelAnnunci);
    tableAnnunci.setRowHeight(25);
    tableAnnunci.getTableHeader().setResizingAllowed(false);
    tableAnnunci.getTableHeader().setReorderingAllowed(false);
  }

  // [NUOVO METODO] Permette al Controller di ascoltare i click sulla tabella
  public void addTableAnnunciListener(MouseListener listener) {
    tableAnnunci.addMouseListener(listener);
  }

  // --- Metodi per Recensioni ---
  public void aggiungiRecensione(int voto, String descrizione) {
    String stelle = "★".repeat(voto);
    modelRecensioni.addRow(new Object[]{stelle, descrizione});
  }

  public void pulisciTabelle() {
    modelRecensioni.setRowCount(0);
    modelAnnunci.setRowCount(0);
  }

  // --- Metodi per Annunci ---
  public void aggiungiAnnuncio(String titolo, String categoria, String tipo) {
    modelAnnunci.addRow(new Object[]{titolo, categoria, tipo});
  }

  // --- Setters Dati Utente ---
  public void setTitoloProfilo(String titolo) {
    lblTitolo.setText(titolo);
    setTitle(titolo);
  }
  public void setUsername(String username) { lblUsername.setText(username); }
  public void setEmail(String email) { lblEmail.setText(email); }
  public void setTelefono(String telefono) { lblTelefono.setText(telefono); }

  public void setMediaVoto(double media) {
    String val = String.format("%.1f / 5", media);
    lblMediaVoto.setText(val);
    if (media >= 4) lblMediaVoto.setForeground(new Color(0, 150, 0));
    else if (media >= 2.5) lblMediaVoto.setForeground(new Color(200, 150, 0));
    else lblMediaVoto.setForeground(Color.RED);
  }
}