/*
 * This extends Indicator to setup and draw on the chart.
 * 2/13/11 1:33 PM It is drawing the SMA, using TimeSeriesCollection instead of a XYSeriesCollection
 * seems to work alright.
 */
package ptscharts.indicators;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.util.ArrayList;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.LengthAdjustmentType;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.TextAnchor;
import org.joda.time.DateTime;
import ptscharts.PtsChart;
import ptscharts.PtsChart.plotTypes;
import ptscharts.PtsChartFactory;
import ptscharts.PtsSymbolInfos;
import ptsutils.PtsSymbolInfo;

/**
 *
 * @author rickcharon
 */
public class ChartIndicator extends Indicator {

  private PtsChart myChart;
  PtsChart.plotTypes myPlot = null;
  private IndicatorOrig myIndicator;
  //private XYSeriesCollection xySeriesCollection = new XYSeriesCollection();
  private TimeSeriesCollection seriesCollection = new TimeSeriesCollection();
  int seriesCollectionIdx;
  IndicatorType myType;
  Color lineColor = null;
  NumberAxis verticalAxis = null;

  public NumberAxis getVerticalAxis() {
    return verticalAxis;
  }
//  new NumberAxis("Volume");
//    vAxis.setAutoRangeIncludesZero(false);

  public Color getLineColor() {
    return lineColor;
  }

  public void setLineColor(Color lineColor) {
    this.lineColor = lineColor;
  }

  public PtsChart getMyChart() {
    return myChart;
  }

  public void setMyChart(PtsChart myChart) {
    this.myChart = myChart;
  }

  public TimeSeriesCollection getSeriesCollection() {
    return seriesCollection;
  }

  public plotTypes getMyPlot() {
    return myPlot;
  }

  @Override
  public void intialRun() {
    super.intialRun(); // 2/15/11 1:22 PM Nothing here presently, Everything that isn't chart specific should be moved to Indicator
    TimeSeries xys = new TimeSeries(myType.toString() + "-" + getOptInTimePeriod());
    if (myGroup == null) {
      //2/21/11 2:29 PM Not fully implemented, need closes, volume, etc.
      setInClose(myChart.getOhlcv().getCloses(0));
      setOutput(new double[getInClose().length]);
      setStartIdx(0);
      setEndIdx(getInClose().length - 1);
    }

    runIndicator(myType);
    //XYSeriesCollection dOut;
    TimeSeriesCollection dOut = (TimeSeriesCollection) myChart.getDataSetFromPlot(seriesCollectionIdx, myPlot);
    dOut.addSeries(xys);
    ArrayList<RegularTimePeriod> periods = null;
    if (myGroup == null) {
      periods = myChart.getOhlcv().getPeriods(0);
    } else {
      periods = myGroup.getPeriods();
    }
    for (int i = 0; i < getOutNbElement().value; i++) {
      dOut.getSeries(0).add(periods.get(i + getOutBegIdx().value), getOutput()[i]);
    }
    XYPlot plot = myChart.getPlot(myPlot);
    boolean a1 = plot.isDomainGridlinesVisible();
    boolean a2 = plot.isRangeGridlinesVisible();
    boolean a3 = plot.isRangeMinorGridlinesVisible();
    ValueAxis axis = plot.getRangeAxis();
    Paint col = plot.getDomainGridlinePaint();
    Paint col1 = plot.getDomainGridlinePaint();
    XYItemRenderer renderer = plot.getRenderer();
    setupRenderer();
    int j = 3;
  }

  // 2/14/11 11:17 AM A jump on the chart, new data added, the current in arrays can be used
  // to just get the new data,
  @Override
  public void jumpRun(int bars) {
    int beginJumpIdx;
    if (myGroup == null) {
      beginJumpIdx = myChart.getOhlcv().getItemCount() - bars;
    } else {
      beginJumpIdx = myGroup.getJumpBars();
    }
    if (myGroup == null) {
      setInClose(myChart.getOhlcv().getCloses(beginJumpIdx - Math.max(getOptInTimePeriod(), getOptInSlowPeriod())));
      setOutput(new double[getInClose().length]);
      setStartIdx(0);
      setEndIdx(getInClose().length - 1);
    }

    runIndicator(myType);
    ArrayList<RegularTimePeriod> periods = null;
    if (myGroup == null) {
      periods = myChart.getOhlcv().getPeriods(beginJumpIdx + 1);
    } else {
      periods = myGroup.getPeriods(beginJumpIdx + 1);
    }

    TimeSeriesCollection dOut = (TimeSeriesCollection) myChart.getDataSetFromPlot(seriesCollectionIdx, myPlot);
    for (int i = 0; i < periods.size(); i++) {
      dOut.getSeries(0).add(periods.get(i), getOutput()[i]);
      int j = 3;
    }
  }

  public void setupRenderer() {
    XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
    renderer.setSeriesPaint(0, lineColor);
    renderer.setSeriesStroke(0, new BasicStroke(2.0f));
    renderer.setSeriesLinesVisible(0, true);
    renderer.setSeriesShapesVisible(0, false);
    getMyChart().getPlot(myPlot).setRenderer(seriesCollectionIdx, renderer);
  }

  public void setupRangeAxis(String label) {
    NumberAxis vAxis = new NumberAxis(label);
    vAxis.setAutoRangeIncludesZero(false);
    getMyChart().getPlot(myPlot).setRangeAxis(vAxis);
    getMyChart().getPlot(myPlot).setRangeAxisLocation(AxisLocation.TOP_OR_LEFT);
  }

