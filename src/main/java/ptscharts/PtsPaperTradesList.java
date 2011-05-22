package ptscharts;

/*
 * Used stuff from
 *http://download.oracle.com/javase/tutorial/uiswing/examples/components/TableDemoProject/src/components/TableDemo.java
 * as template, more or less
 */
import ptsutils.PtsPaperTrade;
import ptsutils.PtsDBops;
import java.awt.Component;
import java.awt.Frame;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.KeyStroke;
import javax.swing.border.BevelBorder;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Months;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

/**
 * TableDemo is just like SimpleTableDemo, except that it
 * uses a custom TableModel.
 */
public class PtsPaperTradesList extends JPanel {

  PaperTradesTableModel tabModel;

  class SimpleAction extends AbstractAction {

    public SimpleAction(String name) {
      putValue(Action.NAME, name);
    }

    public void actionPerformed(ActionEvent e) {
      System.out.println(getValue(Action.NAME));
    }
  }

  public PtsPaperTradesList() {
    super(new GridLayout(1, 0));
    tabModel = new PaperTradesTableModel();
    JTable paperTradesTable = new JTable(tabModel);
    paperTradesTable.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
    paperTradesTable.setFont(new java.awt.Font("DejaVu Sans", 1, 16));
    paperTradesTable.setRowHeight(24);
    //paperTradesTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
    paperTradesTable.setFillsViewportHeight(true);
    paperTradesTable.getTableHeader().setFont(new java.awt.Font("DejaVu Sans", 0, 18));
    paperTradesTable.setAutoCreateRowSorter(true);

    //Create the scroll pane and add the table to it.
    JScrollPane scrollPane = new JScrollPane(paperTradesTable);
    scrollPane.setBorder(javax.swing.BorderFactory.createBevelBorder(BevelBorder.RAISED));
    scrollPane.setMinimumSize(new java.awt.Dimension(1042, tabModel.getColumnCount() * 18));
    scrollPane.setPreferredSize(new java.awt.Dimension(10042,
            (tabModel.getColumnCount() + 2) * paperTradesTable.getRowHeight()));
    add(scrollPane);

    InputMap im = getInputMap();
    ActionMap am = getActionMap();
    Action action = new SimpleAction("Left");
    Object key = action.getValue(Action.NAME);
    KeyStroke keyStroke = KeyStroke.getKeyStroke("LEFT");
    im.put(keyStroke, key);
    am.put(key, action);
    calcColumnWidths(paperTradesTable);
    //paperTradesTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
  }

  class PaperTradesTableModel extends AbstractTableModel {

    Class[] columnTypes = new Class[]{
      String.class,
      String.class,
      DateTime.class,
      DateTime.class,
      Interval.class,
      Double.class,
      DateTime.class
    };
    private String[] columnNames = {"Symbol", "Long/Short", "Begin Trade DateTime", "End Trade DateTime",
      "Time in Trade", "Outcome", "Entered in DB"};
    private ArrayList<PtsPaperTrade> paperTrades;

    public Object getValueAt(int row, int col) {
      PtsPaperTrade trade = paperTrades.get(row);
      switch (col) {
        case 0:
          return trade.getSymbol();
        case 1:
          return trade.getPosition();
        case 2:
          return trade.getBeginTradeDateTime();
        case 3:
          return trade.getExitTradeDateTime();
        case 4:
          return PtsDateOps.elapsedTimeString(trade.getBeginTradeDateTime().getTime(),
                  trade.getExitTradeDateTime().getTime());
        case 5:
          return trade.getOutcome();
        case 6:
          return trade.getEnteredInDB();
        default:
          return null;
      }
    }

    public PaperTradesTableModel() {
      paperTrades = PtsDBops.getPaperTrades();
    }

    public int getColumnCount() {
      return columnNames.length;
    }

    public int getRowCount() {
      return paperTrades.size();
    }

    @Override
    public String getColumnName(int col) {
      return columnNames[col];
    }


    /*
     * JTable uses this method to determine the default renderer/
     * editor for each cell.  If we didn't implement this method,
     * then the last column would contain text ("true"/"false"),
     * rather than a check box.
     */
    @Override
    public Class getColumnClass(int c) {
      //return getValueAt(0, c).getClass();
      return columnTypes[c];
    }

    /*
     * Don't need to implement this method unless your table's
     * editable.
     */
//    @Override
//    public boolean isCellEditable(int row, int col) {
//      //Note that the data/cell address is constant,
//      //no matter where the cell appears onscreen.
//      if (col < 2) {
//        return false;
//      } else {
//        return true;
//      }
//    }

    /*
     * Don't need to implement this method unless your table's
     * data can change.
     */
//    public void setValueAt(Object value, int row, int col) {
//      if (DEBUG) {
//        System.out.println("Setting value at " + row + "," + col
//                + " to " + value
//                + " (an instance of "
//                + value.getClass() + ")");
//      }
//
//      data[row][col] = value;
//      fireTableCellUpdated(row, col);
//
//      if (DEBUG) {
//        System.out.println("New value of data:");
//        printDebugData();
//      }
//    }
//    private void printDebugData() {
//      int numRows = getRowCount();
//      int numCols = getColumnCount();
//
//      for (int i = 0; i < numRows; i++) {
//        System.out.print("    row " + i + ":");
//        for (int j = 0; j < numCols; j++) {
//          System.out.print("  " + data[i][j]);
//        }
//        System.out.println();
//      }
//      System.out.println("--------------------------");
//    }
  }

