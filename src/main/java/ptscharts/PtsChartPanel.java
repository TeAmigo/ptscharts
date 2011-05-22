/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ptscharts;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
//import org.jfree.chart.ChartPanel;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.DateTickUnitType;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.Crosshair;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.Range;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.ExtensionFileFilter;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.RectangleEdge;

/**
 *
 * @author rickcharon
 */
public class PtsChartPanel extends ChartPanel implements KeyListener, ChartMouseListener {

  PtsChart chart;
  PtsChartFrame frame;
  PtsCrosshair crosshairDomain;
  PtsCrosshair crosshairRange;
  Crosshair crosshairRange2;
  private PtsCrosshairOverlay crosshair1 = null;
  boolean[] keys = new boolean[525];
  private double rangeAdjustor = 0.025;
  PtsCrosshairsOpsFrame crosshairsOpsFrame;
  PtsMouseKeyboardController controller = null;

  



  public double getRangeAdjustor() {
    return rangeAdjustor;
  }

  public void setRangeAdjustor(double rangeAdjustor) {
    this.rangeAdjustor = rangeAdjustor;
  }

  public PtsChartPanel(JFreeChart chart, int width, int height, int minimumDrawWidth, int minimumDrawHeight, int maximumDrawWidth, int maximumDrawHeight, boolean useBuffer, boolean properties, boolean copy, boolean save, boolean print, boolean zoom, boolean tooltips) {
    super(chart, width, height, minimumDrawWidth, minimumDrawHeight, maximumDrawWidth, maximumDrawHeight, useBuffer, properties, copy, save, print, zoom, tooltips);
  }

  public PtsChartPanel(JFreeChart chart, int width, int height, int minimumDrawWidth, int minimumDrawHeight, int maximumDrawWidth, int maximumDrawHeight, boolean useBuffer, boolean properties, boolean save, boolean print, boolean zoom, boolean tooltips) {
    super(chart, width, height, minimumDrawWidth, minimumDrawHeight, maximumDrawWidth, maximumDrawHeight, useBuffer, properties, save, print, zoom, tooltips);
  }

//  public PtsChartPanel(JFreeChart chart, boolean properties, boolean save, boolean print, boolean zoom, boolean tooltips) {
//    super(chart, properties, save, print, zoom, tooltips);
//  }
//
//  public PtsChartPanel(JFreeChart chart, boolean useBuffer) {
//    super(chart, useBuffer);
//  }
  @Override
  public PtsChart getChart() {
    return chart;
  }

  public PtsCrosshairOverlay getCrosshair1() {
    return crosshair1;
  }

  public void setCrosshair1(PtsCrosshairOverlay crosshair1) {
    this.crosshair1 = crosshair1;
  }

  //rpc - NOTE:7/23/10 2:14 PM - Only used by test charts,
  public PtsChartPanel(JFreeChart chart) {
    super(chart);
    setFocusable(true);
    addKeyListener(this);
    this.chart = null;
  }

  //rpc - NOTE:7/20/10 6:50 AM - PtsChartFrame calls this ctor
  public PtsChartPanel(PtsChart chart, PtsChartFrame frameIn) {
    super(chart);
    frame = frameIn;
    this.chart = chart;
    setFocusable(true);
    addKeyListener(this);
    addChartMouseListener(this);
    crosshair1 = new PtsCrosshairOverlay(this);
    addOverlay(crosshair1);
    crosshairsOpsFrame = new PtsCrosshairsOpsFrame(this);
    setDefaultDirectoryForSaveAs(new File("/share/Images"));
    extendPopupMenu();
    controller = new PtsMouseKeyboardController(this, this.chart, keys);
    //setupCrosshairs();
  }

  //rpc - NOTE:8/9/10 1:20 PM - Completely unused, keeping for possibly useful code snippets,
  public void setRangeBoundsForPlots() {

    if (chart.getPlot() instanceof CombinedDomainXYPlot) {
      CombinedDomainXYPlot plot = (CombinedDomainXYPlot) chart.getPlot();
      DateAxis domainAxis = (DateAxis) plot.getDomainAxis();
      DateTickUnitType dtut = domainAxis.getTickUnit().getUnitType(); //DAY?
      DateTickUnit dtu = new DateTickUnit(DateTickUnitType.MINUTE, 1);
      Date dateHi = domainAxis.calculateHighestVisibleTickValue(dtu);
      Date dateLo = domainAxis.calculateLowestVisibleTickValue(dtu);
      List<Plot> plots = (List<Plot>) plot.getSubplots();
      for (Plot p : plots) {
        if (p instanceof XYPlot) {
          TimeSeriesCollection ds = (TimeSeriesCollection) ((XYPlot) p).getDataset();
        }

      }
      double hiVal = PtsTimeSeriesOps.maxVal(null);
      List<XYPlot> subplots = plot.getSubplots();
      for (int i = 0; i < subplots.size(); i++) {
        XYPlot subplot = (XYPlot) subplots.get(i);
        //rpc - WORKING HERE:4/8/10 2:55 PM - Need to get the max and min value between dateHi and dateLo
        //rpc - WORKING HERE:4/8/10 2:56 PM - and set the range axis to those values
      }
    }
  }

