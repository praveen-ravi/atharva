/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atharva.sharekhan.broadcast;

import com.atharva.sharekhan.core.CConstants;
import com.atharva.sharekhan.core.MessageHeader;

import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 *
 * @author Janus
 */
public class FeedResponse {
    
    public MessageHeader Prop01Header;
    public String Prop02Exchange;
    public String Prop03ScripToken;
    public int Prop04LTPrice;
    public int Prop05LTQuantity;
    public String Prop06LTDate;
    public int Prop07BidPrice;
    public int Prop08BidQuantity;
    public int Prop09OfferPrice;
    public int Prop10OfferQuantity;
    public int Prop11TotalTradedQty;
    public int Prop12TradedQuantity;
    public int Prop13AverageTradePrice;
    public int Prop14Open;
    public int Prop15High;
    public int Prop16Low;
    public int Prop17Close;
    public int Prop18PerChange;
    public int Prop19TurnOver;
    public int Prop20YearlyHigh;
    public int Prop21YearlyLow;
    public int Prop22UpperCkt;
    public int Prop23LowerCkt;
    public int Prop24Difference;
    public int Prop25CostofCarry1;
    public int Prop26CostOfCarry2;
    public String Prop27ChangeIndicator;
    public int Prop28SpotPrice;
    public String Prop29OITime;
    public int Prop30OI;
    public int Prop31OIHigh;
    public int Prop32OILow;
    public int Prop33TotalTrades;
    public String Prop34TradeValueFlag;
    public String Prop35Trend;
    public String Prop36SunFlag;
    public String Prop37AllnoneFlag;
    public int Prop38Tender;
    public String Prop39PriceQuotation;
    public int Prop40TotalBuyQty;
    public int Prop41TotalSellQty;
    public String Prop42SegmentId;
    public int Prop43OIDifference;
    public int Prop44OIDiffPercentage;
    public String Prop45Reserved;
    