  public void addHorizontalLine(int val, Color col, String label, double stroke) {
    Marker start = new ValueMarker(val);

    start.setStroke(new BasicStroke((float) stroke));
    start.setLabelOffsetType(LengthAdjustmentType.EXPAND);
    start.setPaint(col);
    start.setLabel(label);
    start.setLabelAnchor(RectangleAnchor.BOTTOM_RIGHT);
    start.setLabelTextAnchor(TextAnchor.TOP_LEFT);
    getMyChart().getPlot(myPlot).addRangeMarker(start);
  }

  public ChartIndicator(IndicatorGroup indGroup, IndicatorType id, PtsChart.plotTypes plot) {
    myGroup = indGroup;
    indGroup.addIndicator(this);
    myChart = indGroup.getMyChart();
    myType = id;
    myPlot = plot;
    seriesCollectionIdx = myChart.addIndicatorToChart(this);
  }

  public ChartIndicator(IndicatorGroup indGroup, IndicatorType id, PtsChart.plotTypes plot, int periodIn) {
    myGroup = indGroup;
    indGroup.addIndicator(this);
    myChart = indGroup.getMyChart();
    myType = id;
    myPlot = plot;
    seriesCollectionIdx = myChart.addIndicatorToChart(this);
  }

//  public ChartIndicator(PtsChart myChart, IndicatorType id) {
//    this.myChart = myChart;
//    myType = id;
//    initChartComponents();
//  }
//  public ChartIndicator(PtsChart myChart, IndicatorType id, PtsChart.plotTypes plot) {
//    this.myChart = myChart;
//    myType = id;
//    myPlot = plot;
//    initChartComponents();
//  }
  public static void main(String args[]) {
    PtsSymbolInfos syminfs = new PtsSymbolInfos();
    syminfs.getDistinctSymbolInfos();
    PtsChartFactory factory = new PtsChartFactory(syminfs);
    PtsSymbolInfo sym = syminfs.getSymbolInfo("AUD");
//    PtsChart aud = IndicatorSet1.run(factory, "AUD",
//            new DateTime().minusDays(1).toString(), new DateTime().toString(), 1);
    PtsChart aud = IndicatorSet1.run(factory, "AUD",
            new DateTime().minusMonths(1).toString(), new DateTime().toString(), 60);
  }

  public static void mainold(String args[]) {
    PtsSymbolInfos syminfs = new PtsSymbolInfos();
    syminfs.getDistinctSymbolInfos();
    PtsChartFactory factory = new PtsChartFactory(syminfs);
    // AUD CAD DX  ES EUR GBP JPY ZB ZF ZN
    PtsChart chartcad = factory.createPtsChart("EUR", "2011-01-15T00:00", "2011-02-05T07:00", 60);
    IndicatorGroup indGroup = new IndicatorGroup(chartcad);
    int cnt = chartcad.getPricePlotDatasetCount();

    ChartIndicator cci = new ChartIndicator(indGroup, IndicatorType.CCI, PtsChart.plotTypes.Indicator2Plot);
    //ad.setPeriod(50);
    cci.setOptInTimePeriod(14);
    cci.setupRangeAxis("CCI");
    cci.setLineColor(Color.BLUE);
    cci.addHorizontalLine(100, Color.red, "100", 1.5);
    cci.addHorizontalLine(-100, Color.red, "-100", 1.5);
    cci.setupRenderer();

    ChartIndicator ci20 = new ChartIndicator(indGroup, IndicatorType.SMA, PtsChart.plotTypes.PricePlot);
    ci20.setOptInTimePeriod(20);
    ci20.setLineColor(Color.RED);
    ci20.setupRenderer();
    //ci20.addHorizontalLine(100000, Color.red, "100,000");

//    ChartIndicator ci50 = new ChartIndicator(indGroup, IndicatorType.SMA, PtsChart.plotTypes.PricePlot);
//    ci50.setPeriod(50);
//    ci50.setLineColor(Color.BLUE);
//    ci50.setupRenderer();


    ChartIndicator adosc = new ChartIndicator(indGroup, IndicatorType.ADOSC, PtsChart.plotTypes.Indicator3Plot);
    //ad.setPeriod(50);
    adosc.setupRangeAxis("ADOSC");
    adosc.setLineColor(Color.BLACK);
    adosc.setOptInFastPeriod(3);
    adosc.setOptInSlowPeriod(10);
    adosc.addHorizontalLine(0, Color.red, "0", 1.5);
    adosc.setupRenderer();

    ChartIndicator ci70 = new ChartIndicator(indGroup, IndicatorType.SMA, PtsChart.plotTypes.PricePlot);
    ci70.setOptInTimePeriod(70);
    ci70.setLineColor(Color.GREEN);
    ci70.setupRenderer();

    ChartIndicator dx14 = new ChartIndicator(indGroup, IndicatorType.DX, PtsChart.plotTypes.Indicator1Plot);
    dx14.setOptInTimePeriod(14);
    dx14.setupRangeAxis("ADX");
    dx14.setLineColor(Color.BLACK);
    dx14.setupRenderer();
    dx14.addHorizontalLine(50, Color.red, "50", 1.5);


    ChartIndicator diminus14 = new ChartIndicator(indGroup, IndicatorType.MINUSDI, PtsChart.plotTypes.Indicator1Plot);
    diminus14.setOptInTimePeriod(14);
    diminus14.setLineColor(Color.RED);
    diminus14.setupRenderer();

    ChartIndicator diplus14 = new ChartIndicator(indGroup, IndicatorType.PLUSDI, PtsChart.plotTypes.Indicator1Plot);
    diplus14.setOptInTimePeriod(14);
    diplus14.setLineColor(Color.BLUE);
    diplus14.setupRenderer();


    indGroup.runIndicatorsInitial();
    chartcad.show();
//    ci20.intialRun();
//    ci50.intialRun();
//    ci70.intialRun();

  }
}
