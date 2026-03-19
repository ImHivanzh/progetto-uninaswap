package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 * Vista profilo utente.
 */
public class Profilo extends BaseFrame {
  /**
   * Pannello principale.
   */
  private JPanel mainPanel;
  /**
   * Etichetta titolo.
   */
  private JLabel lblTitolo;
  /**
   * Etichetta username.
   */
  private JLabel lblUsername;
  /**
   * Etichetta email.
   */
  private JLabel lblEmail;
  /**
   * Etichetta telefono.
   */
  private JLabel lblTelefono;
  /**
   * Etichetta media voto.
   */
  private JLabel lblMediaVoto;
  /**
   * Pannello tab.
   */
  private JTabbedPane tabbedPane;
  /**
   * Tabella recensioni.
   */
  private JTable tableRecensioni;
  /**
   * Tabella annunci.
   */
  private JTable tableAnnunci;
  /**
   * Tabella proposte ricevute.
   */
  private JTable tableProposteRicevute;
  /**
   * Tabella proposte inviate.
   */
  private JTable tableProposteInviate;
  /**
   * Pulsante recensioni ricevute.
   */
  private JButton btnRecensioneRicevuta;
  /**
   * Pulsante recensioni inviate.
   */
  private JButton btnRecensioneInviata;
  /**
   * Pulsante modifica proposta inviata.
   */
  private JButton btnModificaProposta;
  /**
   * Pulsante annulla proposta inviata.
   */
  private JButton btnAnnullaProposta;
  /**
   * Pulsante genera report proposte.
   */
  private JButton btnGeneraReport;
  /**
   * Modello tabella recensioni.
   */
  private DefaultTableModel modelRecensioni;
  /**
   * Modello tabella annunci.
   */
  private DefaultTableModel modelAnnunci;
  /**
   * Modello tabella proposte ricevute.
   */
  private DefaultTableModel modelProposteRicevute;
  /**
   * Modello tabella proposte inviate.
   */
  private DefaultTableModel modelProposteInviate;

  /**
   * Crea vista profilo finestra.
   */
  public Profilo() {
    super("Profilo Utente");
    setContentPane(mainPanel);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setMinimumSize(new Dimension(600, 550));

    setupTables();
    setupSelectionListeners();
    disabilitaPulsantiProposte();

    pack();
    centraFinestra();
  }

  /**
   * Inizializza i modelli delle tabelle e le impostazioni.
   */
  private void setupTables() {
    modelRecensioni = new DefaultTableModel(new Object[]{"Nome utente", "Voto", "Descrizione"}, 0) {
      /**
       * {@inheritDoc}
       */
      @Override
      public boolean isCellEditable(int row, int col) {
        return false;
      }
    };
    tableRecensioni.setModel(modelRecensioni);
    tableRecensioni.getColumnModel().getColumn(1).setMaxWidth(80);
    tableRecensioni.setRowHeight(25);
    tableRecensioni.getTableHeader().setResizingAllowed(false);
    tableRecensioni.getTableHeader().setReorderingAllowed(false);

    modelAnnunci = new DefaultTableModel(new Object[]{"Titolo", "Categoria", "Tipo"}, 0) {
      /**
       * {@inheritDoc}
       */
      @Override
      public boolean isCellEditable(int row, int col) {
        return false;
      }
    };
    tableAnnunci.setModel(modelAnnunci);
    tableAnnunci.setRowHeight(25);
    tableAnnunci.getTableHeader().setResizingAllowed(false);
    tableAnnunci.getTableHeader().setReorderingAllowed(false);

    modelProposteRicevute = new DefaultTableModel(
            new Object[]{"Da", "Annuncio", "Tipo", "Dettaglio", "Stato"}, 0) {
      /**
       * {@inheritDoc}
       */
      @Override
      public boolean isCellEditable(int row, int col) {
        return false;
      }
    };
    tableProposteRicevute.setModel(modelProposteRicevute);
    tableProposteRicevute.setRowHeight(25);
    tableProposteRicevute.getTableHeader().setResizingAllowed(false);
    tableProposteRicevute.getTableHeader().setReorderingAllowed(false);
    applyStatoRenderer(tableProposteRicevute, 4);

    modelProposteInviate = new DefaultTableModel(
            new Object[]{"A", "Annuncio", "Tipo", "Dettaglio", "Stato"}, 0) {
      /**
       * {@inheritDoc}
       */
      @Override
      public boolean isCellEditable(int row, int col) {
        return false;
      }
    };
    tableProposteInviate.setModel(modelProposteInviate);
    tableProposteInviate.setRowHeight(25);
    tableProposteInviate.getTableHeader().setResizingAllowed(false);
    tableProposteInviate.getTableHeader().setReorderingAllowed(false);
    applyStatoRenderer(tableProposteInviate, 4);
  }