    public FeedResponse(byte[] ByteStructure) throws Exception{
       Prop01Header = new MessageHeader(ByteStructure);
       Prop02Exchange = CConstants.getString(ByteStructure, 6, 5);
        Prop03ScripToken = CConstants.getString(ByteStructure, 11, 10);
        Prop04LTPrice = CConstants.getInt32(ByteStructure, 21);
        Prop05LTQuantity = CConstants.getInt32(ByteStructure, 25);
        Prop06LTDate = CConstants.getString(ByteStructure, 29, 25);
        Prop07BidPrice = CConstants.getInt32(ByteStructure, 54);
        Prop08BidQuantity = CConstants.getInt32(ByteStructure, 58);
        Prop09OfferPrice = CConstants.getInt32(ByteStructure, 62);
        Prop11TotalTradedQty = CConstants.getInt32(ByteStructure, 66);
        Prop12TradedQuantity = CConstants.getInt32(ByteStructure, 70);
        Prop13AverageTradePrice = CConstants.getInt32(ByteStructure, 74);
        Prop14Open = CConstants.getInt32(ByteStructure, 78);
        Prop15High = CConstants.getInt32(ByteStructure, 82);
        Prop16Low = CConstants.getInt32(ByteStructure, 86);
        Prop17Close = CConstants.getInt32(ByteStructure, 90);
        Prop18PerChange = CConstants.getInt32(ByteStructure, 94);
        Prop19TurnOver = CConstants.getInt32(ByteStructure, 98);
        Prop20YearlyHigh = CConstants.getInt32(ByteStructure, 102);
        Prop21YearlyLow = CConstants.getInt32(ByteStructure, 106);
        Prop22UpperCkt = CConstants.getInt32(ByteStructure, 110);
        Prop23LowerCkt = CConstants.getInt32(ByteStructure, 114);
        Prop24Difference = CConstants.getInt32(ByteStructure, 118);
        Prop25CostofCarry1 = CConstants.getInt32(ByteStructure, 122);
        Prop26CostOfCarry2 = CConstants.getInt32(ByteStructure, 126);
        Prop27ChangeIndicator = CConstants.getString(ByteStructure, 130, 10);
        Prop28SpotPrice = CConstants.getInt32(ByteStructure, 140);
        Prop29OITime = CConstants.getString(ByteStructure, 144, 20);
        Prop30OI = CConstants.getInt32(ByteStructure, 164);
        Prop31OIHigh = CConstants.getInt32(ByteStructure, 168);
        Prop32OILow = CConstants.getInt32(ByteStructure, 172);
        Prop33TotalTrades = CConstants.getInt32(ByteStructure, 176);
        Prop34TradeValueFlag = CConstants.getString(ByteStructure, 180, 10);
        Prop35Trend = CConstants.getString(ByteStructure, 190, 10);
        Prop36SunFlag = CConstants.getString(ByteStructure, 200, 10);
        Prop37AllnoneFlag = CConstants.getString(ByteStructure, 210, 10);
        Prop38Tender = CConstants.getInt32(ByteStructure, 220);
        Prop39PriceQuotation = CConstants.getString(ByteStructure, 224, 20);
        Prop40TotalBuyQty = CConstants.getInt32(ByteStructure, 244);
        Prop41TotalSellQty = CConstants.getInt32(ByteStructure, 248);
        Prop42SegmentId = CConstants.getString(ByteStructure, 252, 20);
        Prop43OIDifference = CConstants.getInt32(ByteStructure, 272);
        Prop44OIDiffPercentage = CConstants.getInt32(ByteStructure, 276);
        Prop45Reserved = CConstants.getString(ByteStructure, 280, 100);
    }
    @Override
    public String toString()
            {
                return Prop01Header.toString() + "|" + "Exchange = " + Prop02Exchange + "|" + "ScripToken = " + Prop03ScripToken + "|" + "LTPrice  = " + Prop04LTPrice + "|" + "LTQuantity = " + Prop05LTQuantity + "|" + "LTDate = " + Prop06LTDate + "|" + "BidPrice = " + Prop07BidPrice + "|" + "BidQuantity = " + Prop08BidQuantity
                    + "|" + "OfferPrice = " + Prop09OfferPrice + "|" + "OfferQuantity = " + Prop10OfferQuantity + "|" + "TotalTradedQty = " + Prop11TotalTradedQty + "|" + "TradedQuantity = "
                    + Prop12TradedQuantity + "|" + "AverageTradePrice = " + Prop13AverageTradePrice + "|" + "Open = " + Prop14Open + "|" + "High = " + Prop15High + "|" + "Low = " + Prop16Low + "|" + "Close = " + Prop17Close + "|" + "PerChange = " + Prop18PerChange + "|" + "TurnOver = " + Prop19TurnOver + "|" + "YearlyHigh = " + Prop20YearlyHigh
                     + "|" + "YearlyLow  = " + Prop21YearlyLow + "|" + "UpperCkt = " + Prop22UpperCkt + "|" + "LowerCkt = " + Prop23LowerCkt + "|" + "Difference  = " + Prop24Difference + "|" + "CostofCarry1 = " + Prop25CostofCarry1 + "|" + "CostOfCarry2 = " + Prop26CostOfCarry2 + "|" + "ChangeIndicator = " + Prop27ChangeIndicator + "|" + "SpotPrice = " + Prop28SpotPrice + "|" + "OITime = " + Prop29OITime + "|" + "OI = " + Prop30OI
                      + "|" + "High  = " + Prop31OIHigh + "|" + "OILow = " + Prop32OILow + "|" + "TotalTrades = " + Prop33TotalTrades + "|" + "TradeValueFlag = " + Prop34TradeValueFlag + "|" + "Trend = " + Prop35Trend
                    + "|" + "SunFlag  = " + Prop36SunFlag + "|" + "AllnoneFlag = " + Prop37AllnoneFlag + "|" + "Tender = " + Prop38Tender + "|" + "PriceQuotation = " + Prop39PriceQuotation + "|" + "TotalBuyQty = " + Prop40TotalBuyQty
                   + "|" + "SellQty  = " + Prop41TotalSellQty + "|" + "SegmentId = " + Prop42SegmentId + "|" + "OIDifference = " + Prop43OIDifference + "|" + "DiffPercentage = " + Prop44OIDiffPercentage + "|" + "Reserved = " + Prop45Reserved;
            }
    public long getLTDate() throws ParseException,NumberFormatException{
      try{
        
           int month = Integer.parseInt(Prop06LTDate.substring(0,2));
        int day = Integer.parseInt(Prop06LTDate.substring(3,5));
        int year = Integer.parseInt(Prop06LTDate.substring(6,10));
        int hour = Integer.parseInt(Prop06LTDate.substring(11,13));
        int minute = Integer.parseInt(Prop06LTDate.substring(14,16));
        int second = Integer.parseInt(Prop06LTDate.substring(17,19));
        Calendar cal = new GregorianCalendar(year, month-1, day, hour, minute, second);
                
       // System.out.println(l);
        return cal.getTimeInMillis();
      }catch(Exception ex){
          System.out.println("error:"+Prop06LTDate+"\t"+ex.toString());
          ex.printStackTrace();
          throw ex;
      }
       
    }
    
    public float getDayHigh(){
        return this.Prop16Low/(float)100.00;
    }
    public float getDayLow(){
         return this.Prop17Close/(float)100.00;
    }
    public float getOpen(){
        return this.Prop14Open/(float)100.00;
    }
    public float getLTP(){
        return this.Prop04LTPrice/(float)100.00;
    }
    public long getVolume(){
        return Prop12TradedQuantity;
    }
    
    public float getBid(){
        return this.Prop07BidPrice/(float)100.00;
    }
    
    public float getOffer(){
        return this.Prop09OfferPrice/(float)100.00;
    }
}
