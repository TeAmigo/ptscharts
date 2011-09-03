package ptscharts;

import ptsutils.PtsSymbolInfo;
import ptsutils.PtsOHLCV;
import ptsutils.PtsDBops;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumMap;
import javax.swing.JPanel;
//import org.apache.batik.svggen.SVGGraphics2DIOException;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.HighLowItemLabelGenerator;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.ohlc.OHLCSeriesCollection;
import org.joda.time.DateTime;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
//import org.apache.batik.dom.GenericDOMImplementation;
//import org.apache.batik.svggen.SVGGraphics2D;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.time.MovingAverage;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import ptscharts.indicators.ChartIndicator;
import ptscharts.indicators.IndicatorGroup;

public class PtsChart extends JFreeChart {

  private PtsChartPanel chartPanel;
  private JPanel topPanel;
  //private List<PriceBar> priceBars;
  //private Instrument instrument;
  private PtsChartControlFrame controlFrame;
  private CombinedDomainXYPlot comboPlot;
  private OHLCSeriesCollection priceSeriesCollection;
  //private OHLCSeries priceSeries;
  private TimeSeries volumeSeries;
  private XYPlot plotPrices = null;
  private XYPlot plotVolume = null;
  private XYPlot plotIndicator1 = null;
  private XYPlot plotIndicator2 = null;
  private XYPlot plotIndicator3 = null;
  //SimpleBindings plots = new SimpleBindings();

  public static enum plotTypes {
    PricePlot, VolumePlot, Indicator1Plot, Indicator2Plot, Indicator3Plot
  };

  private EnumMap plots = new EnumMap<plotTypes, XYPlot>(plotTypes.class);
  private Date beginDate, endDate, jumpDate;
  private boolean jumpDateSet = false;
  private PtsSymbolInfo symbolInfo;
  private int compressionFactor;
  private PtsTraderFrame traderFrame;
  PtsChartFrame frame;
  PtsOHLCV ohlcv;
  private int jumpBarSize = 4;
  private int movementFactor;
  private int nextPricePlotDataSet = 1;
  ArrayList<Integer> priceSeriesLines = new ArrayList<Integer>();
  //ArrayList<ChartIndicator> indicators = new ArrayList<ChartIndicator>();
  IndicatorGroup indicators = null;

  class coord {

    double x = 0.0;
    double y = 0.0;
  };
  coord firstPoint;

  public IndicatorGroup getIndicators() {
    return indicators;
  }

  public void setIndicators(IndicatorGroup indicators) {
    this.indicators = indicators;
    //indicators.runIndicatorsInitial();
  }

  public XYPlot getPlotIndicator1() {
    return plotIndicator1;
  }

  public XYPlot getPlotIndicator2() {
    return plotIndicator2;
  }

  public XYPlot getPlotIndicator3() {
    return plotIndicator3;
  }

  public XYPlot getPlotPrices() {
    return plotPrices;
  }

  public XYPlot getPlotVolume() {
    return plotVolume;
  }

//  public void addIndicator(ChartIndicator newIndicator) {
//    indicators.add(newIndicator);
//    newIndicator.intialRun();
//  }
//  public void removeIndicator(String indName) {
//    for (ChartIndicator ind : indicators) {
//      //     if (ind.name().equals(indName)) {
//      indicators.remove(ind);
//      break;
//    }
//  }
  public PtsOHLCV getOhlcv() {
    return ohlcv;
  }

  public void setOhlcv(PtsOHLCV ohlcv) {
    this.ohlcv = ohlcv;
  }

  public void setJumpDate(Date jumpDate) {
    this.jumpDate = jumpDate;
  }

  public void setJumpDateSet(boolean jumpDateSet) {
    this.jumpDateSet = jumpDateSet;
  }

  public int getMovementFactor() {
    return movementFactor;
  }

  public void setMovementFactor(int movementFactor) {
    this.movementFactor = movementFactor;
  }

  public int getJumpBarSize() {
    return jumpBarSize;
  }

  public void setJumpBarSize(int jumpBarSize) {
    this.jumpBarSize = jumpBarSize;
  }

  public PtsTraderFrame getTraderFrame() {
    return traderFrame;
  }

  public int getCompressionFactor() {
    return compressionFactor;
  }

  public PtsSymbolInfo getSymbolInfo() {
    return symbolInfo;
  }
  //private IndicatorResultsAggregator indicatorResultsAggregate;

