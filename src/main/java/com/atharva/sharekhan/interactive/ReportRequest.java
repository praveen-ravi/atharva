/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atharva.sharekhan.interactive;

import com.atharva.sharekhan.core.CConstants;
import com.atharva.sharekhan.core.MessageHeader;

import java.io.UnsupportedEncodingException;

/**
 *
 * @author Janus
 */
public class ReportRequest {
  public MessageHeader header;
  public String LoginID;
  public String CustomerID;
  public String DateTime="";
  public String ScripCode="";
  public String OrderID="";
  public String Reserved="";
    
  /**
   * 
   * @param TransactionCode  :Provide transaction code from report request types
   */
  public ReportRequest(short TransactionCode){
      this.header = new MessageHeader(181, TransactionCode);//Assumed Size to be 181
  }
  
  public byte[] getBytes() throws UnsupportedEncodingException{
      byte[] bytes = new byte[181];
      System.arraycopy(header, 0, bytes, 0, 6);
      CConstants.setString(LoginID, 20, bytes, 6);
      CConstants.setString(CustomerID, 10, bytes, 26);
      CConstants.setString(DateTime, 25, bytes, 36);
      CConstants.setString(ScripCode, 10, bytes, 61);
      CConstants.setString(OrderID, 10, bytes, 71);
      CConstants.setString(Reserved, 100, bytes, 81);
     
     return bytes;
  }
  
}
