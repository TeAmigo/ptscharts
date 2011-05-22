/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ptscharts;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import org.jfree.chart.labels.StandardXYToolTipGenerator;

/**
 *
 * @author rickcharon
 */
public class PtsChartToolTipGenerators {

  public static StandardXYToolTipGenerator DatedToolTipGenerator() {
    return new StandardXYToolTipGenerator(
            "{0} : {1} = {2}", new SimpleDateFormat("MM/dd/yyyy HH:mm:ss"),
            new DecimalFormat("0.00"));
  }


}
