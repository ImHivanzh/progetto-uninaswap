package gui;

import controller.PubblicaAnnuncioController;
import model.enums.Categoria;
import model.enums.TipoAnnuncio;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Form per pubblicazione annuncio.
 */
public class PubblicaAnnuncio extends BaseFrame {
  /**
   * Pannello principale.
   */
  private JPanel mainPanel;
  /**
   * Campo titolo.
   */
  private JTextField txtTitolo;
  /**
   * Area testo descrizione.
   */
  private JTextArea txtDescrizione;
  /**
   * Combo categoria.
   */
  private JComboBox<Categoria> cmbCategoria;
  /**
   * Combo tipo annuncio.
   */
  private JComboBox<TipoAnnuncio> cmbTipo;
  /**
   * Campo prezzo.
   */
  private JTextField txtPrezzo;
  /**
   * Pulsante pubblica.
   */
  private JButton btnPubblica;
  /**
   * Pulsante carica immagine.
   */
  private JButton btnCaricaImg;
  /**
   * Pannello modalità consegna.
   */
  private JPanel pnlConsegna;
  /**
   * Radio button ritiro.
   */
  private JRadioButton rbRitiro;
  /**
   * Radio button spedizione.
   */
  private JRadioButton rbSpedizione;

  /**
   * Pannello preview immagini.
   */
  private JPanel pnlPreview;
  /**
   * Scroll pane per preview.
   */
  private JScrollPane scrollPreview;
  /**
   * Bordo normale area drag and drop.
   */
  private Border normalBorder;
  /**
   * Bordo hover area drag and drop.
   */
  private Border hoverBorder;

  /**
   * Lista immagini selezionate.
   */
  private List<File> immaginiSelezionate;

  /**
   * Crea form pubblica annuncio.
   */
  public PubblicaAnnuncio() {
    super("Pubblica Annuncio");
    setContentPane(mainPanel);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    immaginiSelezionate = new ArrayList<>();
    initUI();

    pack();
    centraFinestra();
  }

  /**
   * Inizializza UI stato e listener.
   */
  private void initUI() {
    cmbCategoria.setModel(new DefaultComboBoxModel<>(Categoria.values()));
    cmbTipo.setModel(new DefaultComboBoxModel<>(TipoAnnuncio.values()));

    // Raggruppa radio button per modalità consegna
    ButtonGroup groupConsegna = new ButtonGroup();
    groupConsegna.add(rbRitiro);
    groupConsegna.add(rbSpedizione);

    btnPubblica.addActionListener(e -> {
      PubblicaAnnuncioController controller = new PubblicaAnnuncioController(PubblicaAnnuncio.this);
      controller.pubblica();
    });

    if (btnCaricaImg != null) {
      setupDragAndDrop();
    }

    cmbTipo.addActionListener(e -> {
      TipoAnnuncio tipo = (TipoAnnuncio) cmbTipo.getSelectedItem();
      boolean isVendita = (tipo == TipoAnnuncio.VENDITA);
      txtPrezzo.setEnabled(isVendita);
      if (!isVendita) {
        txtPrezzo.setText("");
      }
    });

    txtPrezzo.setEnabled(cmbTipo.getSelectedItem() == TipoAnnuncio.VENDITA);
  }

