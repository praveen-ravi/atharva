/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atharva.sharekhan.broadcast;

import com.atharva.sharekhan.core.CConstants;

/**
 *
 * @author Janus
 */
public class Graph_CorpActItem {
    int dataLength;
    int date;
    int factor;
    int  newqty;
    int oldqty;
    int premium;
    String type;
    
    public Graph_CorpActItem(byte[] bytes) throws Exception{
    dataLength = CConstants.getInt32(bytes, 0);    
    date = CConstants.getInt32(bytes, 4);    
    factor = CConstants.getInt32(bytes, 8);    
    newqty = CConstants.getInt32(bytes, 12);    
    oldqty = CConstants.getInt32(bytes, 16);    
    premium = CConstants.getInt32(bytes, 20);    
    type =  CConstants.getString(bytes, 24, 15);
    }
}
