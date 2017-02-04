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
public class DerivativeOrderReportItem {
 public int DataLength;
    public String ExchangeCode;
    public String OrderStatus;
    public String OrderID;
    public String ExchangeOrderID;
    public String CustomerID;
    public String S2KID;
    public String ScripToken;
     public String Order_Type;
    public String BuySell;
    public int OrderQty;
    public int ExecutedQty;
    public int OrderPrice;
    public int ExecutedPrice;
    public int AveragePrice;
    public String DateTime;
    public String RequestStatus;
    public String ChannelCode;
    public String ChannelUser;
    public String LastModTime;
    public int OpenQty;
    public String POI;
    public int DisclosedQty;
    public String MIF;
    public int TriggerPrice;
    public String RMSCode;
    public String AfterHour;
    public String GoodTill;
    public String GoodTillDate;
    public String UpdateDate;
    public String UpdateUser;
    public String CALevel;
    public String AON;
    public String OPOC;
    public String FNOOrderType;
    public String BuySellFlag;
    public String Reserved;
    
    public DerivativeOrderReportItem(byte[] bytes) throws Exception{
    DataLength  = CConstants.getInt32(bytes, 0);
    ExchangeCode= CConstants.getString(bytes, 4, 2);
    OrderStatus = CConstants.getString(bytes, 6, 20);
    OrderID = CConstants.getString(bytes, 26, 20);
    ExchangeOrderID = CConstants.getString(bytes, 46, 25);
    CustomerID = CConstants.getString(bytes, 71, 10);
    S2KID = CConstants.getString(bytes, 81, 25);
    ScripToken = CConstants.getString(bytes, 106, 10);
    Order_Type = CConstants.getString(bytes, 116, 10);
    BuySell = CConstants.getString(bytes,126, 2);
    OrderQty = CConstants.getInt32(bytes, 128);
    ExecutedQty = CConstants.getInt32(bytes, 132);
    OrderPrice = CConstants.getInt32(bytes, 136);
    AveragePrice = CConstants.getInt32(bytes, 140);
    DateTime = CConstants.getString(bytes, 144, 25);
    RequestStatus = CConstants.getString(bytes, 169, 15);
    ChannelCode = CConstants.getString(bytes, 184, 10);
    ChannelUser = CConstants.getString(bytes, 194, 20);
    LastModTime = CConstants.getString(bytes, 214, 25);
    OpenQty = CConstants.getInt32(bytes, 239);
    POI = CConstants.getString(bytes, 243, 25);
    DisclosedQty = CConstants.getInt32(bytes, 268);
    MIF = CConstants.getString(bytes, 272, 50);
    TriggerPrice = CConstants.getInt32(bytes, 322);
    RMSCode = CConstants.getString(bytes, 326, 15);
    AfterHour = CConstants.getString(bytes, 341, 1);
    GoodTill = CConstants.getString(bytes, 342, 5);
    GoodTillDate = CConstants.getString(bytes, 347, 25);
    UpdateDate = CConstants.getString(bytes, 372, 25);
    UpdateUser = CConstants.getString(bytes, 397, 25);
    CALevel = CConstants.getString(bytes, 422, 15);
    AON = CConstants.getString(bytes, 437, 25);
    OPOC = CConstants.getString(bytes, 462, 25);
    FNOOrderType = CConstants.getString(bytes, 487, 25);
    BuySellFlag = CConstants.getString(bytes, 512, 1);
    Reserved = CConstants.getString(bytes, 513, 100);
    }
}
