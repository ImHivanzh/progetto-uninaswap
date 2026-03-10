package controller;

import dao.PropostaDAO;
import exception.DatabaseException;
import gui.ReportProposteDialog;
import java.awt.Dimension;
import javax.swing.JOptionPane;
import model.ReportProposte;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import utils.SessionManager;

/**
 * Controller per report proposte inviate.
 */
public class ReportProposteController {

  private static final String ETICHETTA_TOTALE = "Totale";
  private static final String ETICHETTA_ACCETTATE = "Accettate";

  /**
   * Vista dialogo report.
   */
  private final ReportProposteDialog view;
  /**
   * DAO proposte.
   */
  private final PropostaDAO propostaDAO;

  /**
   * Crea controller per report proposte.
   *
   * @param view dialogo report
   */
  public ReportProposteController(ReportProposteDialog view) {
    this.view = view;
    PropostaDAO dao = null;
    try {
      dao = new PropostaDAO();
    } catch (DatabaseException _) {
      JOptionPane.showMessageDialog(
              view,
              "Errore di connessione al database",
              "Errore",
              JOptionPane.ERROR_MESSAGE
      );
    }
    this.propostaDAO = dao;
    loadReportData();
  }

  private void loadReportData() {
    if (propostaDAO == null) {
      return;
    }
    try {
      int idUtente = SessionManager.getInstance().getUtente().getIdUtente();
      ReportProposte report = propostaDAO.getReportProposte(idUtente);

      if (report != null) {
        view.setTotaleVendita(report.totaleVendita());
        view.setAccettateVendita(report.accettateVendita());
        view.setValoreMinimo(report.valoreMinimoVendita());
        view.setValoreMassimo(report.valoreMassimoVendita());
        view.setValoreMedio(report.valoreMedioVendita());
        view.setTotaleScambio(report.totaleScambio());
        view.setAccettateScambio(report.accettateScambio());
        view.setTotaleRegalo(report.totaleRegalo());
        view.setAccettateRegalo(report.accettateRegalo());

        createChart(report);
      }
    } catch (DatabaseException e) {
      JOptionPane.showMessageDialog(
              view,
              "Errore nel caricamento del report: " + e.getMessage(),
              "Errore",
              JOptionPane.ERROR_MESSAGE
      );
    }
  }

  private void createChart(ReportProposte report) {
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    dataset.addValue(report.totaleVendita(), ETICHETTA_TOTALE, "Vendita");
    dataset.addValue(report.accettateVendita(), ETICHETTA_ACCETTATE, "Vendita");
    dataset.addValue(report.totaleScambio(), ETICHETTA_TOTALE, "Scambio");
    dataset.addValue(report.accettateScambio(), ETICHETTA_ACCETTATE, "Scambio");
    dataset.addValue(report.totaleRegalo(), ETICHETTA_TOTALE, "Regalo");
    dataset.addValue(report.accettateRegalo(), ETICHETTA_ACCETTATE, "Regalo");

    JFreeChart barChart = ChartFactory.createBarChart(
            "Riepilogo Proposte Inviate",
            "Tipo Proposta",
            "Numero",
            dataset,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
    );

    ChartPanel chartPanel = new ChartPanel(barChart);
    chartPanel.setPreferredSize(new Dimension(560, 367));
    view.setChart(chartPanel);
  }
}
