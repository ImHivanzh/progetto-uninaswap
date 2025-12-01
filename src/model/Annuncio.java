package model;

public class Annuncio {
    private int idAnnuncio;
    private String titolo;
    private String descrizione;
    private boolean stato; // TRUE = disponibile
    private float prezzo;
    private TipoAnnuncio tipoAnnuncio;
    private int idUtente;
}