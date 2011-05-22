/*
 * //rpc - NOTE:9/14/10 1:54 PM -
Moved the traderFrame.form to /share/PetrasyBackup, nice for design but gets in the way.

 * TraderFrame.java
 *
 * Created on Jul 11, 2010, 12:05:13 PM
 */
package ptscharts;

import com.ib.client.Contract;
import ptsutils.PtsSymbolInfo;
import ptsutils.PtsPaperTrade;
import ptsutils.PtsOrder;
import ptsutils.PtsDBops;
import java.awt.Dimension;
import java.sql.Timestamp;
import java.util.Date;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;
import org.apache.commons.math.util.MathUtils;
import ptstrader.PtsTrader;
import ptsutils.BuySell;
import ptsutils.PtsContractFactory;

/**
 *
 *
 * @author rickcharon
 * //rpc - WORKING HERE:3/5/10 2:33 PM - Trader - Responsible for executing the trades,
 * to a local paper protfolio
 */
public class PtsTraderFrame extends javax.swing.JFrame {

  PtsChart chart;
  PtsSymbolInfo symbolInfo;
  boolean portfolioUpdate = false;
  boolean marketData = false;
  final String portfolioUpdateActive = "PortfolioUdpate (is on)";
  final String portfolioUpdateInactive = "PortfolioUdpate (is off)";
  DefaultTableCellRenderer dtc;
  boolean paperTradeActive = false;
  //DefaultTableModel dtml;
  Double stopLossAmount = 200.00;
  Double profitStopAmount = 500.00;
  String[] buySellStrings = {"BUY", "SELL"};
  JComboBox buySellCombo = new JComboBox(buySellStrings);
  //DefaultCellEditor dce = new DefaultCellEditor(parentBuySellEditor);
  //String[] orderTypeString = {"LMT", "STP", "MKT", "TRAIL"};
  //JComboBox orderTypeCombo = new JComboBox(orderTypeString);
  String[] TIFString = {"DAY", "GTC"};
  //JComboBox TIFCombo = new JComboBox(orderTypeString);
  PtsTrader trader = null;

  public PtsTraderFrame() {
    //dtml = initDtm1();
    initComponents();
  }

  /** Creates new form TraderFrame */
  public PtsTraderFrame(PtsChart chartIn, PtsSymbolInfo symbolInfoIn) {
    chart = chartIn;
    symbolInfo = symbolInfoIn;
    //dtml = initDtm1();
    initComponents();
    setTitle("Trading - " + symbolInfo.getFullName() + "  expiry: " + symbolInfo.getMaxActiveExpiry());
    setLocationToBottom();
  }

  public Double getProfitStopAmount() {
    return profitStopAmount;
  }

  public void setProfitStopAmount(Double profitStopAmount) {
    this.profitStopAmount = profitStopAmount;
  }

  public Double getStopLossAmount() {
    return stopLossAmount;
  }

  public void setStopLossAmount(Double stopLossAmount) {
    this.stopLossAmount = stopLossAmount;
  }

  public void setLocationToBottom() {
    Dimension screen = getToolkit().getScreenSize();
    int height = getSize().height;
    setLocation(100, (screen.getSize().height - height + 50));
  }

  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    buttonPanel = new javax.swing.JPanel();
    ExecuteTradeButton = new javax.swing.JButton();
    paperTradeButton = new javax.swing.JButton();
    openOrdersButton = new javax.swing.JButton();
    madeTradesButton = new javax.swing.JButton();
    currentPortfolioButton = new javax.swing.JButton();
    linesToChartButton = new javax.swing.JButton();
    ExecuteTradeButton = new javax.swing.JButton();
    parentOrderScrollPane1 = new javax.swing.JScrollPane();
    ParentTradeTable = new PtsOrderTable1Row();
    stopLossOrderScrollPane2 = new javax.swing.JScrollPane();
    StopLossTable = new PtsOrderTable1Row();
    profitStopOrderScrollPane3 = new javax.swing.JScrollPane();
    ProfitStopTable = new PtsOrderTable1Row();

    setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    getContentPane().setLayout(new java.awt.GridBagLayout());

