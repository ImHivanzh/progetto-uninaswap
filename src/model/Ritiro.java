package model;
import java.util.Date;

public class Ritiro {
    private int idRitiro;
    private String sede;
    private String orario;
    private Date data;
    private String numeroTelefono;
    private boolean ritirato;
    private Annuncio annuncio;

    /**
     * Crea vuoto record ritiro.
     */
    public Ritiro() {
    }

    /**
     * Crea record ritiro con fornito campi.
     *
     * @param idRitiro id ritiro
     * @param sede luogo ritiro
     * @param orario orario ritiro
     * @param data data ritiro
     * @param numeroTelefono numero telefono contatto
     * @param ritirato flag completamento ritiro
     * @param annuncio collegato annuncio
     */
    public Ritiro(int idRitiro, String sede, String orario, Date data, String numeroTelefono,
                  boolean ritirato, Annuncio annuncio) {
        this.idRitiro = idRitiro;
        this.sede = sede;
        this.orario = orario;
        this.data = data;
        this.numeroTelefono = numeroTelefono;
        this.ritirato = ritirato;
        this.annuncio = annuncio;
    }

    /**
     * Restituisce id ritiro.
     *
     * @return id ritiro
     */
    public int getIdRitiro() { return idRitiro; }

    /**
     * Imposta id ritiro.
     *
     * @param idRitiro id ritiro
     */
    public void setIdRitiro(int idRitiro) { this.idRitiro = idRitiro; }

    /**
     * Restituisce luogo ritiro.
     *
     * @return luogo ritiro
     */
    public String getSede() { return sede; }

    /**
     * Imposta luogo ritiro.
     *
     * @param sede luogo ritiro
     */
    public void setSede(String sede) { this.sede = sede; }

    /**
     * Restituisce orario ritiro.
     *
     * @return orario ritiro
     */
    public String getOrario() { return orario; }

    /**
     * Imposta orario ritiro.
     *
     * @param orario orario ritiro
     */
    public void setOrario(String orario) { this.orario = orario; }

    /**
     * Restituisce data ritiro.
     *
     * @return data ritiro
     */
    public Date getData() { return data; }

    /**
     * Imposta data ritiro.
     *
     * @param data data ritiro
     */
    public void setData(Date data) { this.data = data; }

    /**
     * Restituisce numero telefono contatto.
     *
     * @return numero telefono contatto
     */
    public String getNumeroTelefono() { return numeroTelefono; }

    /**
     * Imposta numero telefono contatto.
     *
     * @param numeroTelefono numero telefono contatto
     */
    public void setNumeroTelefono(String numeroTelefono) { this.numeroTelefono = numeroTelefono; }

    /**
     * Restituisce se ritiro e completato.
     *
     * @return true quando completato
     */
    public boolean isRitirato() { return ritirato; }

    /**
     * Imposta flag completamento ritiro.
     *
     * @param ritirato completamento flag
     */
    public void setRitirato(boolean ritirato) { this.ritirato = ritirato; }

    /**
     * Restituisce collegato annuncio.
     *
     * @return collegato annuncio
     */
    public Annuncio getAnnuncio() { return annuncio; }

    /**
     * Imposta collegato annuncio.
     *
     * @param annuncio collegato annuncio
     */
    public void setAnnuncio(Annuncio annuncio) { this.annuncio = annuncio; }
}
