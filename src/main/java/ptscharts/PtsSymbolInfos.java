/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ptscharts;

import ptsutils.PtsSymbolInfo;
import ptsutils.PtsDBops;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import org.joda.time.DateTime;

/**
 *
 * @author rickcharon
 */
public class PtsSymbolInfos {

  HashMap symInfos = null;
  ArrayList<String> symbols = null;

  public HashMap getSymInfos() {
    return symInfos;
  }

  public void setSymInfos(HashMap symInfos) {
    this.symInfos = symInfos;
  }

  public ArrayList<String> getSymbols() {
    return symbols;
  }

  public void setSymbols(ArrayList<String> symbols) {
    this.symbols = symbols;
  }



  public PtsSymbolInfos() {
    symbols = new ArrayList<String>();
    symInfos = new HashMap();
  }



  public PtsSymbolInfo getSymbolInfo(String sym) {
    return (PtsSymbolInfo) (symInfos.get(sym));
  }

  public void getDistinctSymbolInfos() {
    try {
      PreparedStatement pstmt = PtsDBops.activeFuturesDetails();
      ResultSet res = pstmt.executeQuery();
      symInfos.clear();
      symbols.clear();
      while (res.next()) {
        PtsSymbolInfo si = new PtsSymbolInfo();
        si.symbol = res.getString("symbol");
        symbols.add(si.symbol);
        si.exchange = res.getString("exchange");
        si.multiplier = res.getInt("multiplier");
        si.priceMagnifier = res.getInt("priceMagnifier");
        si.mintick = res.getDouble("mintick");
        si.fullName = res.getString("fullName");
        si.minDate = new DateTime(res.getDate("mindate"));
        si.maxDate = new DateTime(res.getDate("maxdate"));
        si.maxActiveExpiry = res.getInt("maxexpiry");
        symInfos.put(si.symbol, si);
        //symbols.add(res.getString("symbol"));
      }
      res.close();
    } catch (SQLException ex) {
      System.err.println("SQL Exception in getDistinctSymbolInfos:");
      System.err.println(ex.getMessage());
    }
  }
}
