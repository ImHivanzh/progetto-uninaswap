package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseListener;

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
  private JTable tableProposteRicevute;
  private JTable tableProposteInviate;

  private DefaultTableModel modelRecensioni;
  private DefaultTableModel modelAnnunci;
  private DefaultTableModel modelProposteRicevute;
  private DefaultTableModel modelProposteInviate;

  /**
   * Creates the profile view frame.
   */
  public Profilo() {
    super("Profilo Utente");
    setContentPane(mainPanel);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setMinimumSize(new Dimension(600, 550));

    setupTables();

    pack();
    centraFinestra();
  }

  /**
   * Initializes table models and table settings.
   */
  private void setupTables() {
    modelRecensioni = new DefaultTableModel(new Object[]{"Nome utente", "Voto", "Descrizione"}, 0) {
      @Override public boolean isCellEditable(int row, int col) { return false; }
    };
    tableRecensioni.setModel(modelRecensioni);
    tableRecensioni.getColumnModel().getColumn(1).setMaxWidth(80);
    tableRecensioni.setRowHeight(25);
    tableRecensioni.getTableHeader().setResizingAllowed(false);
    tableRecensioni.getTableHeader().setReorderingAllowed(false);

    modelAnnunci = new DefaultTableModel(new Object[]{"Titolo", "Categoria", "Tipo"}, 0) {
      @Override public boolean isCellEditable(int row, int col) { return false; }
    };
    tableAnnunci.setModel(modelAnnunci);
    tableAnnunci.setRowHeight(25);
    tableAnnunci.getTableHeader().setResizingAllowed(false);
    tableAnnunci.getTableHeader().setReorderingAllowed(false);

    modelProposteRicevute = new DefaultTableModel(
            new Object[]{"Da", "Annuncio", "Tipo", "Dettaglio", "Stato"}, 0) {
      @Override public boolean isCellEditable(int row, int col) { return false; }
    };
    tableProposteRicevute.setModel(modelProposteRicevute);
    tableProposteRicevute.setRowHeight(25);
    tableProposteRicevute.getTableHeader().setResizingAllowed(false);
    tableProposteRicevute.getTableHeader().setReorderingAllowed(false);

    modelProposteInviate = new DefaultTableModel(
            new Object[]{"A", "Annuncio", "Tipo", "Dettaglio", "Stato"}, 0) {
      @Override public boolean isCellEditable(int row, int col) { return false; }
    };
    tableProposteInviate.setModel(modelProposteInviate);
    tableProposteInviate.setRowHeight(25);
    tableProposteInviate.getTableHeader().setResizingAllowed(false);
    tableProposteInviate.getTableHeader().setReorderingAllowed(false);
  }

  /**
   * Adds a mouse listener for the listings table.
   *
   * @param listener mouse listener
   */
  public void addTableAnnunciListener(MouseListener listener) {
    tableAnnunci.addMouseListener(listener);
  }

  /**
   * Adds a mouse listener for the received proposals table.
   *
   * @param listener mouse listener
   */
  public void addTableProposteRicevuteListener(MouseListener listener) {
    tableProposteRicevute.addMouseListener(listener);
  }

  /**
   * Adds a mouse listener for the sent proposals table.
   *
   * @param listener mouse listener
   */
  public void addTableProposteInviateListener(MouseListener listener) {
    tableProposteInviate.addMouseListener(listener);
  }

  /**
   * Adds a review row to the reviews table.
   *
   * @param nomeUtente reviewer name
   * @param voto rating value
   * @param descrizione review text
   */
  public void aggiungiRecensione(String nomeUtente, int voto, String descrizione) {
    String stelle = "★".repeat(voto);
    modelRecensioni.addRow(new Object[]{nomeUtente, stelle, descrizione});
  }

  /**
   * Clears all table rows.
   */
  public void pulisciTabelle() {
    modelRecensioni.setRowCount(0);
    modelAnnunci.setRowCount(0);
    modelProposteRicevute.setRowCount(0);
    modelProposteInviate.setRowCount(0);
  }

  /**
   * Adds a listing row to the listings table.
   *
   * @param titolo listing title
   * @param categoria listing category
   * @param tipo listing type
   */
  public void aggiungiAnnuncio(String titolo, String categoria, String tipo) {
    modelAnnunci.addRow(new Object[]{titolo, categoria, tipo});
  }

  /**
   * Adds a received proposal row to the table.
   *
   * @param utente proposer user
   * @param annuncio listing title
   * @param tipo listing type
   * @param dettaglio detail text
   * @param stato status text
   */
  public void aggiungiPropostaRicevuta(String utente, String annuncio, String tipo, String dettaglio, String stato) {
    modelProposteRicevute.addRow(new Object[]{utente, annuncio, tipo, dettaglio, stato});
  }

  /**
   * Adds a sent proposal row to the table.
   *
   * @param utente recipient user
   * @param annuncio listing title
   * @param tipo listing type
   * @param dettaglio detail text
   * @param stato status text
   */
  public void aggiungiPropostaInviata(String utente, String annuncio, String tipo, String dettaglio, String stato) {
    modelProposteInviate.addRow(new Object[]{utente, annuncio, tipo, dettaglio, stato});
  }

  /**
   * Sets the profile title in the view and window.
   *
   * @param titolo profile title
   */
  public void setTitoloProfilo(String titolo) {
    lblTitolo.setText(titolo);
    setTitle(titolo);
  }

  /**
   * Sets the username label.
   *
   * @param username username
   */
  public void setUsername(String username) { lblUsername.setText(username); }

  /**
   * Sets the email label.
   *
   * @param email email address
   */
  public void setEmail(String email) { lblEmail.setText(email); }

  /**
   * Sets the phone label.
   *
   * @param telefono phone number
   */
  public void setTelefono(String telefono) { lblTelefono.setText(telefono); }

  /**
   * Sets the average rating label and color.
   *
   * @param media average rating
   */
  public void setMediaVoto(double media) {
    String val = String.format("%.1f / 5", media);
    lblMediaVoto.setText(val);
    if (media >= 4) lblMediaVoto.setForeground(new Color(0, 150, 0));
    else if (media >= 2.5) lblMediaVoto.setForeground(new Color(200, 150, 0));
    else lblMediaVoto.setForeground(Color.RED);
  }
}