  public void keyPressed(KeyEvent e) {
    controller.keyPressed(e);
  }

  public void keyPressed1(KeyEvent e) {
    //System.out.println("Key Pressed Code: " + e.getKeyCode());
    int k = e.getKeyCode();
    keys[e.getKeyCode()] = true;

    if (e.isAltDown()) {
      if (e.getKeyCode() == KeyEvent.VK_A) {
        //System.out.println("Control A");
        restoreAutoBounds();
      } else if (e.getKeyCode() == KeyEvent.VK_R) {
        //System.out.println("Control R");
        //restoreAutoRangeBounds();
        //rpc - WORKING HERE:4/4/10 11:30 AM - This doesn't seem to do what is desired
        // for all plots, just one,
        //setRangeBoundsForPlots();
      } else if (e.getKeyCode() == KeyEvent.VK_M) {
        chart.openControlFrame(); // Chart Menu
      } else if (e.getKeyCode() == KeyEvent.VK_C) {
        clearKeys();
      } else if (keys[KeyEvent.VK_T]) {     // Invoke the Trade Window,
        keys[KeyEvent.VK_T] = false; //necessary because we loose the focus, so keyReleased isn't called,
        //toFront() doesn't work, so need to diddle Visible
//        PtsChart.getInstrument().getTraderFrame().setVisible(false);
//        PtsChart.getInstrument().getTraderFrame().setLocationToBottom();
//        PtsChart.getInstrument().getTraderFrame().setVisible(true);

        //PtsChart.getInstrument().getTraderFrame().toFront();
      }
    }
  }

  public void clearKeys() {
    controller.clearKeys();
  }

  public void clearKeys1() {
    for (int i = 0; i < keys.length; i++) {
      keys[i] = false;
    }
  }

  public void keyReleased(KeyEvent e) {
    controller.keyReleased(e);
  }

  public void keyReleased1(KeyEvent e) {
    //System.out.println("Key Released: " + e.getKeyChar());
    int k = e.getKeyCode();
    keys[e.getKeyCode()] = false;

    if (k == KeyEvent.VK_LEFT || k == KeyEvent.VK_RIGHT) {
      CombinedDomainXYPlot combinedPlot = (CombinedDomainXYPlot) chart.getPlot();
      XYPlot pricePlot = (XYPlot) combinedPlot.getSubplots().get(0);
      DateAxis domainAxis = (DateAxis) pricePlot.getDomainAxis();
      Date minD = domainAxis.getMinimumDate();
      Date maxD = domainAxis.getMaximumDate();
      Calendar cal = Calendar.getInstance();
      int minsInBar = chart.getCompressionFactor();
      int direction = 1; // 1 goes right, -1 goes left
      if (e.getKeyCode() == KeyEvent.VK_LEFT) {
        direction = -1;
      }
      cal.setTime(minD);
      cal.add(Calendar.MINUTE, (direction * minsInBar));
      minD = cal.getTime();
      cal.setTime(maxD);
      cal.add(Calendar.MINUTE, (direction * minsInBar));
      maxD = cal.getTime();
      domainAxis.setMinimumDate(minD);
      domainAxis.setMaximumDate(maxD);
    } else if (k == KeyEvent.VK_UP || k == KeyEvent.VK_DOWN) {
      CombinedDomainXYPlot combinedPlot = (CombinedDomainXYPlot) chart.getPlot();
      XYPlot pricePlot = (XYPlot) combinedPlot.getSubplots().get(0);
      ValueAxis rangeAxis = pricePlot.getRangeAxis();
      int direction = 1; // 1 goes up, -1 goes down
      if (e.getKeyCode() == KeyEvent.VK_UP) {
        direction = -1;
      }
      Range rg = rangeAxis.getRange();
      double length = rg.getLength();
      Range adjusted = new Range(
              rg.getLowerBound() + (direction * (rangeAdjustor * length)),
              rg.getUpperBound() + (direction * (rangeAdjustor * length)));
      rangeAxis.setRange(adjusted);
    }
  }

  public void keyTyped(KeyEvent e) {
    controller.keyTyped(e);
  }

  public void keyTyped1(KeyEvent e) {
    //System.out.println("Key Typed: " + e.getKeyChar());
  }

