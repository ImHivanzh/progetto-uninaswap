package model.enums;

public enum TipoAnnuncio {
    SCAMBIO("Scambio"),
    VENDITA("Vendita"),
    REGALO("Regalo");

    private final String descrizione;

    TipoAnnuncio(String descrizione) {
        this.descrizione = descrizione;
    }

    @Override
    public String toString() {
        return descrizione;
    }
}
