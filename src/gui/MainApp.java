package gui;

import model.Annuncio;
import model.Vendita;
import model.enums.Categoria;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;

/**
 * Finestra principale della bacheca annunci.
 */
public class MainApp extends BaseFrame {
  /**
   * Action command per aprire profilo.
   */
  public static final String ACTION_PROFILO = "main.profilo";
  /**
   * Action command per logout.
   */
  public static final String ACTION_LOGOUT = "main.logout";
  /**
   * Testo pulsante logout.
   */
  private static final String LOGOUT_TEXT = "Logout";
  /**
   * Action command per pubblica annuncio.
   */
  public static final String ACTION_PUBBLICA = "main.pubblica";
  /**
   * Action command per ricerca.
   */
  public static final String ACTION_RICERCA = "main.ricerca";
  /**
   * Action command per reset filtri.
   */
  public static final String ACTION_RESET = "main.reset";
  /**
   * Action command per aggiorna annunci.
   */
  public static final String ACTION_AGGIORNA = "main.aggiorna";
  /**
   * Action command per dettaglio annuncio.
   */
  public static final String ACTION_DETTAGLIO = "main.dettaglio";
  /**
   * Chiave client property per annuncio.
   */
  public static final String KEY_ANNUNCIO = "main.annuncio";

  /**
   * Colore principale pulsanti.
   */
  private static final Color MAIN_COLOR = new Color(0, 102, 204);
  /**
   * Colore pulsante logout.
   */
  private static final Color LOGOUT_COLOR = new Color(170, 60, 60);

  /**
   * Pannello principale.
   */
  private JPanel mainPanel;

  /**
   * Pulsante pubblica annuncio.
   */
  private JButton btnPubblica;

  /**
   * Lista categorie.
   */
  private JList<String> categoryList;
  /**
   * Radio tutti.
   */
  private JRadioButton radioTutti;
  /**
   * Radio scambio.
   */
  private JRadioButton radioScambio;
  /**
   * Radio vendita.
   */
  private JRadioButton radioVendita;
  /**
   * Radio regalo.
   */
  private JRadioButton radioRegalo;
  /**
   * Campo prezzo massimo.
   */
  private JTextField txtPrezzoMax;

  /**
   * Campo testo ricerca.
   */
  private JTextField searchField;
  /**
   * Pulsante ricerca.
   */
  private JButton searchButton;
  /**
   * Pulsante reset filtri.
   */
  private JButton resetButton;
  /**
   * Pulsante aggiorna annunci.
   */
  private JButton btnAggiorna;

  /**
   * Pulsante profilo.
   */
  private JButton btnProfilo;
  /**
   * Pulsante logout.
   */
  private JButton btnLogout;
  /**
   * Pannello cards annunci.
   */
  private JPanel cardsPanel;

  /**
   * Contenitore annuncio con immagine in evidenza.
   *
   * @param annuncio annuncio associato
   * @param immagine immagine associata
   */
  public record AnnuncioEvidenza(Annuncio annuncio, byte[] immagine) {
    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      AnnuncioEvidenza that = (AnnuncioEvidenza) o;
      return java.util.Objects.equals(annuncio, that.annuncio) &&
             java.util.Arrays.equals(immagine, that.immagine);
    }

    @Override
    public int hashCode() {
      return java.util.Objects.hash(annuncio, java.util.Arrays.hashCode(immagine));
    }

