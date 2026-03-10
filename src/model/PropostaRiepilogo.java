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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PropostaRiepilogo that = (PropostaRiepilogo) o;
    return accettata == that.accettata &&
           inattesa == that.inattesa &&
           java.util.Objects.equals(annuncio, that.annuncio) &&
           java.util.Objects.equals(utenteCoinvolto, that.utenteCoinvolto) &&
           java.util.Objects.equals(dettaglio, that.dettaglio) &&
           java.util.Arrays.equals(immagine, that.immagine);
  }

  @Override
  public int hashCode() {
    return java.util.Objects.hash(annuncio, utenteCoinvolto, dettaglio, accettata, inattesa,
                                   java.util.Arrays.hashCode(immagine));
  }

  @Override
  public String toString() {
    return "PropostaRiepilogo{" +
           "annuncio=" + annuncio +
           ", utenteCoinvolto=" + utenteCoinvolto +
           ", dettaglio='" + dettaglio + '\'' +
           ", accettata=" + accettata +
           ", inattesa=" + inattesa +
           ", immagine=" + (immagine != null ? immagine.length + " bytes" : "null") +
           '}';
  }

  /**
   * Restituisce leggibile stato stringa.
   *
   * @return testo stato
   */
  public String getStatoTestuale() {
    return StatoProposta.fromFlags(accettata, inattesa).getDescrizione();
  }
}
