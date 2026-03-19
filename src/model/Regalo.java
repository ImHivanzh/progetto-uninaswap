package model;

import model.enums.Categoria;
import model.enums.TipoAnnuncio;

/**
 * Modello annuncio regalo.
 */
public class Regalo extends Annuncio {

    /**
     * Crea un annuncio regalo con id esplicito.
     *
     * @param id id annuncio
     * @param titolo titolo
     * @param descrizione descrizione
     * @param categoria categoria
     * @param utente proprietario
     */
    public Regalo(int id, String titolo, String descrizione, Categoria categoria, Utente utente) {
        super(id, titolo, descrizione, categoria, utente, TipoAnnuncio.REGALO);
    }

    /**
     * Crea un annuncio regalo per nuovo inserimento.
     *
     * @param titolo titolo
     * @param descrizione descrizione
     * @param categoria categoria
     * @param utente proprietario
     */
    public Regalo(String titolo, String descrizione, Categoria categoria, Utente utente) {
        super(utente, titolo, descrizione, categoria, TipoAnnuncio.REGALO);
    }
}
