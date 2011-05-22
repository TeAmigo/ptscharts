/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ptscharts;

import java.awt.BasicStroke;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Calendar;
import java.util.Date;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.Range;
import org.jfree.ui.RectangleEdge;

/**
 *
 * @author rickcharon
 */
public class PtsMouseKeyboardController {

  PtsChartPanel panel;
  PtsChart chart;
  boolean[] keys;

  public PtsMouseKeyboardController(PtsChartPanel panel, PtsChart chart, boolean[] keys) {
    this.panel = panel;
    this.chart = chart;
    this.keys = keys;
  }

  public void adjustTopAndBottom() {
    CombinedDomainXYPlot combinedPlot = (CombinedDomainXYPlot) chart.getPlot();
    XYPlot pricePlot = (XYPlot) combinedPlot.getSubplots().get(0);
    NumberAxis rangeAxis = (NumberAxis) pricePlot.getRangeAxis();
    rangeAxis.setAutoRange(true);
    double um = rangeAxis.getUpperMargin();
    rangeAxis.setUpperMargin(0.01); //Because autoAdjustRange() is protected, don't know why
  }

  public void keyPressed(KeyEvent e) {
    //System.out.println("Key Pressed Code: " + e.getKeyCode());
    int k = e.getKeyCode();
    keys[e.getKeyCode()] = true;

    if (e.isAltDown()) {
      if (e.getKeyCode() == KeyEvent.VK_A) {
        //System.out.println("Alt A");
        panel.restoreAutoBounds();
        adjustTopAndBottom();
      } else if (e.getKeyCode() == KeyEvent.VK_R) {
        adjustTopAndBottom();
      } else if (e.getKeyCode() == KeyEvent.VK_E) { // Remove all Crosshairs.
        panel.resetOverlays();
      } else if (e.getKeyCode() == KeyEvent.VK_M) {
        chart.openControlFrame(); // Chart Menu
      } else if (e.getKeyCode() == KeyEvent.VK_C) {
        clearKeys();
      } else if (keys[KeyEvent.VK_T]) {     // Invoke the Trade Window,
        keys[KeyEvent.VK_T] = false; //necessary because we loose the focus, so keyReleased isn't called,
        //toFront() doesn't work, so need to diddle Visible
        //PtsTraderFrame tf = chart.getTraderFrame();
        chart.getTraderFrame().setVisible(false);
        chart.getTraderFrame().setVisible(true);
        chart.getTraderFrame().setLocationToBottom();
      }
    }
  }

  public void clearKeys() {
    for (int i = 0; i < keys.length; i++) {
      keys[i] = false;
    }
  }

