package model;

import model.enums.Categoria;
import model.enums.TipoAnnuncio;

public class Scambio extends Annuncio {
    private String oggettoRichiesto;

    /**
     * Creates an exchange listing with an explicit id.
     *
     * @param id listing id
     * @param titolo title
     * @param descrizione description
     * @param categoria category
     * @param utenteID owner id
     * @param oggettoRichiesto requested item
     */
    public Scambio(int id, String titolo, String descrizione, Categoria categoria, int utenteID,
                   String oggettoRichiesto) {
        super(id, titolo, descrizione, categoria, utenteID, TipoAnnuncio.SCAMBIO);
        this.oggettoRichiesto = oggettoRichiesto;
    }

    /**
     * Creates an exchange listing for new insertion.
     *
     * @param titolo title
     * @param descrizione description
     * @param categoria category
     * @param utenteID owner id
     * @param oggettoRichiesto requested item
     */
    public Scambio(String titolo, String descrizione, Categoria categoria, int utenteID, String oggettoRichiesto) {
        super(utenteID, titolo, descrizione, categoria, TipoAnnuncio.SCAMBIO);
        this.oggettoRichiesto = oggettoRichiesto;
    }

    /**
     * Returns the requested item description.
     *
     * @return requested item
     */
    public String getOggettoRichiesto() { return oggettoRichiesto; }

    /**
     * Sets the requested item description.
     *
     * @param oggettoRichiesto requested item
     */
    public void setOggettoRichiesto(String oggettoRichiesto) { this.oggettoRichiesto = oggettoRichiesto; }

    /**
     * Returns a string representation of the exchange listing.
     *
     * @return string representation
     */
    @Override
    public String toString() {
        return "Scambio{" +
                "oggettoRichiesto='" + oggettoRichiesto + '\'' +
                "} " + super.toString();
    }
}
