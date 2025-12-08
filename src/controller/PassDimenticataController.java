package controller;

import gui.PassDimenticataForm;
import dao.UtenteDAO;
import utils.DataCheck;
import exception.DatabaseException;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PassDimenticataController {

    private final PassDimenticataForm view;
    private final UtenteDAO utenteDAO;

    public PassDimenticataController(PassDimenticataForm view) {
        this.view = view;
        this.utenteDAO = new UtenteDAO();
        initListeners();
    }

    private void initListeners() {
        this.view.addInvioListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gestisciCambioPassword();
            }
        });
    }

    private void gestisciCambioPassword() {
        String username = view.getUsername();
        String nuovaPass = view.getNuovaPassword();
        String confermaPass = view.getConfermaPassword();

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
            ex.printStackTrace();
        }
    }
}