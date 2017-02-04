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
public class GraphResponse {
   public MessageHeader msgheader;
   public String exchangecode;
   public String scripcode;
   public  String datatype;
   public  int recordcount;
   public int corpcount;
   
   public GraphResponse(byte[] bytes) throws UnsupportedEncodingException, Exception{
       msgheader = new MessageHeader(bytes);
       exchangecode = CConstants.getString(bytes, 6, 2);
       scripcode = CConstants.getString(bytes, 8, 10);
       datatype = CConstants.getString(bytes, 18, 10);
       recordcount = CConstants.getInt32(bytes, 28);
       corpcount = CConstants.getInt32(bytes, 32);
   }
}
