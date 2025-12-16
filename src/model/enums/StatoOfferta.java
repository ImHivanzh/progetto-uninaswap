package model.enums;

public enum StatoOfferta {
    IN_ATTESA("In Attesa"),
    ACCETTATA("Accettata"),
    RIFIUTATA("Rifiutata");

    private final String descrizione;

    StatoOfferta(String descrizione) {
        this.descrizione = descrizione;
    }

    @Override
    public String toString() {
        return descrizione;
    }
}
