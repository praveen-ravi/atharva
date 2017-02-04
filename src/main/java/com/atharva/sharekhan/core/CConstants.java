/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atharva.sharekhan.core;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

/**
 *
 * @author Janus
 */
public class CConstants {
        public static char Seperator = '|';
        public static int SendTrail = 5;
        public static int MaxThreadWaitTime = 50;
        public static int TrailCountMain = 3;		/// No of times to try send data Main 
        public static long BytesOut = 0;
        public static String LogSeparator = "@";


        public static int TAPHeaderSize = 22;
        public static int MinPacketSize = 60;
        public static int MinBcastpacket = 50;



        public static int SignOff = 190;//185;
        public static int Contract = 28;
        public static int LegInfo = 80;

        public static String TapIp;
        public static String TapPort;
        public static String LoginId;
        public static String MemberPassword;
        public static String TradingPassword;
        public static int MsgHeaderSize = 6;
        public static int LoginRequestSize = 196;
        public static int LoginResponseSize = 433;
        public static int LogOffRequestSize = 136;
        //public static int OrderRequestSize = 357;
        public static int OrderItemSize = 227;
        public static int FeedRequestSize = 120;
        public static int ScripMasterRequest = 108;
        public static int ReportRequestSize = 181;
        public static int MarketDepthRequestSize = 121;
        public static int CommodityMasterSize = 235;
        public static int CurrencycripMasterSize = 329;
        public static int CashcripMasterSize = 184;
        public static int OrderRequestSize = 357;
        public static int DerivativeMasterItemSize = 217;
        public static int GraphRequestSize = 164;
        public static int OrderConfirmationItemSize = 459;
        public static int ExchangeConfirmationSize = 599;
        public static int EquityOrderReportItemSize = 459;
        public static int DerivativeOrderReportItemSize = 613;
        

        public static String NFExCode = "NF";
        public static String NCExcode = "NC";
        public static String BCExcode = "BC";
        public static String NXExcode = "NX";
        public static String MXExcode = "MX";       
        public static String RNExCode = "RN";
        public static String RMExcode = "RM";
        
        public static  class TransactionCode
        {  
        public static short LoginRequest = 1;
        public static short  LogOffRequest = 2;
        public static short  ScripMasterRequest = 21;
        public static short Invitation = 15000;
        public static short OrderRequest = 11;
        public static short SharekhanOrderConfirmation = 11;
        public static short ExchangeOrderConfirmation = 13;
        public static short ExchangeTradeConfirmation = 14;
        public static short FeedRequest = 22;
        public static short DepthRequest = 24;
        public static short GraphRequest = 61;
        public static short CashOrderReport = 31;
        public static short CashDPSRReport = 32;
        public static short CashOrdeDetailsReport = 33;
        public static short CashTradeDetailsReport = 34;
        public static short CashLimitReport = 35;
        public static short CashNetPositionsReport = 35;
        public static short DerivativeOrderReport = 41;
        public static short DerivativeTurnoverReport = 42;
        public static short DerivativeFOOrderDetailsReport = 43;
        public static short DerivativeFOTradeDetailsReport = 44;
        
        
        }
        
        public static class AckCode{
            
        }
        
        public static String getString(byte[] bytes,int index, int size) throws Exception{
            byte[] array = new byte[size];
            System.arraycopy(bytes, index, array, 0, array.length);
            return getString(array);
        }
        
        public static String getString(byte[] data) throws Exception{
             return new String(data,Charset.forName("UTF-8")).replaceAll("\0", "");
        }
        
        public static int getInt32(byte[] bytes, int beginIndex){
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        System.arraycopy(bytes, beginIndex, buffer.array(), 0, buffer.array().length);
        return buffer.getInt();
        }
        public static short getInt16(byte[] bytes, int beginIndex){
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        System.arraycopy(bytes, beginIndex, buffer.array(), 0, buffer.array().length);
        return buffer.getShort();
        }
        
        public static void setBytes(int int32,byte[] bytes,int index){
        ByteBuffer buff = ByteBuffer.allocate(4);
        buff.order(ByteOrder.LITTLE_ENDIAN);
        buff.putInt((int)int32);
        System.arraycopy(buff.array() , 0, bytes, index, buff.array().length);
        }
        public static void setBytes(short int16, byte[] bytes, int index){
        ByteBuffer buff = ByteBuffer.allocate(2);
        buff.order(ByteOrder.LITTLE_ENDIAN);
        buff.putShort(int16);
        System.arraycopy(buff.array() , 0, bytes, index, buff.array().length);
        }
        
        public static void setString(String str, int size, byte[] array,int index) throws UnsupportedEncodingException{
            byte[] byteArray = new byte[size];
            byte[] stringBytes=str.getBytes("ISO-8859-1");
            System.arraycopy(stringBytes, 0, byteArray, 0, stringBytes.length);
            System.arraycopy(byteArray, 0, array, index, byteArray.length);
        }
        
}
