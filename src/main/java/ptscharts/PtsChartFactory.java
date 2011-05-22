/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ptscharts;

import java.awt.Font;
import javax.swing.UIManager;
import org.apache.commons.math.util.MathUtils;
import org.joda.time.DateTime;

/**
 *
 * @author rickcharon
 */
public class PtsChartFactory {

  public PtsSymbolInfos symInfos;

  public PtsChartFactory(PtsSymbolInfos symInfos) {
    this.symInfos = symInfos;
    Font f = new Font("dialog", Font.PLAIN, 18);
    UIManager.put("ToolTip.font", f);
  }

  public PtsChart createPtsChart(String sym, String begindtIn, String enddtIn, int compressionFactorIn) {
    DateTime beginDt = new DateTime(begindtIn);
    DateTime endDt = new DateTime(enddtIn);
    return createPtsChart(sym, beginDt, endDt, compressionFactorIn);
  }

  public PtsChart createPtsChart(String sym, DateTime beginDt, DateTime endDt, int compressionFactorIn) {
    PtsChartFrame frame = new PtsChartFrame(symInfos.getSymbolInfo(sym), beginDt, endDt, compressionFactorIn);
    return frame.getChart();
  }

  public static void main(String args[]) {
    double tt = 12.53596789;
    double r = 0.25;
    double qr = tt / r;
    double qrFrac = qr % 1;
    double qrWhole = qr - qrFrac;
    double qrMult = qrWhole * r;  //BINGO!





    double qrDiv = qr / r;
    double ttFrac = tt % 1;
    double ttWhole = tt - ttFrac;
    double q = ttFrac / r;
    //double qFrac =
    double s = 1234.5678;
    System.out.println(s);
    long j = (long) s;
    short k = (short) j;  //you didn't say that you wanted the integer part as a short
    double dfrac = s - k;
    long lfrac = (long) (dfrac * 10000);
    short sfrac = (short) lfrac;
    System.out.println(j);
    System.out.println(k);
    System.out.println(dfrac);
    System.out.println(lfrac);
    System.out.println(sfrac);
    double value = 3.25;




    int j2 = 3;
  }
}
