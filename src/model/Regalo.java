package model;

import model.enums.Categoria;
import model.enums.TipoAnnuncio;

public class Regalo extends Annuncio {

    /**
     * Creates a gift listing with an explicit id.
     *
     * @param id listing id
     * @param titolo title
     * @param descrizione description
     * @param categoria category
     * @param utenteID owner id
     */
    public Regalo(int id, String titolo, String descrizione, Categoria categoria, int utenteID) {
        super(id, titolo, descrizione, categoria, utenteID, TipoAnnuncio.REGALO);
    }

    /**
     * Creates a gift listing for new insertion.
     *
     * @param titolo title
     * @param descrizione description
     * @param categoria category
     * @param utenteID owner id
     */
    public Regalo(String titolo, String descrizione, Categoria categoria, int utenteID) {
        super(utenteID, titolo, descrizione, categoria, TipoAnnuncio.REGALO);
    }
}