  public void keyReleased(KeyEvent e) {
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
      int direction = 1; // 1 goes right, -1 goes left
      if (e.getKeyCode() == KeyEvent.VK_LEFT) {
        direction = -1;
      }
      cal.setTime(minD);
      cal.add(Calendar.MINUTE, (direction * chart.getMovementFactor()));
      minD = cal.getTime();
      cal.setTime(maxD);
      cal.add(Calendar.MINUTE, (direction * chart.getMovementFactor()));
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
              rg.getLowerBound() + (direction * (panel.getRangeAdjustor() * length)),
              rg.getUpperBound() + (direction * (panel.getRangeAdjustor() * length)));
      rangeAxis.setRange(adjusted);
    }
  }

  public void widenCandles() {
    CombinedDomainXYPlot combinedPlot = (CombinedDomainXYPlot) panel.chart.getPlot();
    XYPlot pricePlot = (XYPlot) combinedPlot.getSubplots().get(0);
    XYItemRenderer renderer = pricePlot.getRenderer();
    if (keys[KeyEvent.VK_B]) {
      if (renderer instanceof CandlestickRenderer) {
        double width = ((CandlestickRenderer) renderer).getCandleWidth();
        ((CandlestickRenderer) renderer).setCandleWidth(width + 1);
      }
    } else if (keys[KeyEvent.VK_C]) {
      float strokeWidth = ((BasicStroke) renderer.getSeriesStroke(0)).getLineWidth();
      int numSeries = pricePlot.getSeriesCount();
      for (int ctr = 0; ctr
              < numSeries; ctr++) {
        renderer.setSeriesStroke(ctr, new BasicStroke(strokeWidth + 1.0f));
      }
    }
  }

  public void narrowCandles() {
    CombinedDomainXYPlot combinedPlot = (CombinedDomainXYPlot) panel.chart.getPlot();
    XYPlot pricePlot = (XYPlot) combinedPlot.getSubplots().get(0);
    XYItemRenderer renderer = pricePlot.getRenderer();
    if (keys[KeyEvent.VK_B]) {
      if (renderer instanceof CandlestickRenderer) {
        double width = ((CandlestickRenderer) renderer).getCandleWidth();
        ((CandlestickRenderer) renderer).setCandleWidth(width - 1);
      }
    } else if (keys[KeyEvent.VK_C]) {
      float strokeWidth = ((BasicStroke) renderer.getSeriesStroke(0)).getLineWidth();
      int numSeries = pricePlot.getSeriesCount();
      for (int ctr = 0; ctr
              < numSeries; ctr++) {
        renderer.setSeriesStroke(ctr, new BasicStroke(strokeWidth - 1.0f));
      }
    }
  }

  public void addSpaceToTopOrBottom() {
    CombinedDomainXYPlot combinedPlot = (CombinedDomainXYPlot) panel.chart.getPlot();
    XYPlot pricePlot = (XYPlot) combinedPlot.getSubplots().get(0);
    ValueAxis rangeAxis = pricePlot.getRangeAxis();
    if (keys[KeyEvent.VK_T]) {
      Range rg = rangeAxis.getRange();
      double length = rg.getLength();
      Range adjusted = new Range(rg.getLowerBound(),
              rg.getUpperBound() + (panel.getRangeAdjustor() * length));
      rangeAxis.setRange(adjusted);
    } else if (keys[KeyEvent.VK_B]) {
      Range rg = rangeAxis.getRange();
      double length = rg.getLength();
      Range adjusted = new Range(rg.getLowerBound()
              - (panel.getRangeAdjustor() * length), rg.getUpperBound());
      rangeAxis.setRange(adjusted);
    }
  }

  public void removeSpaceFromTopOrBottom() {
    CombinedDomainXYPlot combinedPlot = (CombinedDomainXYPlot) panel.chart.getPlot();
    XYPlot pricePlot = (XYPlot) combinedPlot.getSubplots().get(0);
    ValueAxis rangeAxis = pricePlot.getRangeAxis();
    if (keys[KeyEvent.VK_T]) {
      Range rg = rangeAxis.getRange();
      double length = rg.getLength();
      Range adjusted = new Range(rg.getLowerBound(), rg.getUpperBound()
              - (panel.getRangeAdjustor() * length));
      rangeAxis.setRange(adjusted);
    } else if (keys[KeyEvent.VK_B]) {
      Range rg = rangeAxis.getRange();
      double length = rg.getLength();
      Range adjusted = new Range(rg.getLowerBound() + (panel.getRangeAdjustor() * length),
              rg.getUpperBound());
      rangeAxis.setRange(adjusted);
    }
  }

  public void moveChartLower() {
    CombinedDomainXYPlot combinedPlot = (CombinedDomainXYPlot) panel.chart.getPlot();
    XYPlot pricePlot = (XYPlot) combinedPlot.getSubplots().get(0);
    ValueAxis rangeAxis = pricePlot.getRangeAxis();
    Range rg = rangeAxis.getRange();
    double length = rg.getLength();
    Range adjusted = new Range(rg.getLowerBound() + (panel.getRangeAdjustor() * length),
            rg.getUpperBound() + (panel.getRangeAdjustor() * length));
    rangeAxis.setRange(adjusted);
  }

  public void moveChartHigher() {
    CombinedDomainXYPlot combinedPlot = (CombinedDomainXYPlot) panel.chart.getPlot();
    XYPlot pricePlot = (XYPlot) combinedPlot.getSubplots().get(0);
    ValueAxis rangeAxis = pricePlot.getRangeAxis();
    Range rg = rangeAxis.getRange();
    double length = rg.getLength();
    Range adjusted = new Range(rg.getLowerBound() - (panel.getRangeAdjustor() * length),
            rg.getUpperBound() - (panel.getRangeAdjustor() * length));
    rangeAxis.setRange(adjusted);
  }

  public void keyTyped(KeyEvent e) {
    if (!(e.isAltDown() || e.isControlDown())) { //Ctrl and Alt key handled elsewhere
      if (keys[KeyEvent.VK_W]) { //Widen candle body or outline
        widenCandles();
      } else if (keys[KeyEvent.VK_N]) { //Narrow candle body or outline
        narrowCandles();
      } else if (keys[KeyEvent.VK_J]) { //Jump, meaning load new data
        chart.jump();
      } else if (keys[KeyEvent.VK_A]) { //Add space to bottom or top of chart
        addSpaceToTopOrBottom();
      } else if (keys[KeyEvent.VK_R]) { //Subtract space from bottom or top of chart
        removeSpaceFromTopOrBottom();
      } else if (keys[KeyEvent.VK_D]) { //Subtract space from bottom or top of chart
        moveChartLower();
      } else if (keys[KeyEvent.VK_H]) { //Subtract space from bottom or top of chart
        moveChartHigher();
      }
      //System.out.println("Key Typed: " + e.getKeyChar());
    } else if(e.isControlDown()) {
      if (keys[KeyEvent.VK_J]) {
        //Jump, meaning load new data after resetting endDate so that it corresponds to actual last bar in chart
        chart.resetEndDate();
        chart.jump();
      }
    }
  }

  public void chartMouseClicked(ChartMouseEvent event) {
    int mouseX = event.getTrigger().getX();
    int mouseY = event.getTrigger().getY();
    Point2D mousePoint = panel.translateScreenToJava2D(new Point(mouseX, mouseY));
    CombinedDomainXYPlot combinedPlot = (CombinedDomainXYPlot) panel.chart.getPlot();
    PlotRenderingInfo info = panel.getChartRenderingInfo().getPlotInfo();
    XYPlot plot = (XYPlot) combinedPlot.findSubplot(info, mousePoint);
    plot.setDomainCrosshairVisible(true);
    plot.setRangeCrosshairVisible(true);
    int subPlotIdx = info.getSubplotIndex(mousePoint);
    Rectangle2D dataArea = info.getDataArea();
    int subPlotCount = info.getSubplotCount();
    DateAxis domainAxis = (DateAxis) plot.getDomainAxis();
    RectangleEdge domainAxisEdge = plot.getDomainAxisEdge();
    ValueAxis rangeAxis = plot.getRangeAxis();
    RectangleEdge rangeAxisEdge = plot.getRangeAxisEdge();
    double chartX =
            domainAxis.java2DToValue(mousePoint.getX(), dataArea,
            //Correct x val, i.e - the datetime
            domainAxisEdge);
    double chartY = rangeAxis.java2DToValue(mousePoint.getY(), dataArea,
            //Correct Y val
            rangeAxisEdge);
    double subChartY = rangeAxis.java2DToValue(mousePoint.getY(),
            info.getSubplotInfo(subPlotIdx).getDataArea(),
            rangeAxisEdge);
    int plotWeight;
    ////rpc - NOTE:9/2/10 1:28 PM - Added this to add/move stop and profit lines.
    if (keys[KeyEvent.VK_L]) {
      if (chart.getTraderFrame().setupStopLoss(subChartY)) {
        panel.setupStopLossCrosshair(subChartY, "STOPLOSS");
      }
    } else if (keys[KeyEvent.VK_P]) {
      if (chart.getTraderFrame().setupProfitExit(subChartY)) {
        panel.setupProfitStopCrosshair(subChartY, "PROFITEXIT");
      }
    } else if (keys[KeyEvent.VK_B]) {
      PtsCrosshair vertCrosshair = panel.setupVerticalCrosshair(chartX);
      panel.setupBuyCrosshair(vertCrosshair, subChartY, "BUY");
      chart.getTraderFrame().setupBuyTrade(chartX, subChartY);
    } else if (keys[KeyEvent.VK_S]) { //rpc - WORKING HERE:8/6/10 2:50 PM - SELL Trade.
      PtsCrosshair vertCrosshair = panel.setupVerticalCrosshair(chartX);
      panel.setupSellCrosshair(vertCrosshair, subChartY, "SELL");
      chart.getTraderFrame().setupSellTrade(chartX, subChartY);
      //panel.chart.getTraderFrame().instigateSellTrade(chartX, subChartY);
    } else if (keys[KeyEvent.VK_F6]) { //1/11/11 1:16 PM First point for line clicked
      chart.setFirstPointForLine(chartX, subChartY);
    } else if (keys[KeyEvent.VK_F7]) { //1/11/11 1:16 PM Add line to price chart
      chart.drawLineBetweenPoints(chartX, subChartY);
    } else if (keys[KeyEvent.VK_V] && !(event.getTrigger().isControlDown())) {
      panel.setupVerticalCrosshair(chartX);
    }
    // C O N T R O L   K E Y    C O D E S
    // Following require Ctrl down at same time as significant key.
    if (event.getTrigger().isControlDown()) {
      if (keys[KeyEvent.VK_A]) {  // Increase the relative size of subplot.
        plotWeight = plot.getWeight();
        plot.setWeight(plotWeight + 1);
      } 
        else if (keys[KeyEvent.VK_R] || keys[KeyEvent.VK_X]) { // Remove this subplot.
        combinedPlot.remove(plot);
      } else if (keys[KeyEvent.VK_H]) {
        panel.setupHorizontalCrosshair(subChartY);
      } else if (keys[KeyEvent.VK_V]) {
        panel.setupVerticalCrosshair(chartX);
        chart.setJumpDate(new Date((long) chartX));
        chart.setJumpDateSet(true);
      } else if (keys[KeyEvent.VK_C]) {
        panel.setupCrosshairs(chartX, chartY, subChartY, plot);
      } else if (keys[KeyEvent.VK_B]) {
        panel.setupHorizontalPrefixedCrosshair(subChartY, "BUY");
        panel.setupVerticalCrosshair(chartX);
        chart.getTraderFrame().setupBuyTrade(chartX, subChartY);
      } else if (keys[KeyEvent.VK_S]) { //rpc - WORKING HERE:8/6/10 2:50 PM - SELL Trade.
        panel.setupHorizontalPrefixedCrosshair(subChartY, "SELL");
        panel.setupVerticalCrosshair(chartX);
        panel.chart.getTraderFrame().instigateSellTrade(chartX, subChartY);
      }
    }
    //double dd = new Day(12, 4, 2010).getFirstMillisecond();
    clearKeys();
  }
  //
} // End of class

