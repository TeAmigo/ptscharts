/* ===========================================================
This was lifted from  CrosshairOverlay.java
 *
 */
package ptscharts;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.panel.AbstractOverlay;
import org.jfree.chart.panel.Overlay;
import org.jfree.chart.plot.Crosshair;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.text.TextUtilities;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.TextAnchor;
import org.jfree.util.ObjectUtilities;
import org.jfree.util.PublicCloneable;

/**
 * An overlay for a {@link ChartPanel} that draws crosshairs on a plot.
 *
 * @since 1.0.13
 */
public class PtsCrosshairOverlay extends AbstractOverlay implements Overlay,
        PropertyChangeListener, PublicCloneable, Cloneable, Serializable {
// This is for javadoc - CrosshairOverlay co;

  /** Storage for the crosshairs along the x-axis. */
  private List xCrosshairs;
  /** Storage for the crosshairs along the y-axis. */
  private List yCrosshairs;
  private PtsChartPanel panel;
  DecimalFormat twoDForm = new DecimalFormat("#.##");

  /**
   * Default constructor.
   */
  public PtsCrosshairOverlay(PtsChartPanel myPanelIn) {
    super();
    panel = myPanelIn;
    this.xCrosshairs = new java.util.ArrayList();
    this.yCrosshairs = new java.util.ArrayList();
  }

  /**
   * Adds a crosshair against the domain axis.
   *
   * @param crosshair  the crosshair.
   */
  public void addDomainCrosshair(PtsCrosshair crosshair) {
    if (crosshair == null) {
      throw new IllegalArgumentException("Null 'crosshair' argument.");
    }
    this.xCrosshairs.add(crosshair);
    crosshair.addPropertyChangeListener(this);
  }

  public void removeDomainCrosshair(Crosshair crosshair) {
    if (crosshair == null) {
      throw new IllegalArgumentException("Null 'crosshair' argument.");
    }
    if (this.xCrosshairs.remove(crosshair)) {
      crosshair.removePropertyChangeListener(this);
      fireOverlayChanged();
    }
  }

  public void clearDomainCrosshairs() {
    if (this.xCrosshairs.isEmpty()) {
      return;  // nothing to do
    }
    List crosshairs = getDomainCrosshairs();
    for (int i = 0; i < crosshairs.size(); i++) {
      Crosshair c = (Crosshair) crosshairs.get(i);
      this.xCrosshairs.remove(c);
      c.removePropertyChangeListener(this);
    }
    fireOverlayChanged();
  }

  public List getDomainCrosshairs() {
    return new ArrayList(this.xCrosshairs);
  }

  //rpc - NOTE:8/3/10 4:39 PM - Return the idx of crosshair if it exits, -1 otherwise,
  public int rangeCrosshairExists(PtsCrosshair crosshair) {
    for (int i = 0; i < yCrosshairs.size(); i++) {
      Double val1 = Double.valueOf(twoDForm.format(((PtsCrosshair) yCrosshairs.get(i)).getValue()));
      Double val2 = Double.valueOf(twoDForm.format(crosshair.getValue()));
      if (val1.compareTo(val2) == 0) {
        return i;
      }
    }
    return -1;
  }

  /**
   * Adds a crosshair against the Horizontal (range) axis. 
   */
  public void addRangeCrosshair(PtsCrosshair crosshair) {
    if (crosshair == null) {
      System.err.println("No Crosshair in addRangeCrosshair in CrosshairOverlayADX");
    }
    int idx = rangeCrosshairExists(crosshair);
    if (idx  != -1) {
      removeRangeCrosshair((PtsCrosshair) (yCrosshairs.get(idx)));
    }
      this.yCrosshairs.add(crosshair);
      crosshair.addPropertyChangeListener(this);
  }

  public void removeRangeCrosshair(Crosshair crosshair) {
    if (crosshair == null) {
      throw new IllegalArgumentException("Null 'crosshair' argument.");
    }
    if (this.yCrosshairs.remove(crosshair)) {
      crosshair.removePropertyChangeListener(this);
      fireOverlayChanged();
    }
  }

  public void removeRangeCrosshairs(String[] crosshairValues) {
    if (crosshairValues == null) {
      System.err.println("Null 'crosshairValue' argument in removeRangeCrosshair()");
    }
    boolean fireOverlay = false;
    for (int j = 0; j < crosshairValues.length; j++) {
      for (int i = 0; i < yCrosshairs.size(); i++) {
        Double val = Double.valueOf(twoDForm.format(((PtsCrosshair) yCrosshairs.get(i)).getValue()));
        Double valIn = Double.parseDouble(crosshairValues[j]);
        if (val.compareTo(valIn) == 0) {
          ((PtsCrosshair) (yCrosshairs.get(i))).removePropertyChangeListener(this);
          yCrosshairs.remove(yCrosshairs.get(i));
          fireOverlay = true;
        }
        int k = 3;
      }
    }
    if (fireOverlay) {
      //crosshair.removePropertyChangeListener(this);
      fireOverlayChanged();
    }
  }

  public void clearRangeCrosshairs() {
    if (this.yCrosshairs.isEmpty()) {
      return;  // nothing to do
    }
    List crosshairs = getRangeCrosshairs();
    for (int i = 0; i < crosshairs.size(); i++) {
      Crosshair c = (Crosshair) crosshairs.get(i);
      this.yCrosshairs.remove(c);
      c.removePropertyChangeListener(this);
    }
    fireOverlayChanged();
  }

  public List getRangeCrosshairs() {
    return new ArrayList(this.yCrosshairs);
  }

  public Double[] getRangeCrosshairsValues() {
    //BigDecimal[] bd   = new BigDecimal[getRangeCrosshairs().size()];;
    Double[] ret = new Double[getRangeCrosshairs().size()];
    for (int i = 0; i < ret.length; i++) {
      ret[i] = ((PtsCrosshair) yCrosshairs.get(i)).getValue();
      ret[i] = Double.valueOf(twoDForm.format(ret[i]));
    }
    return ret;
  }

  public String[] getDomainCrosshairsValues() {
    String[] ret = new String[getDomainCrosshairs().size()];
    for (int i = 0; i < ret.length; i++) {
      long tt = (long) (((PtsCrosshair) xCrosshairs.get(i)).getValue());
      ret[i] = PtsDateOps.prettyString(new Date(tt));
    }
    return ret;
  }

  /**
   * Receives a property change event (typically a change in one of the
   * crosshairs).
   *
   * @param e  the event.
   */
  public void propertyChange(PropertyChangeEvent e) {
    fireOverlayChanged();
  }

  private void paintEachPanel(XYPlot plot, Graphics2D g2, Rectangle2D dataArea, Rectangle2D dataAreaH) {
    ValueAxis xAxis = plot.getDomainAxis();
    RectangleEdge xAxisEdge = plot.getDomainAxisEdge();
    Iterator iterator = this.xCrosshairs.iterator();
    while (iterator.hasNext()) {
      PtsCrosshair ch = (PtsCrosshair) iterator.next();
      if (ch.isVisible()) { //Date axis, along bottom, so vertical line
        double x = ch.getValue();
        double xx = xAxis.valueToJava2D(x, dataArea, xAxisEdge);
        if (plot.getOrientation() == PlotOrientation.VERTICAL) {
          drawVerticalCrosshair(g2, dataArea, xx, ch);
        } else {
          drawHorizontalCrosshair(g2, dataArea, xx, ch);
        }
      }
    }
    ValueAxis yAxis = plot.getRangeAxis();
    RectangleEdge yAxisEdge = plot.getRangeAxisEdge();
    iterator = this.yCrosshairs.iterator();
    while (iterator.hasNext()) {
      PtsCrosshair ch = (PtsCrosshair) iterator.next();
      if (ch.isVisible()) { //here is the horizontal line(s)
        double y = ch.getValue();
        double yy = yAxis.valueToJava2D(y, dataAreaH, yAxisEdge);
        //rpc - NOTE:7/26/10 12:59 PM - Seem to have this redrawing correctly!
        //double yy = yAxis.valueToJava2D(y, ch.getPlot(), yAxisEdge);
        if (plot.getOrientation() == PlotOrientation.VERTICAL) {
          drawHorizontalCrosshair(g2, dataAreaH, yy, ch);
        } else {
          drawVerticalCrosshair(g2, dataArea, yy, ch);
        }
      }
    }
  }

  public void paintOverlay(Graphics2D g2, ChartPanel chartPanel) {
    Shape savedClip = g2.getClip();
    Rectangle2D dataArea = chartPanel.getScreenDataArea();
    PtsChart chart = ((PtsChartPanel) chartPanel).getChart();
    PlotRenderingInfo plotInfo = chartPanel.getChartRenderingInfo().getPlotInfo();
    Rectangle2D dataAreaH = chartPanel.scale(plotInfo.getSubplotInfo(0).getDataArea());
    g2.clip(dataArea);
    XYPlot plot = (XYPlot) chart.getPricePlot();
    paintEachPanel(plot, g2, dataArea, dataAreaH);


    //Next is culled from ChartPanel.getScreenDataArea(int x, int y)
    //The above call gets the data area for the top chart, but not the subplot


//    PlotRenderingInfo plotInfo = chartPanel.getChartRenderingInfo().getPlotInfo();
//    Rectangle2D dataAreaADX = chartPanel.scale(plotInfo.getSubplotInfo(1).getDataArea());
//    g2.clip(dataAreaADX);
//
//
//    plot = (XYPlot) chart.getPlotADX();
//    Range rg = plot.getDataRange(plot.getRangeAxis());


    //paintEachPanel(plot, g2, dataAreaADX);

//    ValueAxis xAxis = plot.getDomainAxis();
//    RectangleEdge xAxisEdge = plot.getDomainAxisEdge();
//    Iterator iterator = this.xCrosshairs.iterator();
//    while (iterator.hasNext()) {
//      Crosshair ch = (Crosshair) iterator.next();
//      if (ch.isVisible()) { //Date axis, along bottom, so vertical line
//        double x = ch.getValue();
//        double xx = xAxis.valueToJava2D(x, dataArea, xAxisEdge);
//        if (plot.getOrientation() == PlotOrientation.VERTICAL) {
//          drawVerticalCrosshair(g2, dataArea, xx, ch);
//        } else {
//          drawHorizontalCrosshair(g2, dataArea, xx, ch);
//        }
//      }
//    }
//    ValueAxis yAxis = plot.getRangeAxis();
//    RectangleEdge yAxisEdge = plot.getRangeAxisEdge();
//    iterator = this.yCrosshairs.iterator();
//    while (iterator.hasNext()) {
//      Crosshair ch = (Crosshair) iterator.next();
//      if (ch.isVisible()) { //here is the horizontal line(s)
//        double y = ch.getValue();
//        double yy = yAxis.valueToJava2D(y, dataArea, yAxisEdge);
//        if (plot.getOrientation() == PlotOrientation.VERTICAL) {
//          drawHorizontalCrosshair(g2, dataArea, yy, ch);
//        } else {
//          drawVerticalCrosshair(g2, dataArea, yy, ch);
//        }
//      }
//    }
//    g2.setClip(savedClip);
  }

  /**
   * Draws a crosshair horizontally across the plot.
   *
   * @param g2  the graphics target.
   * @param dataArea  the data area.
   * @param y  the y-value in Java2D space.
   * @param crosshair  the crosshair
   * //rpc - NOTE:7/17/10 5:55 PM - This is problematic, doesn't redraw correctly because
   * of subchart mapping problem
   * */
  //rpc - NOTE:7/19/10 8:00 AM - the y value is not right on redraws, only the first time,
  protected void drawHorizontalCrosshair(Graphics2D g2, Rectangle2D dataArea,
          double y, PtsCrosshair crosshair) {
//    PlotRenderingInfo plotInfo = panel.getChartRenderingInfo().getPlotInfo();
//    dataArea = panel.scale(plotInfo.getSubplotInfo(0).getDataArea());
//    g2.clip(dataArea);
    if (y >= dataArea.getMinY() && y <= dataArea.getMaxY()) {
      //System.out.println("CHOverlayADX.drawHorizontalCrosshair, y = " + y);
      Line2D line = new Line2D.Double(dataArea.getMinX(), y,
              dataArea.getMaxX(), y);
      Paint savedPaint = g2.getPaint();
      Stroke savedStroke = g2.getStroke();
      g2.setPaint(crosshair.getPaint());
      g2.setStroke(crosshair.getStroke());
      g2.draw(line);
      if (crosshair.isLabelVisible()) {
        Font savedFont = g2.getFont();
        g2.setFont(crosshair.getLabelFont());
        String label = crosshair.getLabelGenerator().generateLabel(
                crosshair);
        RectangleAnchor anchor = crosshair.getLabelAnchor();
        Point2D pt = calculateLabelPoint(line, anchor, 5, 5);
        float xx = (float) pt.getX();
        float yy = (float) pt.getY();
        TextAnchor alignPt = textAlignPtForLabelAnchorH(anchor);
        Shape hotspot = TextUtilities.calculateRotatedStringBounds(
                label, g2, xx, yy, alignPt, 0.0, TextAnchor.CENTER);
        if (!dataArea.contains(hotspot.getBounds2D())) {
          anchor = flipAnchorV(anchor);
          pt = calculateLabelPoint(line, anchor, 5, 5);
          xx = (float) pt.getX();
          yy = (float) pt.getY();
          alignPt = textAlignPtForLabelAnchorH(anchor);
          hotspot = TextUtilities.calculateRotatedStringBounds(
                  label, g2, xx, yy, alignPt, 0.0, TextAnchor.CENTER);
        }
        g2.setPaint(crosshair.getLabelBackgroundPaint());
        g2.fill(hotspot);
        g2.setPaint(crosshair.getLabelOutlinePaint());
        g2.draw(hotspot);
        TextUtilities.drawAlignedString(label, g2, xx, yy, alignPt);
        g2.setFont(savedFont);
      }
      g2.setPaint(savedPaint);
      g2.setStroke(savedStroke);
    }
  }

  /**
   * Draws a crosshair vertically on the plot.
   *
   * @param g2  the graphics target.
   * @param dataArea  the data area.
   * @param x  the x-value in Java2D space.
   * @param crosshair  the crosshair.
   */
  protected void drawVerticalCrosshair(Graphics2D g2, Rectangle2D dataArea,
          double x, PtsCrosshair crosshair) {

    if (x >= dataArea.getMinX() && x <= dataArea.getMaxX()) {
      Line2D line = new Line2D.Double(x, dataArea.getMinY(), x,
              dataArea.getMaxY());
      Paint savedPaint = g2.getPaint();
      Stroke savedStroke = g2.getStroke();
      g2.setPaint(crosshair.getPaint());
      g2.setStroke(crosshair.getStroke());
      g2.draw(line);
      if (crosshair.isLabelVisible()) {
        Font savedFont = g2.getFont();
        g2.setFont(crosshair.getLabelFont());
        String label = crosshair.getLabelGenerator().generateLabel(
                crosshair);
        RectangleAnchor anchor = crosshair.getLabelAnchor();
        Point2D pt = calculateLabelPoint(line, anchor, 5, 5);
        float xx = (float) pt.getX();
        float yy = (float) pt.getY();
        TextAnchor alignPt = textAlignPtForLabelAnchorV(anchor);
        Shape hotspot = TextUtilities.calculateRotatedStringBounds(
                label, g2, xx, yy, alignPt, 0.0, TextAnchor.CENTER);
        if (!dataArea.contains(hotspot.getBounds2D())) {
          anchor = flipAnchorH(anchor);
          pt = calculateLabelPoint(line, anchor, 5, 5);
          xx = (float) pt.getX();
          yy = (float) pt.getY();
          alignPt = textAlignPtForLabelAnchorV(anchor);
          hotspot = TextUtilities.calculateRotatedStringBounds(
                  label, g2, xx, yy, alignPt, 0.0, TextAnchor.CENTER);
        }
        g2.setPaint(crosshair.getLabelBackgroundPaint());
        g2.fill(hotspot);
        g2.setPaint(crosshair.getLabelOutlinePaint());
        g2.draw(hotspot);
        TextUtilities.drawAlignedString(label, g2, xx, yy, alignPt);
        g2.setFont(savedFont);
      }
      g2.setPaint(savedPaint);
      g2.setStroke(savedStroke);
    }
  }

  /**
   * Calculates the anchor point for a label.
   *
   * @param line  the line for the crosshair.
   * @param anchor  the anchor point.
   * @param deltaX  the x-offset.
   * @param deltaY  the y-offset.
   *
   * @return The anchor point.
   */
  private Point2D calculateLabelPoint(Line2D line, RectangleAnchor anchor,
          double deltaX, double deltaY) {
    double x = 0.0;
    double y = 0.0;
    boolean left = (anchor == RectangleAnchor.BOTTOM_LEFT
            || anchor == RectangleAnchor.LEFT
            || anchor == RectangleAnchor.TOP_LEFT);
    boolean right = (anchor == RectangleAnchor.BOTTOM_RIGHT
            || anchor == RectangleAnchor.RIGHT
            || anchor == RectangleAnchor.TOP_RIGHT);
    boolean top = (anchor == RectangleAnchor.TOP_LEFT
            || anchor == RectangleAnchor.TOP
            || anchor == RectangleAnchor.TOP_RIGHT);
    boolean bottom = (anchor == RectangleAnchor.BOTTOM_LEFT
            || anchor == RectangleAnchor.BOTTOM
            || anchor == RectangleAnchor.BOTTOM_RIGHT);
    Rectangle rect = line.getBounds();
    Point2D pt = RectangleAnchor.coordinates(rect, anchor);
    // we expect the line to be vertical or horizontal
    if (line.getX1() == line.getX2()) {  // vertical
      x = line.getX1();
      y = (line.getY1() + line.getY2()) / 2.0;
      if (left) {
        x = x - deltaX;
      }
      if (right) {
        x = x + deltaX;
      }
      if (top) {
        y = Math.min(line.getY1(), line.getY2()) + deltaY;
      }
      if (bottom) {
        y = Math.max(line.getY1(), line.getY2()) - deltaY;
      }
    } else {  // horizontal
      x = (line.getX1() + line.getX2()) / 2.0;
      y = line.getY1();
      if (left) {
        x = Math.min(line.getX1(), line.getX2()) + deltaX;
      }
      if (right) {
        x = Math.max(line.getX1(), line.getX2()) - deltaX;
      }
      if (top) {
        y = y - deltaY;
      }
      if (bottom) {
        y = y + deltaY;
      }
    }
    return new Point2D.Double(x, y);
  }

  /**
   * Returns the text anchor that is used to align a label to its anchor
   * point.
   *
   * @param anchor  the anchor.
   *
   * @return The text alignment point.
   */
  private TextAnchor textAlignPtForLabelAnchorV(RectangleAnchor anchor) {
    TextAnchor result = TextAnchor.CENTER;
    if (anchor.equals(RectangleAnchor.TOP_LEFT)) {
      result = TextAnchor.TOP_RIGHT;
    } else if (anchor.equals(RectangleAnchor.TOP)) {
      result = TextAnchor.TOP_CENTER;
    } else if (anchor.equals(RectangleAnchor.TOP_RIGHT)) {
      result = TextAnchor.TOP_LEFT;
    } else if (anchor.equals(RectangleAnchor.LEFT)) {
      result = TextAnchor.HALF_ASCENT_RIGHT;
    } else if (anchor.equals(RectangleAnchor.RIGHT)) {
      result = TextAnchor.HALF_ASCENT_LEFT;
    } else if (anchor.equals(RectangleAnchor.BOTTOM_LEFT)) {
      result = TextAnchor.BOTTOM_RIGHT;
    } else if (anchor.equals(RectangleAnchor.BOTTOM)) {
      result = TextAnchor.BOTTOM_CENTER;
    } else if (anchor.equals(RectangleAnchor.BOTTOM_RIGHT)) {
      result = TextAnchor.BOTTOM_LEFT;
    }
    return result;
  }

  /**
   * Returns the text anchor that is used to align a label to its anchor
   * point.
   *
   * @param anchor  the anchor.
   *
   * @return The text alignment point.
   */
  private TextAnchor textAlignPtForLabelAnchorH(RectangleAnchor anchor) {
    TextAnchor result = TextAnchor.CENTER;
    if (anchor.equals(RectangleAnchor.TOP_LEFT)) {
      result = TextAnchor.BOTTOM_LEFT;
    } else if (anchor.equals(RectangleAnchor.TOP)) {
      result = TextAnchor.BOTTOM_CENTER;
    } else if (anchor.equals(RectangleAnchor.TOP_RIGHT)) {
      result = TextAnchor.BOTTOM_RIGHT;
    } else if (anchor.equals(RectangleAnchor.LEFT)) {
      result = TextAnchor.HALF_ASCENT_LEFT;
    } else if (anchor.equals(RectangleAnchor.RIGHT)) {
      result = TextAnchor.HALF_ASCENT_RIGHT;
    } else if (anchor.equals(RectangleAnchor.BOTTOM_LEFT)) {
      result = TextAnchor.TOP_LEFT;
    } else if (anchor.equals(RectangleAnchor.BOTTOM)) {
      result = TextAnchor.TOP_CENTER;
    } else if (anchor.equals(RectangleAnchor.BOTTOM_RIGHT)) {
      result = TextAnchor.TOP_RIGHT;
    }
    return result;
  }

  private RectangleAnchor flipAnchorH(RectangleAnchor anchor) {
    RectangleAnchor result = anchor;
    if (anchor.equals(RectangleAnchor.TOP_LEFT)) {
      result = RectangleAnchor.TOP_RIGHT;
    } else if (anchor.equals(RectangleAnchor.TOP_RIGHT)) {
      result = RectangleAnchor.TOP_LEFT;
    } else if (anchor.equals(RectangleAnchor.LEFT)) {
      result = RectangleAnchor.RIGHT;
    } else if (anchor.equals(RectangleAnchor.RIGHT)) {
      result = RectangleAnchor.LEFT;
    } else if (anchor.equals(RectangleAnchor.BOTTOM_LEFT)) {
      result = RectangleAnchor.BOTTOM_RIGHT;
    } else if (anchor.equals(RectangleAnchor.BOTTOM_RIGHT)) {
      result = RectangleAnchor.BOTTOM_LEFT;
    }
    return result;
  }

  private RectangleAnchor flipAnchorV(RectangleAnchor anchor) {
    RectangleAnchor result = anchor;
    if (anchor.equals(RectangleAnchor.TOP_LEFT)) {
      result = RectangleAnchor.BOTTOM_LEFT;
    } else if (anchor.equals(RectangleAnchor.TOP_RIGHT)) {
      result = RectangleAnchor.BOTTOM_RIGHT;
    } else if (anchor.equals(RectangleAnchor.TOP)) {
      result = RectangleAnchor.BOTTOM;
    } else if (anchor.equals(RectangleAnchor.BOTTOM)) {
      result = RectangleAnchor.TOP;
    } else if (anchor.equals(RectangleAnchor.BOTTOM_LEFT)) {
      result = RectangleAnchor.TOP_LEFT;
    } else if (anchor.equals(RectangleAnchor.BOTTOM_RIGHT)) {
      result = RectangleAnchor.TOP_RIGHT;
    }
    return result;
  }

  /**
   * Tests this overlay for equality with an arbitrary object.
   *
   * @param obj  the object (<code>null</code> permitted).
   *
   * @return A boolean.
   */
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof PtsCrosshairOverlay)) {
      return false;
    }
    PtsCrosshairOverlay that = (PtsCrosshairOverlay) obj;
    if (!this.xCrosshairs.equals(that.xCrosshairs)) {
      return false;
    }
    if (!this.yCrosshairs.equals(that.yCrosshairs)) {
      return false;
    }
    return true;
  }

  /**
   * Returns a clone of this instance.
   *
   * @return A clone of this instance.
   *
   * @throws java.lang.CloneNotSupportedException if there is some problem
   *     with the cloning.
   */
  public Object clone() throws CloneNotSupportedException {
    PtsCrosshairOverlay clone = (PtsCrosshairOverlay) super.clone();
    clone.xCrosshairs = (List) ObjectUtilities.deepClone(this.xCrosshairs);
    clone.yCrosshairs = (List) ObjectUtilities.deepClone(this.yCrosshairs);
    return clone;
  }
}
