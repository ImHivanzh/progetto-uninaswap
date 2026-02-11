package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

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
   * Applica renderer per stato alle celle della colonna indicata.
   *
   * @param table tabella target
   * @param colIndex indice colonna
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
          if (testo.startsWith("accettata")) {
            comp.setForeground(new Color(0, 140, 0));
          } else if (testo.startsWith("rifiutato")) {
            comp.setForeground(new Color(180, 0, 0));
          } else if (testo.startsWith("in attesa")) {
            comp.setForeground(new Color(200, 140, 0));
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
   * Aggiunge listener a pulsante recensione proposte ricevute.
   *
   * @param listener azione listener
   */
  public void addRecensioneRicevutaListener(ActionListener listener) {
    if (btnRecensioneRicevuta != null) {
      btnRecensioneRicevuta.addActionListener(listener);
    }
  }

  /**
   * Aggiunge listener a pulsante recensione proposte inviate.
   *
   * @param listener azione listener
   */
  public void addRecensioneInviataListener(ActionListener listener) {
    if (btnRecensioneInviata != null) {
      btnRecensioneInviata.addActionListener(listener);
    }
  }

  /**
   * Aggiunge listener a pulsante modifica proposte inviate.
   *
   * @param listener azione listener
   */
  public void addModificaPropostaListener(ActionListener listener) {
    if (btnModificaProposta != null) {
      btnModificaProposta.addActionListener(listener);
    }
  }

  /**
   * Aggiunge listener a pulsante annulla proposte inviate.
   *
   * @param listener azione listener
   */
  public void addAnnullaPropostaListener(ActionListener listener) {
    if (btnAnnullaProposta != null) {
      btnAnnullaProposta.addActionListener(listener);
    }
  }

  /**
   * Restituisce riga selezionata proposte ricevute.
   *
   * @return indice riga o -1
   */
  public int getSelectedPropostaRicevutaRow() {
    return tableProposteRicevute != null ? tableProposteRicevute.getSelectedRow() : -1;
  }

  /**
   * Restituisce riga selezionata proposte inviate.
   *
   * @return indice riga o -1
   */
  public int getSelectedPropostaInviataRow() {
    return tableProposteInviate != null ? tableProposteInviate.getSelectedRow() : -1;
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
   * Rimuove tab se presente con titolo indicato.
   *
   * @param titolo titolo tab
   */
  private void rimuoviTabSePresente(String titolo) {
    for (int i = tabbedPane.getTabCount() - 1; i >= 0; i--) {
      if (titolo.equals(tabbedPane.getTitleAt(i))) {
        tabbedPane.removeTabAt(i);
      }
    }
  }
}
