/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ptscharts;

import ptsutils.PtsSymbolInfo;
import java.awt.BorderLayout;
import java.awt.Frame;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.joda.time.DateTime;
//import petrasys.instruments.Instrument;

/**
 *
 * @author rickcharon
 */
public class PtsChartFrame extends JFrame {

  PtsChart chart;
  PtsChartPanel chartPanel;
  JPanel topPanel;
  PtsSymbolInfo symbolInfo;
  //private Crosshair crosshair1;
  //private Crosshair crosshair2;
  java.awt.Toolkit toolkit = this.getToolkit();
  java.awt.Image appIcon = toolkit.createImage("/share/icons/MultiLine.png");

  public PtsChart getChart() {
    return chart;
  }

  public PtsSymbolInfo getSymbolInfo() {
    return symbolInfo;
  }

  public PtsChartFrame(PtsSymbolInfo symInfo, DateTime bdt, DateTime edt, int compressionFactorIn) {
    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
//    addWindowListener(new java.awt.event.WindowAdapter() {
//      public void windowClosing(java.awt.event.WindowEvent evt) {
//        formWindowClosing(evt);
//      }
//    });
    symbolInfo = symInfo;
    chart = new PtsChart(this, new CombinedDomainXYPlot(new DateAxis("DateTime")), symInfo, bdt, edt, compressionFactorIn);
    setupChartWindow(chart);
    setIconImage(appIcon);
    String title1 =  symInfo.fullName + "-" + compressionFactorIn + "min "
            + PtsDateOps.prettyString(bdt.toDate())
            + " - " + PtsDateOps.prettyString(edt.toDate());
    setTitle(title1);
    setExtendedState(Frame.MAXIMIZED_BOTH);
  }

  private void formWindowClosing(java.awt.event.WindowEvent evt) {
    dispose();
    //int i = 3;
  }

  public void setupChartWindow(PtsChart chart) {
    chartPanel = new PtsChartPanel(chart, this);
    chart.setChartPanel(chartPanel);
    chartPanel.setMouseWheelEnabled(true);
    topPanel = new JPanel(new BorderLayout());
    topPanel.add(chartPanel);
    setContentPane(topPanel);
    pack();
    
  }

  



//  public static void main(String[] args) {
//    Instrument inst = new Instrument("PLAY", WIDTH, WIDTH, SOMEBITS, null, null);
//    PtsChartFrame f = new PtsChartFrame(inst);
//    JFrame sf = new ADXChartFrame();
//    sf.setTitle("2nd Chart");
//    sf.setExtendedState(Frame.MAXIMIZED_BOTH);
//    sf.setVisible(true);
//  }
}
