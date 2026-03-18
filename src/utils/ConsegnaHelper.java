package utils;

import dao.RitiroDAO;
import dao.SpedizioneDAO;
import exception.DatabaseException;
import model.PropostaRiepilogo;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.sql.Date;
import java.sql.Time;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;

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
    String message = """
            Dettagli Spedizione:
            ID Spedizione: %d
            Indirizzo: %s
            Numero di Telefono: %s
            Data Invio: %s
            Data Arrivo: %s
            Spedito: %s""".formatted(
            spedizione.getIdSpedizione(),
            spedizione.getIndirizzo(),
            spedizione.getNumeroTelefono(),
            spedizione.getDataInvio().toString(),
            spedizione.getDataArrivo().toString(),
            spedizione.isSpedito() ? "Si" : "No"
    );

    JOptionPane.showMessageDialog(parent, message, "Dettagli Spedizione", JOptionPane.INFORMATION_MESSAGE);
  }

  /**
   * Mostra dettagli di un ritiro in dialog.
   *
   * @param ritiro ritiro da visualizzare
   */
  public void mostraDettagliRitiro(model.Ritiro ritiro) {
    String message = """
            Dettagli Ritiro:
            ID Ritiro: %d
            Sede: %s
            Orario: %s
            Data: %s
            Numero di Telefono: %s
            Ritirato: %s""".formatted(
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

    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = FormHelper.createFormConstraints();
    FormHelper.addFormRow(panel, gbc, 0, "Indirizzo di consegna:", indirizzoField);
    FormHelper.addFormRow(panel, gbc, 1, "Numero telefono:", telefonoField);

    boolean inputValido = false;
    while (!inputValido) {
      int result = JOptionPane.showConfirmDialog(
              parent,
              panel,
              "Inserisci i tuoi dati per la spedizione",
              JOptionPane.OK_CANCEL_OPTION,
              JOptionPane.PLAIN_MESSAGE);
      if (result != JOptionPane.OK_OPTION) {
        return;
      }

      String indirizzo = indirizzoField.getText().trim();
      String telefono = telefonoField.getText().trim();

      String errore = validaInputSpedizione(indirizzo, telefono);
      if (errore != null) {
        onError.accept(errore);
        continue;
      }

      // Imposta date predefinite: oggi per invio, oggi+7 giorni per arrivo stimato
      java.util.Calendar cal = java.util.Calendar.getInstance();
      java.util.Date dataInvio = cal.getTime();
      cal.add(java.util.Calendar.DAY_OF_MONTH, 7);
      java.util.Date dataArrivo = cal.getTime();

      SpedizioneData data = new SpedizioneData(indirizzo, telefono, dataInvio, dataArrivo);
      inputValido = salvaSpedizioneDB(idAnnuncio, idUtente, data, onSuccess, onError);
    }
  }

  /**
   * Dati per spedizione.
   */
  private static class SpedizioneData {
    final String indirizzo;
    final String telefono;
    final java.util.Date dataInvio;
    final java.util.Date dataArrivo;

    SpedizioneData(String indirizzo, String telefono, java.util.Date dataInvio, java.util.Date dataArrivo) {
      this.indirizzo = indirizzo;
      this.telefono = telefono;
      this.dataInvio = dataInvio;
      this.dataArrivo = dataArrivo;
    }
  }

  private String validaInputSpedizione(String indirizzo, String telefono) {
    if (indirizzo.isEmpty()) {
      return "Indirizzo obbligatorio.";
    }
    if (!DataCheck.isValidPhoneNumber(telefono)) {
      return "Numero di telefono non valido (richieste 10 cifre).";
    }
    return null;
  }

  private boolean salvaSpedizioneDB(int idAnnuncio, int idUtente, SpedizioneData data,
                                     java.util.function.Consumer<String> onSuccess,
                                     java.util.function.Consumer<String> onError) {
    try {
      boolean ok = spedizioneDAO.inserisciSpedizione(
              new Date(data.dataInvio.getTime()),
              new Date(data.dataArrivo.getTime()),
              data.indirizzo,
              data.telefono,
              idAnnuncio);
      gestisciRisultatoSalvataggio(ok, onSuccess, onError, "spedizione");
      return ok;
    } catch (DatabaseException e) {
      onError.accept("Errore durante il salvataggio della spedizione: " + e.getMessage());
      Logger.error("Errore salvataggio spedizione", e);
      return false;
    }
  }

  private void gestisciRisultatoSalvataggio(boolean ok, java.util.function.Consumer<String> onSuccess,
                                             java.util.function.Consumer<String> onError, String tipo) {
    if (ok) {
      onSuccess.accept("Dettagli " + tipo + " salvati.");
    } else {
      onError.accept("Salvataggio " + tipo + " non riuscito.");
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

    boolean inputValido = false;
    while (!inputValido) {
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

      String errore = validaInputRitiro(sede, telefono, data, orario);
      if (errore != null) {
        onError.accept(errore);
        continue;
      }

      inputValido = salvaRitiroDB(idAnnuncio, sede, telefono, data, orario, onSuccess, onError);
    }
  }

  private String validaInputRitiro(String sede, String telefono, java.util.Date data, java.util.Date orario) {
    if (sede.isEmpty()) {
      return "Sede obbligatoria.";
    }
    if (!DataCheck.isValidPhoneNumber(telefono)) {
      return "Numero di telefono non valido (richieste 10 cifre).";
    }
    if (data == null || orario == null) {
      return "Inserisci data e orario validi.";
    }
    return null;
  }

  private boolean salvaRitiroDB(int idAnnuncio, String sede, String telefono,
                                 java.util.Date data, java.util.Date orario,
                                 java.util.function.Consumer<String> onSuccess,
                                 java.util.function.Consumer<String> onError) {
    try {
      boolean ok = ritiroDAO.inserisciRitiro(
              sede,
              new Time(orario.getTime()),
              new Date(data.getTime()),
              telefono,
              idAnnuncio);
      gestisciRisultatoSalvataggio(ok, onSuccess, onError, "ritiro");
      return ok;
    } catch (DatabaseException e) {
      onError.accept("Errore durante il salvataggio del ritiro: " + e.getMessage());
      Logger.error("Errore salvataggio ritiro", e);
      return false;
    }
  }
}
