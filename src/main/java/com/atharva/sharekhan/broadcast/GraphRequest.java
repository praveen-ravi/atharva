/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atharva.sharekhan.broadcast;

import com.atharva.sharekhan.core.CConstants;
import com.atharva.sharekhan.core.MessageHeader;

import java.io.UnsupportedEncodingException;

/**
 *
 * @author Janus
 */
public class GraphRequest {
    MessageHeader header;
    String exchange;
    String scripCode;
    String dataType;
    short CorporateActionAdjusted=0;
    short numDays=0;
    String reserved;//To be ignored;

    public GraphRequest(String exchange, String scripCode) {
        header = new MessageHeader(CConstants.GraphRequestSize, (short) CConstants.TransactionCode.GraphRequest);
        this.exchange = exchange;
        this.scripCode = scripCode;
        this.dataType = "Graph";
        
    }
    
    public byte[] getBytes() throws UnsupportedEncodingException{
        byte[] bytes = new byte[CConstants.GraphRequestSize];
        System.arraycopy(header.getByteArray(), 0, bytes, 0, 6);
        CConstants.setString(this.exchange, 2, bytes, 6);
        CConstants.setString(this.scripCode, 10, bytes, 8);
        CConstants.setString(this.dataType, 10, bytes, 18);
        CConstants.setBytes(this.CorporateActionAdjusted, bytes, 28);
        CConstants.setBytes(this.numDays, bytes, 30);
        return bytes;
    }
}
