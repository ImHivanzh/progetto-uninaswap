package gui;

import controller.PubblicaAnnuncioController;
import model.enums.Categoria;
import model.enums.TipoAnnuncio;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

  // Lista per memorizzare le immagini selezionate dall'utente
  private List<File> immaginiSelezionate;

  public PubblicaAnnuncio() {
    super("Pubblica Annuncio");
    setContentPane(mainPanel);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    immaginiSelezionate = new ArrayList<>();
    initUI();

    pack();
    centraFinestra();
  }

  private void initUI() {
    // 1. Popolamento ComboBox
    cmbCategoria.setModel(new DefaultComboBoxModel<>(Categoria.values()));
    cmbTipo.setModel(new DefaultComboBoxModel<>(TipoAnnuncio.values()));

    // 2. Listener Pubblica
    btnPubblica.addActionListener(e -> {
      PubblicaAnnuncioController controller = new PubblicaAnnuncioController(PubblicaAnnuncio.this);
      controller.pubblica();
    });

    // 3. Listener Caricamento Immagini
    if (btnCaricaImg != null) {
      btnCaricaImg.addActionListener(e -> {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true); // Permette selezione multipla

        // Filtro per sole immagini
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

    // 4. Gestione campo Prezzo
    cmbTipo.addActionListener(e -> {
      TipoAnnuncio tipo = (TipoAnnuncio) cmbTipo.getSelectedItem();
      boolean isVendita = (tipo == TipoAnnuncio.VENDITA);
      txtPrezzo.setEnabled(isVendita);
      if (!isVendita) {
        txtPrezzo.setText("");
      }
    });

    // Stato iniziale
    txtPrezzo.setEnabled(cmbTipo.getSelectedItem() == TipoAnnuncio.VENDITA);
  }

  // --- GETTERS ---

  public String getTitolo() {
    return txtTitolo.getText();
  }

  public String getDescrizione() {
    return txtDescrizione.getText();
  }

  public Categoria getCategoriaSelezionata() {
    return (Categoria) cmbCategoria.getSelectedItem();
  }

  public TipoAnnuncio getTipoSelezionato() {
    return (TipoAnnuncio) cmbTipo.getSelectedItem();
  }

  public String getPrezzo() {
    return txtPrezzo.getText();
  }

  // Metodo aggiunto per risolvere l'errore nel Controller
  public List<File> getImmagini() {
    return immaginiSelezionate;
  }
}