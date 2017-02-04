/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atharva.sharekhan.interactive;

import com.atharva.sharekhan.core.CConstants;
import com.atharva.sharekhan.core.MessageHeader;

/**
 *
 * @author Janus
 */
public class ExchangeConfirmation {
    public MessageHeader header;
    public String ExchangeCode;
    public short AckCode;
    public short Msg_Length;
    public  String ShareKhanOrderID="";
    public String ExchangeOrderID="";
    public String ExchangeDateTime;
    public String TradeID;
    public String CustomerID;
    public String ScripToken;
    public String BuySell;
    public int  OrderQty;
    public int RemainingQty;
    public int TradeQty;
    public int DiscQty;
    public int DiscRemainingQty;
    public int OrderPrice;
    public int TriggerPrice;
    public int TradePrice;
    public String ExchangeGTD;
    public String ExchangeGTDDate;
    public String ChannelCode;
    public String ChannelUser;
    public String ErrorMessage;
    public int OrderTrailingPrice;
    public int OrderTargetPrice;
    public int UpperPrice;
    public int ChildSLPrice;
    public int LowerPrice;
    public String Reserved;
    
    public ExchangeConfirmation(byte[] bytes) throws Exception{
        header = new MessageHeader(bytes);
        ExchangeCode = CConstants.getString(bytes, 6, 2);
        AckCode = CConstants.getInt16(bytes, 8);
        Msg_Length = CConstants.getInt16(bytes, 10);
        ShareKhanOrderID = CConstants.getString(bytes,12,20);
        ExchangeOrderID = CConstants.getString(bytes,32,20);
        ExchangeDateTime = CConstants.getString(bytes,52,25);
        TradeID = CConstants.getString(bytes,77,20);
        CustomerID = CConstants.getString(bytes,97,10);
        ScripToken = CConstants.getString(bytes,107,10);
        BuySell = CConstants.getString(bytes,117,10);
        OrderQty = CConstants.getInt32(bytes, 127);
        RemainingQty = CConstants.getInt32(bytes, 131);
        TradeQty = CConstants.getInt32(bytes, 135);
        DiscQty = CConstants.getInt32(bytes, 139);
        DiscRemainingQty = CConstants.getInt32(bytes, 143);
        OrderPrice = CConstants.getInt32(bytes, 147);
        TriggerPrice = CConstants.getInt32(bytes, 151);
        TradePrice = CConstants.getInt32(bytes, 155);
        ExchangeGTD = CConstants.getString(bytes,159,5);
        ExchangeGTDDate = CConstants.getString(bytes,164,25);
        ChannelCode = CConstants.getString(bytes,189,10);
        ChannelUser = CConstants.getString(bytes,199,30);
        ErrorMessage = CConstants.getString(bytes,229,250);
        OrderTrailingPrice = CConstants.getInt32(bytes, 479);
        OrderTargetPrice = CConstants.getInt32(bytes, 483);
        UpperPrice = CConstants.getInt32(bytes, 487);
        ChildSLPrice = CConstants.getInt32(bytes, 491);
        LowerPrice = CConstants.getInt32(bytes, 495);
        Reserved = CConstants.getString(bytes,499,100);
    }
}
