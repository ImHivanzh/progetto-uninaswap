package model;

import model.enums.Categoria;
import model.enums.TipoAnnuncio;

public class Regalo extends Annuncio {

    // Costruttore completo (dal DB) - Questo era già corretto
    public Regalo(int id, String titolo, String descrizione, Categoria categoria, int utenteID) {
        super(id, titolo, descrizione, categoria, utenteID, TipoAnnuncio.REGALO);
    }

    // Costruttore nuovo inserimento - CORRETTO
    public Regalo(String titolo, String descrizione, Categoria categoria, int utenteID) {
        // L'ordine corretto è: idUtente, titolo, descrizione, categoria, tipo
        super(utenteID, titolo, descrizione, categoria, TipoAnnuncio.REGALO);
    }
}