  /**
   * Create the GUI and show it.  For thread safety,
   * this method should be invoked from the
   * event-dispatching thread.
   */
  public static void createAndShow() {
    //Create and set up the window.
    JFrame frame = new JFrame("Paper Trades");
    frame.setExtendedState(Frame.MAXIMIZED_HORIZ);
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    //Create and set up the content pane.
    PtsPaperTradesList newContentPane = new PtsPaperTradesList();
    //newContentPane.setOpaque(true); //content panes must be opaque
    frame.setContentPane(newContentPane);

    //Display the window.
    frame.pack();
    frame.setVisible(true);
  }

  public static void main(String[] args) {
    //Schedule a job for the event-dispatching thread:
    //creating and showing this application's GUI.
    // Create an Interval from now to one month from now.
//    PeriodFormatter daysHoursMinutes = new PeriodFormatterBuilder() //            .appendDays().appendSuffix(" day", " days").appendSeparator(" and ").appendMinutes()
//            //            .appendSuffix(" minute", " minutes").appendSeparator(" and ").appendSeconds()
//            //            .appendSuffix(" second", " seconds").toFormatter();
//            .appendDays().appendSeparator(" days,", "&")
//            .appendHours().appendSeparator(" hours,", "&")
//            .appendHours().appendSeparator(" hours, ", "&").appendMinutes().toFormatter();
//
//    DateTime startTime = new DateTime(); // now
//    DateTime endTime = startTime.plus(Months.months(1));
//    Interval interval = new Interval(startTime, endTime);
//    System.out.println("interval = " + interval);
//    System.out.println("start = " + interval.getStart());
//    System.out.println("end = " + interval.getEnd());
//    Period weeks = interval.toPeriod(PeriodType.weeks());
//    Period years = interval.toPeriod(PeriodType.years());
//    Period days = interval.toPeriod(PeriodType.days());
//    Period pp = new Period(interval.toDuration());
//    int pphrs = pp.getHours();

//    Period period = new Period(72, 24, 12, 0);
//
//    System.out.println(daysHoursMinutes.print(period));
//    System.out.println(daysHoursMinutes.print(period.normalizedStandard()));
//    System.out.printf(
//    "%d years, %d months, %d days, %d hours, %d minutes, %d seconds%n",
//    pp.getYears(), pp.getMonths(), pp.getDays(),
//    pp.getHours(), pp.getMinutes(), pp.getSeconds());
//
//    System.out.println(daysHoursMinutes.print(pp));
//    System.out.println(daysHoursMinutes.print(pp.normalizedStandard()));
//
//    System.out.println("Period: " + pp);
//    System.out.println("Period to days: " + pp.toStandardDays());
//

//    System.out.println("YEARS " + years.getYears() + " WEEKS "
//            + weeks.getWeeks() / 54 + " DAYS " + days.getDays() / 365);
//    System.out.println("duration = " + (ReadableDuration) (interval.toDuration()));

    javax.swing.SwingUtilities.invokeLater(new Runnable() {

      public void run() {
        createAndShow();
      }
    });
  }

  //1/9/11 2:15 PM Thanks to IchBin:
  //http://www.javakb.com/Uwe/Forum.aspx/java-programmer/28790/JTable-and-optimal-column-width
  public void calcColumnWidths(JTable table) {
    JTableHeader header = table.getTableHeader();
    TableCellRenderer defaultHeaderRenderer = null;
    if (header != null) {
      defaultHeaderRenderer = header.getDefaultRenderer();
    }
    TableColumnModel columns = table.getColumnModel();
    TableModel data = table.getModel();
    int margin = columns.getColumnMargin(); // only JDK1.3
    int rowCount = data.getRowCount();
    int totalWidth = 0;
    for (int i = columns.getColumnCount() - 1; i >= 0; --i) {
      TableColumn column = columns.getColumn(i);
      int columnIndex = column.getModelIndex();
      int width = -1;
      TableCellRenderer h = column.getHeaderRenderer();
      if (h == null) {
        h = defaultHeaderRenderer;
      }
      if (h != null) { // Not explicitly impossible
        Component c = h.getTableCellRendererComponent(table, column.getHeaderValue(), false, false, -1, i);
        width = c.getPreferredSize().width;
      }

      for (int row = rowCount - 1; row >= 0; --row) {
        TableCellRenderer r = table.getCellRenderer(row, i);
        Component c = r.getTableCellRendererComponent(table, data.getValueAt(row, columnIndex), false, false,
                row, i);
        width = Math.max(width, c.getPreferredSize().width);
      }
      if (width >= 0) {
        column.setPreferredWidth(width + margin); // <1.3:    without margin
      } else {
        totalWidth += column.getPreferredWidth();
      }
    }
  }
}