  /**
   * Configura listener per selezione righe tabelle.
   */
  private void setupSelectionListeners() {
    if (tableProposteRicevute != null) {
      tableProposteRicevute.getSelectionModel().addListSelectionListener(e -> {
        if (!e.getValueIsAdjusting()) {
          aggiornaPulsantiProposteRicevute();
        }
      });
    }

    if (tableProposteInviate != null) {
      tableProposteInviate.getSelectionModel().addListSelectionListener(e -> {
        if (!e.getValueIsAdjusting()) {
          aggiornaPulsantiProposteInviate();
        }
      });
    }
  }

  /**
   * Disabilita tutti i pulsanti delle proposte all'avvio.
   */
  private void disabilitaPulsantiProposte() {
    if (btnRecensioneRicevuta != null) btnRecensioneRicevuta.setEnabled(false);
    if (btnRecensioneInviata != null) btnRecensioneInviata.setEnabled(false);
    if (btnModificaProposta != null) btnModificaProposta.setEnabled(false);
    if (btnAnnullaProposta != null) btnAnnullaProposta.setEnabled(false);
  }

  /**
   * Aggiorna stato pulsanti proposte ricevute in base a selezione.
   */
  private void aggiornaPulsantiProposteRicevute() {
    int selectedRow = getSelectedPropostaRicevutaRow();
    boolean hasSelection = selectedRow >= 0;
    boolean isConcluso = false;

    if (hasSelection && selectedRow < modelProposteRicevute.getRowCount()) {
      Object statoObj = modelProposteRicevute.getValueAt(selectedRow, 4);
      if (statoObj != null) {
        String stato = statoObj.toString().trim().toLowerCase();
        isConcluso = stato.startsWith("concluso");
      }
    }

    if (btnRecensioneRicevuta != null) {
      btnRecensioneRicevuta.setEnabled(hasSelection && isConcluso);
    }
  }

  /**
   * Aggiorna stato pulsanti proposte inviate in base a selezione.
   */
  private void aggiornaPulsantiProposteInviate() {
    int selectedRow = getSelectedPropostaInviataRow();
    boolean hasSelection = selectedRow >= 0;
    boolean isConcluso = false;
    boolean isInAttesa = false;

    if (hasSelection && selectedRow < modelProposteInviate.getRowCount()) {
      Object statoObj = modelProposteInviate.getValueAt(selectedRow, 4);
      if (statoObj != null) {
        String stato = statoObj.toString().trim().toLowerCase();
        isConcluso = stato.startsWith("concluso");
        isInAttesa = stato.startsWith("in attesa");
      }
    }

    if (btnRecensioneInviata != null) {
      btnRecensioneInviata.setEnabled(hasSelection && isConcluso);
    }
    if (btnModificaProposta != null) {
      btnModificaProposta.setEnabled(hasSelection && isInAttesa);
    }
    if (btnAnnullaProposta != null) {
      btnAnnullaProposta.setEnabled(hasSelection && isInAttesa);
    }
  }

