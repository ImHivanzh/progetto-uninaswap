package controller;

import gui.PassDimenticataForm;
import dao.UtenteDAO;
import utils.DataCheck;
import utils.Logger;
import exception.DatabaseException;

/**
 * Controller per recupero password.
 */
public class PassDimenticataController {

    /**
     * Vista recupero password.
     */
    private final PassDimenticataForm view;
    /**
     * DAO utenti.
     */
    private final UtenteDAO utenteDAO;

    /**
     * Crea il controller e registra i listener.
     *
     * @param view vista di recupero password
     */
    public PassDimenticataController(PassDimenticataForm view) {
        this.view = view;
        this.utenteDAO = new UtenteDAO();
        initListeners();
    }

    /**
     * Registra i listener dell'interfaccia per la vista di recupero password.
     */
    private void initListeners() {
        this.view.addInvioListener(e -> gestisciCambioPassword());
    }

    /**
     * Valida l'input e aggiorna la password dell'utente.
     */
    private void gestisciCambioPassword() {
        String username = view.getUsername().trim();
        String nuovaPass = view.getNuovaPassword().trim();
        String confermaPass = view.getConfermaPassword().trim();

        if (username.isEmpty() || nuovaPass.isEmpty() || confermaPass.isEmpty()) {
            view.mostraErrore("Tutti i campi sono obbligatori.");
            return;
        }

        if (!nuovaPass.equals(confermaPass)) {
            view.mostraErrore("Le password non coincidono.");
            return;
        }

        if (!DataCheck.isStrongPassword(nuovaPass)) {
            view.mostraErrore("Password troppo debole.\nDeve contenere almeno 8 caratteri, una maiuscola, un numero e un carattere speciale.");
            return;
        }

        try {
            boolean successo = utenteDAO.aggiornaPassword(username, nuovaPass);
            if (successo) {
                view.mostraMessaggio("Password aggiornata con successo!");
                view.dispose();
            } else {
                view.mostraErrore("Impossibile aggiornare la password.\nVerifica che l'username sia corretto.");
            }
        } catch (IllegalArgumentException ex) {
            view.mostraErrore(ex.getMessage());
        } catch (DatabaseException ex) {
            view.mostraErrore("Errore di connessione al database: " + ex.getMessage());
            Logger.error("Errore database in password dimenticata", ex);
        }
    }
}
