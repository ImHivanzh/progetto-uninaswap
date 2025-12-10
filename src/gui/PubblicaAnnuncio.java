package gui;

import controller.PubblicaAnnuncioController;
import model.enums.Categoria;
import model.enums.TipoAnnuncio;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PubblicaAnnuncio extends BaseFrame {
  private JPanel mainPanel;
  private JTextField txtTitolo;
  private JTextArea txtDescrizione;
  private JComboBox<Categoria> cmbCategoria;
  private JComboBox<TipoAnnuncio> cmbTipo;
  private JTextField txtPrezzo;
  private JButton btnPubblica;
  private JButton btnCaricaImg; // Aggiunto questo campo mancante per risolvere l'errore di binding

  // Se nel form hai messo una label specifica per il prezzo (es. "Prezzo (€):"),
  // puoi aggiungerla qui per nasconderla quando non serve (es. private JLabel lblPrezzo;)

  public PubblicaAnnuncio() {
    super("Pubblica Annuncio");
    setContentPane(mainPanel);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    initUI();

    pack();
    centraFinestra();
  }

  private void initUI() {
    // 1. Popolamento ComboBox con i valori degli Enum
    cmbCategoria.setModel(new DefaultComboBoxModel<>(Categoria.values()));
    cmbTipo.setModel(new DefaultComboBoxModel<>(TipoAnnuncio.values()));

    // 2. Listener per il bottone Pubblica
    btnPubblica.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // Passo 'this' (la vista corrente) al controller
        PubblicaAnnuncioController controller = new PubblicaAnnuncioController(PubblicaAnnuncio.this);
        controller.pubblica();
      }
    });

    // 3. Listener per il caricamento immagini (Predisposizione)
    if (btnCaricaImg != null) {
      btnCaricaImg.addActionListener(e -> {
        // Qui andrà la logica per aprire il FileChooser
        JOptionPane.showMessageDialog(this, "Funzionalità caricamento immagini in arrivo!");
      });
    }

    // 4. UX: Abilito il campo prezzo solo se il tipo è VENDITA
    cmbTipo.addActionListener(e -> {
      TipoAnnuncio tipo = (TipoAnnuncio) cmbTipo.getSelectedItem();
      boolean isVendita = (tipo == TipoAnnuncio.VENDITA);

      txtPrezzo.setEnabled(isVendita);
      // Opzionale: se non è vendita, pulisco il campo o lo nascondo
      if (!isVendita) {
        txtPrezzo.setText("");
      }
    });

    // Imposto lo stato iniziale corretto
    txtPrezzo.setEnabled(cmbTipo.getSelectedItem() == TipoAnnuncio.VENDITA);
  }

  // --- Metodi Getters richiesti dal Controller ---

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
}