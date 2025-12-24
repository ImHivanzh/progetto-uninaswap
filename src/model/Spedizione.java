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
     * Crea vuoto spedizione record.
     */
    public Spedizione() {}

    /**
     * Crea spedizione record con fornito campi.
     *
     * @param idSpedizione id spedizione
     * @param indirizzo indirizzo spedizione
     * @param numeroTelefono numero telefono contatto
     * @param dataInvio data spedizione
     * @param dataArrivo data arrivo
     * @param spedito spedito flag
     * @param utente utente riferimento
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
     * Restituisce id spedizione.
     *
     * @return id spedizione
     */
    public int getIdSpedizione() { return idSpedizione; }

    /**
     * Imposta id spedizione.
     *
     * @param idSpedizione id spedizione
     */
    public void setIdSpedizione(int idSpedizione) { this.idSpedizione = idSpedizione; }

    /**
     * Restituisce indirizzo spedizione.
     *
     * @return indirizzo spedizione
     */
    public String getIndirizzo() { return indirizzo; }

    /**
     * Imposta indirizzo spedizione.
     *
     * @param indirizzo indirizzo spedizione
     */
    public void setIndirizzo(String indirizzo) { this.indirizzo = indirizzo; }

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
     * Restituisce data spedizione.
     *
     * @return data spedizione
     */
    public Date getDataInvio() { return dataInvio; }

    /**
     * Imposta data spedizione.
     *
     * @param dataInvio data spedizione
     */
    public void setDataInvio(Date dataInvio) { this.dataInvio = dataInvio; }

    /**
     * Restituisce data arrivo.
     *
     * @return data arrivo
     */
    public Date getDataArrivo() { return dataArrivo; }

    /**
     * Imposta data arrivo.
     *
     * @param dataArrivo data arrivo
     */
    public void setDataArrivo(Date dataArrivo) { this.dataArrivo = dataArrivo; }

    /**
     * Restituisce se spedizione e stata inviate.
     *
     * @return true quando inviate
     */
    public boolean isSpedito() { return spedito; }

    /**
     * Imposta spedito flag.
     *
     * @param spedito spedito flag
     */
    public void setSpedito(boolean spedito) { this.spedito = spedito; }

    /**
     * Restituisce collegato annuncio.
     *
     * @return collegato annuncio
     */
    public Annuncio getAnnuncio() { return annuncio; }

    /**
     * Imposta collegato annuncio.
     *
     * @param utente utente riferimento
     */
    public void setAnnuncio(Utente utente) { this.annuncio = annuncio; }

}
