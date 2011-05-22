/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ptscharts;

import java.awt.Font;
import javax.swing.UIManager;
import org.joda.time.DateTime;
import ptscharts.PtsChart;

/**
 *
 * @author rickcharon
 */
public class PtsChartDriver {

  

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    //PtsChartDriver pcharts = new PtsChartDriver();
    Font f = new Font("dialog", Font.PLAIN, 18);
    UIManager.put("ToolTip.font", f);
    PtsSymbolInfos syminfs = new PtsSymbolInfos();
    syminfs.getDistinctSymbolInfos();
    PtsChartFactory factory = new PtsChartFactory(syminfs);
    //PtsChart chart1 = factory.createPtsChart("AUD", "2009-03-11", "2011-01-01", 60 * 24);
    //PtsChart chart1 = factory.createPtsChart("AUD", "2009-03-11", "2009-05-01", 60);
    PtsChart chartcad =  factory.createPtsChart("CAD", "2010-08-14T03:00", "2010-09-21T07:00", 60);
    chartcad.setJumpBarSize(4);
    chartcad.show();
    int cnt = chartcad.getPricePlotDatasetCount();
    int j = 3;
    //chart1.jumpBars(24 * 5);
    
//    CommandLineParser cl = new CommandLineParser();
//    if (args.length == 0) {
//      Scanner sc = new Scanner("cmd");
//      cl.processLineIn(sc);
//    } else if (args.length > 0) {
//      String argLine = args[0];
//      for (int i = 1; i < args.length; i++) {
//        argLine += " " + args[i];
//      }
//      Scanner sc = new Scanner(argLine);
//      cl.processLineIn(sc);
//    }
//    Thread thread = new Thread(cl);
//    thread.start();
  }
}
