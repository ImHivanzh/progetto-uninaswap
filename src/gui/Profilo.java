package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader; // Import necessario
import java.awt.*;

public class Profilo extends BaseFrame {
  private JPanel mainPanel;
  private JLabel lblTitolo;
  private JLabel lblUsername;
  private JLabel lblEmail;
  private JLabel lblTelefono;
  private JLabel lblMediaVoto;
  private JTable tableRecensioni;
  private DefaultTableModel tableModel;

  public Profilo() {
    super("Profilo Utente");
    setContentPane(mainPanel);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    // Imposto una dimensione minima alla finestra per evitare che si stringa troppo
    setMinimumSize(new Dimension(550, 450));

    setupTable();

    pack();
    centraFinestra();
  }

  private void setupTable() {
    tableModel = new DefaultTableModel(
            new Object[]{"Voto", "Descrizione"},
            0
    ) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };
    tableRecensioni.setModel(tableModel);

    // 1. Imposto larghezza fissa per la colonna Voto
    tableRecensioni.getColumnModel().getColumn(0).setMinWidth(100);
    tableRecensioni.getColumnModel().getColumn(0).setMaxWidth(100);
    tableRecensioni.getColumnModel().getColumn(0).setPreferredWidth(100);

    // 2. Disabilito il ridimensionamento delle colonne dall'header
    JTableHeader header = tableRecensioni.getTableHeader();
    header.setResizingAllowed(false); // L'utente non può più trascinare i bordi delle colonne

    // 3. Disabilito lo spostamento delle colonne (opzionale, ma consigliato)
    header.setReorderingAllowed(false);

    tableRecensioni.setRowHeight(25);
  }

  // --- SETTERS per aggiornare la UI ---

  public void setTitoloProfilo(String titolo) {
    lblTitolo.setText(titolo);
    setTitle(titolo);
  }

  public void setUsername(String username) {
    lblUsername.setText(username);
  }

  public void setEmail(String email) {
    lblEmail.setText(email);
  }

  public void setTelefono(String telefono) {
    lblTelefono.setText(telefono);
  }

  public void setMediaVoto(double media) {
    String val = String.format("%.1f / 5", media);
    lblMediaVoto.setText(val);

    if (media >= 4) lblMediaVoto.setForeground(new Color(0, 150, 0));
    else if (media >= 2.5) lblMediaVoto.setForeground(new Color(200, 150, 0));
    else lblMediaVoto.setForeground(Color.RED);
  }

  public void aggiungiRecensione(int voto, String descrizione) {
    String stelle = "★".repeat(voto);
    tableModel.addRow(new Object[]{stelle, descrizione});
  }

  public void pulisciTabella() {
    tableModel.setRowCount(0);
  }
}