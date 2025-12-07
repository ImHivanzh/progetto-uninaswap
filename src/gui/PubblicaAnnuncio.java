package gui;

import model.enums.TipoAnnuncio;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PubblicaAnnuncio extends BaseFrame {
  private JPanel mainPanel;
  private JTextField txtTitolo;
  private JComboBox<TipoAnnuncio> cmbTipo;
  private JComboBox<String> cmbCategoria; // Stringa per semplicità, o crea enum Categoria
  private JTextField txtPrezzo;
  private JTextArea txtDescrizione;
  private JButton btnCaricaImg;
  private JPanel pnlImmagini;
  private JPanel pnlButtonContainer; // Contenitore per il bottone custom

  // Bottone Custom
  private JPanel customButton;
  private JLabel customButtonLabel;

  // Dati
  private List<File> immaginiSelezionate;

  public PubblicaAnnuncio() {
    super("Pubblica Nuovo Annuncio");
    setContentPane(mainPanel);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    immaginiSelezionate = new ArrayList<>();

    initUI();

    pack();
    centraFinestra();
  }

  private void initUI() {
    for (TipoAnnuncio tipo : TipoAnnuncio.values()) {
      cmbTipo.addItem(tipo);
    }

    String[] categorie = {"Elettronica", "Libri", "Abbigliamento", "Arredamento", "Altro"};
    for (String cat : categorie) {
      cmbCategoria.addItem(cat);
    }

    // 3. Listener Tipo (Disabilita prezzo se Regalo)
    cmbTipo.addActionListener(e -> {
      if (cmbTipo.getSelectedItem() == TipoAnnuncio.REGALO) {
        txtPrezzo.setText("0.0");
        txtPrezzo.setEnabled(false);
      } else {
        txtPrezzo.setEnabled(true);
        if(txtPrezzo.getText().equals("0.0")) txtPrezzo.setText("");
      }
    });

    // 4. Listener Immagini
    btnCaricaImg.addActionListener(e -> selezionaImmagini());

    // 5. Crea Bottone Custom
    creaBottoneCustom();
  }

  private void creaBottoneCustom() {
    customButton = new JPanel();
    customButton.setBackground(new Color(34, 139, 34)); // Verde Foresta
    customButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    customButton.setLayout(new GridBagLayout()); // Per centrare il testo
    customButton.setPreferredSize(new Dimension(200, 50));

    customButtonLabel = new JLabel("PUBBLICA ORA");
    customButtonLabel.setForeground(Color.WHITE);
    customButtonLabel.setFont(new Font("SansSerif", Font.BOLD, 16));

    customButton.add(customButtonLabel);

    // Effetto Hover
    customButton.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseEntered(MouseEvent e) {
        customButton.setBackground(new Color(0, 100, 0)); // Verde scuro
      }
      @Override
      public void mouseExited(MouseEvent e) {
        customButton.setBackground(new Color(34, 139, 34)); // Verde originale
      }
    });

    pnlButtonContainer.add(customButton, BorderLayout.CENTER);
  }

  private void selezionaImmagini() {
    JFileChooser chooser = new JFileChooser();
    chooser.setMultiSelectionEnabled(true);
    chooser.setFileFilter(new FileNameExtensionFilter("Immagini", "jpg", "png", "jpeg"));

    int result = chooser.showOpenDialog(this);
    if (result == JFileChooser.APPROVE_OPTION) {
      File[] files = chooser.getSelectedFiles();
      for (File file : files) {
        immaginiSelezionate.add(file);
        aggiungiAnteprima(file);
      }
      // Ridisegna il pannello
      pnlImmagini.revalidate();
      pnlImmagini.repaint();
    }
  }

  private void aggiungiAnteprima(File file) {
    JLabel lblIcon = new JLabel(file.getName());
    lblIcon.setToolTipText(file.getAbsolutePath());
    lblIcon.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    lblIcon.setPreferredSize(new Dimension(100, 30)); // Semplice etichetta per ora
    lblIcon.setHorizontalAlignment(SwingConstants.CENTER);
    // Se volessi l'icona vera:
    // ImageIcon icon = new ImageIcon(new ImageIcon(file.getPath()).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
    // lblIcon.setIcon(icon);

    pnlImmagini.add(lblIcon);
  }

  // --- GETTERS ---
  public String getTitolo() { return txtTitolo.getText(); }
  public String getDescrizione() { return txtDescrizione.getText(); }
  public String getPrezzo() { return txtPrezzo.getText(); }
  public TipoAnnuncio getTipo() { return (TipoAnnuncio) cmbTipo.getSelectedItem(); }
  public String getCategoria() { return (String) cmbCategoria.getSelectedItem(); }
  public List<File> getImmagini() { return immaginiSelezionate; }

  // --- LISTENER CUSTOM ---
  // Non usiamo più ActionListener standard perché è un JPanel
  public void addPubblicaListener(MouseAdapter listener) {
    customButton.addMouseListener(listener);
  }
}