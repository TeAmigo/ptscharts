/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ptscharts.indicators;

import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;
import java.util.EnumSet;

/**
 *
 * @author rickcharon
 */
public class Indicator {

  private Core lib;
  private int startIdx;
  private int endIdx;
  private double[] inDouble = null;
  private double[] inOpen = null;
  private double[] inHigh = null;
  private double[] inLow = null;
  private double[] inClose = null;
  private double[] inVolume = null;
  private int optInTimePeriod;
  private int optInFastPeriod;
  private int optInSlowPeriod;
  private MInteger outBegIdx = new MInteger();
  private MInteger outNbElement = new MInteger();
  double[] output = null;
  private int lookback;
  private RetCode retCode;
  IndicatorGroup myGroup = null;

  public IndicatorGroup getMyGroup() {
    return myGroup;
  }

  public void setMyGroup(IndicatorGroup myGroup) {
    this.myGroup = myGroup;
  }

  public MInteger getOutBegIdx() {
    return outBegIdx;
  }

  public MInteger getOutNbElement() {
    return outNbElement;
  }

  public int getEndIdx() {
    return endIdx;
  }

  public void setEndIdx(int endIdx) {
    this.endIdx = endIdx;
  }

  public double[] getInOpen() {
    return inOpen;
  }

  public void setInOpen(double[] inOpen) {
    this.inOpen = inOpen;
  }

  public double[] getInClose() {
    return inClose;
  }

  public void setInClose(double[] inClose) {
    this.inClose = inClose;
  }

  public void appendToInClose(double[] newVals) {
  }

  public double[] getInDouble() {
    return inDouble;
  }

  public void setInDouble(double[] inDouble) {
    this.inDouble = inDouble;
  }

  public double[] getInHigh() {
    return inHigh;
  }

  public void setInHigh(double[] inHigh) {
    this.inHigh = inHigh;
  }

  public double[] getInLow() {
    return inLow;
  }

  public void setInLow(double[] inLow) {
    this.inLow = inLow;
  }

  public double[] getInVolume() {
    return inVolume;
  }

  public void setInVolume(double[] inVolume) {
    this.inVolume = inVolume;
  }

  public int getLookback() {
    return lookback;
  }

  public void setLookback(int lookback) {
    this.lookback = lookback;
  }

  
  public int getOptInTimePeriod() {
    return optInTimePeriod;
  }

  public void setOptInTimePeriod(int period) {
    this.optInTimePeriod = period;
  }

  public int getOptInFastPeriod() {
    return optInFastPeriod;
  }

  public void setOptInFastPeriod(int optInFastPeriod) {
    this.optInFastPeriod = optInFastPeriod;
  }

  public int getOptInSlowPeriod() {
    return optInSlowPeriod;
  }

  public void setOptInSlowPeriod(int optInSlowPeriod) {
    this.optInSlowPeriod = optInSlowPeriod;
  }

  

  public int getStartIdx() {
    return startIdx;
  }

  public void setStartIdx(int startIdx) {
    this.startIdx = startIdx;
  }

  public double[] getOutput() {
    return output;
  }

  public void setOutput(double[] output) {
    this.output = output;
  }

  public Indicator() {
    lib = new Core();
    //output = new double[inClose.length];
    outBegIdx = new MInteger();
    outNbElement = new MInteger();
    EnumSet es;
  }

  public String runIndicator(IndicatorType id) {

    switch (id) {
      case AD:
        retCode = lib.ad(startIdx, endIdx, inHigh, inLow, inClose, inVolume, outBegIdx, outNbElement, output);
        break;
      case ADOSC:
        retCode = lib.adOsc(startIdx, endIdx, inHigh, inLow, inClose, inVolume,
                optInFastPeriod, optInSlowPeriod, outBegIdx, outNbElement, output);
        break;
      case CCI:
        retCode = lib.cci(startIdx, endIdx, inHigh, inLow, inClose, optInTimePeriod, outBegIdx, outNbElement, output);
        break;
      case SMA:
        retCode = lib.sma(startIdx, endIdx, inClose, optInTimePeriod, outBegIdx, outNbElement, output);
        break;
      case DX:
        retCode = lib.dx(startIdx, endIdx, inHigh, inLow, inClose, optInTimePeriod, outBegIdx, outNbElement, output);
        break;
      case ADX:
        retCode = lib.adx(startIdx, endIdx, inHigh, inLow, inClose, optInTimePeriod, outBegIdx, outNbElement, output);
        break;
      case MINUSDI:
        retCode = lib.minusDI(startIdx, endIdx, inHigh, inLow, inClose, optInTimePeriod, outBegIdx, outNbElement, output);
        break;
      case PLUSDI:
        retCode = lib.plusDI(startIdx, endIdx, inHigh, inLow, inClose, optInTimePeriod, outBegIdx, outNbElement, output);
        break;
    }
    return retCode.toString();
  }

  // 2/15/11 1:13 PM Currently only implemented in ChartIndicator, 
  public void intialRun() {
  }

  // 2/15/11 3:26 PM Currently only implemented in ChartIndicator, 
  public void jumpRun(int bars) {
  }

  public static void main(String args[]) {
    Indicator ind = new Indicator();
    double[] closePrice = new double[100];
    for (int i = 0; i < closePrice.length; i++) {
      closePrice[i] = (double) i;
    }
    ind.setInClose(closePrice);
    ind.setOutput(new double[ind.getInClose().length]);
    ind.setStartIdx(0);
    ind.setEndIdx(ind.getInClose().length - 1);
    ind.setOptInTimePeriod(30);
    String retStr = ind.runIndicator(IndicatorType.SMA);
    System.out.println("Return: " + retStr);
    for (int i = 0; i < ind.getOutNbElement().value; i++) {
      StringBuilder line = new StringBuilder();
      line.append("Period #");
      line.append(i + ind.outBegIdx.value);
      line.append(" close= ");
      line.append(closePrice[i + ind.getOutBegIdx().value]);
      line.append(" mov avg= ");
      line.append(ind.getOutput()[i]);
      System.out.println(line.toString());
    }


  }
}
