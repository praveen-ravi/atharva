/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atharva.sharekhan.interactive;

import com.atharva.sharekhan.core.CConstants;
import com.atharva.sharekhan.core.MessageHeader;

import java.io.UnsupportedEncodingException;

/**
 *
 * @author Janus
 */
public class OrderRequest {
    public  MessageHeader header;
     public String requestID;
     public short orderCount;
     public String exchangecode;
     public String OrderType1;
     public OrderItem OrderItem;//Structure size 227 bytes
     public String Reserved="";
     public OrderRequest()
     {
         header = new MessageHeader(CConstants.OrderRequestSize,CConstants.TransactionCode.OrderRequest);
     }
     
     public byte[] getBytes() throws UnsupportedEncodingException{
         byte[] bytes = new byte[CConstants.OrderRequestSize];
         System.arraycopy(header.getByteArray(), 0, bytes, 0, header.getByteArray().length);
         CConstants.setString(requestID, 10, bytes, 6);
         CConstants.setBytes(orderCount,  bytes, 16);
         CConstants.setString(exchangecode, 2, bytes, 18);
         CConstants.setString(OrderType1, 10, bytes, 20);
         System.arraycopy(OrderItem.getBytes(), 0, bytes, 30, CConstants.OrderItemSize);
         CConstants.setString(Reserved, 100, bytes, 257);
         return bytes;
     }
}
