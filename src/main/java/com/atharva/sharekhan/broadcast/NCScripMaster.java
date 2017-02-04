/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atharva.sharekhan.broadcast;

import com.atharva.sharekhan.core.CConstants;

import java.io.UnsupportedEncodingException;

/**
 *
 * @author Janus
 */
public class NCScripMaster {
    public int dataLength;
    public String segment;
    public String scripCode;
    public String scripShortName;
    public long scrip_id;//Applicable when fetching
    public String gexchange;
    

    public String getGexchange() {
        return gexchange;
    }

    public void setGexchange(String gexchange) {
        this.gexchange = gexchange;
    }
    public NCScripMaster(byte[] bytes) throws UnsupportedEncodingException, Exception{
        dataLength = CConstants.getInt32(bytes, 0);
        segment = CConstants.getString(bytes, 4, 10);
        scripCode = CConstants.getString(bytes, 14, 10);
        scripShortName = CConstants.getString(bytes, 24, 60);
                
    }

    public int getDataLength() {
        return dataLength;
    }

    public void setDataLength(int dataLength) {
        this.dataLength = dataLength;
    }

    public String getSegment() {
        return segment;
    }

    public void setSegment(String segment) {
        this.segment = segment;
    }

    public String getScripCode() {
        return scripCode;
    }

    public void setScripCode(String scripCode) {
        this.scripCode = scripCode;
    }

    public String getScripShortName() {
        return scripShortName;
    }

    public void setScripShortName(String scripShortName) {
        this.scripShortName = scripShortName;
    }
    
     @Override
    public String toString(){
    return scripShortName;    
    }
    
    public NCScripMaster(){
        
    }
}
