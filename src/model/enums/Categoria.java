package model.enums;

/**
 * Categorie annuncio.
 */
public enum Categoria {
    /**
     * Categoria cartoleria.
     */
    CARTOLERIA("Cartoleria"),
    /**
     * Categoria elettronica.
     */
    ELETTRONICA("Elettronica"),
    /**
     * Categoria dispense e appunti.
     */
    DISPENSE_E_APPUNTI("Dispense e Appunti"),
    /**
     * Categoria sport.
     */
    SPORT("Sport"),
    /**
     * Categoria musica.
     */
    MUSICA("Musica"),
    /**
     * Categoria abbigliamento.
     */
    ABBIGLIAMENTO("Abbigliamento"),
    /**
     * Categoria libri.
     */
    LIBRI("Libri"),
    /**
     * Categoria altro.
     */
    ALTRO("Altro");

    /**
     * Descrizione leggibile.
     */
    private final String descrizione;

    /**
     * Crea categoria con descrizione testuale.
     *
     * @param descrizione descrizione
     */
    Categoria(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * Restituisce descrizione leggibile.
     *
     * @return descrizione
     */
    @Override
    public String toString() {
        return descrizione;
    }
}
