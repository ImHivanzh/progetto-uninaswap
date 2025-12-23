package model;

public record PropostaRiepilogo(int idAnnuncio, String titoloAnnuncio, String tipoAnnuncio, String utenteCoinvolto,
                                String dettaglio, boolean accettata, boolean inattesa) {

  /**
   * Returns a human-readable status string.
   *
   * @return status text
   */
  public String getStatoTestuale() {
    if (accettata) {
      return "Accettata";
    }
    if (inattesa) {
      return "In attesa";
    }
    return "Rifiutato";
  }
}
