/*-***-   /share/JavaDev/PeTraSys/src/petrasys/chart/utils/SubPlotHorizontalCrosshairLabelGenerator.java  -***-

 * * Mon Jun 14 11:40:19 2010 rpc - 
 * 
 * Special Label Generator for subPlots, which need the Y value of CombinedPlot for drawing Y
 * crosshair, but sublot Y value for label.
 */


package ptscharts;

import java.io.Serializable;
import java.text.MessageFormat;
import java.text.NumberFormat;
import org.jfree.chart.labels.CrosshairLabelGenerator;
import org.jfree.chart.plot.Crosshair;


public class PtsSubPlotHorizontalCrosshairLabelGenerator implements CrosshairLabelGenerator,
                                                        Serializable {

  /** The label format string. */
  private String labelTemplate;

  /** A number formatter for the value. */
  private NumberFormat numberFormat;

  //rpc - NOTE:6/14/10 12:22 PM - Added for proper Subplot values
  private double subPlotValue;

  //rpc - NOTE:7/26/10 2:32 PM - This is to put in prefix string, like "BUY", "SELL", "STP", etc
  private String prefix;

  public String getPrefix() {
    return prefix;
  }

  public void setPrefix(String prefix) {
    this.prefix = prefix;
  }



  /**
   * Creates a new instance with default attributes.
   */
  public PtsSubPlotHorizontalCrosshairLabelGenerator(double subPlotValueIn) {
    //this("{0}", NumberFormat.getNumberInstance());
    this("{0}", NumberFormat.getIntegerInstance());
    subPlotValue = subPlotValueIn;
    prefix = "";
  }

  /**
   * Creates a new instance with the specified attributes.
   *
   * @param labelTemplate  the label template (<code>null</code> not
   *     permitted).
   * @param numberFormat  the number formatter (<code>null</code> not
   *     permitted).
   */
  public PtsSubPlotHorizontalCrosshairLabelGenerator(String labelTemplate,
                                         NumberFormat numberFormat) {
    super();
    if (labelTemplate == null) {
      throw new IllegalArgumentException(
                                         "Null 'labelTemplate' argument.");
    }
    if (numberFormat == null) {
      throw new IllegalArgumentException(
                                         "Null 'numberFormat' argument.");
    }
    this.labelTemplate = labelTemplate;
    this.numberFormat = numberFormat;
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
  public NumberFormat getNumberFormat() {
    return this.numberFormat;
  }

  /**
   * Returns a string that can be used as the label for a crosshair.
   *
   * @param crosshair  the crosshair (<code>null</code> not permitted).
   *
   * @return The label (possibly <code>null</code>).
   */
  public String generateLabel(Crosshair crosshair) {
    Object[] v = new Object[] { this.numberFormat.format(subPlotValue) };
    String result =  prefix + " " + MessageFormat.format(this.labelTemplate, v);
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
    if (!(obj instanceof PtsSubPlotHorizontalCrosshairLabelGenerator)) {
      return false;
    }
    PtsSubPlotHorizontalCrosshairLabelGenerator that
      = (PtsSubPlotHorizontalCrosshairLabelGenerator) obj;
    if (!this.labelTemplate.equals(that.labelTemplate)) {
      return false;
    }
    if (!this.numberFormat.equals(that.numberFormat)) {
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
