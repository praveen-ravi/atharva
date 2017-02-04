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
public class Graph_DataItem {
 public int dataLength;
 public int date;
 public String time;//25
 public int open;
 public int high;
 public int low;
 public int close;
 public int average;
 public int tradedvalue;
 public int quantity;
 public String reserved;
 
        public Graph_DataItem(byte[] bytes) throws Exception{
            dataLength = CConstants.getInt32(bytes, 0);
            date = CConstants.getInt32(bytes, 4);
            time = CConstants.getString(bytes, 8,25);
            open = CConstants.getInt32(bytes, 33);    
            high = CConstants.getInt32(bytes, 37);    
            low = CConstants.getInt32(bytes, 41);    
            close = CConstants.getInt32(bytes, 45);    
            average = CConstants.getInt32(bytes, 49);    
            tradedvalue = CConstants.getInt32(bytes, 53);    
            quantity = CConstants.getInt32(bytes, 57);    
            reserved = CConstants.getString(bytes, 61,100);
        }
}
