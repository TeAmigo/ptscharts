/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ptscharts;

import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.time.ohlc.OHLCSeries;
import org.jfree.data.time.ohlc.OHLCSeriesCollection;
import org.jfree.data.xy.DefaultOHLCDataset;

/**
 *
 * @author rickcharon
 */
public class PtsPricePlot extends XYPlot {

  OHLCSeries ohlc;

  public PtsPricePlot(OHLCSeriesCollection dataset, ValueAxis domainAxis, ValueAxis rangeAxis, XYItemRenderer renderer) {
    super(dataset, domainAxis, rangeAxis, renderer);
    ohlc = dataset.getSeries(0);
    //int j = 3;
  }

  public PtsPricePlot(DefaultOHLCDataset dataset, ValueAxis domainAxis, ValueAxis rangeAxis, XYItemRenderer renderer) {
    super(dataset, domainAxis, rangeAxis, renderer);
    //ohlc = dataset.getSeries(0);
    //int j = 3;
  }
}
