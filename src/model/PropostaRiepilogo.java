package model;

/**
 * Riepilogo proposta per visualizzazione.
 *
 * @param idAnnuncio id annuncio
 * @param titoloAnnuncio titolo annuncio
 * @param tipoAnnuncio tipo annuncio
 * @param utenteCoinvolto utente coinvolto
 * @param dettaglio dettaglio proposta
 * @param accettata flag accettata
 * @param inattesa flag in attesa
 * @param immagine immagine proposta
 */
public record PropostaRiepilogo(int idAnnuncio, String titoloAnnuncio, String tipoAnnuncio, String utenteCoinvolto,
                                String dettaglio, boolean accettata, boolean inattesa, byte[] immagine) {

  /**
   * Restituisce leggibile stato stringa.
   *
   * @return testo stato
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