  public void setupHorizontalCrosshair(double subChartY) {
    crosshairRange = new PtsCrosshair(subChartY);
    PtsSubPlotHorizontalCrosshairLabelGenerator slg = new PtsSubPlotHorizontalCrosshairLabelGenerator(subChartY);
    Font fp = new Font("Tahoma", Font.BOLD, 14);
    crosshairRange.setLabelFont(fp);
    crosshairRange.setLabelBackgroundPaint(Color.white);
    crosshairRange.setLabelAnchor(RectangleAnchor.TOP_RIGHT);
    crosshairRange.setLabelGenerator(slg);
    crosshairRange.setLabelVisible(true);
    crosshairRange.setPaint(Color.BLACK);
    crosshair1.addRangeCrosshair(crosshairRange);


  }

  public void setupHorizontalPrefixedCrosshairHelper(double subChartY, String prefix) {
    PtsSubPlotHorizontalCrosshairLabelGenerator slg = new PtsSubPlotHorizontalCrosshairLabelGenerator(subChartY);
    slg.setPrefix(prefix);
    Font fp = new Font("Tahoma", Font.BOLD, 14);
    crosshairRange.setLabelFont(fp);
    crosshairRange.setLabelBackgroundPaint(Color.white);
    crosshairRange.setLabelAnchor(RectangleAnchor.TOP_RIGHT);
    crosshairRange.setLabelGenerator(slg);
    crosshairRange.setLabelVisible(true);
    crosshairRange.setPaint(Color.BLACK);
    crosshair1.addRangeCrosshair(crosshairRange);
  }

  public void setupHorizontalPrefixedCrosshair(double subChartY, String prefix) {
    crosshairRange = new PtsCrosshair(subChartY);
    setupHorizontalPrefixedCrosshairHelper(subChartY, prefix);
  }

  public void setupStopLossCrosshair(double subChartY, String prefix) {
    List rangeHairs = crosshair1.getRangeCrosshairs();
    for (int i = 0; i < rangeHairs.size(); i++) {
      if (((PtsCrosshair) rangeHairs.get(i)).isStopLossCrosshair()) {
        crosshair1.removeRangeCrosshair((PtsCrosshair) rangeHairs.get(i));
      }
    }
    crosshairRange = new PtsCrosshair(subChartY);
    crosshairRange.setStopLossCrosshair(true);
    setupHorizontalPrefixedCrosshairHelper(subChartY, prefix);
  }

  public void setupProfitStopCrosshair(double subChartY, String prefix) {
    List rangeHairs = crosshair1.getRangeCrosshairs();
    for (int i = 0; i < rangeHairs.size(); i++) {
      if (((PtsCrosshair) rangeHairs.get(i)).isProfitStopCrosshair()) {
        crosshair1.removeRangeCrosshair((PtsCrosshair) rangeHairs.get(i));
      }
    }
    crosshairRange = new PtsCrosshair(subChartY);
    crosshairRange.setProfitStopCrosshair(true);
    setupHorizontalPrefixedCrosshairHelper(subChartY, prefix);
  }

  public void setupBuyCrosshair(PtsCrosshair vertCrosshair, double subChartY, String prefix) {
    List rangeHairs = crosshair1.getRangeCrosshairs();
    for (int i = 0; i < rangeHairs.size(); i++) {
      if (((PtsCrosshair) rangeHairs.get(i)).isBuyCrosshair()) {
        crosshair1.removeDomainCrosshair(((PtsCrosshair)rangeHairs.get(i)).getVertHairPartner());
        crosshair1.removeRangeCrosshair((PtsCrosshair) rangeHairs.get(i));
      }
    }
    crosshairRange = new PtsCrosshair(subChartY);
    crosshairRange.setBuyCrosshair(true);
    crosshairRange.setVertHairPartner(vertCrosshair);
    setupHorizontalPrefixedCrosshairHelper(subChartY, prefix);
  }

  public void setupSellCrosshair(PtsCrosshair vertCrosshair, double subChartY, String prefix) {
    List rangeHairs = crosshair1.getRangeCrosshairs();
    for (int i = 0; i < rangeHairs.size(); i++) {
      if (((PtsCrosshair) rangeHairs.get(i)).isSellCrosshair()) {
        crosshair1.removeDomainCrosshair(((PtsCrosshair)rangeHairs.get(i)).getVertHairPartner());
        crosshair1.removeRangeCrosshair((PtsCrosshair) rangeHairs.get(i));
      }
    }
    crosshairRange = new PtsCrosshair(subChartY);
    crosshairRange.setSellCrosshair(true);
    crosshairRange.setVertHairPartner(vertCrosshair);
    setupHorizontalPrefixedCrosshairHelper(subChartY, prefix);
  }

