package model.enums;

public enum Categoria {
    CARTOLERIA("Cartoleria"),
    ELETTRONICA("Elettronica"),
    DISPENSE_E_APPUNTI("Dispense e Appunti"),
    SPORT("Sport"),
    MUSICA("Musica"),
    ABBIGLIAMENTO("Abbigliamento"),
    LIBRI("Libri"),
    ALTRO("Altro");

    private final String descrizione;

    Categoria(String descrizione) {
        this.descrizione = descrizione;
    }

    @Override
    public String toString() {
        return descrizione;
    }
}
