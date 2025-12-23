package model;

import java.util.Date;

public class Spedizione {
    private int idSpedizione;
    private String indirizzo;
    private String numeroTelefono;
    private Date dataInvio;
    private Date dataArrivo;
    private boolean spedito;
    private Annuncio annuncio;

    /**
     * Creates an empty shipment record.
     */
    public Spedizione() {}

    /**
     * Creates a shipment record with the provided fields.
     *
     * @param idSpedizione shipment id
     * @param indirizzo shipping address
     * @param numeroTelefono contact phone number
     * @param dataInvio shipping date
     * @param dataArrivo arrival date
     * @param spedito shipped flag
     * @param utente user reference
     */
    public Spedizione(int idSpedizione, String indirizzo, String numeroTelefono, Date dataInvio,
                      Date dataArrivo, boolean spedito, Utente utente) {
        this.idSpedizione = idSpedizione;
        this.indirizzo = indirizzo;
        this.numeroTelefono = numeroTelefono;
        this.dataInvio = dataInvio;
        this.dataArrivo = dataArrivo;
        this.spedito = spedito;
        this.annuncio = annuncio;
    }

    /**
     * Returns the shipment id.
     *
     * @return shipment id
     */
    public int getIdSpedizione() { return idSpedizione; }

    /**
     * Sets the shipment id.
     *
     * @param idSpedizione shipment id
     */
    public void setIdSpedizione(int idSpedizione) { this.idSpedizione = idSpedizione; }

    /**
     * Returns the shipping address.
     *
     * @return shipping address
     */
    public String getIndirizzo() { return indirizzo; }

    /**
     * Sets the shipping address.
     *
     * @param indirizzo shipping address
     */
    public void setIndirizzo(String indirizzo) { this.indirizzo = indirizzo; }

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
     * Returns the shipping date.
     *
     * @return shipping date
     */
    public Date getDataInvio() { return dataInvio; }

    /**
     * Sets the shipping date.
     *
     * @param dataInvio shipping date
     */
    public void setDataInvio(Date dataInvio) { this.dataInvio = dataInvio; }

    /**
     * Returns the arrival date.
     *
     * @return arrival date
     */
    public Date getDataArrivo() { return dataArrivo; }

    /**
     * Sets the arrival date.
     *
     * @param dataArrivo arrival date
     */
    public void setDataArrivo(Date dataArrivo) { this.dataArrivo = dataArrivo; }

    /**
     * Returns whether the shipment has been sent.
     *
     * @return true when sent
     */
    public boolean isSpedito() { return spedito; }

    /**
     * Sets the shipped flag.
     *
     * @param spedito shipped flag
     */
    public void setSpedito(boolean spedito) { this.spedito = spedito; }

    /**
     * Returns the related listing.
     *
     * @return related listing
     */
    public Annuncio getAnnuncio() { return annuncio; }

    /**
     * Sets the related listing.
     *
     * @param utente user reference
     */
    public void setAnnuncio(Utente utente) { this.annuncio = annuncio; }

}
