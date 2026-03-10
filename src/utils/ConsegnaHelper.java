package utils;

import dao.RitiroDAO;
import dao.SpedizioneDAO;
import exception.DatabaseException;
import model.PropostaRiepilogo;

import javax.swing.*;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.sql.Date;
import java.sql.Time;

/**
 * Helper per la gestione delle consegne (spedizione e ritiro).
 */
public class ConsegnaHelper {

  private final SpedizioneDAO spedizioneDAO;
  private final RitiroDAO ritiroDAO;
  private final JFrame parent;

  /**
   * Crea helper per gestione consegne.
   *
   * @param spedizioneDAO DAO spedizione
   * @param ritiroDAO DAO ritiro
   * @param parent frame parent per dialogs
   */
  public ConsegnaHelper(SpedizioneDAO spedizioneDAO, RitiroDAO ritiroDAO, JFrame parent) {
    this.spedizioneDAO = spedizioneDAO;
    this.ritiroDAO = ritiroDAO;
    this.parent = parent;
  }

  /**
   * Visualizza i dettagli della consegna per una proposta.
   *
   * @param proposta proposta da visualizzare
   * @param onError callback per errori
   * @param onNoDetails callback quando non ci sono dettagli
   */
  public void visualizzaDettagli(PropostaRiepilogo proposta,
                                   java.util.function.Consumer<String> onError,
                                   Runnable onNoDetails) {
    if (spedizioneDAO == null || ritiroDAO == null) {
      onError.accept("Connessione ai servizi di spedizione/ritiro non disponibile.");
      return;
    }

    try {
      if (proposta == null || proposta.annuncio() == null) {
        onError.accept("Proposta o annuncio non disponibile.");
        return;
      }

      int idAnnuncio = proposta.annuncio().getIdAnnuncio();

      model.Spedizione spedizione = spedizioneDAO.getSpedizioneByAnnuncio(idAnnuncio);
      if (spedizione != null) {
        mostraDettagliSpedizione(spedizione);
        return;
      }

      model.Ritiro ritiro = ritiroDAO.getRitiroByAnnuncio(idAnnuncio);
      if (ritiro != null) {
        mostraDettagliRitiro(ritiro);
        return;
      }

      onNoDetails.run();
    } catch (DatabaseException e) {
      onError.accept("Errore durante la visualizzazione dei dettagli di consegna: " + e.getMessage());
      Logger.error("Errore visualizzazione dettagli consegna", e);
    }
  }

  /**
   * Mostra dettagli di una spedizione in dialog.
   *
   * @param spedizione spedizione da visualizzare
   */
  public void mostraDettagliSpedizione(model.Spedizione spedizione) {
    StringBuilder message = new StringBuilder("Dettagli Spedizione:\n");
    if (spedizione.getIdSpedizione() != 0) {
      message.append("ID Spedizione: ").append(spedizione.getIdSpedizione()).append("\n");
    }
    message.append("Indirizzo: ").append(spedizione.getIndirizzo()).append("\n");
    message.append("Numero di Telefono: ").append(spedizione.getNumeroTelefono()).append("\n");
    message.append("Data Invio: ").append(spedizione.getDataInvio().toString()).append("\n");
    message.append("Data Arrivo: ").append(spedizione.getDataArrivo().toString()).append("\n");
    message.append("Spedito: ").append(spedizione.isSpedito() ? "Si" : "No");

    JOptionPane.showMessageDialog(parent, message.toString(), "Dettagli Spedizione", JOptionPane.INFORMATION_MESSAGE);
  }

  /**
   * Mostra dettagli di un ritiro in dialog.
   *
   * @param ritiro ritiro da visualizzare
   */
  public void mostraDettagliRitiro(model.Ritiro ritiro) {
    String message = String.format(
            "Dettagli Ritiro:\n" +
            "ID Ritiro: %d\n" +
            "Sede: %s\n" +
            "Orario: %s\n" +
            "Data: %s\n" +
            "Numero di Telefono: %s\n" +
            "Ritirato: %s",
            ritiro.getIdRitiro(),
            ritiro.getSede(),
            ritiro.getOrario(),
            ritiro.getData().toString(),
            ritiro.getNumeroTelefono(),
            ritiro.isRitirato() ? "Si" : "No"
    );
    JOptionPane.showMessageDialog(parent, message, "Dettagli Ritiro", JOptionPane.INFORMATION_MESSAGE);
  }

