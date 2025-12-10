package model;

import model.enums.Categoria;
import model.enums.TipoAnnuncio;

public class Regalo extends Annuncio {

    // Costruttore completo (dal DB)
    public Regalo(int id, String titolo, String descrizione, Categoria categoria, int utenteID) {
        super(id, titolo, descrizione, categoria, utenteID, TipoAnnuncio.REGALO);
    }

    // Costruttore nuovo inserimento
    public Regalo(String titolo, String descrizione, Categoria categoria, int utenteID) {
        super(titolo, descrizione, categoria, utenteID, TipoAnnuncio.REGALO);
    }
}