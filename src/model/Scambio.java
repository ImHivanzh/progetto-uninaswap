package model;

import model.enums.Categoria;
import model.enums.TipoAnnuncio;

public class Scambio extends Annuncio {
    private String oggettoRichiesto;

    /**
     * Crea scambio annuncio con esplicito id.
     *
     * @param id id annuncio
     * @param titolo titolo
     * @param descrizione descrizione
     * @param categoria categoria
     * @param utenteID proprietario id
     * @param oggettoRichiesto oggetto richiesto
     */
    public Scambio(int id, String titolo, String descrizione, Categoria categoria, int utenteID,
                   String oggettoRichiesto) {
        super(id, titolo, descrizione, categoria, utenteID, TipoAnnuncio.SCAMBIO);
        this.oggettoRichiesto = oggettoRichiesto;
    }

    /**
     * Crea scambio annuncio per nuovo inserimento.
     *
     * @param titolo titolo
     * @param descrizione descrizione
     * @param categoria categoria
     * @param utenteID proprietario id
     * @param oggettoRichiesto oggetto richiesto
     */
    public Scambio(String titolo, String descrizione, Categoria categoria, int utenteID, String oggettoRichiesto) {
        super(utenteID, titolo, descrizione, categoria, TipoAnnuncio.SCAMBIO);
        this.oggettoRichiesto = oggettoRichiesto;
    }

    /**
     * Restituisce descrizione oggetto richiesto.
     *
     * @return oggetto richiesto
     */
    public String getOggettoRichiesto() { return oggettoRichiesto; }

    /**
     * Imposta descrizione oggetto richiesto.
     *
     * @param oggettoRichiesto oggetto richiesto
     */
    public void setOggettoRichiesto(String oggettoRichiesto) { this.oggettoRichiesto = oggettoRichiesto; }

    /**
     * Restituisce rappresentazione stringa di scambio annuncio.
     *
     * @return rappresentazione stringa
     */
    @Override
    public String toString() {
        return "Scambio{" +
                "oggettoRichiesto='" + oggettoRichiesto + '\'' +
                "} " + super.toString();
    }
}
