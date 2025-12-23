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
     * Creates an empty pickup record.
     */
    public Ritiro() {
    }

    /**
     * Creates a pickup record with the provided fields.
     *
     * @param idRitiro pickup id
     * @param sede pickup location
     * @param orario pickup time
     * @param data pickup date
     * @param numeroTelefono contact phone number
     * @param ritirato pickup completion flag
     * @param annuncio related listing
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
     * Returns the pickup id.
     *
     * @return pickup id
     */
    public int getIdRitiro() { return idRitiro; }

    /**
     * Sets the pickup id.
     *
     * @param idRitiro pickup id
     */
    public void setIdRitiro(int idRitiro) { this.idRitiro = idRitiro; }

    /**
     * Returns the pickup location.
     *
     * @return pickup location
     */
    public String getSede() { return sede; }

    /**
     * Sets the pickup location.
     *
     * @param sede pickup location
     */
    public void setSede(String sede) { this.sede = sede; }

    /**
     * Returns the pickup time.
     *
     * @return pickup time
     */
    public String getOrario() { return orario; }

    /**
     * Sets the pickup time.
     *
     * @param orario pickup time
     */
    public void setOrario(String orario) { this.orario = orario; }

    /**
     * Returns the pickup date.
     *
     * @return pickup date
     */
    public Date getData() { return data; }

    /**
     * Sets the pickup date.
     *
     * @param data pickup date
     */
    public void setData(Date data) { this.data = data; }

    /**
     * Returns the contact phone number.
     *
     * @return contact phone number
     */
    public String getNumeroTelefono() { return numeroTelefono; }

    /**
     * Sets the contact phone number.
     *
     * @param numeroTelefono contact phone number
     */
    public void setNumeroTelefono(String numeroTelefono) { this.numeroTelefono = numeroTelefono; }

    /**
     * Returns whether the pickup is completed.
     *
     * @return true when completed
     */
    public boolean isRitirato() { return ritirato; }

    /**
     * Sets the pickup completion flag.
     *
     * @param ritirato completion flag
     */
    public void setRitirato(boolean ritirato) { this.ritirato = ritirato; }

    /**
     * Returns the related listing.
     *
     * @return related listing
     */
    public Annuncio getAnnuncio() { return annuncio; }

    /**
     * Sets the related listing.
     *
     * @param annuncio related listing
     */
    public void setAnnuncio(Annuncio annuncio) { this.annuncio = annuncio; }
}
