package model;

import model.enums.StatoProposta;

/**
 * Riepilogo proposta per visualizzazione.
 *
 * @param annuncio annuncio
 * @param utenteCoinvolto utente coinvolto
 * @param dettaglio dettaglio proposta
 * @param accettata flag accettata
 * @param inattesa flag in attesa
 * @param immagine immagine proposta
 */
public record PropostaRiepilogo(Annuncio annuncio, Utente utenteCoinvolto,
                                String dettaglio, boolean accettata, boolean inattesa, byte[] immagine) {

  /**
   * Restituisce leggibile stato stringa.
   *
   * @return testo stato
   */
  public String getStatoTestuale() {
    return StatoProposta.fromFlags(accettata, inattesa).getDescrizione();
  }
}
