/*-***-   /share/JavaDev/PeTraSys/src/petrasys/chart/utils/SubPlotHorizontalCrosshairLabelGenerator.java  -***-

 * * Mon Jun 14 11:40:19 2010 rpc - 
 * 
 * Special Label Generator for subPlots, which need the Y value of CombinedPlot for drawing Y
 * crosshair, but sublot Y value for label.
 */


package ptscharts;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import org.jfree.chart.labels.CrosshairLabelGenerator;
import org.jfree.chart.plot.Crosshair;


public class PtsVerticalCrosshairLabelGenerator implements CrosshairLabelGenerator,
                                                        Serializable {

  /** The label format string. */
  private String labelTemplate;

  /** A number formatter for the value. */
  //private NumberFormat numberFormat;
  private DateFormat dateFormat;

  //rpc - NOTE:6/14/10 12:22 PM - Added for proper Subplot values
  private double value;

  /**
   * Creates a new instance with default attributes.
   */
  public PtsVerticalCrosshairLabelGenerator(double valueIn) {
    //this("{0}", NumberFormat.getNumberInstance());
    this("{0}", PtsDateOps.strFormat);
    value = valueIn;
  }

  /**
   * Creates a new instance with the specified attributes.
   *
   * @param labelTemplate  the label template (<code>null</code> not
   *     permitted).
   * @param numberFormat  the number formatter (<code>null</code> not
   *     permitted).
   */
  public PtsVerticalCrosshairLabelGenerator(String labelTemplate,
                                         DateFormat dateFormat) {
    super();
    if (labelTemplate == null) {
      throw new IllegalArgumentException(
                                         "Null 'labelTemplate' argument.");
    }
    if (dateFormat == null) {
      throw new IllegalArgumentException(
                                         "Null 'dateFormat' argument.");
    }
    this.labelTemplate = labelTemplate;
    this.dateFormat = dateFormat;
  }

  /**
   * Returns the label template string.
   *
   * @return The label template string (never <code>null</code>).
   */
  public String getLabelTemplate() {
    return this.labelTemplate;
  }

  /**
   * Returns the number formatter.
   *
   * @return The formatter (never <code>null</code>).
   */
  public DateFormat getDateFormat() {
    return this.dateFormat;
  }

  /**
   * Returns a string that can be used as the label for a crosshair.
   *
   * @param crosshair  the crosshair (<code>null</code> not permitted).
   *
   * @return The label (possibly <code>null</code>).
   */
  public String generateLabel(Crosshair crosshair) {
    Object[] v = new Object[] { this.dateFormat.format(value) };
    String result = MessageFormat.format(this.labelTemplate, v);
    return result;
  }

  /**
   * Tests this generator for equality with an arbitrary object.
   * 
   * @param obj  the object (<code>null</code> permitted).
   * 
   * @return A boolean.
   */
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof PtsVerticalCrosshairLabelGenerator)) {
      return false;
    }
    PtsVerticalCrosshairLabelGenerator that
      = (PtsVerticalCrosshairLabelGenerator) obj;
    if (!this.labelTemplate.equals(that.labelTemplate)) {
      return false;
    }
    if (!this.dateFormat.equals(that.dateFormat)) {
      return false;
    }
    return true;
  }

  /**
   * Returns a hash code for this instance.
   *
   * @return A hash code for this instance.
   */
  public int hashCode() {
    return this.labelTemplate.hashCode();
  }

}
