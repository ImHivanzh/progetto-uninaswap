package model;

public class Recensione {
    private int voto;
    private String descrizione;
    private int idUtente;
    private Utente utenteRecensito;

    //costruttore di defualt
    public Recensione() {
    }

    //costruttore
    public Recensione(int voto, String descrizione, int idUtente, Utente utenteRecensito) {
        this.voto = voto;
        this.descrizione = descrizione;
        this.idUtente = idUtente;
        this.utenteRecensito = utenteRecensito;
    }

    // metodi getter e setter

    // voto
    public int getVoto() {
        return voto;
    }

    public void setVoto(int voto) {
        this.voto = voto;
    }

    // descrizione
    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    // idUtente (Recensore)
    public int getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(int idUtente) {
        this.idUtente = idUtente;
    }

    // idUtenteRecensito
    public Utente getUtenteRecensito() {
        return utenteRecensito;
    }

    public void setUtenteRecensito(Utente utenteRecensito) {
        this.utenteRecensito = utenteRecensito;
    }
}