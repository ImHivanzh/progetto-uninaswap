package controller;

import dao.PropostaDAO;
import exception.DatabaseException;
import gui.ReportProposteDialog;
import model.ReportProposte;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import utils.SessionManager;

import javax.swing.*;
import java.awt.Dimension;

/**
 * Controller per report proposte inviate.
 */
public class ReportProposteController {

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
    } catch (DatabaseException e) {
      JOptionPane.showMessageDialog(
              view,
              "Errore di connessione al database: " + e.getMessage(),
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
    dataset.addValue(report.totaleVendita(), "Totale", "Vendita");
    dataset.addValue(report.accettateVendita(), "Accettate", "Vendita");
    dataset.addValue(report.totaleScambio(), "Totale", "Scambio");
    dataset.addValue(report.accettateScambio(), "Accettate", "Scambio");
    dataset.addValue(report.totaleRegalo(), "Totale", "Regalo");
    dataset.addValue(report.accettateRegalo(), "Accettate", "Regalo");

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