    @Override
    public String toString() {
      return "AnnuncioEvidenza{" +
             "annuncio=" + annuncio +
             ", immagine=" + (immagine != null ? immagine.length + " bytes" : "null") +
             '}';
    }
  }
  /**
   * Crea il contenitore dell'interfaccia dell'applicazione principale.
   */
  public MainApp() {
    super("UninaSwap - Bacheca");
    initLayout();
    initActionCommands();
    initFiltri();

    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setMinimumSize(new Dimension(900, 650));

    pack();
    centraFinestra();
  }

  /**
   * Pannello scrollabile che traccia la larghezza del viewport.
   */
  private static class ScrollablePanel extends JPanel implements Scrollable {

    public ScrollablePanel(LayoutManager layout) {
      super(layout);
    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
      return getPreferredSize();
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
      return 16;
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
      return 160;
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
      return true;
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
      return false;
    }
  }

  /**
   * Inizializza la barra di navigazione e avvolge il pannello principale.
   */
  private void initLayout() {
    JPanel root = new JPanel(new BorderLayout());
    root.add(mainPanel, BorderLayout.CENTER);

    // Configura accessibilità per pulsanti generati dal form
    if (btnAggiorna != null) {
      btnAggiorna.getAccessibleContext().setAccessibleName("Aggiorna annunci");
    }

    JPanel topBar = new JPanel(new BorderLayout());
    topBar.setBorder(new EmptyBorder(4, 8, 4, 8));

    btnProfilo = new JButton("Profilo");
    btnProfilo.setToolTipText("Il mio profilo");
    btnProfilo.setIcon(new ProfiloIcon(16, 16, Color.WHITE));
    btnProfilo.setIconTextGap(6);
    btnProfilo.getAccessibleContext().setAccessibleName("Il mio profilo");
    btnProfilo.setBackground(MAIN_COLOR);
    btnProfilo.setForeground(Color.WHITE);
    btnProfilo.setOpaque(true);
    btnProfilo.setContentAreaFilled(true);
    btnProfilo.setFocusPainted(false);
    btnProfilo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(MAIN_COLOR.darker()),
            BorderFactory.createEmptyBorder(4, 10, 4, 10)
    ));

    btnLogout = new JButton(LOGOUT_TEXT);
    btnLogout.setToolTipText(LOGOUT_TEXT);
    btnLogout.getAccessibleContext().setAccessibleName(LOGOUT_TEXT);
    btnLogout.setBackground(LOGOUT_COLOR);
    btnLogout.setForeground(Color.WHITE);
    btnLogout.setOpaque(true);
    btnLogout.setContentAreaFilled(true);
    btnLogout.setFocusPainted(false);
    btnLogout.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LOGOUT_COLOR.darker()),
            BorderFactory.createEmptyBorder(4, 10, 4, 10)
    ));

    JPanel topActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
    topActions.setOpaque(false);
    topActions.add(btnProfilo);
    topActions.add(btnLogout);

    topBar.add(topActions, BorderLayout.EAST);
    root.add(topBar, BorderLayout.NORTH);

    JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 8));
    bottomBar.setBorder(new EmptyBorder(8, 8, 8, 8));
    if (btnPubblica == null) {
      btnPubblica = new JButton("+ Pubblica Annuncio");
    } else {
      Container parent = btnPubblica.getParent();
      if (parent != null) {
        parent.remove(btnPubblica);
        parent.revalidate();
        parent.repaint();
      }
    }
    bottomBar.add(btnPubblica);
    root.add(bottomBar, BorderLayout.SOUTH);

    setContentPane(root);

    stilePubblicaAnnuncio();
  }

  /**
   * Imposta i comandi delle azioni per l'instradamento del controller.
   */
  private void initActionCommands() {
    if (btnProfilo != null) btnProfilo.setActionCommand(ACTION_PROFILO);
    if (btnLogout != null) btnLogout.setActionCommand(ACTION_LOGOUT);
    if (btnPubblica != null) btnPubblica.setActionCommand(ACTION_PUBBLICA);
    if (searchButton != null) searchButton.setActionCommand(ACTION_RICERCA);
    if (resetButton != null) resetButton.setActionCommand(ACTION_RESET);
    if (btnAggiorna != null) btnAggiorna.setActionCommand(ACTION_AGGIORNA);
    if (searchField != null) searchField.setActionCommand(ACTION_RICERCA);
  }

  /**
   * Inizializza lo stato dei widget di filtro.
   */
  private void initFiltri() {
    if (categoryList != null) {
      // Popola lista categorie dinamicamente dall'enum
      DefaultListModel<String> model = new DefaultListModel<>();
      model.addElement(utils.Constanti.CATEGORIA_TUTTE);
      for (Categoria cat : Categoria.values()) {
        model.addElement(cat.toString());
      }
      categoryList.setModel(model);

      categoryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      if (categoryList.getSelectedIndex() < 0 && categoryList.getModel().getSize() > 0) {
        categoryList.setSelectedIndex(0);
      }
    }
  }

  /**
   * Abilita o disabilita i controlli di navigazione.
   *
   * @param abilitata flag per abilitare la navigazione
   */
  public void setNavigazioneAbilitata(boolean abilitata) {
    if (btnPubblica != null) btnPubblica.setEnabled(abilitata);
    if (btnProfilo != null) btnProfilo.setEnabled(abilitata);
    if (btnLogout != null) btnLogout.setEnabled(abilitata);
    if (searchField != null) searchField.setEnabled(abilitata);
    if (searchButton != null) searchButton.setEnabled(abilitata);
    if (resetButton != null) resetButton.setEnabled(abilitata);
    if (btnAggiorna != null) btnAggiorna.setEnabled(abilitata);
    if (categoryList != null) categoryList.setEnabled(abilitata);
    if (radioTutti != null) radioTutti.setEnabled(abilitata);
    if (radioScambio != null) radioScambio.setEnabled(abilitata);
    if (radioVendita != null) radioVendita.setEnabled(abilitata);
    if (radioRegalo != null) radioRegalo.setEnabled(abilitata);
    if (txtPrezzoMax != null) txtPrezzoMax.setEnabled(abilitata);
  }

  /**
   * Aggiorna il titolo della finestra con l'utente corrente.
   *
   * @param username username corrente
   */
  public void setTitoloUtente(String username) {
    if (username == null || username.trim().isEmpty()) {
      setTitle("UninaSwap - Bacheca");
    } else {
      setTitle("UninaSwap - " + username);
    }
  }

  /**
   * Mostra finestra principale.
   */
  public void mostra() {
    setVisible(true);
  }

  /**
   * Restituisce il testo di ricerca corrente.
   *
   * @return testo di ricerca
   */
  public String getTestoRicerca() {
    return searchField != null ? searchField.getText() : "";
  }

  /**
   * Restituisce la categoria selezionata.
   *
   * @return nome della categoria
   */
  public String getCategoriaSelezionata() {
    if (categoryList == null) {
      return "Tutte";
    }
    Object valore = categoryList.getSelectedValue();
    return valore != null ? valore.toString() : "Tutte";
  }

  /**
   * Restituisce l'etichetta del tipo di annuncio selezionato.
   *
   * @return testo del tipo selezionato
   */
  public String getTipoSelezionato() {
    if (radioScambio != null && radioScambio.isSelected()) return "Scambio";
    if (radioVendita != null && radioVendita.isSelected()) return "Vendita";
    if (radioRegalo != null && radioRegalo.isSelected()) return "Regalo";
    return "Tutti";
  }

  /**
   * Restituisce il filtro del prezzo massimo.
   *
   * @return prezzo massimo
   */
  public String getPrezzoMax() {
    return txtPrezzoMax != null ? txtPrezzoMax.getText() : "";
  }

  /**
   * Ripristina filtri al valore di default.
   */
  public void resetFiltri() {
    if (searchField != null) searchField.setText("");
    if (txtPrezzoMax != null) txtPrezzoMax.setText("");
    if (categoryList != null) {
      if (categoryList.getModel().getSize() > 0) {
        categoryList.setSelectedIndex(0);
      } else {
        categoryList.clearSelection();
      }
    }
    if (radioTutti != null) {
      radioTutti.setSelected(true);
    }
  }

  /**
   * Aggiunge un listener per la navigazione al profilo.
   *
   * @param listener listener dell'azione
   */
  public void addProfiloListener(ActionListener listener) {
    if (btnProfilo != null) btnProfilo.addActionListener(listener);
  }

  /**
   * Aggiunge logout listener.
   *
   * @param listener listener dell'azione
   */
  public void addLogoutListener(ActionListener listener) {
    if (btnLogout != null) btnLogout.addActionListener(listener);
  }

  /**
   * Aggiunge un listener per la pubblicazione di annunci.
   *
   * @param listener listener dell'azione
   */
  public void addPubblicaListener(ActionListener listener) {
    if (btnPubblica != null) btnPubblica.addActionListener(listener);
  }

  /**
   * Aggiunge un listener per l'azione di ricerca.
   *
   * @param listener listener dell'azione
   */
  public void addSearchListener(ActionListener listener) {
    if (searchButton != null) searchButton.addActionListener(listener);
    if (searchField != null) searchField.addActionListener(listener);
  }

  /**
   * Aggiunge listener per reset filtri.
   *
   * @param listener listener dell'azione
   */
  public void addResetListener(ActionListener listener) {
    if (resetButton != null) resetButton.addActionListener(listener);
  }

  /**
   * Aggiunge un listener per l'aggiornamento degli annunci.
   *
   * @param listener listener dell'azione
   */
  public void addAggiornaListener(ActionListener listener) {
    if (btnAggiorna != null) btnAggiorna.addActionListener(listener);
  }

  /**
   * Renderizza la sezione degli annunci in evidenza.
   *
   * @param annunci annunci in evidenza con immagini
   * @param listener listener dell'azione per i pulsanti di dettaglio
   */
  public void mostraAnnunciInEvidenza(List<AnnuncioEvidenza> annunci, ActionListener listener) {
    mostraAnnunci(annunci, listener, "Nessun annuncio in evidenza con foto disponibile.");
  }

  /**
   * Renderizza i risultati della ricerca.
   *
   * @param annunci annunci risultanti con immagini
   * @param listener listener dell'azione per i pulsanti di dettaglio
   */
  public void mostraRisultatiRicerca(List<AnnuncioEvidenza> annunci, ActionListener listener) {
    mostraAnnunci(annunci, listener, "Nessun annuncio trovato con i filtri selezionati.");
  }

  /**
   * Renderizza la lista di annunci nel pannello delle card.
   *
   * @param annunci annunci da mostrare
   * @param listener listener per il bottone di dettaglio
   * @param emptyMessage messaggio da mostrare quando vuoto
   */
  private void mostraAnnunci(List<AnnuncioEvidenza> annunci, ActionListener listener, String emptyMessage) {
    if (cardsPanel == null) {
      return;
    }

    cardsPanel.removeAll();

    if (annunci == null || annunci.isEmpty()) {
      cardsPanel.setLayout(new BorderLayout());
      JLabel empty = new JLabel("<html><div style='text-align:center;'>" + emptyMessage + "</div></html>");
      empty.setHorizontalAlignment(SwingConstants.CENTER);
      cardsPanel.add(empty, BorderLayout.CENTER);
    } else {
      ScrollablePanel contentPanel = new ScrollablePanel(new GridLayout(0, 2, 10, 10));

      for (AnnuncioEvidenza annuncioEvidenza : annunci) {
        contentPanel.add(creaCardEvidenza(annuncioEvidenza, listener));
      }

      JScrollPane scrollPane = new JScrollPane(contentPanel);
      scrollPane.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_AS_NEEDED);
      scrollPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);

      cardsPanel.setLayout(new BorderLayout());
      cardsPanel.add(scrollPane, BorderLayout.CENTER);
    }

    cardsPanel.revalidate();
    cardsPanel.repaint();
  }

  /**
   * Costruisce una singola card per l'annuncio in evidenza.
   *
   * @param annuncioEvidenza dati dell'annuncio in evidenza
   * @param listener listener dell'azione per i dettagli
   * @return pannello della card
   */
  private JPanel creaCardEvidenza(AnnuncioEvidenza annuncioEvidenza, ActionListener listener) {
    Annuncio annuncio = annuncioEvidenza.annuncio();
    JPanel card = new JPanel(new BorderLayout(6, 6));
    card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210, 210, 210)),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
    ));

    JLabel lblImmagine = new JLabel();
    lblImmagine.setHorizontalAlignment(SwingConstants.CENTER);
    lblImmagine.setPreferredSize(new Dimension(180, 120));

    ImageIcon icon = creaIcona(annuncioEvidenza.immagine(), 180, 120);
    if (icon != null) {
      lblImmagine.setIcon(icon);
    } else {
      lblImmagine.setText("Immagine non disponibile");
    }

    JLabel lblTitolo = new JLabel(annuncio.getTitolo());
    lblTitolo.setFont(lblTitolo.getFont().deriveFont(Font.BOLD));

    JLabel lblMeta = new JLabel(formatMeta(annuncio));

    JPanel header = new JPanel(new GridLayout(0, 1));
    header.add(lblTitolo);
    header.add(lblMeta);

    JButton btnDettagli = new JButton("Dettagli");
    btnDettagli.setActionCommand(ACTION_DETTAGLIO);
    btnDettagli.putClientProperty(KEY_ANNUNCIO, annuncio);
    if (listener != null) {
      btnDettagli.addActionListener(listener);
    }

    JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
    footer.add(btnDettagli);

    card.add(header, BorderLayout.NORTH);
    card.add(lblImmagine, BorderLayout.CENTER);
    card.add(footer, BorderLayout.SOUTH);

    return card;
  }

  /**
   * Formatta la riga dei metadati per la card dell'annuncio.
   *
   * @param annuncio annuncio
   * @return riga dei metadati
   */
  private String formatMeta(Annuncio annuncio) {
    String categoria = annuncio.getCategoria() != null ? annuncio.getCategoria().toString() : "N/A";
    String tipo = annuncio.getTipoAnnuncio() != null ? annuncio.getTipoAnnuncio().toString() : "N/A";
    String consegna = annuncio.getConsegnaLabel();
    String extra = "";
    if (annuncio instanceof Vendita vendita) {
      extra = " - EUR " + String.format("%.2f", vendita.getPrezzo());
    }
    return categoria + " | " + tipo + " | Consegna: " + consegna + extra;
  }

  /**
   * Crea un'icona dall'array di byte dell'immagine.
   *
   * @param bytes array di byte dell'immagine
   * @param width larghezza target
   * @param height altezza target
   * @return icona ridimensionata o null
   */
  private ImageIcon creaIcona(byte[] bytes, int maxWidth, int maxHeight) {
    if (bytes == null || bytes.length == 0) {
      return null;
    }
    ImageIcon icon = new ImageIcon(bytes);
    int origW = icon.getIconWidth();
    int origH = icon.getIconHeight();

    if (origW <= 0 || origH <= 0) {
      return icon;
    }

    // Preserva aspect ratio
    double scale = Math.min((double) maxWidth / origW, (double) maxHeight / origH);
    int targetW = (int) Math.round(origW * scale);
    int targetH = (int) Math.round(origH * scale);

    Image img = icon.getImage().getScaledInstance(targetW, targetH, Image.SCALE_SMOOTH);
    return new ImageIcon(img);
  }

  /**
   * Applica uno stile visivo più forte al pulsante di pubblicazione.
   */
  private void stilePubblicaAnnuncio() {
    if (btnPubblica == null) {
      return;
    }
    btnPubblica.setBackground(MAIN_COLOR);
    btnPubblica.setForeground(Color.WHITE);
    btnPubblica.setFont(btnPubblica.getFont().deriveFont(Font.BOLD));
    btnPubblica.setFocusPainted(false);
    btnPubblica.setOpaque(true);
    btnPubblica.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
  }

  /**
   * Icona profilo disegnata a mano.
   */
  private static final class ProfiloIcon implements Icon {
    /**
     * Larghezza icona.
     */
    private final int width;
    /**
     * Altezza icona.
     */
    private final int height;
    /**
     * Colore icona.
     */
    private final Color color;

    /**
     * Crea icona profilo con dimensioni e colore.
     *
     * @param width larghezza icona
     * @param height altezza icona
     * @param color colore icona
     */
    private ProfiloIcon(int width, int height, Color color) {
      this.width = width;
      this.height = height;
      this.color = color;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
      Graphics2D g2 = (Graphics2D) g.create();
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      g2.setColor(color);

      int headSize = Math.min(width, height) / 2;
      int headX = x + (width - headSize) / 2;
      int headY = y;
      g2.fillOval(headX, headY, headSize, headSize);

      int bodyWidth = width;
      int bodyHeight = height - headSize;
      int bodyX = x;
      int bodyY = y + headSize - 1;
      g2.fillRoundRect(bodyX, bodyY, bodyWidth, bodyHeight, bodyHeight, bodyHeight);

      g2.dispose();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getIconWidth() {
      return width;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getIconHeight() {
      return height;
    }
  }
}
