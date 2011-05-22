/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ptscharts;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Stroke;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import org.jfree.chart.HashUtilities;
import org.jfree.chart.labels.CrosshairLabelGenerator;
import org.jfree.chart.labels.StandardCrosshairLabelGenerator;
import org.jfree.chart.plot.Crosshair;
import org.jfree.chart.plot.XYPlot;
import org.jfree.io.SerialUtilities;
import org.jfree.ui.RectangleAnchor;
import org.jfree.util.PaintUtilities;
import org.jfree.util.PublicCloneable;

/**
 * //rpc - NOTE:7/19/10 6:52 AM - Taken from Crosshair in JFreeChart, need plot info embedded.
 * 
 */
public class PtsCrosshair extends Crosshair implements Cloneable, PublicCloneable, Serializable {

  private XYPlot plot;

  /** Flag controlling visibility. */
  private boolean visible;
  /** The crosshair value. */
  private double value;
  /** The paint for the crosshair line. */
  private transient Paint paint;
  /** The stroke for the crosshair line. */
  private transient Stroke stroke;
  /**
   * A flag that controls whether or not the crosshair has a label
   * visible.
   */
  private boolean labelVisible;
  /**
   * The label anchor.
   */
  private RectangleAnchor labelAnchor;
  /** A label generator. */
  private CrosshairLabelGenerator labelGenerator;
  /**
   * The x-offset in Java2D units.
   */
  private double labelXOffset;
  /**
   * The y-offset in Java2D units.
   */
  private double labelYOffset;
  /**
   * The label font.
   */
  private Font labelFont;
  /**
   * The label paint.
   */
  private transient Paint labelPaint;
  /**
   * The label background paint.
   */
  private transient Paint labelBackgroundPaint;
  /** A flag that controls the visibility of the label outline. */
  private boolean labelOutlineVisible;
  /** The label outline stroke. */
  private transient Stroke labelOutlineStroke;
  /** The label outline paint. */
  private transient Paint labelOutlinePaint;
  /** Property change support. */
  private transient PropertyChangeSupport pcs;

  private boolean stopLossCrosshair = false;
  private boolean profitExitCrosshair = false;
  private boolean buyCrosshair = false;
  private boolean sellCrosshair = false;
  private PtsCrosshair vertHairPartner = null;

  public PtsCrosshair getVertHairPartner() {
    return vertHairPartner;
  }

  public void setVertHairPartner(PtsCrosshair vertHairPartner) {
    this.vertHairPartner = vertHairPartner;
  }

  

  public boolean isBuyCrosshair() {
    return buyCrosshair;
  }

  public void setBuyCrosshair(boolean buyCrosshair) {
    this.buyCrosshair = buyCrosshair;
  }

  public boolean isSellCrosshair() {
    return sellCrosshair;
  }

  public void setSellCrosshair(boolean sellCrosshair) {
    this.sellCrosshair = sellCrosshair;
  }
  
  public boolean isProfitStopCrosshair() {
    return profitExitCrosshair;
  }

  public void setProfitStopCrosshair(boolean profitStopCrosshair) {
    this.profitExitCrosshair = profitStopCrosshair;
  }

  public boolean isStopLossCrosshair() {
    return stopLossCrosshair;
  }

  public void setStopLossCrosshair(boolean stopLossCrosshair) {
    this.stopLossCrosshair = stopLossCrosshair;
  }



//  public XYPlot getPlot() {
//    return plot;
//  }



  /**
   * Creates a new crosshair with value 0.0.
   */
  public PtsCrosshair() {
    this(0.0);
  }

  /**
   * Creates a new crosshair with the specified value.
   *
   * @param value  the value.
   */
  public PtsCrosshair(double value) {
    this(value, Color.black, new BasicStroke(1.0f));
  }

