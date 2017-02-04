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
public class OrderConfirmationItem {
    public int DataLength;
    public String StatusCode;
    public String Message;
    public String SharekhanOrderID;
    public String OrderDateTime;
    public String RMSCode;
    public String CoverOrderID;
    public String Reserved;
    
    public OrderConfirmationItem(byte[] data) throws Exception{
        CConstants.getInt32(data, 0);
        StatusCode = CConstants.getString(data, 4, 25);
        Message = CConstants.getString(data, 29, 250);
        SharekhanOrderID = CConstants.getString(data, 279, 20);
        OrderDateTime = CConstants.getString(data, 25, 299);
        RMSCode = CConstants.getString(data, 15, 324);
        CoverOrderID = CConstants.getString(data, 20, 339);
        Reserved = CConstants.getString(data, 100, 359);
    }
}
