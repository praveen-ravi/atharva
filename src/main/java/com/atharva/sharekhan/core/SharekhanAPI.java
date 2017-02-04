/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atharva.sharekhan.core;


import com.atharva.sharekhan.broadcast.*;
import com.atharva.sharekhan.interactive.ExchangeConfirmation;
import com.atharva.sharekhan.interactive.LoginResponse;
import com.atharva.sharekhan.interactive.OrderConfirmationItem;
import com.atharva.sharekhan.interactive.OrderResponse;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Janus
 */
public class SharekhanAPI extends Observable
{
    public BroadcastSubject broadcast;
    public boolean socketMade;
    public class BroadcastSubject extends Observable{
        public void newData(String exchange,String token,FeedResponse dayMod){
            // System.out.println(exchange+"\t"+token+"\t"+dayMod.getLTP());
            setChanged();
            notifyObservers(new Object[]{exchange,token,dayMod});
        }
    }

    /**
     * Login Credentials required by Sharekhan Sockets
     */
    private final String username;
    private final String password;
    private final String tranPassword;
    private final String ip;

    /**
     * Upon each scripmaster filling, individual filled boolean would be set to true
     */
    public boolean ncMasterFilled;
    public boolean nfMasterFilled;
    public boolean bcMasterFilled;

    /**
     * Respective Exchange Token with it's Scripmaster item object
     */
    public HashMap<String,NCScripMaster> ncScripmaster;//Token, Master
    public HashMap<String,NCScripMaster> bcScripmaster;//Token, Master
    public HashMap<String,NFScripMaster> nfScripmaster;//Token, Master

    /**
     * Socket Used to connect with Sharekhan
     */
    Socket echoSocket;

    /**
     * When the API logs in - this will be set to true, indicating that the socket is now available
     */
    public boolean isLoggedin = false;

    /**
     * Scripmaster download
     */
    public ScripMasterDownloader downloadDisplayer;
    Calendar dayCal;



    SimpleDateFormat cashFormat = new SimpleDateFormat("DD/MM/YYYY HH:mm:ss");
    SimpleDateFormat derFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private ExecutorService interactive;


    private class Listen implements Runnable{

