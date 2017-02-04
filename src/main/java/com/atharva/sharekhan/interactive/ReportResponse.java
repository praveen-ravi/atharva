/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atharva.sharekhan.interactive;

import com.atharva.sharekhan.core.CConstants;
import com.atharva.sharekhan.core.MessageHeader;

/**
 *
 * @author Janus
 */
public class ReportResponse {
    public MessageHeader header;
    public int RecordCount;
    //Only one of the following two can be populated in one instance of ReportReponse
    public EquityOrderReportItem[] EquityOrderReportItems;
    public DerivativeOrderReportItem[] DerivativeOrderReportItems;
    public String Reserved;
    
    public ReportResponse(byte[] bytes) throws Exception{
        header = new MessageHeader(bytes);
        RecordCount = CConstants.getInt16(bytes, 6);
        if(header.getTransaction_code()==CConstants.TransactionCode.CashOrderReport){
            EquityOrderReportItems = new EquityOrderReportItem[RecordCount];
            for (int i = 0; i < RecordCount; i++) {
                byte[] subStruct = new byte[CConstants.EquityOrderReportItemSize];
                System.arraycopy(bytes, (i*CConstants.EquityOrderReportItemSize)+8, subStruct, 0, CConstants.EquityOrderReportItemSize);
                EquityOrderReportItems[i]  = new EquityOrderReportItem(subStruct);
            }
            
        }
        else if(header.getTransaction_code()==CConstants.TransactionCode.DerivativeOrderReport){
            DerivativeOrderReportItems = new DerivativeOrderReportItem[RecordCount];
            for (int i = 0; i < RecordCount; i++) {
                byte[] subStruct = new byte[CConstants.DerivativeOrderReportItemSize];
                System.arraycopy(bytes, (i*CConstants.DerivativeOrderReportItemSize)+8, subStruct, 0, CConstants.DerivativeOrderReportItemSize);
                DerivativeOrderReportItems[i]  = new DerivativeOrderReportItem(subStruct);
            }    
        }
        //Mapping of reserved string is skipped because it is not used;
    }
}
