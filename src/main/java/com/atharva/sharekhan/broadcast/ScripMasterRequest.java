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
public class ScripMasterRequest {
    private MessageHeader msgHeader;
    private String exchangecode;
    
    public ScripMasterRequest(MessageHeader msgHeader, String exchangecode) {
        this.msgHeader = msgHeader;
        this.exchangecode = exchangecode;
    }
    
    public byte[] getAsByteArray() throws UnsupportedEncodingException{
        byte[] array = new byte[108];
        System.arraycopy(msgHeader.getByteArray(), 0, array, 0, msgHeader.getByteArray().length);
        CConstants.setString(exchangecode, 2, array, 6);
        return array;
    }
    
}