  public PtsChartPanel getChartPanel() {
    return chartPanel;
  }

  public PtsChartFrame getFrame() {
    return frame;
  }

  public void setChartPanel(PtsChartPanel chartPanel) {
    this.chartPanel = chartPanel;
  }

  public void show() {
    frame.setVisible(true);
  }

  public void dispose() {
    frame.dispose();
  }

  public void hide() {
    frame.setVisible(false);
  }

  public void destroy() {
    frame.dispose();
  }

  private void createIndicatorPlots() {
  }

  private void mapPlots() {
    plots.put(plotTypes.PricePlot, plotPrices);
    plots.put(plotTypes.VolumePlot, plotVolume);
    plots.put(plotTypes.Indicator1Plot, plotIndicator1);
    plots.put(plotTypes.Indicator2Plot, plotIndicator2);
    plots.put(plotTypes.Indicator3Plot, plotIndicator3);

  }

  public PtsChart(PtsChartFrame frameIn, CombinedDomainXYPlot plot, PtsSymbolInfo symInfoIn,
          DateTime bDate, DateTime eDate, int compressionFactorIn) {
    super(plot);
    firstPoint = new coord();
    frame = frameIn;
    symbolInfo = symInfoIn;
    traderFrame = new PtsTraderFrame(this, symbolInfo);
    compressionFactor = compressionFactorIn;
    movementFactor = compressionFactorIn;
    comboPlot = plot;
    //this.instrument = instrument;
    //priceBars = instrument.getPriceBars().getDatas();
    beginDate = bDate.toDate();
    endDate = eDate.toDate();
    controlFrame = new PtsChartControlFrame(this);
    //indicatorResultsAggregate = new IndicatorResultsAggregator(instrument);
    setupChart();
    mapPlots();
  }

  public XYPlot getPlot(PtsChart.plotTypes type) {
    return (XYPlot) plots.get(type);
  }

  public CombinedDomainXYPlot getComboPlot() {
    return comboPlot;
  }

  public XYPlot getPricePlot() {
    return plotPrices;
  }

  public XYPlot getVolumePlot() {
    return plotVolume;
  }

