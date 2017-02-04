package com.atharva.trade;

import com.atharva.Atharva;
import com.atharva.SystemPropertiesConfig;
import com.atharva.exceptions.NetworkCallFailedException;
import com.jayway.jsonpath.JsonPath;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.xpath.operations.Or;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by 16733 on 28/01/17.
 */
public class TradeHandler extends Thread{
    static final Level REPORT=Level.forName("REPORT",50);
    Logger log= LogManager.getLogger(TradeHandler.class);

    private BigDecimal totalProfitorLoss=BigDecimal.ZERO;   //Over All profit or loss
    private Double totalPriceDiff=0.0;      //Over All price difference
    private Double activeTradePriceDiff=0.0;        //Price difference of the active trade (reset after stop loss)
    private BigDecimal activeTradeProfitorLoss=BigDecimal.ZERO;     //Profit/Loss of the active trade (reset after stop loss)
    private Double cummulativePriceDiff=0.0;        //Difference between lastseen price and current price
    private Double trailingStoplossOnPrice=0.0;
    private Double trailingTrendReversalOnPrice=0.0;
    private BigDecimal trailingStopLoss=BigDecimal.ZERO;
    private BigDecimal trailingTrendReversal=BigDecimal.ZERO;
    private BigDecimal capital;
    private Order openingOrder;
    private ArrayList<Order> closedOrders;
    private Order activeOrder;
    private TradeSettings tradeSettings;
    private boolean endTrade;
    private TradeMode tradeMode;
    private TradeType onGoingTradeType;
    private Double currentMarketPrice=0.0;
    private Double lastSeenMarketPrice=0.0;
    private ArrayList<Double> cummulativePriceDiffList=new ArrayList<Double>();
    Client client =Client.create();

    public void setOrder(Order order){
        this.activeOrder=order;
    }
    public void setTradeSettings(TradeSettings tradeSettings){
        this.tradeSettings=tradeSettings;
    }

    @Override
    public void run(){
        try {
            startTrade(activeOrder,tradeSettings);
        } catch (Exception e) {
            log.error("Failed to execute trade "+e);
        }
    }

    public void startTrade(Order order,TradeSettings tradeSettings) throws Exception {
        try {
            openingOrder = order;
            activeOrder = order;
            this.tradeSettings = tradeSettings;
            onGoingTradeType=order.getTradeType();
            lastSeenMarketPrice=0.0;
            //Place the order and enter trade
            enterTrade(order);
            updateCurrentPrice();
            Long processStartTime = System.currentTimeMillis();
            do {
                //Cancelling the processing time of the logic

                Long waitTime =   (tradeSettings.getTimeInterval()-(System.currentTimeMillis() - processStartTime));
                if (waitTime > 0) {
                    try {
                        Thread.sleep(waitTime);
                    } catch (InterruptedException e) {
                        log.warn("Encountered exception " + e);
                    }
                }
                processStartTime = System.currentTimeMillis();
                updateCurrentPrice();
                activeTradePriceDiff = activeTradePriceDiff + cummulativePriceDiff;
                activeTradeProfitorLoss = BigDecimal.valueOf(activeTradePriceDiff * activeOrder.getOrderQty().doubleValue());
                if (tradeMode.equals(TradeMode.ACTIVE)) {
                    //Update the profit
                    totalPriceDiff = totalPriceDiff + cummulativePriceDiff;
                    totalProfitorLoss = BigDecimal.valueOf(totalPriceDiff * activeOrder.getOrderQty().doubleValue());
                }
                updateStopLossAndTrendReversalTrail();
                //Check if StoplossCondition has reached
                if (isStopLossContitionReached()) {
                    stopLoss();
                }
                if(Math.abs(trailingTrendReversalOnPrice) > Math.abs(tradeSettings.getTrendReversal())) {
                    reverseTrade();
                }


                    //Check of the trade is active in case of profit
                if (activeTradePriceDiff > tradeSettings.getReEntry()) {
                    if (tradeMode.equals(TradeMode.STOPLOSS)) {
                        //Check if the proftiablity has reached the re-entry criteria

                            Calendar cal = Calendar.getInstance();
                            try {
                                if (isMarketEndTime(order.getAssetClass().getMarketCloseTime())) {
                                    //endTrade();
                                } else {
                                    order.setTradeType(this.onGoingTradeType);
                                    enterTrade(order);
                                }
                            } catch (ParseException e) {
                                log.fatal("Error occurred during time compare ending trade " + e);
                                endTrade();
                            }
                        }else if(tradeMode.equals(TradeMode.MARKETWATCH)) {
                        log.info("Report profitiablity");
                        }
                    }


                //Check if the market close time has reached.
                try {
                    if (isMarketEndTime(order.getAssetClass().getMarketCloseTime())) {
                        //endTrade();
                    }
                } catch (ParseException e) {
                    log.fatal("Error occurred during time compare ending trade " + e);
                    enterTrade(order);
                }
                log.log(REPORT, this.toString());
            } while (!endTrade);
        }catch (Exception e){
            log.fatal("Encounter exception "+e);
        }
    }

