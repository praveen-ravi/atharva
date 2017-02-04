/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atharva.sharekhan.interactive;

import com.atharva.sharekhan.core.CConstants;

/**
 *
 * @author Janus
 */
public class EquityOrderReportItem {
    public int DataLength;
    public String ExchangeCode;
    public String OrderStatus;
    public String OrderID;
    public String ExchangeOrderID;
    public String ExchangeAckDateTime;
    public String CustomerID;
    public String S2KID;
    public String ScripToken;
    public String BuySell;
    public int OrderQty;
    public int DisclosedQty;
    public int ExecutedQty;
    public int OrderPrice;
    public int ExecutedPrice;
    public int TriggerPrice;
    public String RequestStatus;
    public String DateTime;
    public String AfterHour;
    public String RMSCode;
    public String GoodTill;
    public String GoodTillDate;
    public String ChannelCode;
    public String ChannelUser;
    public int OrderTrailingPrice;
    public int OrderTargetPrice;
    public int UpperPrice;
    public int LowerPrice;
    public int BracketSLPrice;
    public String Order_Type;
    public String TrailingStatus;
    public String CoverOrderID;
    public String UpperLowerFlag;
    public String Reserved;
    
    public EquityOrderReportItem(byte[] bytes) throws Exception
    {
    DataLength  = CConstants.getInt32(bytes, 0);
    ExchangeCode= CConstants.getString(bytes, 4, 2);
    OrderStatus = CConstants.getString(bytes, 6, 20);
    OrderID = CConstants.getString(bytes, 26, 20);
    ExchangeOrderID = CConstants.getString(bytes, 20, 46);
    ExchangeAckDateTime = CConstants.getString(bytes, 66, 25);
    CustomerID = CConstants.getString(bytes, 91, 10);
    S2KID = CConstants.getString(bytes, 101, 10);
    ScripToken = CConstants.getString(bytes, 111, 10);
    BuySell = CConstants.getString(bytes,121, 2);
    OrderQty = CConstants.getInt32(bytes, 123);
    DisclosedQty = CConstants.getInt32(bytes, 127);
    ExecutedQty = CConstants.getInt32(bytes, 131);
    OrderPrice = CConstants.getInt32(bytes, 135);
    ExecutedPrice = CConstants.getInt32(bytes, 139);
    TriggerPrice = CConstants.getInt32(bytes, 143);
    RequestStatus = CConstants.getString(bytes, 147, 15);
    DateTime = CConstants.getString(bytes, 162, 25);
    AfterHour = CConstants.getString(bytes, 187, 1);
    RMSCode = CConstants.getString(bytes, 188, 15);
    GoodTill = CConstants.getString(bytes, 203, 5);
    GoodTillDate = CConstants.getString(bytes, 208, 25);
    ChannelCode = CConstants.getString(bytes, 232, 10);
    ChannelUser = CConstants.getString(bytes, 242, 20);
    OrderTrailingPrice = CConstants.getInt32(bytes, 262);
    OrderTargetPrice = CConstants.getInt32(bytes, 267);
    UpperPrice = CConstants.getInt32(bytes, 271);
    LowerPrice = CConstants.getInt32(bytes, 275);
    BracketSLPrice = CConstants.getInt32(bytes, 279);
    Order_Type = CConstants.getString(bytes, 283, 25);
    TrailingStatus = CConstants.getString(bytes, 308, 25);
    CoverOrderID = CConstants.getString(bytes, 333, 25);
    UpperLowerFlag = CConstants.getString(bytes, 358, 1);
    Reserved = CConstants.getString(bytes, 359, 100);
    }
}
