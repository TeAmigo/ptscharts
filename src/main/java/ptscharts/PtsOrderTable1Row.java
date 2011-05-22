/*********************************************************************
 * File path:     /share/JavaDev/ptscharts/src/ptscharts/PtsOrderTable1Row.java
 * Version:       
 * Description:   Order definition has changed <2010-12-31 Fri 15:05> see below
 *                
 *                Original OrderTable1Row.java Created Aug 28, 2010 3:33:17 PM in Project PeTraSys
 * Author:        Rick Charon <rickcharon@gmail.com>
 * Created at:    Mon Dec 27 13:38:02 2010
 * Modified at:   Fri Dec 31 15:06:59 2010
 ********************************************************************/
package ptscharts;

import ptsutils.PtsOrder;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

// <2010-12-31 Fri 15:06> New definitions in Postgresql DB:
// CREATE TYPE paperordertype AS ENUM
//    ('BuyToOpen',
//     'SellToOpen',
//     'SellProfitToClose',
//     'SellLossToClose',
//     'BuyProfitToClose',
//     'BuyLossToClose');
// ALTER TYPE status OWNER TO trader1;
// CREATE TABLE paperorders
// (
//   idx serial NOT NULL,
//   ul character varying(12) NOT NULL,
//   price numeric NOT NULL DEFAULT 0::numeric,
//   ordertype paperordertype NOT NULL,
//   translatedprice numeric NOT NULL,
//   bartime timestamp without time zone NOT NULL,
//   lossorgain numeric,
//   orderid integer NOT NULL,
//   parentid integer,
//   entrytimestamp timestamp without time zone NOT NULL,
//   CONSTRAINT paperorders_pkey PRIMARY KEY (idx)
// )
public class PtsOrderTable1Row extends JTable {

  PtsOrder order = null;
  DefaultTableModel dtml;
  // From http://forums.netbeans.org/post-62966.html
  // idea is to put a combobox in a jTable
  String[] buySellStrings = {"BUY", "SELL"};
  String[] orderTypeStrings = {
    "BuyToOpen", "SellToOpen", "SellProfitToClose", "SellLossToClose", "BuyProfitToClose", "BuyLossToClose"
  };
  JComboBox buySellCombo = new JComboBox(buySellStrings);
  JComboBox orderTypeCombo = new JComboBox(orderTypeStrings);
  //DefaultCellEditor dce = new DefaultCellEditor(parentBuySellEditor);
  //String[] orderTypeString = {"LMT", "STP", "MKT", "TRAIL"};
  //JComboBox orderTypeCombo = new JComboBox(orderTypeString);
  String[] TIFString = {"DAY", "GTC"};
  JComboBox TIFCombo = new JComboBox(TIFString);
  Class[] columnTypes = new Class[]{
    //   idx serial NOT NULL,
    //   ul character varying(12) NOT NULL,
    //   ordertype paperordertype NOT NULL,
    //   price numeric NOT NULL DEFAULT 0::numeric,
    //   translatedprice numeric NOT NULL,
    //   bartime timestamp without time zone NOT NULL,
    //   lossorgain numeric,
    //   orderid integer NOT NULL,
    //   parentid integer,
    //   entrytimestamp timestamp without time zone NOT NULL,

    java.lang.Object.class, //0 "ordertype" orderTypeCombo
    java.lang.Double.class, //1 price
    java.lang.Double.class, //2 TranslatedPrice
    java.lang.Object.class, //3 BarTime
    java.lang.Double.class, //4 LossOrGain
    java.lang.Integer.class, //5 OrderID
    java.lang.Integer.class, //6 ParentID
    java.lang.Object.class, //7 EntryDateTime
  };
  boolean[] canEdit = new boolean[]{
    false, //0 "ordertype" orderTypeCombo
    true, //1 price
    true, //2 TranslatedPrice
    false, //3 BarTime
    false, //4 LossOrGain
    false, //5 OrderID
    false, //6 ParentID
    false //7 EntryDateTime
  };

  public PtsOrderTable1Row() {
    dtml = initTableModel();
    setModel(dtml);
    initTable();
    initLookAndFeel();
    getModel().addTableModelListener(this);
  }