  public Date getBeginDate() {
    return beginDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  private CandlestickRenderer getCandlestickRenderer() {
    CandlestickRenderer r = new CandlestickRenderer(2);
    r.setBaseToolTipGenerator(new HighLowItemLabelGenerator());
    r.setSeriesPaint(0, Color.BLACK);
    r.setSeriesStroke(0, new BasicStroke(1.0f));
    return r;
  }

  private XYBarRenderer getVolumeRenderer() {
    XYBarRenderer volumeRenderer = new XYBarRenderer();
    volumeRenderer.setShadowVisible(false);
    volumeRenderer.setSeriesPaint(0, Color.BLACK);
    volumeRenderer.setSeriesStroke(0, new BasicStroke(2.0f));
    volumeRenderer.setBaseToolTipGenerator(getToolTipGenerator());
    return volumeRenderer;
  }

  private XYLineAndShapeRenderer getLineAndShapeRenderer(TimeSeriesCollection dataset) {
    XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(true, false);
    renderer.setSeriesPaint(dataset.indexOf(dataset.getSeries("ADX")), Color.BLACK);
    renderer.setSeriesPaint(dataset.indexOf(dataset.getSeries("PlusDI")), Color.BLUE);
    renderer.setSeriesPaint(dataset.indexOf(dataset.getSeries("MinusDI")), Color.RED);
    renderer.setSeriesStroke(0, new BasicStroke(1.5f));
    renderer.setSeriesStroke(1, new BasicStroke(1.5f));
    renderer.setSeriesStroke(2, new BasicStroke(1.5f));
    renderer.setBaseToolTipGenerator(getToolTipGenerator());
    return renderer;
  }

  public int jump() {
    jumpBars(jumpBarSize);
    return jumpBarSize;
  }

  public void resetEndDate() {
    endDate = ohlcv.PriceBars.getPeriod(ohlcv.getLastItemCount() - 1).getStart();
    int ic = ohlcv.PriceBars.getItemCount() - 1;
    Date ed2 = ohlcv.PriceBars.getPeriod(ic).getEnd();
    ohlcv.PriceBars.remove(ohlcv.PriceBars.getPeriod(ic));
  }

  public void jumpBars(int bars) {
    DateTime jumpBeginDateTime = new DateTime(endDate.getTime());
    DateTime jumpEndDateTime = null;
    if (jumpDateSet) {
      jumpEndDateTime = new DateTime(jumpDate.getTime());
      jumpDateSet = false;
    } else {
      jumpEndDateTime = jumpBeginDateTime.plusMinutes(bars * compressionFactor);
    }
    bars = PtsDBops.getOHLCandVolumeCompressed(ohlcv, symbolInfo, jumpBeginDateTime.getMillis(),
            jumpEndDateTime.getMillis(), compressionFactor);
    endDate = new Date(jumpEndDateTime.getMillis());
//    for (ChartIndicator ind : indicators) {
//      ind.jumpRun(bars);
//    }
    indicators.jumpRun(bars);
  }

  //2/6/11 3:17 PM Ala JFreeChart
  private void putInMovingAverage() {

    XYDataset dataset2 = MovingAverage.createMovingAverage(
            ohlcv.ohlc, "-MAVG", 20, 0L);
    plotPrices.setDataset(1, dataset2);
    plotPrices.setRenderer(1, new StandardXYItemRenderer());
  }

  public int getPricePlotDatasetCount() {
    return plotPrices.getDatasetCount();
  }

  public int addDataSetToPricePlot(XYSeriesCollection inSet) {
    int dsNum = plotPrices.getDatasetCount();
    plotPrices.setDataset(dsNum, inSet);
    return dsNum;
  }

  public int addIndicatorToChart(ChartIndicator newIndicator) {
    plotTypes typeIn = newIndicator.getMyPlot();
    XYPlot plot = (XYPlot) plots.get(typeIn);
    if (plot == null) {
      plots.put(typeIn, new XYPlot());
      plot = (XYPlot) plots.get(typeIn);
      plot.setRangeAxisLocation(AxisLocation.TOP_OR_LEFT);
      plot.setDomainPannable(true);
      plot.setRangePannable(true);
      plot.setBackgroundPaint(Color.WHITE);
      plot.setRangeGridlinePaint(Color.BLACK);
      plot.setDomainGridlinesVisible(true);
      comboPlot.add(plot, 8);
    }
    int dsNum = plot.getDatasetCount();
    plot.setDataset(dsNum, newIndicator.getSeriesCollection());
    return dsNum;
  }

//  public int addDataSetToPricePlot(TimeSeriesCollection inSet) {
//    int dsNum = plotPrices.getDatasetCount();
//    plotPrices.setDataset(dsNum, inSet);
//    return dsNum;
//  }
  public XYDataset getDataSetFromPlot(int idx, plotTypes plot) {
    return ((XYPlot) plots.get(plot)).getDataset(idx);
  }

  public XYDataset getDataSetFromPricePlot(int idx) {
    return plotPrices.getDataset(idx);
  }

  public void setFirstPointForLine(double xIn, double yIn) {
    firstPoint.x = xIn;
    firstPoint.y = yIn;
    //getSeriesPaintForLines(); 1/19/11 3:51 PM Testing Color.
  }

  /**
   * 
   * @param xIn
   * @param yIn
   */
  public void drawLineBetweenPoints(double xIn, double yIn) {
    XYSeriesCollection inSet = new XYSeriesCollection();
    XYSeries xys = new XYSeries("");
    xys.add(firstPoint.x, firstPoint.y);
    xys.add(xIn, yIn);
    inSet.addSeries(xys);
    int dsNum = addDataSetToPricePlot(inSet);
    XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
    priceSeriesLines.add(dsNum);
    renderer.setSeriesPaint(0, Color.BLACK);
    renderer.setSeriesStroke(0, new BasicStroke(2.0f));
    plotPrices.setRenderer(dsNum, renderer);
  }

  public void getSeriesPaintForLines() {
    for (int i = 0; i < priceSeriesLines.size(); i++) {
      XYItemRenderer rr = plotPrices.getRenderer(priceSeriesLines.get(i));
      java.awt.Paint p = rr.getSeriesPaint(0);
      rr.setSeriesPaint(0, Color.BLACK);
      int g = 3;
    }
  }

  private void setupChart() {
    // create subplot 1...
    //TimeSeriesCollection highsAndLows = createHighLowDataset();
    ohlcv = new PtsOHLCV();
    PtsDBops.getOHLCandVolumeCompressed(ohlcv, symbolInfo, beginDate.getTime(), endDate.getTime(), compressionFactor);
    //indicatorsCalculateInitial();
    priceSeriesCollection = ohlcv.ohlc;
    NumberAxis dAxis = new NumberAxis("High/Low");
    dAxis.setAutoRangeIncludesZero(false);
    dAxis.setUpperMargin(0.01);
    dAxis.setLowerMargin(0.01);
    //plotPrices = new XYPlot(priceSeries, null, dAxis, getHighLowRenderer(priceSeries));
    plotPrices = new PtsPricePlot(priceSeriesCollection, null, dAxis, getCandlestickRenderer());
    plotPrices.setRangeAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
    plotPrices.setDomainPannable(true);
    plotPrices.setRangePannable(true);

    // create subplot 2, adx subplot...
//    TimeSeriesCollection diPlusDiMinusADX = createADXDataset();
//    NumberAxis dAxis2 = new NumberAxis("ADX");
//    dAxis2.setAutoRangeIncludesZero(false);
//    plotADX = new XYPlot(diPlusDiMinusADX, null, dAxis2, getLineAndShapeRenderer(diPlusDiMinusADX));
//    plotADX.setRangeAxisLocation(AxisLocation.TOP_OR_LEFT);
//    plotADX.setDomainPannable(true);
//    plotADX.setRangePannable(true);
    // create subplot 3, volume subplot...

    NumberAxis vAxis = new NumberAxis("Volume");
    vAxis.setAutoRangeIncludesZero(false);
    TimeSeriesCollection volCol = ohlcv.vol;
    plotVolume = new XYPlot(volCol, null, vAxis, getVolumeRenderer());
    plotVolume.setRangeAxisLocation(AxisLocation.TOP_OR_LEFT);
    plotVolume.setDomainPannable(true);
    plotVolume.setRangePannable(true);
    // add the subplots to comboPlot...
    comboPlot.setGap(2.0);
    comboPlot.add(plotPrices, 50);
//    comboPlot.add(plotADX, 10);
    comboPlot.add(plotVolume, 5);
    comboPlot.setOrientation(PlotOrientation.VERTICAL);
    comboPlot.setDomainPannable(true);
    comboPlot.setRangePannable(true);
    comboPlot.getDomainAxis().setTickMarkInsideLength(5.0f);
  }

  private StandardXYToolTipGenerator getToolTipGenerator() {
    return new StandardXYToolTipGenerator(
            "{0} : {1} = {2}", new SimpleDateFormat("MM/dd/yyyy HH:mm:ss"),
            new DecimalFormat("0.00"));
  }

  public void openControlFrame() {
    controlFrame.setVisible(true);
  }

//  public void streamToSVG() {
//    Writer out = null;
//    try {
//      DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();
//      // Create an instance of org.w3c.dom.Document
//      Document document = domImpl.createDocument(null, "svg", null);
//      // Create an instance of the SVG Generator
//      SVGGraphics2D svgGenerator = new SVGGraphics2D(document);
//      // set the precision to avoid a null pointer exception in Batik 1.5
//      svgGenerator.getGeneratorContext().setPrecision(6);
//      // Ask the chart to render into the SVG Graphics2D implementation
//      int height = frame.getHeight();
//      int width = frame.getWidth();
//      this.draw(svgGenerator, new Rectangle2D.Double(0, 0, width, height), null);
//      // Finally, stream out SVG to a file using UTF-8 character to
//      // byte encoding
//      boolean useCSS = true;
//      out = new OutputStreamWriter(new FileOutputStream(new File("/share/Images/test3.svg")), "UTF-8");
//      svgGenerator.stream(out, useCSS);
//    } catch (SVGGraphics2DIOException ex) {
//      System.err.println("SVGGraphics2DIOException: " + ex.getMessage());
//    } catch (FileNotFoundException ex) {
//      System.err.println("FileNotFoundException: " + ex.getMessage());
//    } catch (UnsupportedEncodingException ex) {
//      System.err.println("UnsupportedEncodingException: " + ex.getMessage());
//    } finally {
//      try {
//        out.close();
//      } catch (IOException ex) {
//        System.err.println("IOException: " + ex.getMessage());
//      }
//    }
//  }
}
