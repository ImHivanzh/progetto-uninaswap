package model;

public class Recensione {
    private int voto;
    private String descrizione;
    private int idUtente; // ID dell'utente che scrive la recensione
    private int idUtenteRecensito; // ID dell'utente che riceve la recensione

    // Costruttore di default
    public Recensione() {
    }

    // Costruttore completo
    public Recensione(int voto, String descrizione, int idUtente, int idUtenteRecensito) {
        this.voto = voto;
        this.descrizione = descrizione;
        this.idUtente = idUtente;
        this.idUtenteRecensito = idUtenteRecensito;
    }

    // --- Metodi Getter e Setter ---

    // Voto
    public int getVoto() {
        return voto;
    }

    public void setVoto(int voto) {
        this.voto = voto;
    }

    // Descrizione
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

    // idUtenteRecensito (Destinatario)
    // Rinominato per coerenza con il DAO che chiama getIdUtenteRecensito()
    public int getIdUtenteRecensito() {
        return idUtenteRecensito;
    }

    public void setIdUtenteRecensito(int idUtenteRecensito) {
        this.idUtenteRecensito = idUtenteRecensito;
    }
}