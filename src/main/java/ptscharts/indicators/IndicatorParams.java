/**
 * IndicatorParams.java Created Feb 12, 2011 by rickcharon.
 *
 */
package ptscharts.indicators;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

public class IndicatorParams {

  private int startIdx;
  private int endIdx;
  private int period;
  private double[] inDouble = null;
  private double[] inHigh = null;
  private double[] inLow = null;
  private double[] inClose = null;
  private double[] inVolume = null;
  private int optInTimePeriod;
  private MInteger outBegIdx = null;
  private MInteger outNbElement = null;
  private double[] output = null;
  private int lookback;
  RetCode retCode = null;
}