        @Override
        public void run() {
            InputStream   dIn = null;
            try {
                dIn = echoSocket.getInputStream();

                int msg_length=0;
                int index = 0;

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                while (true) {

                    byte[] fresh = new byte[10240];
                    int size  = dIn.read( fresh );
                    baos.write(fresh, 0, size);

                    while (baos.size()>index) {
                        msg_length = CConstants.getInt32(baos.toByteArray(), index);
                        if(baos.size()-index<msg_length){
                            //  System.out.println("More to come:"+(baos.size()-index)+"/"+msg_length);
                            break;
                        }
                        //System.out.println("Message length:"+msg_length+"\tStream:"+baos.size()+"\tIndex:"+index    );
                        byte[] data = new byte[msg_length];
                        System.arraycopy(baos.toByteArray(), index, data, 0, data.length);
                        short code = CConstants.getInt16(data, 4);
                        if(code==22)//Feeds are opened in individual threads and then passed on to their observers
                        {
                            new Thread(new SharekhanAPI.parseData(data)).start();
                        }
                        else//Interactive Messages are enqued to avoid wrong confirmation sequence
                        {
                            interactive.execute(new parseData(data));
                        }

                        index +=msg_length;
                    }

                    //System.out.println("Continuing to listen to socket at index:"+index);
                    if(baos.size()>msg_length){//There are bytes left after
                        //System.out.println("Bytes are left to be carried forward:"+(baos.size()-index));
                        byte[] remnant = new byte[baos.size()-index];
                        ///    System.out.println("Remnant will receive bytes from:"+(baos.size()-index));
                        System.arraycopy(baos.toByteArray(), index, remnant, 0, remnant.length);
                        baos.reset();
                        baos.write(remnant);
                    }
                    else if(baos.size()==msg_length){
                        baos.reset();

                    }
                    index = 0;
                    //System.out.println("Reset index to zero");







                }

            } catch (IOException ex) {
                Logger.getLogger(SharekhanAPI.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    dIn.close();
                } catch (IOException ex) {
                    Logger.getLogger(SharekhanAPI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }






        }
    }


    public SharekhanAPI(String username, String password, String tranPassword, String ip) throws IOException {
        interactive = Executors.newSingleThreadExecutor();
        dayCal = Calendar.getInstance();
        dayCal.set(Calendar.HOUR_OF_DAY, 0);
        dayCal.set(Calendar.MINUTE, 0);
        this.username = username;
        this.password = password;
        this.tranPassword = tranPassword;
        this.ip = ip;
        int port = 8000;
        echoSocket = new Socket(ip, port);
        echoSocket.setReceiveBufferSize(6092);
        echoSocket.setSendBufferSize(1024);
        echoSocket.setTcpNoDelay(true);
        downloadDisplayer = new ScripMasterDownloader(null, false);
        downloadDisplayer.setVisible(true);
        new Thread (new SharekhanAPI.Listen()).start();
        broadcast = new BroadcastSubject();
        socketMade = true;


    }
    public void connect() throws Exception{
        byte[] loginRequest = new byte[196];
        MessageHeader loginHeader = new MessageHeader(196, (short)CConstants.TransactionCode.LoginRequest);
        byte[] loginheaderbytes = loginHeader.getByteArray();
        System.arraycopy(loginheaderbytes, 0, loginRequest, 0, loginheaderbytes.length);
        CConstants.setString(username, 30, loginRequest, 6);
        CConstants.setString(password, 20, loginRequest, 36);
        CConstants.setString(tranPassword, 20, loginRequest, 56);
        CConstants.setString(ip, 20, loginRequest, 76);
        sendRequest(loginRequest);
        downloadDisplayer.statusLabel.setText("Login Request Sent");

    }

    public void sendScripMasterRequest() throws Exception{
        downloadDisplayer.statusLabel.setText("Sending ScripMaster download request");

        MessageHeader ncheader = new MessageHeader(CConstants.ScripMasterRequest, (short) CConstants.TransactionCode.ScripMasterRequest);
        ScripMasterRequest nfRequest = new ScripMasterRequest(ncheader, CConstants.NFExCode);
        sendRequest(nfRequest.getAsByteArray());
        Thread.sleep(2000L);
        downloadDisplayer.statusLabel.setText("NFO ScripMaster download request sent");
        ScripMasterRequest ncRequest = new ScripMasterRequest(ncheader, CConstants.NCExcode);
        sendRequest(ncRequest.getAsByteArray());
        Thread.sleep(2000L);
        downloadDisplayer.statusLabel.setText("NSE ScripMaster download request sent");
        ScripMasterRequest bcRequest = new ScripMasterRequest(ncheader, CConstants.BCExcode);
        sendRequest(bcRequest.getAsByteArray());
        Thread.sleep(2000L);
        downloadDisplayer.statusLabel.setText("BSE ScripMaster download request sent");
    }



    private class parseData implements Runnable{
        byte[] bytes;

        public parseData(byte[] bytes) {
            this.bytes = bytes;
        }


        @Override
        public void run() {
            MessageHeader header = new MessageHeader(bytes);
            //System.out.println("Length="+header.getMessage_length()+"|type="+header.getTransaction_code());
            if(header.getTransaction_code()==1){
                LoginResponse loginResponse = new LoginResponse(bytes);
                if(loginResponse.status_code==0){
                    isLoggedin = true;
                    downloadDisplayer.statusLabel.setText("Login Succesful");
                }else{
                    isLoggedin = false;
                    downloadDisplayer.statusLabel.setText("Login Failed for Some Reason");
                }
                System.out.println("Login Response: StatusCode="+loginResponse.getStatus_code()+"\t"+loginResponse.getMessage().replaceAll("\0",""));
            }


            else if(header.getTransaction_code()==21){
                //<editor-fold desc="ScripMasterResponse">
                try {
                    downloadDisplayer.statusLabel.setText("Scripmaster response received");
                    ScripMasterResponse masterResponse = new ScripMasterResponse(bytes);
                    switch(masterResponse.exchangecode){
                        case "NF":
                            nfScripmaster = new HashMap<>();
                            byte[] nfData = new byte[header.message_length-8];
                            System.out.println("NF Data size:"+nfData.length);
                            System.arraycopy(bytes, 8, nfData, 0, nfData.length);
                            int sourceIndex = 0;
                            int numberOfScrips = (int)nfData.length / CConstants.DerivativeMasterItemSize;
                            NFScripMaster nfScripMaster;
                            downloadDisplayer.nfoPrg.setMaximum(numberOfScrips);
                            downloadDisplayer.statusLabel.setText("Number of Scrips in master:"+numberOfScrips);
                            for (int i = 0; i < numberOfScrips; i++) {
                                byte[] nfScripData = new byte[CConstants.DerivativeMasterItemSize];
                                System.arraycopy(nfData, sourceIndex, nfScripData, 0, CConstants.DerivativeMasterItemSize);
                                nfScripMaster = new NFScripMaster(nfScripData);

                                // System.out.println(nfScripMaster.derivativeType+"\t"+nfScripMaster.scripShortName+"\t"+nfScripMaster.scripCode);
                                sourceIndex += CConstants.DerivativeMasterItemSize;
                                nfScripmaster.put(nfScripMaster.scripCode, nfScripMaster);
                                downloadDisplayer.nfoPrg.setValue(i);
                            }
                            nfMasterFilled = true;
                            downloadDisplayer.statusLabel.setText("NFO ScripMaster Downloaded");
                            break;
                        case "NC":
                            byte[] ncData = new byte[header.message_length - 8];
                            System.arraycopy(bytes, 8, ncData, 0, ncData.length);
                            NCScripMaster ncScripMaster;
                            numberOfScrips = ncData.length / CConstants.CashcripMasterSize;
                            System.out.println("NC Data size:"+ncData.length);
                            System.out.println("byte array size:"+bytes.length);
                            System.out.println("Number of Scrips in master:"+numberOfScrips);
                            sourceIndex = 0;
                            downloadDisplayer.nsePrg.setMaximum(numberOfScrips);
                            ncScripmaster = new HashMap<>();
                            for (int i = 0; i < numberOfScrips; i++)
                            {
                                byte[] ncScripData = new byte[CConstants.CashcripMasterSize];
                                System.arraycopy(ncData, sourceIndex, ncScripData, 0, CConstants.CashcripMasterSize);
                                ncScripMaster = new NCScripMaster(ncScripData);
                                ncScripMaster.gexchange = "NC";
                                //   System.out.println(ncScripMaster.scripCode+"\t"+ncScripMaster.scripShortName+"\t"+ncScripMaster.segment);
                                sourceIndex += CConstants.CashcripMasterSize;
                                ncScripmaster.put(ncScripMaster.scripCode, ncScripMaster);
                                downloadDisplayer.nsePrg.setValue(i);
                            }
                            downloadDisplayer.statusLabel.setText("NSE ScripMaster Downloaded");
                            ncMasterFilled = true;
                            break;
                        case "BC":
                            byte[] bcData = new byte[header.message_length - 8];
                            System.arraycopy(bytes, 8, bcData, 0, bcData.length);
                            NCScripMaster bcScripMaster;
                            numberOfScrips = bcData.length / CConstants.CashcripMasterSize;
                            downloadDisplayer.bsePrg.setMaximum(numberOfScrips);
                            System.out.println("BC Data size:"+bcData.length);
                            System.out.println("byte array size:"+bytes.length);
                            System.out.println("Number of Scrips in master:"+numberOfScrips);
                            sourceIndex = 0;
                            bcScripmaster= new HashMap<>();
                            for (int i = 0; i < numberOfScrips; i++)
                            {
                                byte[] ncScripData = new byte[CConstants.CashcripMasterSize];
                                System.arraycopy(bcData, sourceIndex, ncScripData, 0, CConstants.CashcripMasterSize);
                                bcScripMaster = new NCScripMaster(ncScripData);
                                bcScripMaster.gexchange ="BC";
                                //   System.out.println(bcScripMaster.scripCode+"\t"+bcScripMaster.scripShortName+"\t"+bcScripMaster.segment);
                                sourceIndex += CConstants.CashcripMasterSize;
                                bcScripmaster.put(bcScripMaster.scripCode, bcScripMaster);
                                downloadDisplayer.bsePrg.setValue(i);
                            }
                            bcMasterFilled = true;
                            downloadDisplayer.statusLabel.setText("BSE ScripMaster Downloaded");
                            break;
                    }
                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(SharekhanAPI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(SharekhanAPI.class.getName()).log(Level.SEVERE, null, ex);
                }
                //</editor-fold>
            }
            else if(header.transaction_code==22){
                //<editor-fold desc="FeedResponse">
                try {
                    FeedResponse feedResponse = new FeedResponse(bytes);
                    /**
                     *  String token = feedResponse.Prop03ScripToken;
                     String exchange = feedResponse.Prop02Exchange;
                     float open  = feedResponse.Prop14Open/(float)100.00;
                     float ltp  = feedResponse.Prop04LTPrice/(float)100.00;
                     float low = feedResponse.Prop17Close/(float)100.00;
                     float high = feedResponse.Prop16Low/(float)100.00;
                     float prev_close = feedResponse.Prop18PerChange/(float)100.00;
                     float yearHigh = feedResponse.Prop20YearlyHigh/(float)100.00;
                     float yearLow = feedResponse.Prop21YearlyLow/(float)100.00;
                     float bid = feedResponse.Prop07BidPrice/(float)100.00;
                     float offer = feedResponse.Prop09OfferPrice/(float)100.00;
                     int trades = feedResponse.Prop31OIHigh;
                     double openInt = (double)feedResponse.Prop30OI;
                     long volume = feedResponse.Prop12TradedQuantity;
                     String dateString = feedResponse.Prop06LTDate;
                     System.out.println(feedResponse.toString());
                     */

                    //System.out.println(token+"\t"+ltp+"\t"+dateString);

                    if(feedResponse.getLTDate()>dayCal.getTimeInMillis()+86400000L
                            || feedResponse.getLTDate()<dayCal.getTimeInMillis()){//Parse error
                        return;
                    }
                    broadcast.newData(feedResponse.Prop02Exchange,feedResponse.Prop03ScripToken, feedResponse);

                } catch (Exception ex) {
                    Logger.getLogger(SharekhanAPI.class.getName()).log(Level.SEVERE, null, ex);
                }
                //</editor-fold>
            }
            else if(header.transaction_code==61)
            {

                try
                {
                    System.out.println("Graph Response Received");
                    GraphResponse graphResponse = new GraphResponse(bytes);
                    System.out.println("NF Graph Response");
                    if(graphResponse.corpcount>0){
                        //Map Graph Response Here
                    }
                    int ellapsed = graphResponse.corpcount*139;
                    ellapsed += 36;
                    byte[] nfData = new byte[header.message_length-ellapsed];
                    System.arraycopy(bytes, ellapsed, nfData, 0, nfData.length);
                    int sourceIndex = 0;
                    int records = graphResponse.recordcount;
                    Graph_DataItem item;

                    try{
                        Calendar cal = Calendar.getInstance();

                        int totalvolume = 0;




                    }catch(Exception ex){
                        ex.printStackTrace(System.err);

                    }

                }catch(Exception ex){
                    ex.printStackTrace(System.err);
                }


            }
            else if(header.transaction_code==CConstants.TransactionCode.SharekhanOrderConfirmation){
                try
                {
                    OrderResponse resp = new OrderResponse(bytes);
                    OrderConfirmationItem[] items =  resp.Confirmations;

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            else if (header.transaction_code==CConstants.TransactionCode.ExchangeOrderConfirmation){
                try
                {
                    //Exchange Order Confirmation
                    ExchangeConfirmation item = new ExchangeConfirmation(bytes);


                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            else if(header.transaction_code==CConstants.TransactionCode.ExchangeTradeConfirmation){
                try {
                    //Exchange Trade Confirmation
                    ExchangeConfirmation trade = new ExchangeConfirmation(bytes);

                } catch (Exception ex) {
                    Logger.getLogger(SharekhanAPI.class.getName()).log(Level.SEVERE, null, ex);
                }


            }
            else
            {
                System.out.println("Unknown Code received :"+header.transaction_code);
            }
        }

    }

    public void subscribe(String exchange, String token) throws IOException{
        FeedRequest request = new FeedRequest((short)1, exchange+token);
        sendRequest(request.getBytes());
        System.out.println("Feed Request sent:"+token+"\t"+exchange+"\t");
    }

    public void sendGraphRequest(String exchange, String token,long scrip_id) throws Exception{
        GraphRequest req = new GraphRequest(exchange, token);
        sendRequest(req.getBytes());
    }

    public void sendRequest(byte[] request) throws IOException{
        DataOutputStream outToServer = new DataOutputStream(echoSocket.getOutputStream());
        outToServer.write(request);
        outToServer.flush();
    }


}