  /**
   * Applica il renderer per lo stato alle celle della colonna indicata.
   *
   * @param table tabella target
   * @param colIndex indice della colonna
   */
  private void applyStatoRenderer(JTable table, int colIndex) {
    if (table == null) {
      return;
    }
    DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
      /**
       * {@inheritDoc}
       */
      @Override
      public Component getTableCellRendererComponent(
              JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component comp = super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);
        if (!isSelected) {
          String testo = value != null ? value.toString().trim().toLowerCase() : "";
          if (testo.startsWith("accettata") || testo.startsWith("da spedire") || testo.startsWith("da ritirare")) {
            comp.setForeground(new Color(0, 140, 0));
          } else if (testo.startsWith("rifiutato")) {
            comp.setForeground(new Color(180, 0, 0));
          } else if (testo.startsWith("in attesa")) {
            comp.setForeground(new Color(200, 140, 0));
          } else if (testo.startsWith("concluso")) {
            comp.setForeground(new Color(0, 100, 200));
          } else {
            comp.setForeground(table.getForeground());
          }
        }
        comp.setFont(comp.getFont().deriveFont(Font.BOLD));
        return comp;
      }
    };
    table.getColumnModel().getColumn(colIndex).setCellRenderer(renderer);
  }

  /**
   * Aggiunge un mouse listener alla tabella degli annunci.
   *
   * @param listener mouse listener
   */
  public void addTableAnnunciListener(MouseListener listener) {
    tableAnnunci.addMouseListener(listener);
  }

  /**
   * Aggiunge un mouse listener alla tabella delle proposte ricevute.
   *
   * @param listener mouse listener
   */
  public void addTableProposteRicevuteListener(MouseListener listener) {
    tableProposteRicevute.addMouseListener(listener);
  }

  /**
   * Aggiunge un mouse listener alla tabella delle proposte inviate.
   *
   * @param listener mouse listener
   */
  public void addTableProposteInviateListener(MouseListener listener) {
    tableProposteInviate.addMouseListener(listener);
  }

  /**
   * Aggiunge un listener al pulsante di recensione per le proposte ricevute.
   *
   * @param listener listener dell'azione
   */
  public void addRecensioneRicevutaListener(ActionListener listener) {
    if (btnRecensioneRicevuta != null) {
      btnRecensioneRicevuta.addActionListener(listener);
    }
  }

  /**
   * Aggiunge un listener al pulsante di recensione per le proposte inviate.
   *
   * @param listener listener dell'azione
   */
  public void addRecensioneInviataListener(ActionListener listener) {
    if (btnRecensioneInviata != null) {
      btnRecensioneInviata.addActionListener(listener);
    }
  }

  /**
   * Aggiunge un listener al pulsante di modifica per le proposte inviate.
   *
   * @param listener listener dell'azione
   */
  public void addModificaPropostaListener(ActionListener listener) {
    if (btnModificaProposta != null) {
      btnModificaProposta.addActionListener(listener);
    }
  }

  /**
   * Aggiunge un listener al pulsante di annullamento per le proposte inviate.
   *
   * @param listener listener dell'azione
   */
  public void addAnnullaPropostaListener(ActionListener listener) {
    if (btnAnnullaProposta != null) {
      btnAnnullaProposta.addActionListener(listener);
    }
  }

  /**
   * Aggiunge un listener al pulsante di generazione del report.
   *
   * @param listener listener dell'azione
   */
  public void addGeneraReportListener(ActionListener listener) {
    if (btnGeneraReport != null) {
      btnGeneraReport.addActionListener(listener);
    }
  }

  /**
   * Restituisce l'indice della riga selezionata nelle proposte ricevute.
   *
   * @return indice della riga o -1
   */
  public int getSelectedPropostaRicevutaRow() {
    return tableProposteRicevute != null ? tableProposteRicevute.getSelectedRow() : -1;
  }

  /**
   * Restituisce l'indice della riga selezionata nelle proposte inviate.
   *
   * @return indice della riga o -1
   */
  public int getSelectedPropostaInviataRow() {
    return tableProposteInviate != null ? tableProposteInviate.getSelectedRow() : -1;
  }

  /**
   * Aggiunge una riga di recensione alla tabella recensioni.
   *
   * @param nomeUtente nome del recensore
   * @param voto valore della valutazione
   * @param descrizione testo della recensione
   */
  public void aggiungiRecensione(String nomeUtente, int voto, String descrizione) {
    String stelle = "★".repeat(voto);
    modelRecensioni.addRow(new Object[]{nomeUtente, stelle, descrizione});
  }

  /**
   * Cancella tutte le righe delle tabelle.
   */
  public void pulisciTabelle() {
    modelRecensioni.setRowCount(0);
    modelAnnunci.setRowCount(0);
    modelProposteRicevute.setRowCount(0);
    modelProposteInviate.setRowCount(0);
    disabilitaPulsantiProposte();
  }

  /**
   * Aggiunge una riga di annuncio alla tabella annunci.
   *
   * @param titolo titolo dell'annuncio
   * @param categoria categoria dell'annuncio
   * @param tipo tipo di annuncio
   */
  public void aggiungiAnnuncio(String titolo, String categoria, String tipo) {
    modelAnnunci.addRow(new Object[]{titolo, categoria, tipo});
  }

  /**
   * Aggiunge una riga di proposta ricevuta alla tabella.
   *
   * @param utente utente proponente
   * @param annuncio titolo dell'annuncio
   * @param tipo tipo di annuncio
   * @param dettaglio testo del dettaglio
   * @param stato testo dello stato
   */
  public void aggiungiPropostaRicevuta(String utente, String annuncio, String tipo, String dettaglio, String stato) {
    modelProposteRicevute.addRow(new Object[]{utente, annuncio, tipo, dettaglio, stato});
  }

  /**
   * Aggiunge una riga di proposta inviata alla tabella.
   *
   * @param utente utente destinatario
   * @param annuncio titolo dell'annuncio
   * @param tipo tipo di annuncio
   * @param dettaglio testo del dettaglio
   * @param stato testo dello stato
   */
  public void aggiungiPropostaInviata(String utente, String annuncio, String tipo, String dettaglio, String stato) {
    modelProposteInviate.addRow(new Object[]{utente, annuncio, tipo, dettaglio, stato});
  }

  /**
   * Imposta il titolo del profilo nella vista e nella finestra.
   *
   * @param titolo titolo del profilo
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
   * Imposta l'etichetta dell'username.
   *
   * @param username username
   */
  public void setUsername(String username) { lblUsername.setText(username); }

  /**
   * Imposta l'etichetta dell'email.
   *
   * @param email indirizzo email
   */
  public void setEmail(String email) { lblEmail.setText(email); }

  /**
   * Imposta l'etichetta del telefono.
   *
   * @param telefono numero di telefono
   */
  public void setTelefono(String telefono) { lblTelefono.setText(telefono); }

  /**
   * Imposta l'etichetta della valutazione media e il colore.
   *
   * @param media valutazione media
   */
  public void setMediaVoto(double media) {
    int filled = (int) Math.round(media);
    if (filled < 0) {
      filled = 0;
    } else if (filled > 5) {
      filled = 5;
    }
    StringBuilder stelle = new StringBuilder(5);
    for (int i = 0; i < 5; i++) {
      stelle.append(i < filled ? "★" : "☆");
    }
    lblMediaVoto.setText(stelle.toString());
    lblMediaVoto.setToolTipText(String.format("%.1f / 5", media));
    if (media >= 4) lblMediaVoto.setForeground(new Color(0, 150, 0));
    else if (media >= 2.5) lblMediaVoto.setForeground(new Color(200, 150, 0));
    else lblMediaVoto.setForeground(Color.RED);
  }

  /**
   * Rimuove il tab se presente con il titolo indicato.
   *
   * @param titolo titolo del tab
   */
  private void rimuoviTabSePresente(String titolo) {
    for (int i = tabbedPane.getTabCount() - 1; i >= 0; i--) {
      if (titolo.equals(tabbedPane.getTitleAt(i))) {
        tabbedPane.removeTabAt(i);
      }
    }
  }
}
