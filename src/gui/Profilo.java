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
   * Crea vista profilo finestra.
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
   * Inizializza tabella modelli e tabella impostazioni.
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
   * Aggiunge mouse listener per tabella annunci.
   *
   * @param listener mouse listener
   */
  public void addTableAnnunciListener(MouseListener listener) {
    tableAnnunci.addMouseListener(listener);
  }

  /**
   * Aggiunge mouse listener per ricevute proposte tabella.
   *
   * @param listener mouse listener
   */
  public void addTableProposteRicevuteListener(MouseListener listener) {
    tableProposteRicevute.addMouseListener(listener);
  }

  /**
   * Aggiunge mouse listener per inviate proposte tabella.
   *
   * @param listener mouse listener
   */
  public void addTableProposteInviateListener(MouseListener listener) {
    tableProposteInviate.addMouseListener(listener);
  }

  /**
   * Aggiunge recensione riga a recensioni tabella.
   *
   * @param nomeUtente recensore nome
   * @param voto valutazione valore
   * @param descrizione testo recensione
   */
  public void aggiungiRecensione(String nomeUtente, int voto, String descrizione) {
    String stelle = "★".repeat(voto);
    modelRecensioni.addRow(new Object[]{nomeUtente, stelle, descrizione});
  }

  /**
   * Cancella tutti tabella righe.
   */
  public void pulisciTabelle() {
    modelRecensioni.setRowCount(0);
    modelAnnunci.setRowCount(0);
    modelProposteRicevute.setRowCount(0);
    modelProposteInviate.setRowCount(0);
  }

  /**
   * Aggiunge annuncio riga a tabella annunci.
   *
   * @param titolo annuncio titolo
   * @param categoria annuncio categoria
   * @param tipo tipo annuncio
   */
  public void aggiungiAnnuncio(String titolo, String categoria, String tipo) {
    modelAnnunci.addRow(new Object[]{titolo, categoria, tipo});
  }

  /**
   * Aggiunge ricevute proposta riga a tabella.
   *
   * @param utente proponente utente
   * @param annuncio annuncio titolo
   * @param tipo tipo annuncio
   * @param dettaglio dettaglio testo
   * @param stato testo stato
   */
  public void aggiungiPropostaRicevuta(String utente, String annuncio, String tipo, String dettaglio, String stato) {
    modelProposteRicevute.addRow(new Object[]{utente, annuncio, tipo, dettaglio, stato});
  }

  /**
   * Aggiunge inviate proposta riga a tabella.
   *
   * @param utente destinatario utente
   * @param annuncio annuncio titolo
   * @param tipo tipo annuncio
   * @param dettaglio dettaglio testo
   * @param stato testo stato
   */
  public void aggiungiPropostaInviata(String utente, String annuncio, String tipo, String dettaglio, String stato) {
    modelProposteInviate.addRow(new Object[]{utente, annuncio, tipo, dettaglio, stato});
  }

  /**
   * Imposta profilo titolo in vista e finestra.
   *
   * @param titolo profilo titolo
   */
  public void setTitoloProfilo(String titolo) {
    lblTitolo.setText(titolo);
    setTitle(titolo);
  }

  /**
   * Nasconde tab delle proposte dal profilo.
   */
  public void nascondiTabProposte() {
    if (tabbedPane == null) {
      return;
    }
    rimuoviTabSePresente("Proposte Ricevute");
    rimuoviTabSePresente("Proposte Inviate");
  }

  /**
   * Imposta username etichetta.
   *
   * @param username username
   */
  public void setUsername(String username) { lblUsername.setText(username); }

  /**
   * Imposta email etichetta.
   *
   * @param email email indirizzo
   */
  public void setEmail(String email) { lblEmail.setText(email); }

  /**
   * Imposta telefono etichetta.
   *
   * @param telefono numero telefono
   */
  public void setTelefono(String telefono) { lblTelefono.setText(telefono); }

  /**
   * Imposta valutazione media etichetta e colore.
   *
   * @param media valutazione media
   */
  public void setMediaVoto(double media) {
    String val = String.format("%.1f / 5", media);
    lblMediaVoto.setText(val);
    if (media >= 4) lblMediaVoto.setForeground(new Color(0, 150, 0));
    else if (media >= 2.5) lblMediaVoto.setForeground(new Color(200, 150, 0));
    else lblMediaVoto.setForeground(Color.RED);
  }

  private void rimuoviTabSePresente(String titolo) {
    for (int i = tabbedPane.getTabCount() - 1; i >= 0; i--) {
      if (titolo.equals(tabbedPane.getTitleAt(i))) {
        tabbedPane.removeTabAt(i);
      }
    }
  }
}
