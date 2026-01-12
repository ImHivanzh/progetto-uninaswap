package model;

import model.enums.Categoria;
import model.enums.TipoAnnuncio;

/**
 * Modello annuncio regalo.
 */
public class Regalo extends Annuncio {

    /**
     * Crea regalo annuncio con esplicito id.
     *
     * @param id id annuncio
     * @param titolo titolo
     * @param descrizione descrizione
     * @param categoria categoria
     * @param utenteID proprietario id
     */
    public Regalo(int id, String titolo, String descrizione, Categoria categoria, int utenteID) {
        super(id, titolo, descrizione, categoria, utenteID, TipoAnnuncio.REGALO);
    }

    /**
     * Crea regalo annuncio per nuovo inserimento.
     *
     * @param titolo titolo
     * @param descrizione descrizione
     * @param categoria categoria
     * @param utenteID proprietario id
     */
    public Regalo(String titolo, String descrizione, Categoria categoria, int utenteID) {
        super(utenteID, titolo, descrizione, categoria, TipoAnnuncio.REGALO);
    }
}
