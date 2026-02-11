package model;

public record ReportProposte(
        int totaleVendita, int accettateVendita,
        int totaleScambio, int accettateScambio,
        int totaleRegalo, int accettateRegalo,
        double valoreMinimoVendita, double valoreMassimoVendita, double valoreMedioVendita) {
}