  /**
   * Creates a new crosshair value with the specified value and line style.
   *
   * @param value  the value.
   * @param paint  the line paint (<code>null</code> not permitted).
   * @param stroke  the line stroke (<code>null</code> not permitted).
   */
  public PtsCrosshair(double value, Paint paint, Stroke stroke) {
    if (paint == null) {
      throw new IllegalArgumentException("Null 'paint' argument.");
    }
    if (stroke == null) {
      throw new IllegalArgumentException("Null 'stroke' argument.");
    }
    this.visible = true;
    this.value = value;
    this.paint = paint;
    this.stroke = stroke;
    this.labelVisible = false;
    this.labelGenerator = new StandardCrosshairLabelGenerator();
    this.labelAnchor = RectangleAnchor.BOTTOM_LEFT;
    this.labelXOffset = 3.0;
    this.labelYOffset = 3.0;
    this.labelFont = new Font("Tahoma", Font.PLAIN, 12);
    this.labelPaint = Color.black;
    this.labelBackgroundPaint = new Color(0, 0, 255, 63);
    this.labelOutlineVisible = true;
    this.labelOutlinePaint = Color.black;
    this.labelOutlineStroke = new BasicStroke(0.5f);
    this.pcs = new PropertyChangeSupport(this);
  }

  /**
   * Returns the flag that indicates whether or not the crosshair is
   * currently visible.
   *
   * @return A boolean.
   */
  public boolean isVisible() {
    return this.visible;
  }

  /**
   * Sets the flag that controls the visibility of the crosshair and sends
   * a proerty change event (with the name 'visible') to all registered
   * listeners.
   *
   * @param visible  the new flag value.
   */
  public void setVisible(boolean visible) {
    boolean old = this.visible;
    this.visible = visible;
    this.pcs.firePropertyChange("visible", old, visible);
  }

  /**
   * Returns the crosshair value.
   *
   * @return The crosshair value.
   */
  public double getValue() {
    return this.value;
  }

  /**
   * Sets the crosshair value and sends a property change event with the name
   * 'value' to all registered listeners.
   *
   * @param value  the value.
   */
  public void setValue(double value) {
    Double oldValue = new Double(this.value);
    this.value = value;
    this.pcs.firePropertyChange("value", oldValue, new Double(value));
  }

  /**
   * Returns the paint for the crosshair line.
   *
   * @return The paint (never <code>null</code>).
   */
  public Paint getPaint() {
    return this.paint;
  }

  /**
   * Sets the paint for the crosshair line and sends a property change event
   * with the name "paint" to all registered listeners.
   *
   * @param paint  the paint (<code>null</code> not permitted).
   */
  public void setPaint(Paint paint) {
    Paint old = this.paint;
    this.paint = paint;
    this.pcs.firePropertyChange("paint", old, paint);
  }

  /**
   * Returns the stroke for the crosshair line.
   *
   * @return The stroke (never <code>null</code>).
   */
  public Stroke getStroke() {
    return this.stroke;
  }

  /**
   * Sets the stroke for the crosshair line and sends a property change event
   * with the name "stroke" to all registered listeners.
   *
   * @param stroke  the stroke (<code>null</code> not permitted).
   */
  public void setStroke(Stroke stroke) {
    Stroke old = this.stroke;
    this.stroke = stroke;
    this.pcs.firePropertyChange("stroke", old, stroke);
  }

  /**
   * Returns the flag that controls whether or not a label is drawn for
   * this crosshair.
   *
   * @return A boolean.
   */
  public boolean isLabelVisible() {
    return this.labelVisible;
  }

  /**
   * Sets the flag that controls whether or not a label is drawn for the
   * crosshair and sends a property change event (with the name
   * 'labelVisible') to all registered listeners.
   *
   * @param visible  the new flag value.
   */
  public void setLabelVisible(boolean visible) {
    boolean old = this.labelVisible;
    this.labelVisible = visible;
    this.pcs.firePropertyChange("labelVisible", old, visible);
  }

