package gui;

import model.Annuncio;
import model.Vendita;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

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
  private JList categoryList;
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
   * Pulsante vista 1.
   */
  private JButton btnView1;
  /**
   * Pulsante vista 2.
   */
  private JButton btnView2;

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
   */
  public static final class AnnuncioEvidenza {
    /**
     * Annuncio associato.
     */
    private final Annuncio annuncio;
    /**
     * Immagine associata.
     */
    private final byte[] immagine;

    /**
     * Crea contenitore annuncio con immagine opzionale.
     *
     * @param annuncio annuncio in evidenza
     * @param immagine bytes immagine
     */
    public AnnuncioEvidenza(Annuncio annuncio, byte[] immagine) {
      this.annuncio = annuncio;
      this.immagine = immagine;
    }

    /**
     * Restituisce annuncio collegato.
     *
     * @return annuncio
     */
    public Annuncio getAnnuncio() {
      return annuncio;
    }

    /**
     * Restituisce immagine associata.
     *
     * @return bytes immagine
     */
    public byte[] getImmagine() {
      return immagine;
    }
  }
  /**
   * Crea app principale UI contenitore.
   */
  public MainApp() {
    super("UninaSwap - Bacheca");
    initLayout();
    initActionCommands();
    initFiltri();

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setMinimumSize(new Dimension(900, 650));

    pack();
    centraFinestra();
  }

  /**
   * Inizializza navigazione barra e avvolge principale pannello.
   */
  private void initLayout() {
    JPanel root = new JPanel(new BorderLayout());
    root.add(mainPanel, BorderLayout.CENTER);

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

    btnLogout = new JButton("Logout");
    btnLogout.setToolTipText("Logout");
    btnLogout.getAccessibleContext().setAccessibleName("Logout");
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
   * Imposta azione comandi per controller instradamento.
   */
  private void initActionCommands() {
    if (btnProfilo != null) btnProfilo.setActionCommand(ACTION_PROFILO);
    if (btnLogout != null) btnLogout.setActionCommand(ACTION_LOGOUT);
    if (btnPubblica != null) btnPubblica.setActionCommand(ACTION_PUBBLICA);
    if (searchButton != null) searchButton.setActionCommand(ACTION_RICERCA);
    if (resetButton != null) resetButton.setActionCommand(ACTION_RESET);
    if (searchField != null) searchField.setActionCommand(ACTION_RICERCA);
  }

  /**
   * Inizializza filtro widget stato.
   */
  private void initFiltri() {
    if (categoryList != null) {
      categoryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      if (categoryList.getSelectedIndex() < 0 && categoryList.getModel().getSize() > 0) {
        categoryList.setSelectedIndex(0);
      }
    }
  }

  /**
   * Abilita o disabilita navigazione controlli.
   *
   * @param abilitata navigazione abilitato flag
   */
  public void setNavigazioneAbilitata(boolean abilitata) {
    if (btnPubblica != null) btnPubblica.setEnabled(abilitata);
    if (btnProfilo != null) btnProfilo.setEnabled(abilitata);
    if (btnLogout != null) btnLogout.setEnabled(abilitata);
    if (searchField != null) searchField.setEnabled(abilitata);
    if (searchButton != null) searchButton.setEnabled(abilitata);
    if (resetButton != null) resetButton.setEnabled(abilitata);
    if (categoryList != null) categoryList.setEnabled(abilitata);
    if (radioTutti != null) radioTutti.setEnabled(abilitata);
    if (radioScambio != null) radioScambio.setEnabled(abilitata);
    if (radioVendita != null) radioVendita.setEnabled(abilitata);
    if (radioRegalo != null) radioRegalo.setEnabled(abilitata);
    if (txtPrezzoMax != null) txtPrezzoMax.setEnabled(abilitata);
  }

  /**
   * Aggiorna finestra titolo con utente corrente.
   *
   * @param username corrente username
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
   * Restituisce corrente testo ricerca.
   *
   * @return testo ricerca
   */
  public String getTestoRicerca() {
    return searchField != null ? searchField.getText() : "";
  }

  /**
   * Restituisce categoria selezionata.
   *
   * @return categoria nome
   */
  public String getCategoriaSelezionata() {
    if (categoryList == null) {
      return "Tutte";
    }
    Object valore = categoryList.getSelectedValue();
    return valore != null ? valore.toString() : "Tutte";
  }

  /**
   * Restituisce etichetta tipo annuncio selezionato.
   *
   * @return testo tipo selezionato
   */
  public String getTipoSelezionato() {
    if (radioScambio != null && radioScambio.isSelected()) return "Scambio";
    if (radioVendita != null && radioVendita.isSelected()) return "Vendita";
    if (radioRegalo != null && radioRegalo.isSelected()) return "Regalo";
    return "Tutti";
  }

  /**
   * Restituisce massimo prezzo filtro.
   *
   * @return massimo prezzo
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
   * Aggiunge profilo navigazione listener.
   *
   * @param listener azione listener
   */
  public void addProfiloListener(ActionListener listener) {
    if (btnProfilo != null) btnProfilo.addActionListener(listener);
  }

  /**
   * Aggiunge logout listener.
   *
   * @param listener azione listener
   */
  public void addLogoutListener(ActionListener listener) {
    if (btnLogout != null) btnLogout.addActionListener(listener);
  }

  /**
   * Aggiunge pubblica annuncio listener.
   *
   * @param listener azione listener
   */
  public void addPubblicaListener(ActionListener listener) {
    if (btnPubblica != null) btnPubblica.addActionListener(listener);
  }

  /**
   * Aggiunge ricerca azione listener.
   *
   * @param listener azione listener
   */
  public void addSearchListener(ActionListener listener) {
    if (searchButton != null) searchButton.addActionListener(listener);
    if (searchField != null) searchField.addActionListener(listener);
  }

  /**
   * Aggiunge listener per reset filtri.
   *
   * @param listener azione listener
   */
  public void addResetListener(ActionListener listener) {
    if (resetButton != null) resetButton.addActionListener(listener);
  }

  /**
   * Renderizza annunci in evidenza sezione.
   *
   * @param annunci annunci in evidenza con immagini
   * @param listener azione listener per dettaglio pulsanti
   */
  public void mostraAnnunciInEvidenza(List<AnnuncioEvidenza> annunci, ActionListener listener) {
    mostraAnnunci(annunci, listener, "Nessun annuncio in evidenza con foto disponibile.");
  }

  /**
   * Renderizza risultati ricerca.
   *
   * @param annunci annunci risultati con immagini
   * @param listener azione listener per dettaglio pulsanti
   */
  public void mostraRisultatiRicerca(List<AnnuncioEvidenza> annunci, ActionListener listener) {
    mostraAnnunci(annunci, listener, "Nessun annuncio trovato con i filtri selezionati.");
  }

  /**
   * Renderizza lista di annunci in pannello cards.
   *
   * @param annunci annunci da mostrare
   * @param listener listener per bottone dettaglio
   * @param emptyMessage messaggio quando vuoto
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
      cardsPanel.setLayout(new GridLayout(0, 3, 10, 10));
      for (AnnuncioEvidenza annuncioEvidenza : annunci) {
        cardsPanel.add(creaCardEvidenza(annuncioEvidenza, listener));
      }
    }

    cardsPanel.revalidate();
    cardsPanel.repaint();
  }

  /**
   * Costruisce singolo annuncio in evidenza scheda.
   *
   * @param annuncioEvidenza annuncio in evidenza data
   * @param listener azione listener per dettagli
   * @return scheda pannello
   */
  private JPanel creaCardEvidenza(AnnuncioEvidenza annuncioEvidenza, ActionListener listener) {
    Annuncio annuncio = annuncioEvidenza.getAnnuncio();
    JPanel card = new JPanel(new BorderLayout(6, 6));
    card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210, 210, 210)),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
    ));

    JLabel lblImmagine = new JLabel();
    lblImmagine.setHorizontalAlignment(SwingConstants.CENTER);
    lblImmagine.setPreferredSize(new Dimension(180, 120));

    ImageIcon icon = creaIcona(annuncioEvidenza.getImmagine(), 180, 120);
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
   * Formatta metadati linea per annuncio scheda.
   *
   * @param annuncio annuncio
   * @return metadati linea
   */
  private String formatMeta(Annuncio annuncio) {
    String categoria = annuncio.getCategoria() != null ? annuncio.getCategoria().toString() : "N/A";
    String tipo = annuncio.getTipoAnnuncio() != null ? annuncio.getTipoAnnuncio().toString() : "N/A";
    String consegna = annuncio.getConsegnaLabel();
    String extra = "";
    if (annuncio instanceof Vendita) {
      extra = " - EUR " + String.format("%.2f", ((Vendita) annuncio).getPrezzo());
    }
    return categoria + " | " + tipo + " | Consegna: " + consegna + extra;
  }

  /**
   * Crea icona da byte immagine.
   *
   * @param bytes byte immagine
   * @param width target larghezza
   * @param height target altezza
   * @return ridimensionata icona o null
   */
  private ImageIcon creaIcona(byte[] bytes, int width, int height) {
    if (bytes == null || bytes.length == 0) {
      return null;
    }
    ImageIcon icon = new ImageIcon(bytes);
    Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
    return new ImageIcon(img);
  }

  /**
   * Applica piu forte visivo stile a pubblica pulsante.
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
