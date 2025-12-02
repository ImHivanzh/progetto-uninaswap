package mdoel;

public class Recensione {
    private int idRecensione;
    private int voto;
    private String descrizione;
    private int idUtente;
    private int idUtenteRecensito;

    //costruttore di defualt
    public Recensione() {
    }

    //costruttore
    public Recensione(int idRecensione, int voto, String descrizione, int idUtente, int idUtenteRecensito) {
        this.idRecensione = idRecensione;
        this.voto = voto;
        this.descrizione = descrizione;
        this.idUtente = idUtente;
        this.idUtenteRecensito = idUtenteRecensito;
    }

    // metodi getter e setter

    // idRecensione
    public int getIdRecensione() {
        return idRecensione;
    }

    public void setIdRecensione(int idRecensione) {
        this.idRecensione = idRecensione;
    }

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
    public int getIdUtenteRecensito() {
        return idUtenteRecensito;
    }

    public void setIdUtenteRecensito(int idUtenteRecensito) {
        this.idUtenteRecensito = idUtenteRecensito;
    }
}