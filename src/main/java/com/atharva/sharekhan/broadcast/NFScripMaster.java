/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atharva.sharekhan.broadcast;

import com.atharva.sharekhan.core.CConstants;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 *
 * @author Janus
 */
public class NFScripMaster {
    public int dataLenght;
    public String derivativeType;
    public String scripCode;
    public String scripShortName;
    public String expiryDate;
    public String futOption;
    public int strikePrice;
    public int lotSize;
    public long scrip_id;//Scrip ID for fetching purposes only;
    public long expiry;
  
    public long getScrip_id() {
        return scrip_id;
    }

    public void setScrip_id(long scrip_id) {
        this.scrip_id = scrip_id;
    }
    
    public NFScripMaster(){
        
    }
    public NFScripMaster(byte[] bytes) throws UnsupportedEncodingException, Exception{
        dataLenght = CConstants.getInt32(bytes, 0);
        derivativeType = CConstants.getString(bytes, 4, 10);
        scripCode = CConstants.getString(bytes, 14, 10);
        scripShortName = CConstants.getString(bytes, 24, 60);
        expiryDate = CConstants.getString(bytes, 84, 15);
        futOption = CConstants.getString(bytes, 99, 10);
        strikePrice = CConstants.getInt32(bytes, 109);
        lotSize = CConstants.getInt32(bytes, 113);
          SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yy");
        try {
            expiry = format.parse(expiryDate).getTime();
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
    }

    public int getDataLenght() {
        return dataLenght;
    }

    public String getDerivativeType() {
        return derivativeType;
    }

    public String getScripCode() {
        return scripCode;
    }

    public String getScripShortName() {
        return scripShortName;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public String getFutOption() {
        return futOption;
    }

    public int getStrikePrice() {
        return strikePrice;
    }

    public int getLotSize() {
        return lotSize;
    }
    
    @Override
    public String toString(){
    return scripShortName;    
    }
    
    public long getExpiryMillis(){
        
        return expiry;
    }
    
    public void setExpiry(long expiry){
        this.expiry = expiry;
    }
    
    public void setExpiryDate(String date){
        expiryDate =date;
        SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yy");
        try {
            expiry = format.parse(expiryDate).getTime();
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
    }
}
