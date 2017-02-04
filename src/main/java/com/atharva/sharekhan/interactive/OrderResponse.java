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
public class OrderResponse {
    public MessageHeader header;
    public String RequestID;
    public String ExchangeCode;
    public short Count;
    public OrderConfirmationItem[] Confirmations;
    public String Reserved;
    
    public OrderResponse(byte[] bytes) throws Exception{
        header = new MessageHeader(bytes);
        RequestID = CConstants.getString(bytes, 6, 10);
        ExchangeCode = CConstants.getString(bytes, 2, 16);
        Count = CConstants.getInt16(bytes, 18);
        Confirmations = new OrderConfirmationItem[Count];
        for (int i = 0; i < Count; i++) {
            byte[] subStruct = new byte[CConstants.OrderConfirmationItemSize];
            System.arraycopy(bytes, (i*459)+20, subStruct, 0, CConstants.OrderConfirmationItemSize);
            Confirmations[i] = new OrderConfirmationItem(subStruct);
        }
        Reserved = CConstants.getString(bytes,20+(Count*CConstants.OrderConfirmationItemSize), 100);
        
    }
}
