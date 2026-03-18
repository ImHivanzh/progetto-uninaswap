package gui;

import controller.FaiPropostaController;
import model.enums.TipoAnnuncio;

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
 * Dialogo per invio proposta.
 */
public class FaiPropostaDialog extends JDialog {
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
   * Etichetta descrizione.
   */
  private JLabel lblDescrizione;
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
   * Etichetta anteprima immagine.
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
   * Pulsante invio.
   */
  private JButton btnInvia;
  /**
   * Pulsante annulla.
   */
  private JButton btnAnnulla;

  /**
   * Controller associato al dialogo.
   */
  private transient FaiPropostaController controller;

  /**
   * Crea dialogo proposta per fornito tipo annuncio.
   *
   * @param owner padre finestra
   * @param tipoAnnuncio tipo annuncio
   */
  public FaiPropostaDialog(Frame owner, TipoAnnuncio tipoAnnuncio) {
    super(owner, "Fai una Proposta", true);
    setContentPane(contentPane);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    getRootPane().setDefaultButton(btnInvia);

    this.controller = new FaiPropostaController(this, tipoAnnuncio);
    configuraInterfaccia(tipoAnnuncio);
    setupListeners();

    pack();
    setLocationRelativeTo(owner);
  }

  /**
   * Configura UI basato in tipo annuncio.
   *
   * @param tipo tipo annuncio
   */
  private void configuraInterfaccia(TipoAnnuncio tipo) {
    pnlPrezzo.setVisible(tipo == TipoAnnuncio.VENDITA);

    if (tipo == TipoAnnuncio.SCAMBIO) {
      lblDescrizione.setText("Descrizione oggetto (Obbligatorio):");
    } else {
      lblDescrizione.setText("Messaggio per l'utente:");
    }

    pnlImmagine.setVisible(tipo == TipoAnnuncio.SCAMBIO);

    if (tipo == TipoAnnuncio.SCAMBIO) {
      btnCaricaImmagine.setVisible(false);
      setupDragAndDrop();
    }
  }

  /**
   * Registra dialogo pulsante listener.
   */
  private void setupListeners() {
    btnInvia.addActionListener(e -> controller.azioneConferma());
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
    lblImagePreview.setText("<html><center>Clicca per selezionare<br>o trascina qui l'immagine</center></html>");
    lblImagePreview.setHorizontalAlignment(JLabel.CENTER);
    lblImagePreview.setVerticalAlignment(JLabel.CENTER);

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
              JOptionPane.showMessageDialog(FaiPropostaDialog.this,
                  "Formato non supportato. Usa JPG o PNG.",
                  "Errore",
                  JOptionPane.ERROR_MESSAGE);
            }
          }
          dtde.dropComplete(true);
        } catch (Exception ex) {
          dtde.dropComplete(false);
          JOptionPane.showMessageDialog(FaiPropostaDialog.this,
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

  /**
   * Restituisce input prezzo testo.
   *
   * @return input prezzo
   */
  public String getPrezzoInput() {
    return txtPrezzo.getText();
  }

  /**
   * Restituisce input descrizione testo.
   *
   * @return input descrizione
   */
  public String getDescrizioneInput() {
    return txtDescrizione.getText();
  }

  /**
   * Aggiorna area anteprima immagine.
   *
   * @param imgData byte immagine
   */
  public void aggiornaAnteprimaImmagine(byte[] imgData) {
    if (imgData != null) {
      ImageIcon icon = new ImageIcon(imgData);
      int origW = icon.getIconWidth();
      int origH = icon.getIconHeight();

      if (origW > 0 && origH > 0) {
        double scale = Math.min(100.0 / origW, 100.0 / origH);
        int targetW = (int) Math.round(origW * scale);
        int targetH = (int) Math.round(origH * scale);
        Image img = icon.getImage().getScaledInstance(targetW, targetH, Image.SCALE_SMOOTH);
        lblImagePreview.setIcon(new ImageIcon(img));
      } else {
        lblImagePreview.setIcon(icon);
      }
      lblImagePreview.setText("");
    }
  }

  /**
   * Mostra errore dialogo.
   *
   * @param messaggio errore testo
   */
  public void mostraErrore(String messaggio) {
    JOptionPane.showMessageDialog(this, messaggio, "Errore", JOptionPane.ERROR_MESSAGE);
  }

  /**
   * Restituisce controller istanza.
   *
   * @return controller
   */
  public FaiPropostaController getController() {
    return controller;
  }
}
