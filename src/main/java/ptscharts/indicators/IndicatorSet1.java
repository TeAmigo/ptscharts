
/**
 * IndicatorSet1.java Created Mar 2, 2011 by rickcharon.
 *
 */


package ptscharts.indicators;

import java.awt.Color;
import ptscharts.PtsChart;
import ptscharts.PtsChartFactory;
import ptscharts.PtsSymbolInfos;



public class IndicatorSet1 {

public static PtsChart run(PtsChartFactory factory, String symbol, String beginDate, String endDate, int period) {
    PtsChart chartind = factory.createPtsChart(symbol, beginDate, endDate, period);
    IndicatorGroup indGroup = new IndicatorGroup(chartind);
    int cnt = chartind.getPricePlotDatasetCount();


    ChartIndicator ci20 = new ChartIndicator(indGroup, IndicatorType.SMA, PtsChart.plotTypes.PricePlot);
    ci20.setOptInTimePeriod(20);
    ci20.setLineColor(Color.RED);
    ci20.setupRenderer();
     
    ChartIndicator ci70 = new ChartIndicator(indGroup, IndicatorType.SMA, PtsChart.plotTypes.PricePlot);
    ci70.setOptInTimePeriod(70);
    ci70.setLineColor(Color.GREEN);
    ci70.setupRenderer();
    
    
    
    ChartIndicator cci = new ChartIndicator(indGroup, IndicatorType.CCI, PtsChart.plotTypes.Indicator2Plot);
    cci.setOptInTimePeriod(14);
    cci.setupRangeAxis("CCI");
    cci.setLineColor(Color.BLUE);
    cci.addHorizontalLine(100, Color.red, "100", 1.5);
    cci.addHorizontalLine(-100, Color.red, "-100", 1.5);
    cci.setupRenderer();


    ChartIndicator adosc = new ChartIndicator(indGroup, IndicatorType.ADOSC, PtsChart.plotTypes.Indicator3Plot);
    adosc.setupRangeAxis("ADOSC");
    adosc.setLineColor(Color.BLACK);
    adosc.setOptInFastPeriod(3);
    adosc.setOptInSlowPeriod(10);
    adosc.addHorizontalLine(0, Color.red, "0", 1.5);
    adosc.setupRenderer();


    ChartIndicator dx14 = new ChartIndicator(indGroup, IndicatorType.DX, PtsChart.plotTypes.Indicator1Plot);
    dx14.setOptInTimePeriod(14);
    dx14.setupRangeAxis("ADX");
    dx14.setLineColor(Color.BLACK);
    dx14.setupRenderer();
    dx14.addHorizontalLine(50, Color.red, "50", 1.5);


    ChartIndicator diminus14 = new ChartIndicator(indGroup, IndicatorType.MINUSDI, PtsChart.plotTypes.Indicator1Plot);
    diminus14.setOptInTimePeriod(14);
    diminus14.setLineColor(Color.RED);
    diminus14.setupRenderer();

    ChartIndicator diplus14 = new ChartIndicator(indGroup, IndicatorType.PLUSDI, PtsChart.plotTypes.Indicator1Plot);
    diplus14.setOptInTimePeriod(14);
    diplus14.setLineColor(Color.BLUE);
    diplus14.setupRenderer();


    indGroup.runIndicatorsInitial();
    chartind.show();
    return chartind;
  }

}