  /**
   * Returns the crosshair label generator.
   *
   * @return The label crosshair generator (never <code>null</code>).
   */
  public CrosshairLabelGenerator getLabelGenerator() {
    return this.labelGenerator;
  }

  /**
   * Sets the crosshair label generator and sends a property change event
   * (with the name 'labelGenerator') to all registered listeners.
   *
   * @param generator  the new generator (<code>null</code> not permitted).
   */
  public void setLabelGenerator(CrosshairLabelGenerator generator) {
    if (generator == null) {
      throw new IllegalArgumentException("Null 'generator' argument.");
    }
    CrosshairLabelGenerator old = this.labelGenerator;
    this.labelGenerator = generator;
    this.pcs.firePropertyChange("labelGenerator", old, generator);
  }

  /**
   * Returns the label anchor point.
   *
   * @return the label anchor point (never <code>null</code>.
   */
  public RectangleAnchor getLabelAnchor() {
    return this.labelAnchor;
  }

  /**
   * Sets the label anchor point and sends a property change event (with the
   * name 'labelAnchor') to all registered listeners.
   *
   * @param anchor  the anchor (<code>null</code> not permitted).
   */
  public void setLabelAnchor(RectangleAnchor anchor) {
    RectangleAnchor old = this.labelAnchor;
    this.labelAnchor = anchor;
    this.pcs.firePropertyChange("labelAnchor", old, anchor);
  }

  /**
   * Returns the x-offset for the label (in Java2D units).
   *
   * @return The x-offset.
   */
  public double getLabelXOffset() {
    return this.labelXOffset;
  }

  /**
   * Sets the x-offset and sends a property change event (with the name
   * 'labelXOffset') to all registered listeners.
   *
   * @param offset  the new offset.
   */
  public void setLabelXOffset(double offset) {
    Double old = new Double(this.labelXOffset);
    this.labelXOffset = offset;
    this.pcs.firePropertyChange("labelXOffset", old, new Double(offset));
  }

  /**
   * Returns the y-offset for the label (in Java2D units).
   *
   * @return The y-offset.
   */
  public double getLabelYOffset() {
    return this.labelYOffset;
  }

  /**
   * Sets the y-offset and sends a property change event (with the name
   * 'labelYOffset') to all registered listeners.
   *
   * @param offset  the new offset.
   */
  public void setLabelYOffset(double offset) {
    Double old = new Double(this.labelYOffset);
    this.labelYOffset = offset;
    this.pcs.firePropertyChange("labelYOffset", old, new Double(offset));
  }

  /**
   * Returns the label font.
   *
   * @return The label font (never <code>null</code>).
   */
  public Font getLabelFont() {
    return this.labelFont;
  }

  /**
   * Sets the label font and sends a property change event (with the name
   * 'labelFont') to all registered listeners.
   *
   * @param font  the font (<code>null</code> not permitted).
   */
  public void setLabelFont(Font font) {
    if (font == null) {
      throw new IllegalArgumentException("Null 'font' argument.");
    }
    Font old = this.labelFont;
    this.labelFont = font;
    this.pcs.firePropertyChange("labelFont", old, font);
  }

  /**
   * Returns the label paint.
   *
   * @return The label paint (never <code>null</code>).
   */
  public Paint getLabelPaint() {
    return this.labelPaint;
  }

  /**
   * Sets the label paint and sends a property change event (with the name
   * 'labelPaint') to all registered listeners.
   *
   * @param paint  the paint (<code>null</code> not permitted).
   */
  public void setLabelPaint(Paint paint) {
    if (paint == null) {
      throw new IllegalArgumentException("Null 'paint' argument.");
    }
    Paint old = this.labelPaint;
    this.labelPaint = paint;
    this.pcs.firePropertyChange("labelPaint", old, paint);
  }

  /**
   * Returns the label background paint.
   *
   * @return The label background paint (possibly <code>null</code>).
   */
  public Paint getLabelBackgroundPaint() {
    return this.labelBackgroundPaint;
  }

