package model;

import java.util.Date;

public class Spedizione {
    private int idSpedizione;
    private String indirizzo;
    private String numeroTelefono;
    private Date dataInvio;
    private Date dataArrivo;    
    private boolean spedito;
    private int idAnnuncio;

    // costruttore di default
    public Spedizione() {}

    //costruttore
    public Spedizione(int idSpedizione, String indirizzo, String numeroTelefono, Date dataInvio, Date dataArrivo, boolean spedito, int idAnnuncio) {
        this.idSpedizione = idSpedizione;
        this.indirizzo = indirizzo;
        this.numeroTelefono = numeroTelefono;
        this.dataInvio = dataInvio;
        this.dataArrivo = dataArrivo;
        this.spedito = spedito;
        this.idAnnuncio = idAnnuncio;
    }



    //metodi getter e setter

    // idSpedizione
    public int getIdSpedizione() { return idSpedizione; }

    public void setIdSpedizione(int idSpedizione) { this.idSpedizione = idSpedizione; }

    // indirizzo
    public String getIndirizzo() { return indirizzo; }

    public void setIndirizzo(String indirizzo) { this.indirizzo = indirizzo; }

    // numeroTelefono
    public String getNumeroTelefono() { return numeroTelefono; }

    public void setNumeroTelefono(String numeroTelefono) { this.numeroTelefono = numeroTelefono; }

    // dataInvio
    public Date getDataInvio() {return dataInvio;}

    public void setDataInvio(Date dataInvio) {this.dataInvio = dataInvio;}

    // dataArrivo
    public Date getDataArrivo() {return dataArrivo;}

    public void setDataArrivo(Date dataArrivo) {this.dataArrivo = dataArrivo;}

    // spedito (boolean -> isSpedito)
    public boolean isSpedito() {return spedito;}

    public void setSpedito(boolean spedito) {this.spedito = spedito;}

    // idAnnuncio
    public int getIdAnnuncio() {return idAnnuncio;}

    public void setIdAnnuncio(int idAnnuncio) {this.idAnnuncio = idAnnuncio;}

}