  public PtsCrosshair setupVerticalCrosshair(double chartX) {
    PtsCrosshair crosshairDomainLocal = new PtsCrosshair(chartX);
    PtsVerticalCrosshairLabelGenerator slg = new PtsVerticalCrosshairLabelGenerator(chartX);
    Font fp = new Font("Tahoma", Font.BOLD, 14);
    crosshairDomainLocal.setLabelFont(fp);
    crosshairDomainLocal.setLabelBackgroundPaint(Color.white);
    crosshairDomainLocal.setLabelAnchor(RectangleAnchor.BOTTOM_RIGHT);
    crosshairDomainLocal.setLabelGenerator(slg);
    crosshairDomainLocal.setLabelVisible(true);
    crosshairDomainLocal.setPaint(Color.BLACK);
    crosshair1.addDomainCrosshair(crosshairDomainLocal);
    return crosshairDomainLocal;


  }

  public void setupCrosshairs(double chartX, double chartY, double subChartY, XYPlot plot) {
    setupHorizontalCrosshair(subChartY);
    setupVerticalCrosshair(
            chartX);


  }

  public void chartMouseClicked(ChartMouseEvent event) {
    controller.chartMouseClicked(event);
  }

  

  public void chartMouseMoved(ChartMouseEvent event) {
    int j = 3;
  }
  static int repaintCount = 0;

  @Override
  public void repaint() {
    super.repaint();
    if (chart != null) {
      CombinedDomainXYPlot combinedPlot = (CombinedDomainXYPlot) chart.getPlot();
      DateAxis dAxis = (DateAxis) combinedPlot.getDomainAxis();
      double lower = dAxis.getLowerBound();
      double higher = dAxis.getUpperBound();
      Date dl = new Date((long) lower);
      Date du = new Date((long) higher);
      //System.out.println("repaint " + ++repaintCount + " lowerD: " + dl + " upperD: " + du);
      //String title = frame.getTitle();
//      frame.setTitle(PtsChart.getInstrument().getChartNameSansDates() + DateOps.prettyString(dl)
//              + " - " + DateOps.prettyString(du));
      frame.setTitle(chart.getSymbolInfo().getFullName() + "-" + chart.getCompressionFactor() + "min"
              + "-" + PtsDateOps.fileFormatString(dl) + "-to-" + PtsDateOps.fileFormatString(du));
    }
  }

  public void resetOverlays() {
    removeOverlay(crosshair1);
    crosshair1 = new PtsCrosshairOverlay(this);
    addOverlay(crosshair1);
  }
  public static final String REMOVEOVERLAYS_COMMAND = "REMOVEOVERLAYS";
  public static final String CROSSHAIROPS_COMMAND = "CROSSHAIROPS";

  public void extendPopupMenu() {
    JPopupMenu popMenu = getPopupMenu();
    JMenuItem propertiesItem = new JMenuItem("Remove Overlays (LeftClick-e)");
    propertiesItem.setActionCommand(REMOVEOVERLAYS_COMMAND);
    propertiesItem.addActionListener(this);
    popMenu.insert(propertiesItem, 0);
    propertiesItem = new JMenuItem("View and Manipulate Crosshairs...");
    propertiesItem.setActionCommand(CROSSHAIROPS_COMMAND);
    propertiesItem.addActionListener(this);
    popMenu.insert(propertiesItem, 0);
  }

  @Override
  public void actionPerformed(ActionEvent event) {
    super.actionPerformed(event);
    String command = event.getActionCommand();
    if (command.equals(REMOVEOVERLAYS_COMMAND)) {
      resetOverlays();
    } else if (command.equals(CROSSHAIROPS_COMMAND)) {
      crosshairsOpsFrame.setTitle(frame.getTitle());
      crosshairsOpsFrame.refreshCrosshairLists();
      crosshairsOpsFrame.setVisible(true);
    }
  }

  @Override
  public void doSaveAs() throws IOException {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setCurrentDirectory(new File("/share/Images"));
    ExtensionFileFilter filter = new ExtensionFileFilter(
            localizationResources.getString("PNG_Image_Files"), ".png");
    fileChooser.addChoosableFileFilter(filter);
    //System.out.println(frame.getTitle());
    fileChooser.setSelectedFile(new File(frame.getTitle()));
    int option = fileChooser.showSaveDialog(this);
    if (option == JFileChooser.APPROVE_OPTION) {
      String filename = fileChooser.getSelectedFile().getPath();
      if (isEnforceFileExtensions()) {
        if (!filename.endsWith(".png")) {
          filename = filename + ".png";
        }
      }
      ChartUtilities.saveChartAsPNG(new File(filename), this.chart,
              getWidth(), getHeight());
    }
  }
}