  /**
   * Richiede e salva dati spedizione.
   *
   * @param idAnnuncio id annuncio
   * @param idUtente id utente
   * @param onSuccess callback successo
   * @param onError callback errore
   */
  public void salvaSpedizione(int idAnnuncio, int idUtente,
                               java.util.function.Consumer<String> onSuccess,
                               java.util.function.Consumer<String> onError) {
    JTextField indirizzoField = new JTextField(24);
    JTextField telefonoField = new JTextField(12);
    JSpinner dataInvioSpinner = FormHelper.createDateSpinner();
    JSpinner dataArrivoSpinner = FormHelper.createDateSpinner();

    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = FormHelper.createFormConstraints();
    FormHelper.addFormRow(panel, gbc, 0, "Indirizzo:", indirizzoField);
    FormHelper.addFormRow(panel, gbc, 1, "Numero telefono:", telefonoField);
    FormHelper.addFormRow(panel, gbc, 2, "Data invio:", dataInvioSpinner);
    FormHelper.addFormRow(panel, gbc, 3, "Data arrivo:", dataArrivoSpinner);

    while (true) {
      int result = JOptionPane.showConfirmDialog(
              parent,
              panel,
              "Dettagli spedizione",
              JOptionPane.OK_CANCEL_OPTION,
              JOptionPane.PLAIN_MESSAGE);
      if (result != JOptionPane.OK_OPTION) {
        return;
      }

      String indirizzo = indirizzoField.getText().trim();
      String telefono = telefonoField.getText().trim();
      java.util.Date dataInvio = (java.util.Date) dataInvioSpinner.getValue();
      java.util.Date dataArrivo = (java.util.Date) dataArrivoSpinner.getValue();

      if (indirizzo.isEmpty()) {
        onError.accept("Indirizzo obbligatorio.");
        continue;
      }
      if (!DataCheck.isValidPhoneNumber(telefono)) {
        onError.accept("Numero di telefono non valido (richieste 10 cifre).");
        continue;
      }
      if (dataInvio == null || dataArrivo == null) {
        onError.accept("Inserisci date valide.");
        continue;
      }
      if (dataArrivo.before(dataInvio)) {
        onError.accept("La data di arrivo non puo essere precedente alla data di invio.");
        continue;
      }

      try {
        boolean ok = spedizioneDAO.inserisciSpedizione(
                new Date(dataInvio.getTime()),
                new Date(dataArrivo.getTime()),
                indirizzo,
                telefono,
                idAnnuncio,
                idUtente);
        if (ok) {
          onSuccess.accept("Dettagli spedizione salvati.");
        } else {
          onError.accept("Salvataggio spedizione non riuscito.");
        }
      } catch (DatabaseException e) {
        onError.accept("Errore durante il salvataggio della spedizione: " + e.getMessage());
        Logger.error("Errore salvataggio spedizione", e);
      }
      return;
    }
  }

  /**
   * Richiede e salva dati ritiro.
   *
   * @param idAnnuncio id annuncio
   * @param onSuccess callback successo
   * @param onError callback errore
   */
  public void salvaRitiro(int idAnnuncio,
                           java.util.function.Consumer<String> onSuccess,
                           java.util.function.Consumer<String> onError) {
    JTextField sedeField = new JTextField(20);
    JTextField telefonoField = new JTextField(12);
    JSpinner dataSpinner = FormHelper.createDateSpinner();
    JSpinner orarioSpinner = FormHelper.createTimeSpinner();

    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = FormHelper.createFormConstraints();
    FormHelper.addFormRow(panel, gbc, 0, "Sede:", sedeField);
    FormHelper.addFormRow(panel, gbc, 1, "Numero telefono:", telefonoField);
    FormHelper.addFormRow(panel, gbc, 2, "Data:", dataSpinner);
    FormHelper.addFormRow(panel, gbc, 3, "Orario:", orarioSpinner);

    while (true) {
      int result = JOptionPane.showConfirmDialog(
              parent,
              panel,
              "Dettagli ritiro",
              JOptionPane.OK_CANCEL_OPTION,
              JOptionPane.PLAIN_MESSAGE);
      if (result != JOptionPane.OK_OPTION) {
        return;
      }

      String sede = sedeField.getText().trim();
      String telefono = telefonoField.getText().trim();
      java.util.Date data = (java.util.Date) dataSpinner.getValue();
      java.util.Date orario = (java.util.Date) orarioSpinner.getValue();

      if (sede.isEmpty()) {
        onError.accept("Sede obbligatoria.");
        continue;
      }
      if (!DataCheck.isValidPhoneNumber(telefono)) {
        onError.accept("Numero di telefono non valido (richieste 10 cifre).");
        continue;
      }
      if (data == null || orario == null) {
        onError.accept("Inserisci data e orario validi.");
        continue;
      }

      try {
        boolean ok = ritiroDAO.inserisciRitiro(
                sede,
                new Time(orario.getTime()),
                new Date(data.getTime()),
                telefono,
                idAnnuncio);
        if (ok) {
          onSuccess.accept("Dettagli ritiro salvati.");
        } else {
          onError.accept("Salvataggio ritiro non riuscito.");
        }
      } catch (DatabaseException e) {
        onError.accept("Errore durante il salvataggio del ritiro: " + e.getMessage());
        Logger.error("Errore salvataggio ritiro", e);
      }
      return;
    }
  }
}