  @Override
  public void tableChanged(TableModelEvent e) {
    super.tableChanged(e);
    int column = e.getColumn();
    int type = e.getType();
    Object d1;
    if (type == TableModelEvent.UPDATE && column > -1) {
      d1 = dtml.getValueAt(0, column);
      int k = 3;
    }
    int delEv = TableModelEvent.DELETE;
    int insertEv = TableModelEvent.INSERT;
    int updateEv = TableModelEvent.UPDATE;
    int j = 3; //82800.4954773736
  }

//  public void tableChanged(TableModelEvent e) {
//    int row = e.getFirstRow();
//    int column = e.getColumn();
//    TableModel model = (TableModel) e.getSource();
//    String columnName = model.getColumnName(column);
//    Object data = model.getValueAt(row, column);
//    int j = 3;
//  }
  private void initLookAndFeel() {
    this.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
    this.setFont(new java.awt.Font("DejaVu Sans", 1, 18)); // NOI18N
    this.setRowHeight(24);
    this.setRowSelectionAllowed(false);
  }

  public PtsOrder getOrder() {
    return order;
  }

  public void setOrder(PtsOrder order) {
    this.order = order;
    setValueAt(order.getOrderType(), 0, 0);               //0 "ordertype" orderTypeCombo
    setValueAt(order.getPrice(), 0, 1);                   //1 price
    setValueAt(order.getTranslatedPrice(), 0, 2);         //2 TranslatedPrice
    setValueAt(order.getBarTime(), 0, 3);                 //3 BarTime
    setValueAt(order.getLossOrGain(), 0, 4);              //4 LossOrGain
    setValueAt(order.getOrderID(), 0, 5);                 //5 OrderID
    setValueAt(order.getParentID(), 0, 6);                //6 ParentID
    setValueAt(order.getEntryDateTime(), 0, 7);           //7 EntryDateTime
  }

//    ParentTradeTable.setValueAt(instrument.getActualPriceFromExpandedPrice(expandedPrice), 0, 2);
//    ParentTradeTable.setValueAt("LMT", 0, 3);
//    ParentTradeTable.setValueAt("DAY", 0, 4);
//    ParentTradeTable.setValueAt(expandedPrice, 0, 9);
//    ParentTradeTable.setValueAt(tradeBarDateTime, 0, 6);
//    ParentTradeTable.setValueAt(getNextTradeID(), 0, 7);
  private DefaultTableModel initTableModel() {
    return new javax.swing.table.DefaultTableModel(
            new Object[][]{{null, null, null, null, null, null, null, null}},
            new String[]{"OrderType", "Price", "TranslatedPrice", "BarTime", "LossOrGain",
              "OrderID", "ParentID", "EntryDateTime"}) {

      @Override
      public Class getColumnClass(int columnIndex) {
        return columnTypes[columnIndex];
      }

      @Override
      public boolean isCellEditable(int rowIndex, int columnIndex) {
        return canEdit[columnIndex];
      }
    };

  }

  private void initTable() {
    /** next as of 12/31/10 4:15 PM
      //0 "ordertype" orderTypeCombo
      //1 price
      //2 TranslatedPrice
      //3 BarTime
      //4 LossOrGain
      //5 OrderID
      //6 ParentID
      //7 EntryDateTime
     */
    this.setRowHeight(24);
    this.setRowSelectionAllowed(false);
    //stopLossOrderScrollPane2.setViewportView(this);
    this.getColumnModel().getColumn(0).setPreferredWidth(45); //0 "ordertype" Combo
    this.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(orderTypeCombo));
    this.getColumnModel().getColumn(1).setPreferredWidth(45); //1 "price"
    this.getColumnModel().getColumn(2).setPreferredWidth(45); //2 TranslatedPrice
    this.getColumnModel().getColumn(3).setPreferredWidth(45); //3 BarTime
    this.getColumnModel().getColumn(4).setPreferredWidth(75); //4 LossOrGain
    this.getColumnModel().getColumn(5).setPreferredWidth(75); //5 OrderID
    this.getColumnModel().getColumn(6).setPreferredWidth(75); //6 ParentID
    this.getColumnModel().getColumn(7).setPreferredWidth(50); //7 EntryDateTime
    this.getColumnModel().getColumn(7).setCellEditor(new DefaultCellEditor(orderTypeCombo));
    this.getTableHeader().setFont(new java.awt.Font("DejaVu Sans", 0, 18));
    DefaultCellEditor editor = (DefaultCellEditor) this.getDefaultEditor(Integer.class);
    DefaultCellEditor editor2 = (DefaultCellEditor) this.getDefaultEditor(String.class);
    JTextField textField = (JTextField) editor.getComponent();
    textField.setFont(new java.awt.Font("DejaVu Sans", 0, 18));
    JTextField textField2 = (JTextField) editor2.getComponent();
    textField2.setFont(new java.awt.Font("DejaVu Sans", 0, 18));
  }
}
