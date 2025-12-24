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
   * Crea form pubblica annuncio.
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
   * Inizializza UI stato e listener.
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
}
