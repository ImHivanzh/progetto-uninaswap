package model;

import model.enums.Categoria;
import model.enums.TipoAnnuncio;

public class Scambio extends Annuncio {
    private String oggettoRichiesto;

    // Costruttore completo (dal DB)
    public Scambio(int id, String titolo, String descrizione, Categoria categoria, int utenteID, String oggettoRichiesto) {
        super(id, titolo, descrizione, categoria, utenteID, TipoAnnuncio.SCAMBIO);
        this.oggettoRichiesto = oggettoRichiesto;
    }

    // Costruttore nuovo inserimento
    public Scambio(String titolo, String descrizione, Categoria categoria, int utenteID, String oggettoRichiesto) {
        // CORRETTO: idUtente va per primo
        super(utenteID, titolo, descrizione, categoria, TipoAnnuncio.SCAMBIO);
        this.oggettoRichiesto = oggettoRichiesto;
    }

    public String getOggettoRichiesto() { return oggettoRichiesto; }
    public void setOggettoRichiesto(String oggettoRichiesto) { this.oggettoRichiesto = oggettoRichiesto; }

    @Override
    public String toString() {
        return "Scambio{" +
                "oggettoRichiesto='" + oggettoRichiesto + '\'' +
                "} " + super.toString();
    }
}