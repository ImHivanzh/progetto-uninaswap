package gui;

import controller.ModificaPropostaController;
import exception.DatabaseException;
import model.PropostaRiepilogo;
import model.enums.TipoAnnuncio;
import utils.Logger;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.io.File;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

/**
 * Dialogo per modifica proposta.
 */
public class ModificaPropostaDialog extends JDialog {
  /**
   * Pannello contenuto.
   */
  private JPanel contentPane;
  /**
   * Pannello prezzo.
   */
  private JPanel pnlPrezzo;
  /**
   * Campo prezzo.
   */
  private JTextField txtPrezzo;
  /**
   * Area testo descrizione.
   */
  private JTextArea txtDescrizione;
  /**
   * Pannello immagine.
   */
  private JPanel pnlImmagine;
  /**
   * Pulsante carica immagine.
   */
  private JButton btnCaricaImmagine;
  /**
   * Anteprima immagine.
   */
  private JLabel lblImagePreview;

  /**
   * Bordo normale area drag and drop.
   */
  private Border normalBorder;
  /**
   * Bordo hover area drag and drop.
   */
  private Border hoverBorder;
  /**
   * Pulsante salva.
   */
  private JButton btnSalva;
  /**
   * Pulsante annulla.
   */
  private JButton btnAnnulla;

  /**
   * Controller dialogo.
   */
  private transient ModificaPropostaController controller;

  /**
   * Crea dialogo modifica proposta.
   *
   * @param owner frame proprietario
   * @param proposta proposta da modificare
   */
  public ModificaPropostaDialog(Frame owner, PropostaRiepilogo proposta) {
    super(owner, "Modifica Proposta", true);
    setContentPane(contentPane);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    getRootPane().setDefaultButton(btnSalva);

    try {
      this.controller = new ModificaPropostaController(this, proposta);
    } catch (DatabaseException e) {
      Logger.error("Errore inizializzazione controller modifica proposta", e);
      JOptionPane.showMessageDialog(this,
        "Errore durante l'inizializzazione: " + e.getMessage(),
        "Errore",
        JOptionPane.ERROR_MESSAGE);
      dispose();
      return;
    }

    configuraInterfaccia(proposta.annuncio().getTipoAnnuncio());
    setupListeners();
    popolaDati();

    pack();
    setLocationRelativeTo(owner);
  }

  private void configuraInterfaccia(TipoAnnuncio tipo) {
    pnlPrezzo.setVisible(tipo == TipoAnnuncio.VENDITA);
    pnlImmagine.setVisible(tipo == TipoAnnuncio.SCAMBIO);

    if (tipo == TipoAnnuncio.SCAMBIO) {
      btnCaricaImmagine.setVisible(false);
      setupDragAndDrop();
    }
  }

  private void setupListeners() {
    btnSalva.addActionListener(e -> controller.azioneSalva());
    btnAnnulla.addActionListener(e -> controller.azioneAnnulla());
    btnCaricaImmagine.addActionListener(e -> controller.azioneCaricaImmagine());
  }

  /**
   * Configura drag and drop per area immagine.
   */
  private void setupDragAndDrop() {
    normalBorder = BorderFactory.createDashedBorder(Color.GRAY, 2, 5, 5, true);
    hoverBorder = BorderFactory.createDashedBorder(new Color(33, 150, 243), 3, 5, 5, true);

    pnlImmagine.setBorder(normalBorder);
    if (lblImagePreview.getIcon() == null) {
      lblImagePreview.setText("<html><center>Clicca per selezionare<br>o trascina qui l'immagine</center></html>");
      lblImagePreview.setHorizontalAlignment(JLabel.CENTER);
      lblImagePreview.setVerticalAlignment(JLabel.CENTER);
    }

    // Aggiungi listener per click che apre file chooser
    pnlImmagine.addMouseListener(new java.awt.event.MouseAdapter() {
      @Override
      public void mouseClicked(java.awt.event.MouseEvent e) {
        controller.azioneCaricaImmagine();
      }
    });

    new DropTarget(pnlImmagine, new DropTargetAdapter() {
      @Override
      public void dragEnter(DropTargetDragEvent dtde) {
        pnlImmagine.setBorder(hoverBorder);
        pnlImmagine.setBackground(new Color(227, 242, 253));
      }

      @Override
      public void dragExit(DropTargetEvent dte) {
        pnlImmagine.setBorder(normalBorder);
        pnlImmagine.setBackground(null);
      }

      @Override
      public void drop(DropTargetDropEvent dtde) {
        try {
          dtde.acceptDrop(DnDConstants.ACTION_COPY);
          @SuppressWarnings("unchecked")
          List<File> files = (List<File>) dtde.getTransferable()
              .getTransferData(DataFlavor.javaFileListFlavor);

          if (!files.isEmpty()) {
            File file = files.get(0);
            String fileName = file.getName().toLowerCase();
            if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png")) {
              controller.caricaImmagineDaFile(file);
            } else {
              JOptionPane.showMessageDialog(ModificaPropostaDialog.this,
                  "Formato non supportato. Usa JPG o PNG.",
                  "Errore",
                  JOptionPane.ERROR_MESSAGE);
            }
          }
          dtde.dropComplete(true);
        } catch (Exception ex) {
          dtde.dropComplete(false);
          JOptionPane.showMessageDialog(ModificaPropostaDialog.this,
              "Errore durante il caricamento: " + ex.getMessage(),
              "Errore",
              JOptionPane.ERROR_MESSAGE);
        } finally {
          pnlImmagine.setBorder(normalBorder);
          pnlImmagine.setBackground(null);
        }
      }
    });
  }

  private void popolaDati() {
    controller.popolaDati();
  }

  public String getPrezzoInput() {
    return txtPrezzo.getText();
  }

  public void setPrezzoInput(String prezzo) {
    txtPrezzo.setText(prezzo);
  }

  public String getDescrizioneInput() {
    return txtDescrizione.getText();
  }

  public void setDescrizioneInput(String descrizione) {
    txtDescrizione.setText(descrizione);
  }

  public void aggiornaAnteprimaImmagine(byte[] imgData) {
    if (imgData != null) {
      ImageIcon icon = new ImageIcon(imgData);
      Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
      lblImagePreview.setIcon(new ImageIcon(img));
      lblImagePreview.setText("");
    }
  }

  public void mostraErrore(String messaggio) {
    JOptionPane.showMessageDialog(this, messaggio, "Errore", JOptionPane.ERROR_MESSAGE);
  }

  public ModificaPropostaController getController() {
    return controller;
  }
}
