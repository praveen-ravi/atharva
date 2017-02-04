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
public class FeedRequest {
    MessageHeader header;
    short count;
    String scripList;
    
    public FeedRequest(short count,String scripList)
    {
        header = new MessageHeader(CConstants.FeedRequestSize, (short) CConstants.TransactionCode.FeedRequest);
        this.count= count;
        this.scripList = scripList;
    }
    
    public byte[] getBytes() throws UnsupportedEncodingException{
        byte[] bytes = new byte[CConstants.FeedRequestSize];
        System.arraycopy(header.getByteArray(), 0, bytes, 0, 6);
        CConstants.setBytes(count, bytes, 6);
        CConstants.setString(scripList, 12, bytes, 8);
        return bytes;
    }
}
