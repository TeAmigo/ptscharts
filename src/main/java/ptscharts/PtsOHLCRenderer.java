
package ptscharts;

import java.awt.Color;
import java.awt.Paint;
import org.jfree.chart.renderer.xy.HighLowRenderer;
import org.jfree.data.time.ohlc.OHLCItem;
import org.jfree.data.time.ohlc.OHLCSeries;
import org.jfree.data.time.ohlc.OHLCSeriesCollection;

/**
 *
 * @author rickcharon
 */
public class PtsOHLCRenderer extends HighLowRenderer {
 private OHLCSeries ohlcSeries;

  public PtsOHLCRenderer(OHLCSeriesCollection seriesCol) {
    super();
    ohlcSeries = seriesCol.getSeries(0);
  }

  @Override
  public Paint getItemPaint(int row, int column) {
    OHLCItem item = (OHLCItem) ohlcSeries.getDataItem(column);
    Color col;
    if(item.getCloseValue() > item.getOpenValue()) {
      col = Color.BLUE;
    } else if ((item.getCloseValue() < item.getOpenValue())) {
      col = Color.RED;
    } else {
      col = Color.PINK;
    }
    return col;
   //return super.getItemPaint(row, column);
  }

//  @Override
//  public Stroke getItemStroke(int row, int column) {
//    return super.getItemStroke(row, column);
//  }




//  @Override
//  public void drawItem(Graphics2D g2, XYItemRendererState state, Rectangle2D dataArea, PlotRenderingInfo info,
//          XYPlot plot, ValueAxis domainAxis, ValueAxis rangeAxis, XYDataset dataset, int series,
//          int item, CrosshairState crosshairState, int pass) {
//    super.drawItem(g2, state, dataArea, info, plot, domainAxis, rangeAxis, dataset, series, item, crosshairState,
//            pass);
//
//    OHLCSeriesCollection ohsc = (OHLCSeriesCollection) dataset;
//    if(series != 0) {
//      int j = 3;
//    }
//
//  }
}
