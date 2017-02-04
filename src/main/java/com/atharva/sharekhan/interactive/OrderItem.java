/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atharva.sharekhan.interactive;

import com.atharva.sharekhan.core.CConstants;

import java.io.UnsupportedEncodingException;

/**
 *
 * @author Janus
 */
public class OrderItem {
    public int DataLength=227;
    public String OrderID="";//Default Blank
    public String CustomerID;//Client Code
    public String S2KID="";//Another Client Code
    public String ScripToken;
    public String BuySell;
    public int OrderQty;
    public int OrderPrice;
    public int TriggerPrice;
    public int DisclosedQty;
    public int ExecutedQty;
    public String RMSCode="";
    public int ExecutedPrice;
    public String AfterHour="N";
    public String GTDFlag="";
    public String GTD="";
    final String Reserved="";
    
    
   
    
    public byte[] getBytes() throws UnsupportedEncodingException{
        byte[] bytes = new byte[CConstants.OrderItemSize];
        CConstants.setBytes(DataLength, bytes, 0);
        CConstants.setString(OrderID, 20, bytes, 4);
        CConstants.setString(CustomerID, 10, bytes, 24);
        CConstants.setString(S2KID, 10, bytes, 34);
        CConstants.setString(ScripToken, 10, bytes, 44);
        CConstants.setString(BuySell, 3, bytes, 54);
        CConstants.setBytes(OrderQty,bytes, 57);
        CConstants.setBytes(OrderPrice,bytes, 61);
        CConstants.setBytes(TriggerPrice,bytes, 65);
        CConstants.setBytes(DisclosedQty,bytes, 69);
        CConstants.setBytes(ExecutedQty,bytes, 73);
        CConstants.setString(RMSCode,15,bytes, 77);
        CConstants.setBytes(ExecutedPrice,bytes, 92);
        CConstants.setString(AfterHour,1,bytes, 96);
        CConstants.setString(GTDFlag,5,bytes, 97);
        CConstants.setString(GTD,25,bytes, 102);
        CConstants.setString(Reserved,100,bytes, 127);
        return bytes;
    }
}
