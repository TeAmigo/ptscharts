/**
 * IndicatorGroup.java Created Feb 15, 2011 by rickcharon.
 * Methods for operating on a group of indicators.
 *
 */
package ptscharts.indicators;

import ptsutils.OHLCVData;
import java.util.ArrayList;
import org.apache.commons.lang.ArrayUtils;
import org.jfree.data.time.RegularTimePeriod;
import ptscharts.PtsChart;

public class IndicatorGroup {

  ArrayList<Indicator> indicators = new ArrayList<Indicator>();
  private double[] opens = null;
  private double[] closes = null;
  private double[] highs = null;
  private double[] lows = null;
  private double[] volumes = null;
  private ArrayList<RegularTimePeriod> periods = null;
  PtsChart myChart = null;
  private int jumpBars;

  public int getJumpBars() {
    return jumpBars;
  }

  public void setJumpBars(int jumpBars) {
    this.jumpBars = jumpBars;
  }

  public double[] getCloses() {
    return closes;
  }

  public double[] getCloses(int beginIdx) {
    double tmp[] = ArrayUtils.subarray(closes, beginIdx, closes.length);
    return tmp;
  }

  public void setCloses(double[] closes) {
    this.closes = closes;
  }

  public double[] getHighs() {
    return highs;
  }

  public void setHighs(double[] highs) {
    this.highs = highs;
  }

  public ArrayList<Indicator> getIndicators() {
    return indicators;
  }

  public void setIndicators(ArrayList<Indicator> indicators) {
    this.indicators = indicators;
  }

  public double[] getLows() {
    return lows;
  }

  public void setLows(double[] lows) {
    this.lows = lows;
  }

  public double[] getOpens() {
    return opens;
  }

  public void setOpens(double[] opens) {
    this.opens = opens;
  }

  public ArrayList<RegularTimePeriod> getPeriods() {
    return periods;
  }

  public ArrayList<RegularTimePeriod> getPeriods(int beginIdx) {
    ArrayList<RegularTimePeriod> periodsOut = new ArrayList<RegularTimePeriod>();
    for (int i = beginIdx; i < periods.size(); i++) {
      periodsOut.add(periods.get(i));
    }
    return periodsOut;
  }

  public void setPeriods(ArrayList<RegularTimePeriod> periods) {
    this.periods = periods;
  }

  public double[] getVolumes() {
    return volumes;
  }

  public void setVolumes(double[] volumes) {
    this.volumes = volumes;
  }

  public PtsChart getMyChart() {
    return myChart;
  }

  public void setMyChart(PtsChart myChart) {
    this.myChart = myChart;
  }

  public IndicatorGroup() {
  }

  public IndicatorGroup(PtsChart myChartIn) {
    myChart = myChartIn;
    myChart.setIndicators(this);
  }

  public void jumpRun(int bars) {
    if (!(myChart == null)) {
      OHLCVData data = myChart.getOhlcv().setupArrays();
      if (null == data.opens) {
        return;
      }
      for (int i = 0; i < data.opens.length; i++) {
        opens = ArrayUtils.add(opens, data.opens[i]);
        closes = ArrayUtils.add(closes, data.closes[i]);
        highs = ArrayUtils.add(highs, data.highs[i]);
        lows = ArrayUtils.add(lows, data.lows[i]);
        volumes = ArrayUtils.add(volumes, data.volumes[i]);
        periods.add(data.periods.get(i));
      }
      jumpBars = closes.length - bars;
    }
    for (Indicator ind : indicators) {
      ind.setInOpen(ArrayUtils.subarray(opens, 
              (jumpBars - Math.max(ind.getOptInTimePeriod(), ind.getOptInSlowPeriod())), opens.length));
      ind.setInHigh(ArrayUtils.subarray(highs, 
              (jumpBars - Math.max(ind.getOptInTimePeriod(), ind.getOptInSlowPeriod())), highs.length));
      ind.setInLow(ArrayUtils.subarray(lows, 
              (jumpBars - Math.max(ind.getOptInTimePeriod(), ind.getOptInSlowPeriod())), lows.length));
      ind.setInClose(ArrayUtils.subarray(closes, 
              (jumpBars - Math.max(ind.getOptInTimePeriod(), ind.getOptInSlowPeriod())), closes.length));
      ind.setInVolume(ArrayUtils.subarray(volumes, 
              (jumpBars - Math.max(ind.getOptInTimePeriod(), ind.getOptInSlowPeriod())), volumes.length));
      ind.setOutput(new double[ind.getInClose().length]);
      ind.setStartIdx(0);
      ind.setEndIdx(ind.getInClose().length - 1);
      ind.jumpRun(bars);
    }
  }

  public void runIndicatorsInitial() {
    if (!(myChart == null)) {
      OHLCVData data = myChart.getOhlcv().setupArrays();
      opens = data.opens;
      closes = data.closes;
      highs = data.highs;
      lows = data.lows;
      volumes = data.volumes;
      periods = data.periods;
    }
    for (Indicator ind : indicators) {
      ind.setInOpen(opens);
      ind.setInHigh(highs);
      ind.setInLow(lows);
      ind.setInClose(closes);
      ind.setInVolume(volumes);
      ind.setOutput(new double[ind.getInClose().length]);
      ind.setStartIdx(0);
      ind.setEndIdx(ind.getInClose().length - 1);
      ind.intialRun();
    }
  }

  public void addIndicator(ChartIndicator newIndicator) {
    indicators.add(newIndicator);
    newIndicator.setMyGroup(this);
  }
}