  /**
   * Sets the label background paint and sends a property change event with
   * the name 'labelBackgroundPaint') to all registered listeners.
   *
   * @param paint  the paint (<code>null</code> permitted).
   */
  public void setLabelBackgroundPaint(Paint paint) {
    Paint old = this.labelBackgroundPaint;
    this.labelBackgroundPaint = paint;
    this.pcs.firePropertyChange("labelBackgroundPaint", old, paint);
  }

  /**
   * Returns the flag that controls the visibility of the label outline.
   *
   * @return A boolean.
   */
  public boolean isLabelOutlineVisible() {
    return this.labelOutlineVisible;
  }

  /**
   * Sets the flag that controls the visibility of the label outlines and
   * sends a property change event (with the name "labelOutlineVisible") to
   * all registered listeners.
   *
   * @param visible  the new flag value.
   */
  public void setLabelOutlineVisible(boolean visible) {
    boolean old = this.labelOutlineVisible;
    this.labelOutlineVisible = visible;
    this.pcs.firePropertyChange("labelOutlineVisible", old, visible);
  }

  /**
   * Returns the label outline paint.
   *
   * @return The label outline paint (never <code>null</code>).
   */
  public Paint getLabelOutlinePaint() {
    return this.labelOutlinePaint;
  }

  /**
   * Sets the label outline paint and sends a property change event (with the
   * name "labelOutlinePaint") to all registered listeners.
   *
   * @param paint  the paint (<code>null</code> not permitted).
   */
  public void setLabelOutlinePaint(Paint paint) {
    if (paint == null) {
      throw new IllegalArgumentException("Null 'paint' argument.");
    }
    Paint old = this.labelOutlinePaint;
    this.labelOutlinePaint = paint;
    this.pcs.firePropertyChange("labelOutlinePaint", old, paint);
  }

  /**
   * Returns the label outline stroke.
   *
   * @return The label outline stroke (never <code>null</code>).
   */
  public Stroke getLabelOutlineStroke() {
    return this.labelOutlineStroke;
  }

  /**
   * Sets the label outline stroke and sends a property change event (with
   * the name 'labelOutlineStroke') to all registered listeners.
   *
   * @param stroke  the stroke (<code>null</code> not permitted).
   */
  public void setLabelOutlineStroke(Stroke stroke) {
    if (stroke == null) {
      throw new IllegalArgumentException("Null 'stroke' argument.");
    }
    Stroke old = this.labelOutlineStroke;
    this.labelOutlineStroke = stroke;
    this.pcs.firePropertyChange("labelOutlineStroke", old, stroke);
  }

