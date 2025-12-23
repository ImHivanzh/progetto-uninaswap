package gui;

import controller.PubblicaAnnuncioController;
import model.enums.Categoria;
import model.enums.TipoAnnuncio;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PubblicaAnnuncio extends BaseFrame {
  private JPanel mainPanel;
  private JTextField txtTitolo;
  private JTextArea txtDescrizione;
  private JComboBox<Categoria> cmbCategoria;
  private JComboBox<TipoAnnuncio> cmbTipo;
  private JTextField txtPrezzo;
  private JButton btnPubblica;
  private JButton btnCaricaImg;

  private List<File> immaginiSelezionate;

  /**
   * Creates the publish listing form.
   */
  public PubblicaAnnuncio() {
    super("Pubblica Annuncio");
    setContentPane(mainPanel);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    immaginiSelezionate = new ArrayList<>();
    initUI();

    pack();
    centraFinestra();
  }

  /**
   * Initializes UI state and listeners.
   */
  private void initUI() {
    cmbCategoria.setModel(new DefaultComboBoxModel<>(Categoria.values()));
    cmbTipo.setModel(new DefaultComboBoxModel<>(TipoAnnuncio.values()));

    btnPubblica.addActionListener(e -> {
      PubblicaAnnuncioController controller = new PubblicaAnnuncioController(PubblicaAnnuncio.this);
      controller.pubblica();
    });

    if (btnCaricaImg != null) {
      btnCaricaImg.addActionListener(e -> {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true);

        FileNameExtensionFilter filter = new FileNameExtensionFilter("Immagini (JPG, PNG)", "jpg", "png", "jpeg");
        fileChooser.setFileFilter(filter);

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
          File[] files = fileChooser.getSelectedFiles();
          for (File file : files) {
            immaginiSelezionate.add(file);
          }
          JOptionPane.showMessageDialog(this, "Hai selezionato " + files.length + " immagini.");
        }
      });
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
   * Returns the title input.
   *
   * @return title
   */
  public String getTitolo() {
    return txtTitolo.getText();
  }

  /**
   * Returns the description input.
   *
   * @return description
   */
  public String getDescrizione() {
    return txtDescrizione.getText();
  }

  /**
   * Returns the selected category.
   *
   * @return selected category
   */
  public Categoria getCategoriaSelezionata() {
    return (Categoria) cmbCategoria.getSelectedItem();
  }

  /**
   * Returns the selected listing type.
   *
   * @return selected type
   */
  public TipoAnnuncio getTipoSelezionato() {
    return (TipoAnnuncio) cmbTipo.getSelectedItem();
  }

  /**
   * Returns the price input.
   *
   * @return price text
   */
  public String getPrezzo() {
    return txtPrezzo.getText();
  }

  /**
   * Returns the selected image files.
   *
   * @return image file list
   */
  public List<File> getImmagini() {
    return immaginiSelezionate;
  }
}