    private boolean isStopLossContitionReached(){
        if(this.trailingStoplossOnPrice<0){
            if((trailingStoplossOnPrice*-1)>tradeSettings.getStoploss()){
                return (true);
            }else {
                return (false);
            }
        }else{
            return(false);
        }
    }

    @Override
    public String toString(){
        String trade="\t"+this.onGoingTradeType+"\t"+this.activeOrder.getScrip()+"\t"+this.currentMarketPrice+"\t"+this.cummulativePriceDiff+"\t"+this.activeTradePriceDiff+"\t"+this.activeTradeProfitorLoss+"\t"+this.trailingTrendReversalOnPrice+"\t"+this.trailingStoplossOnPrice+"\t"+this.tradeMode+"\t"+totalPriceDiff+"\t"+totalProfitorLoss;
        return(trade);
    }
    private void enterTrade(Order order){
        log.info("Entered trade");
        tradeMode=TradeMode.ACTIVE;
        this.activeTradeProfitorLoss=BigDecimal.ZERO;
        this.activeTradePriceDiff=0.0;
        activeOrder=order;
        activeOrder.setOpenPrice(currentMarketPrice);
    }

    private void endTrade(){
        log.info("Ended traded");
        endTrade=true;
    }

    private void updateStopLossAndTrendReversalTrail(){
        trailingStoplossOnPrice=trailingStoplossOnPrice+this.cummulativePriceDiff;
        trailingTrendReversalOnPrice=trailingTrendReversalOnPrice+this.cummulativePriceDiff;
        if(trailingStoplossOnPrice.doubleValue()>0){
            this.trailingStopLoss=BigDecimal.ZERO;
            this.trailingStoplossOnPrice=0.0;
        }

        if(trailingTrendReversalOnPrice.doubleValue()>0){
            this.trailingTrendReversalOnPrice=0.0;
            this.trailingTrendReversal=BigDecimal.ZERO;
        }
            trailingStopLoss=BigDecimal.valueOf(trailingStoplossOnPrice.doubleValue()*activeOrder.getOrderQty().doubleValue());
            trailingTrendReversal=BigDecimal.valueOf(trailingTrendReversalOnPrice.doubleValue()*activeOrder.getOrderQty().doubleValue());

    }

    private void stopLoss(){
        log.info("Hit stoploss");
        tradeMode=TradeMode.STOPLOSS;
        this.trailingStopLoss=BigDecimal.ZERO;
        this.trailingStoplossOnPrice=0.0;
        this.activeTradePriceDiff=0.0;
        this.activeTradeProfitorLoss=BigDecimal.ZERO;
        if(tradeMode.equals(TradeMode.ACTIVE)){

        }
        reverseTrade();
    }

    private void reverseTrade(){
        log.info("Trade reverse");
        this.trailingTrendReversalOnPrice=0.0;
        this.trailingTrendReversal=BigDecimal.ZERO;
        if(tradeMode.equals(TradeMode.ACTIVE)){

        }
        onGoingTradeType=onGoingTradeType.getOppositeDirection();
    }

    private void updateCurrentPrice() throws Exception {
        this.lastSeenMarketPrice=this.currentMarketPrice;
        this.currentMarketPrice=getMarketPrice();
        log.info("Current price: "+currentMarketPrice);
        this.cummulativePriceDiff=(currentMarketPrice-lastSeenMarketPrice)*this.onGoingTradeType.getTradeDirection();
        cummulativePriceDiffList.add(cummulativePriceDiff);
    }
    int index=0;
    private Double getMarketPrice() throws Exception {
//        if(true){
//            return(Atharva.sample1.get(index++));
//        }
        try {

            WebResource resource=client.resource(new URI("http://finance.google.com/finance/info?client=ig&q="+activeOrder.getAssetClass().getExchange()+":"+activeOrder.getScrip()));
            ClientResponse response = resource.accept("application/json").get(ClientResponse.class);
            if(response.getStatus()!=200){
                throw new NetworkCallFailedException("Get price call returned "+response.getStatus());
            }
            String responseJson=response.getEntity(String.class).replace("[","");
            responseJson=responseJson.replace("//","");
            String currentP =JsonPath.read(responseJson,"$.l");
            currentP=currentP.replace(",","");
            //log.info("Current market Price "+currentP);
            return Double.parseDouble(currentP);

        } catch (Exception e) {
            log.fatal("Failed to get current market price: "+e);
            //throw e;
            //TODO:HANDLE with retry logic
            return (currentMarketPrice);
        }
    }

    private boolean isMarketEndTime(Date marketEndTime) throws ParseException {
        Calendar now = Calendar.getInstance();
        SimpleDateFormat timeTormat = new SimpleDateFormat("HH:mm", Locale.UK);
        Date currentTime=null;
        try {
            currentTime=timeTormat.parse(now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE));
            if(currentTime.compareTo(marketEndTime)==1){
                return(true);
            }else{
                return(false);
            }



        } catch (ParseException e) {
            log.fatal("Time parser error in market time "+e,e);
            throw (e);
        }
    }


}