  /**
   * Tests this crosshair for equality with an arbitrary object.
   *
   * @param obj  the object (<code>null</code> permitted).
   *
   * @return A boolean.
   */
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof PtsCrosshair)) {
      return false;
    }
    PtsCrosshair that = (PtsCrosshair) obj;
    if (this.visible != that.visible) {
      return false;
    }
    if (this.value != that.value) {
      return false;
    }
    if (!PaintUtilities.equal(this.paint, that.paint)) {
      return false;
    }
    if (!this.stroke.equals(that.stroke)) {
      return false;
    }
    if (this.labelVisible != that.labelVisible) {
      return false;
    }
    if (!this.labelGenerator.equals(that.labelGenerator)) {
      return false;
    }
    if (!this.labelAnchor.equals(that.labelAnchor)) {
      return false;
    }
    if (this.labelXOffset != that.labelXOffset) {
      return false;
    }
    if (this.labelYOffset != that.labelYOffset) {
      return false;
    }
    if (!this.labelFont.equals(that.labelFont)) {
      return false;
    }
    if (!PaintUtilities.equal(this.labelPaint, that.labelPaint)) {
      return false;
    }
    if (!PaintUtilities.equal(this.labelBackgroundPaint,
            that.labelBackgroundPaint)) {
      return false;
    }
    if (this.labelOutlineVisible != that.labelOutlineVisible) {
      return false;
    }
    if (!PaintUtilities.equal(this.labelOutlinePaint,
            that.labelOutlinePaint)) {
      return false;
    }
    if (!this.labelOutlineStroke.equals(that.labelOutlineStroke)) {
      return false;
    }
    return true;  // can't find any difference
  }

  /**
   * Returns a hash code for this instance.
   *
   * @return A hash code.
   */
  public int hashCode() {
    int hash = 7;
    hash = HashUtilities.hashCode(hash, this.visible);
    hash = HashUtilities.hashCode(hash, this.value);
    hash = HashUtilities.hashCode(hash, this.paint);
    hash = HashUtilities.hashCode(hash, this.stroke);
    hash = HashUtilities.hashCode(hash, this.labelVisible);
    hash = HashUtilities.hashCode(hash, this.labelAnchor);
    hash = HashUtilities.hashCode(hash, this.labelGenerator);
    hash = HashUtilities.hashCode(hash, this.labelXOffset);
    hash = HashUtilities.hashCode(hash, this.labelYOffset);
    hash = HashUtilities.hashCode(hash, this.labelFont);
    hash = HashUtilities.hashCode(hash, this.labelPaint);
    hash = HashUtilities.hashCode(hash, this.labelBackgroundPaint);
    hash = HashUtilities.hashCode(hash, this.labelOutlineVisible);
    hash = HashUtilities.hashCode(hash, this.labelOutlineStroke);
    hash = HashUtilities.hashCode(hash, this.labelOutlinePaint);
    return hash;
  }

  /**
   * Returns an independent copy of this instance.
   *
   * @return An independent copy of this instance.
   *
   * @throws java.lang.CloneNotSupportedException
   */
  public Object clone() throws CloneNotSupportedException {
    // FIXME: clone generator
    return super.clone();
  }

  /**
   * Adds a property change listener.
   *
   * @param l  the listener.
   */
  public void addPropertyChangeListener(PropertyChangeListener l) {
    this.pcs.addPropertyChangeListener(l);
  }

  /**
   * Removes a property change listener.
   *
   * @param l  the listener.
   */
  public void removePropertyChangeListener(PropertyChangeListener l) {
    this.pcs.removePropertyChangeListener(l);
  }

  /**
   * Provides serialization support.
   *
   * @param stream  the output stream.
   *
   * @throws IOException  if there is an I/O error.
   */
  private void writeObject(ObjectOutputStream stream) throws IOException {
    stream.defaultWriteObject();
    SerialUtilities.writePaint(this.paint, stream);
    SerialUtilities.writeStroke(this.stroke, stream);
    SerialUtilities.writePaint(this.labelPaint, stream);
    SerialUtilities.writePaint(this.labelBackgroundPaint, stream);
    SerialUtilities.writeStroke(this.labelOutlineStroke, stream);
    SerialUtilities.writePaint(this.labelOutlinePaint, stream);
  }

  /**
   * Provides serialization support.
   *
   * @param stream  the input stream.
   *
   * @throws IOException  if there is an I/O error.
   * @throws ClassNotFoundException  if there is a classpath problem.
   */
  private void readObject(ObjectInputStream stream)
          throws IOException, ClassNotFoundException {
    stream.defaultReadObject();
    this.paint = SerialUtilities.readPaint(stream);
    this.stroke = SerialUtilities.readStroke(stream);
    this.labelPaint = SerialUtilities.readPaint(stream);
    this.labelBackgroundPaint = SerialUtilities.readPaint(stream);
    this.labelOutlineStroke = SerialUtilities.readStroke(stream);
    this.labelOutlinePaint = SerialUtilities.readPaint(stream);
    this.pcs = new PropertyChangeSupport(this);
  }
}