    buttonPanel.setMinimumSize(new java.awt.Dimension(1042, 32));
    buttonPanel.setPreferredSize(new java.awt.Dimension(1042, 32));
    buttonPanel.setRequestFocusEnabled(false);
    buttonPanel.setLayout(new java.awt.GridLayout(1, 0));
    paperTradeButton.setFont(new java.awt.Font("DejaVu Sans", 1, 14));
    paperTradeButton.setText("Execute Paper Trade");
    paperTradeButton.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        paperTradeButtonActionPerformed(evt);
      }
    });
    buttonPanel.add(paperTradeButton);

    openOrdersButton.setFont(new java.awt.Font("DejaVu Sans", 1, 14));
    openOrdersButton.setText("Open Orders");
    buttonPanel.add(openOrdersButton);

    madeTradesButton.setFont(new java.awt.Font("DejaVu Sans", 1, 14));
    madeTradesButton.setText("Made Trades");
    madeTradesButton.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        madeTradesButtonActionPerformed(evt);
      }
    });
    buttonPanel.add(madeTradesButton);

    currentPortfolioButton.setFont(new java.awt.Font("DejaVu Sans", 1, 14));
    currentPortfolioButton.setText("Current Portfolio");
    currentPortfolioButton.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        currentPortfolioButtonActionPerformed(evt);
      }
    });
    buttonPanel.add(currentPortfolioButton);

    linesToChartButton.setText("Send Lines to Chart");
    linesToChartButton.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        linesToChartButtonActionPerformed(evt);
      }
    });
    buttonPanel.add(linesToChartButton);

    ExecuteTradeButton.setFont(new java.awt.Font("DejaVu Sans", 1, 14));
    ExecuteTradeButton.setText("Execute - Trade");
    ExecuteTradeButton.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        ExecuteTradeButtonActionPerformed(evt);
      }
    });
    buttonPanel.add(ExecuteTradeButton);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.ipadx = 3174;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    getContentPane().add(buttonPanel, gridBagConstraints);

    parentOrderScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Parent Order", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 18))); // NOI18N
    parentOrderScrollPane1.setMinimumSize(new java.awt.Dimension(1042, 48));
    parentOrderScrollPane1.setPreferredSize(new java.awt.Dimension(1042, 48));
    parentOrderScrollPane1.setViewportView(ParentTradeTable);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.ipadx = 4188;
    gridBagConstraints.ipady = 32;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    getContentPane().add(parentOrderScrollPane1, gridBagConstraints);

    stopLossOrderScrollPane2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Stop Loss Order", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 18))); // NOI18N
    stopLossOrderScrollPane2.setMinimumSize(new java.awt.Dimension(1042, 48));
    stopLossOrderScrollPane2.setPreferredSize(new java.awt.Dimension(1042, 48));

    stopLossOrderScrollPane2.setViewportView(StopLossTable);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.ipadx = 4200;
    gridBagConstraints.ipady = 32;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    getContentPane().add(stopLossOrderScrollPane2, gridBagConstraints);

    profitStopOrderScrollPane3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Profit Stop Order", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 18))); // NOI18N
    profitStopOrderScrollPane3.setMinimumSize(new java.awt.Dimension(1042, 48));
    profitStopOrderScrollPane3.setPreferredSize(new java.awt.Dimension(1042, 48));
    profitStopOrderScrollPane3.setVerifyInputWhenFocusTarget(false);
    profitStopOrderScrollPane3.setViewportView(ProfitStopTable);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.ipadx = 4200;
    gridBagConstraints.ipady = 32;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    getContentPane().add(profitStopOrderScrollPane3, gridBagConstraints);

    pack();
  }

  private void ExecuteTradeButtonActionPerformedTest(java.awt.event.ActionEvent evt) {
    if (trader == null) {
      trader = new PtsTrader(7510);
    }
    for (int i = 0; i < 3; i++) {
      Contract contract = PtsContractFactory.makeContract("AUD", "FUT", "GLOBEX", "201106", "USD");
      trader.PlaceBracketOrder(contract, BuySell.BUY, 1, 1.00, 0.98, 1.23);
    }
    //trader.closeConnection();
  }

  private void ExecuteTradeButtonActionPerformed(java.awt.event.ActionEvent evt) {
    //PtsBracketOrder ord = new PtsBracketOrder();
    if (trader == null) {
      trader = new PtsTrader(7510);
    }
    Contract contract = PtsContractFactory.makeContract(symbolInfo.getSymbol(), "FUT", symbolInfo.getExchange(),
            Integer.toString(symbolInfo.getMaxActiveExpiry()), "USD");

    String buyOrSell = (String) ParentTradeTable.getValueAt(0, 0);
    BuySell buysell;
    if (buyOrSell.compareTo("BuyToOpen") == 0) {
      buysell = BuySell.BUY;
    } else {
      buysell = BuySell.SELL;
    }

    PtsOrder order = ((PtsOrderTable1Row) ParentTradeTable).getOrder();
    Double tradePrice = order.getPrice();
    order = ((PtsOrderTable1Row) StopLossTable).getOrder();
    Double stopLossPrice = order.getPrice();
    order = ((PtsOrderTable1Row) ProfitStopTable).getOrder();
    Double profitStopPrice = order.getPrice();
//    profitStopPrice = MathUtils.round(profitStopPrice, 4);
    double mintick = chart.getFrame().getSymbolInfo().getMintick();
    tradePrice = mintickSetup(tradePrice, mintick);
    stopLossPrice = mintickSetup(stopLossPrice, mintick);
    profitStopPrice = mintickSetup(profitStopPrice, mintick);
    trader.PlaceBracketOrder(contract, buysell, 1, tradePrice, stopLossPrice, profitStopPrice);
    setLocationToBottom();
    int j = 3;
  }

  public double mintickSetup(double val, double mintick) {
    double qr = val / mintick;
    double qrFrac = qr % 1;
    double qrWhole = qr - qrFrac;
    return qrWhole * mintick;  //BINGO!
  }
  private void currentPortfolioButtonActionPerformed(java.awt.event.ActionEvent evt) {
//    if (!portfolioUpdate) {
//      currentPortfolioButton.setText(portfolioUpdateActive);
//    }
//    AccountUpdates au = new AccountUpdates();
//    Thread auThread = new Thread(au);
//    auThread.run();
  }//GEN-LAST:event_currentPortfolioButtonActionPerformed

  private void playItForward() {
    PtsPaperTrade pt = new PtsPaperTrade();
    pt.setSymbol(symbolInfo.getSymbol());
    pt.setBeginTradeDateTime(((PtsOrderTable1Row) ParentTradeTable).getOrder().getBarTime());
    pt.setEntry(((PtsOrderTable1Row) ParentTradeTable).getOrder().getTranslatedPrice());
    pt.setStopLoss(((PtsOrderTable1Row) StopLossTable).getOrder().getTranslatedPrice());
    pt.setProfitStop(((PtsOrderTable1Row) ProfitStopTable).getOrder().getTranslatedPrice());
    pt.setPosition(((PtsOrderTable1Row) ParentTradeTable).getOrder().getOrderType());
    pt.setStopRisk((((PtsOrderTable1Row) StopLossTable).getOrder().getLossOrGain()));
    pt.setProfitpotential(((PtsOrderTable1Row) ProfitStopTable).getOrder().getLossOrGain());
//    Date tradeDate = ((OrderTable1Row) ParentTradeTable).getOrder().getBarTime();
//    Double tradePrice = ((OrderTable1Row) ParentTradeTable).getOrder().getTranslatedPrice();
//    Double stopPrice = ((OrderTable1Row) StopLossTable).getOrder().getTranslatedPrice();
//    Double profitPrice = ((OrderTable1Row) ProfitStopTable).getOrder().getTranslatedPrice();
//    Double result;
//    if(((OrderTable1Row) ParentTradeTable).getOrder().getBuySell().equals("BUY")) {
//      result = instrument.getMinutePriceBars().PlayItForward(tradeDate, profitPrice, stopPrice);
//    }

    //symbolInfo.getMinutePriceBars().PlayItForward(pt);
    PtsDBops.playItForward(pt, symbolInfo);
    Date beginDate = pt.getBeginTradeDateTime();
    Date endDate = pt.getExitTradeDateTime();
    paperTradeActive = false;
    paperTradeButton.setText("Execute Paper Trade");
    PtsDBops.insertIntoPaperTradesTable(pt);
    int j = 3;
  }

  private void madeTradesButtonActionPerformed(java.awt.event.ActionEvent evt) {
    PtsPaperTradesList.createAndShow();
  }

  private void paperTradeButtonActionPerformed(java.awt.event.ActionEvent evt) {
    if (paperTradeActive) {
      playItForward();
      return;
    }
    int orderID = getNextPaperTradeID();
    int parentID = orderID;
    Timestamp timeStamp = new Timestamp((new Date()).getTime());
    ((PtsOrderTable1Row) ParentTradeTable).getOrder().setOrderID(orderID);
    ((PtsOrderTable1Row) ParentTradeTable).getOrder().setParentID(parentID);
    //((PtsOrderTable1Row) ParentTradeTable).getOrder().setExecutedDateTime(timeStamp);
    ((PtsOrderTable1Row) ParentTradeTable).getOrder().setEntryDateTime(timeStamp);

    orderID += 1;
    ((PtsOrderTable1Row) StopLossTable).getOrder().setOrderID(orderID);
    ((PtsOrderTable1Row) StopLossTable).getOrder().setParentID(parentID);
    //((PtsOrderTable1Row) StopLossTable).getOrder().setExecutedDateTime(timeStamp);
    ((PtsOrderTable1Row) StopLossTable).getOrder().setEntryDateTime(timeStamp);

    orderID += 1;
    ((PtsOrderTable1Row) ProfitStopTable).getOrder().setOrderID(orderID);
    ((PtsOrderTable1Row) ProfitStopTable).getOrder().setParentID(parentID);
    //((PtsOrderTable1Row) ProfitStopTable).getOrder().setExecutedDateTime(timeStamp);
    ((PtsOrderTable1Row) ProfitStopTable).getOrder().setEntryDateTime(timeStamp);

    PtsDBops.insertIntoPaperOrdersTable(((PtsOrderTable1Row) ParentTradeTable).getOrder());
    PtsDBops.insertIntoPaperOrdersTable(((PtsOrderTable1Row) StopLossTable).getOrder());
    PtsDBops.insertIntoPaperOrdersTable(((PtsOrderTable1Row) ProfitStopTable).getOrder());
    paperTradeButton.setText("Play It Forward...");
    paperTradeActive = true;
  }

  //rpc - WORKING HERE:7/26/10 1:25 PM - linesToChartButtonActionPerformed
  private void linesToChartButtonActionPerformed(java.awt.event.ActionEvent evt) {
    PtsChartPanel panel = chart.getChartPanel();
    chart.getChartPanel().setupHorizontalPrefixedCrosshair((Double) (ParentTradeTable.getValueAt(0, 9)),
            ParentTradeTable.getValueAt(0, 0).toString());
    chart.getChartPanel().setupHorizontalPrefixedCrosshair((Double) (StopLossTable.getValueAt(0, 9)),
            StopLossTable.getValueAt(0, 0).toString() + "-STOPLOSS");
    chart.getChartPanel().setupHorizontalPrefixedCrosshair((Double) (ProfitStopTable.getValueAt(0, 9)),
            ProfitStopTable.getValueAt(0, 0).toString() + "-PROFIT");

  }

  public int getNextPaperTradeID() {
    return PtsDBops.getNextPaperOrderID();
  }

  public void setupBuyTrade(double tradeDateTime, double expandedPrice) {
    Date tradeBarDateTime = new Date((long) tradeDateTime);
    PtsOrder parentOrder = new PtsOrder();
    parentOrder.setUl(symbolInfo.getSymbol());
    //parentOrder.setExpiry(symbolInfo.getMaxActiveExpiry());
    parentOrder.setOrderType("BuyToOpen");
    //parentOrder.setTotalQuantity(1);
    parentOrder.setPrice(symbolInfo.getActualPriceFromExpandedPrice(expandedPrice));
    //parentOrder.setOrderType("LMT");
    //parentOrder.setTif("DAY");
    parentOrder.setTranslatedPrice(expandedPrice);
    parentOrder.setBarTime(tradeBarDateTime);
    parentOrder.setLossOrGain(0.00);
    //parentOrder.setStatus("Filled");
    ((PtsOrderTable1Row) ParentTradeTable).setOrder(parentOrder);
  }

  public void setupSellTrade(double tradeDateTime, double expandedPrice) {
    Date tradeBarDateTime = new Date((long) tradeDateTime);
    PtsOrder parentOrder = new PtsOrder();
    parentOrder.setUl(symbolInfo.getSymbol());
    //parentOrder.setExpiry(symbolInfo.getMaxActiveExpiry());
    parentOrder.setOrderType("SellToOpen");
    //parentOrder.setTotalQuantity(1);
    parentOrder.setPrice(symbolInfo.getActualPriceFromExpandedPrice(expandedPrice));
    //parentOrder.setOrderType("LMT");
    //parentOrder.setTif("DAY");
    parentOrder.setTranslatedPrice(expandedPrice);
    parentOrder.setBarTime(tradeBarDateTime);
    parentOrder.setLossOrGain(0.00);
    //parentOrder.setStatus("Filled");
    ((PtsOrderTable1Row) ParentTradeTable).setOrder(parentOrder);
  }

  public boolean setupStopLoss(double expandedPrice) {
    PtsOrder parentOrder = ((PtsOrderTable1Row) ParentTradeTable).getOrder();
    if (null == parentOrder) {
      JOptionPane.showMessageDialog(chart.getFrame(), "No Parent Order!");
      return false;
    }
    PtsOrder stopLossOrder = new PtsOrder();
    stopLossOrder.setUl(symbolInfo.getSymbol());
    stopLossOrder.setTranslatedPrice(expandedPrice);
    stopLossOrder.setBarTime(parentOrder.getBarTime());
    if (parentOrder.getOrderType().equals("BuyToOpen")) {
      stopLossOrder.setOrderType("SellLossToClose");
      stopLossOrder.setLossOrGain(stopLossOrder.getTranslatedPrice() - parentOrder.getTranslatedPrice());
    } else if (parentOrder.getOrderType().equals("SellToOpen")) {
      stopLossOrder.setOrderType("BuyLossToClose");
      stopLossOrder.setLossOrGain(parentOrder.getTranslatedPrice() - stopLossOrder.getTranslatedPrice());
    }
    stopLossOrder.setPrice(symbolInfo.getActualPriceFromExpandedPrice(expandedPrice));
    ((PtsOrderTable1Row) StopLossTable).setOrder(stopLossOrder);
    return true;
  }

  public boolean setupProfitExit(double expandedPrice) {
    PtsOrder parentOrder = ((PtsOrderTable1Row) ParentTradeTable).getOrder();
    if (null == parentOrder) {
      JOptionPane.showMessageDialog(chart.getFrame(), "No Parent Order!");
      return false;
    }
    PtsOrder profitStopOrder = new PtsOrder();
    profitStopOrder.setUl(symbolInfo.getSymbol());
    //profitStopOrder.setExpiry(symbolInfo.getMaxActiveExpiry());
    if (parentOrder.getOrderType().equals("BuyToOpen")) {
      profitStopOrder.setOrderType("SellProfitToClose");
    } else if (parentOrder.getOrderType().equals("SellToOpen")) {
      profitStopOrder.setOrderType("BuyProfitToClose");
    }
    profitStopOrder.setPrice(symbolInfo.getActualPriceFromExpandedPrice(expandedPrice));
    //profitStopOrder.set("LMT");
    //profitStopOrder.setTif("GTC");
    profitStopOrder.setTranslatedPrice(expandedPrice);
    profitStopOrder.setBarTime(parentOrder.getBarTime());
    profitStopOrder.setLossOrGain(Math.abs(parentOrder.getTranslatedPrice() - profitStopOrder.getTranslatedPrice()));
    //profitStopOrder.setLossOrGain(expandedProfitStopPrice - expandedPrice);
    //profitStopOrder.setStatus("Submitted");
    ((PtsOrderTable1Row) ProfitStopTable).setOrder(profitStopOrder);
    return true;
    //linesToChartButtonActionPerformed(null);
  }

  public void instigateSellTrade(double tradeDateTime, double expandedPrice) {
    Date tradeBarDateTime = new Date((long) tradeDateTime);
    PtsOrder parentOrder = new PtsOrder();
    parentOrder.setUl(symbolInfo.getSymbol());
    //parentOrder.setExpiry(symbolInfo.getMaxActiveExpiry());
    parentOrder.setOrderType("SellToOpen");
    //parentOrder.setTotalQuantity(1);
    parentOrder.setPrice(symbolInfo.getActualPriceFromExpandedPrice(expandedPrice));
    //parentOrder.setOrderType("LMT");
    //parentOrder.setTif("DAY");
    parentOrder.setTranslatedPrice(expandedPrice);
    parentOrder.setBarTime(tradeBarDateTime);
    //parentOrder.setStatus("Filled");
    ((PtsOrderTable1Row) ParentTradeTable).setOrder(parentOrder);

    PtsOrder stopLossOrder = new PtsOrder();
    stopLossOrder.setUl(symbolInfo.getSymbol());
    //stopLossOrder.setExpiry(symbolInfo.getMaxActiveExpiry());
    stopLossOrder.setOrderType("BuyLossToClose");
    double expandedStopLossPrice = expandedPrice + stopLossAmount;
    stopLossOrder.setPrice(symbolInfo.getActualPriceFromExpandedPrice(expandedStopLossPrice));
    //stopLossOrder.setOrderType("STP");
    //stopLossOrder.setTif("GTC");
    stopLossOrder.setTranslatedPrice(expandedStopLossPrice);
    stopLossOrder.setBarTime(tradeBarDateTime);
    stopLossOrder.setLossOrGain(expandedPrice - expandedStopLossPrice);
    //stopLossOrder.setStatus("Submitted");
    ((PtsOrderTable1Row) StopLossTable).setOrder(stopLossOrder);

    PtsOrder profitStopOrder = new PtsOrder();
    profitStopOrder.setUl(symbolInfo.getSymbol());
    //profitStopOrder.setExpiry(symbolInfo.getMaxActiveExpiry());
    profitStopOrder.setOrderType("BuyProfitToClose");
    double expandedProfitStopPrice = expandedPrice - profitStopAmount;
    profitStopOrder.setPrice(symbolInfo.getActualPriceFromExpandedPrice(expandedProfitStopPrice));
    //profitStopOrder.setOrderType("LMT");
    //profitStopOrder.setTif("GTC");
    profitStopOrder.setTranslatedPrice(expandedProfitStopPrice);
    profitStopOrder.setBarTime(tradeBarDateTime);
    profitStopOrder.setLossOrGain(expandedPrice - expandedProfitStopPrice);
    //profitStopOrder.setStatus("Submitted");
    ((PtsOrderTable1Row) ProfitStopTable).setOrder(profitStopOrder);
    linesToChartButtonActionPerformed(null);
  }

  /**
   * @param args the command line arguments
   */
  public static void main(String args[]) {
    java.awt.EventQueue.invokeLater(new Runnable() {

      public void run() {
        new PtsTraderFrame().setVisible(true);
      }
    });
  }
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton ExecuteTradeButton;
  private javax.swing.JTable ParentTradeTable;
  private javax.swing.JTable ProfitStopTable;
  private javax.swing.JTable StopLossTable;
  private javax.swing.JPanel buttonPanel;
  private javax.swing.JButton currentPortfolioButton;
  private javax.swing.JButton madeTradesButton;
  private javax.swing.JButton linesToChartButton;
  private javax.swing.JButton openOrdersButton;
  private javax.swing.JButton paperTradeButton;
  private javax.swing.JScrollPane parentOrderScrollPane1;
  private javax.swing.JScrollPane profitStopOrderScrollPane3;
  private javax.swing.JScrollPane stopLossOrderScrollPane2;
  // End of variables declaration//GEN-END:variables
}
