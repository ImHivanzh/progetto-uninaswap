package model;

public class Rititro{
    private int idRitiro;
    private String sede;
    private String orario;
    private Date data;
    private String numeroTelefono;
    private boolean ritirato;
    private int idAnnuncio;

    //costruttore di default
    public Ritiro() {
    }

    //costruttore
    public Ritiro(int idRitiro, String sede, String orario, Date data, String numeroTelefono, boolean ritirato, int idAnnuncio) {
        this.idRitiro = idRitiro;
        this.sede = sede;
        this.orario = orario;
        this.data = data;
        this.numeroTelefono = numeroTelefono;
        this.ritirato = ritirato;
        this.idAnnuncio = idAnnuncio;
    }

    // metodi getter e setter
    // idRitiro
    public int getIdRitiro() { return idRitiro; }

    public void setIdRitiro(int idRitiro) { this.idRitiro = idRitiro; }

    //sede
    public String getSede() { return sede; }

    public void setSede(String sede) { this.sede = sede; }

    // orario
    public String getOrario() { return orario; }

    public void setOrario(String orario) { this.orario = orario; }

    // data
    public Date getData() { return data; }

    public void setData(Date data) { this.data = data; }

    // numeroTelefono
    public String getNumeroTelefono() { return numeroTelefono; }

    public void setNumeroTelefono(String numeroTelefono) { this.numeroTelefono = numeroTelefono; }

    // ritirato (boolean -> isRitirato)
    public boolean isRitirato() { return ritirato; }

    public void setRitirato(boolean ritirato) { this.ritirato = ritirato; }

    // idAnnuncio
    public int getIdAnnuncio() { return idAnnuncio; }

    public void setIdAnnuncio(int idAnnuncio) { this.idAnnuncio = idAnnuncio; }
}