  /**
   * Configura drag and drop per caricamento immagini.
   */
  private void setupDragAndDrop() {
    normalBorder = BorderFactory.createDashedBorder(Color.GRAY, 2, 5, 5, true);
    hoverBorder = BorderFactory.createDashedBorder(new Color(33, 150, 243), 3, 5, 5, true);

    // Trasforma il pulsante esistente in area drag-drop
    btnCaricaImg.setText("Clicca per selezionare o trascina qui le immagini");
    btnCaricaImg.setBorder(normalBorder);
    btnCaricaImg.setFocusPainted(false);
    btnCaricaImg.setBorderPainted(true);
    btnCaricaImg.setForeground(Color.GRAY);
    btnCaricaImg.setBackground(Color.WHITE);
    btnCaricaImg.setOpaque(true);
    btnCaricaImg.setContentAreaFilled(true);
    btnCaricaImg.setEnabled(true);
    btnCaricaImg.setVisible(true);
    btnCaricaImg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    btnCaricaImg.setVerticalAlignment(javax.swing.SwingConstants.CENTER);

    // Rimuovi eventuali action listener esistenti
    for (java.awt.event.ActionListener al : btnCaricaImg.getActionListeners()) {
      btnCaricaImg.removeActionListener(al);
    }

    // Aggiungi listener per click che apre file chooser
    btnCaricaImg.addActionListener(e -> {
      JFileChooser fileChooser = new JFileChooser();
      fileChooser.setMultiSelectionEnabled(true);
      FileNameExtensionFilter filter = new FileNameExtensionFilter("Immagini (JPG, PNG)", "jpg", "png", "jpeg");
      fileChooser.setFileFilter(filter);

      int result = fileChooser.showOpenDialog(this);
      if (result == JFileChooser.APPROVE_OPTION) {
        File[] files = fileChooser.getSelectedFiles();
        immaginiSelezionate.addAll(Arrays.asList(files));
        btnCaricaImg.setText(immaginiSelezionate.size() + " immagine/i selezionate");
        btnCaricaImg.setForeground(new Color(76, 175, 80));
        updateImagePreviews();
      }
    });

    new DropTarget(btnCaricaImg, new DropTargetAdapter() {
      @Override
      public void dragEnter(DropTargetDragEvent dtde) {
        btnCaricaImg.setBorder(hoverBorder);
        btnCaricaImg.setBackground(new Color(227, 242, 253));
      }

      @Override
      public void dragExit(DropTargetEvent dte) {
        btnCaricaImg.setBorder(normalBorder);
        btnCaricaImg.setBackground(Color.WHITE);
      }

      @Override
      public void drop(DropTargetDropEvent dtde) {
        try {
          dtde.acceptDrop(DnDConstants.ACTION_COPY);
          @SuppressWarnings("unchecked")
          List<File> files = (List<File>) dtde.getTransferable()
              .getTransferData(DataFlavor.javaFileListFlavor);

          List<File> validFiles = new ArrayList<>();
          for (File file : files) {
            String fileName = file.getName().toLowerCase();
            if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png")) {
              validFiles.add(file);
            }
          }

          if (!validFiles.isEmpty()) {
            immaginiSelezionate.addAll(validFiles);
            btnCaricaImg.setText(immaginiSelezionate.size() + " immagine/i selezionate");
            btnCaricaImg.setForeground(new Color(76, 175, 80));
            updateImagePreviews();
          } else {
            JOptionPane.showMessageDialog(PubblicaAnnuncio.this,
                "Nessun file valido. Usa JPG o PNG.",
                "Errore",
                JOptionPane.ERROR_MESSAGE);
          }
          dtde.dropComplete(true);
        } catch (Exception ex) {
          dtde.dropComplete(false);
          JOptionPane.showMessageDialog(PubblicaAnnuncio.this,
              "Errore durante il caricamento: " + ex.getMessage(),
              "Errore",
              JOptionPane.ERROR_MESSAGE);
        } finally {
          btnCaricaImg.setBorder(normalBorder);
          btnCaricaImg.setBackground(Color.WHITE);
        }
      }
    });
  }

  /**
   * Restituisce input titolo.
   *
   * @return titolo
   */
  public String getTitolo() {
    return txtTitolo.getText();
  }

  /**
   * Restituisce input descrizione.
   *
   * @return descrizione
   */
  public String getDescrizione() {
    return txtDescrizione.getText();
  }

  /**
   * Restituisce categoria selezionata.
   *
   * @return categoria selezionata
   */
  public Categoria getCategoriaSelezionata() {
    return (Categoria) cmbCategoria.getSelectedItem();
  }

  /**
   * Restituisce tipo annuncio selezionato.
   *
   * @return tipo selezionato
   */
  public TipoAnnuncio getTipoSelezionato() {
    return (TipoAnnuncio) cmbTipo.getSelectedItem();
  }

  /**
   * Restituisce input prezzo.
   *
   * @return prezzo testo
   */
  public String getPrezzo() {
    return txtPrezzo.getText();
  }

  /**
   * Restituisce file immagini selezionati.
   *
   * @return immagine file lista
   */
  public List<File> getImmagini() {
    return immaginiSelezionate;
  }

  /**
   * Restituisce se è selezionata la spedizione.
   *
   * @return true se spedizione, false se ritiro
   */
  public boolean isSpedizioneSelezionata() {
    return rbSpedizione.isSelected();
  }

  /**
   * Aggiorna pannello preview con miniature immagini selezionate.
   */
  private void updateImagePreviews() {
    pnlPreview.removeAll();

    for (File imgFile : immaginiSelezionate) {
      try {
        ImageIcon icon = new ImageIcon(imgFile.getAbsolutePath());
        int origW = icon.getIconWidth();
        int origH = icon.getIconHeight();

        ImageIcon thumbIcon;
        if (origW > 0 && origH > 0) {
          double scale = Math.min(100.0 / origW, 100.0 / origH);
          int targetW = (int) Math.round(origW * scale);
          int targetH = (int) Math.round(origH * scale);
          Image img = icon.getImage().getScaledInstance(targetW, targetH, Image.SCALE_SMOOTH);
          thumbIcon = new ImageIcon(img);
        } else {
          thumbIcon = icon;
        }
        JLabel lblThumb = new JLabel(thumbIcon);
        lblThumb.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        pnlPreview.add(lblThumb);
      } catch (Exception ex) {
        // Ignora immagini non valide
      }
    }

    scrollPreview.setVisible(!immaginiSelezionate.isEmpty());
    pnlPreview.revalidate();
    pnlPreview.repaint();

    // Ridimensiona finestra per mostrare preview
    if (!immaginiSelezionate.isEmpty()) {
      pack();
      centraFinestra();
    }
  }
}
