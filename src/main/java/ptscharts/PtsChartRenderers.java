/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ptscharts;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Rectangle;
import org.jfree.chart.renderer.xy.XYDifferenceRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.ohlc.OHLCSeriesCollection;

/**
 *
 * @author rickcharon
 */
public class PtsChartRenderers {

  public static  XYDifferenceRenderer getXYDifferenceRenderer() {
    XYDifferenceRenderer r = new XYDifferenceRenderer(Color.green,
            new Color(229, 255, 247), false);
    r.setSeriesPaint(0, Color.RED);
    r.setSeriesPaint(1, Color.blue);
    r.setBaseShape(new Rectangle(1, 1));
    r.setShapesVisible(true);
    r.setBaseToolTipGenerator(PtsChartToolTipGenerators.DatedToolTipGenerator());
    return r;
  }

  public static  PtsOHLCRenderer getHighLowRenderer(OHLCSeriesCollection seriesCol) {
    PtsOHLCRenderer r = new PtsOHLCRenderer(seriesCol);
    r.setBaseToolTipGenerator(PtsChartToolTipGenerators.DatedToolTipGenerator());
    r.setSeriesPaint(0, Color.BLACK);
    r.setSeriesStroke(0, new BasicStroke(2.0f));
    return r;
  }

  public static  XYLineAndShapeRenderer getLineAndShapeRenderer(TimeSeriesCollection dataset) {
    XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(true, false);
    renderer.setSeriesPaint(dataset.indexOf(dataset.getSeries("ADX")), Color.BLACK);
    renderer.setSeriesPaint(dataset.indexOf(dataset.getSeries("PlusDI")), Color.BLUE);
    renderer.setSeriesPaint(dataset.indexOf(dataset.getSeries("MinusDI")), Color.RED);
    renderer.setSeriesStroke(0, new BasicStroke(1.5f));
    renderer.setSeriesStroke(1, new BasicStroke(1.5f));
    renderer.setSeriesStroke(2, new BasicStroke(1.5f));
//    renderer.setBaseShape(new Rectangle(1, 1));
    //renderer.setShapesVisible(true);
    renderer.setBaseToolTipGenerator(PtsChartToolTipGenerators.DatedToolTipGenerator());
//    XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
//    renderer.setBaseShapesVisible(true);
//    renderer.setDrawOutlines(true);
//    renderer.setUseFillPaint(true);
//    renderer.setBaseFillPaint(Color.white);
//    renderer.setSeriesStroke(0, new BasicStroke(1.0f));
//    renderer.setSeriesOutlineStroke(0, new BasicStroke(1.0f));
//    renderer.setSeriesShape(0, new Ellipse2D.Double(-1.0, -1.0, 1.0, 1.0));
//    renderer.setBaseToolTipGenerator(getToolTipGenerator());
    return renderer;
  }

}
