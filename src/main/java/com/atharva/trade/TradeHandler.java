package com.atharva.trade;

import com.atharva.TradePlatform;
import com.atharva.exceptions.UIOperationFailureException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.Path;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by 16733 on 28/01/17.
 */
@Path("TemplateService")
public class TradeHandler extends Thread{
    static final Level REPORT=Level.forName("REPORT",50);
    Logger log= LogManager.getLogger(TradeHandler.class);

    private String uniqueID;
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
    private ArrayList<Order> closedOrders;
    private Order activeOrder;
    private TradeSettings tradeSettings;
    private boolean endTrade;
    private TradeMode tradeMode;
    private TradeType onGoingTradeType;
    private Double currentMarketPrice=0.0;
    private Double lastSeenMarketPrice=0.0;
    private ArrayList<Double> cummulativePriceDiffList=new ArrayList<Double>();

    private TradePlatform tradePlatform;

    public TradeHandler() {
    }

    public void setOrder(Order order){
        this.activeOrder=order;
    }
    public void setTradeSettings(TradeSettings tradeSettings){
        this.tradeSettings=tradeSettings;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    private void instanciateTradePlatform() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String interfaceInstanciationString = activeOrder.getAssetClass().getTradeingInterface();
        int lastIndexOfDot=interfaceInstanciationString.lastIndexOf(".");
        String className = interfaceInstanciationString.substring(0,lastIndexOfDot);
        String methodName = interfaceInstanciationString.substring(lastIndexOfDot+1);
        Class tradePlatformClass = Class.forName(className);
        Method getInstanceMethod = tradePlatformClass.getMethod(methodName);
        tradePlatform = (TradePlatform) getInstanceMethod.invoke(null,null);
    }
    @Override
    public void run(){
        try {
            uniqueID =activeOrder.getUser().getSkAccountNo()+"."+activeOrder.getScrip()+"."+System.currentTimeMillis();
            tradeSettings.setFlagAverage(activeOrder.getFlagAverage());
            activeOrder.setOrderQty((long) ((activeOrder.getCapital().longValue()*tradeSettings.getRiskOnCapital())/tradeSettings.getStoploss()));
            startTrade();
        } catch (Exception e) {
            try {
                if(orderPlaced)
                    endTrade();
            } catch (UIOperationFailureException e1) {
                log.fatal("Exception occurred during endTrade"+e1);
                e1.printStackTrace();
            }
            log.fatal("Execution of trade interrupted due to exception "+e);
            e.printStackTrace();
        }
    }
    private boolean orderPlaced=false;
    public void startTrade() throws Exception {
            onGoingTradeType=activeOrder.getTradeType();
            instanciateTradePlatform();
            lastSeenMarketPrice=0.0;
            //Place the order and enter trade
            enterTrade(activeOrder);
            orderPlaced=true;
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

                updateProfitLossCounters();
                //Check if StoplossCondition has reached
                if (isStopLossContitionReached()) {

                    if(activeTradePriceDiff>=tradeSettings.getReEntryLimit()){
                        stopLoss();
                    }else{
                        endTrade();
                    }
                }
                if(Math.abs(trailingTrendReversalOnPrice) > Math.abs(tradeSettings.getTrendReversal())) {
                    reverseTrade();
                }


                    //Check of the trade is active in case of profit
                if (activeTradePriceDiff > tradeSettings.getReEntry()) {
                    if (tradeMode.equals(TradeMode.INACTIVE)) {
                        //Check if the proftiablity has reached the re-entry criteria
                        if (isMarketEndTime()) {
                            endTrade();
                        } else {
                            activeOrder.setTradeType(this.onGoingTradeType);
                            enterTrade(activeOrder);
                        }

                        }else if(tradeMode.equals(TradeMode.MARKETWATCH)) {
                        log.info("Report profitiablity");
                        }
                    }


                //Check if the market close time has reached.
                if (isMarketEndTime()) {
                    endTrade();
                }
                log.log(REPORT, this.toString());
            } while (!endTrade);

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
        String trade=this.uniqueID +"\t"+this.onGoingTradeType+"\t"+this.activeOrder.getScrip()+"\t"+this.currentMarketPrice+"\t"+this.cummulativePriceDiff+"\t"+this.activeTradePriceDiff+"\t"+this.activeTradeProfitorLoss+"\t"+this.trailingTrendReversalOnPrice+"\t"+this.trailingStoplossOnPrice+"\t"+this.tradeMode+"\t"+totalPriceDiff+"\t"+totalProfitorLoss;
        return(trade);
    }

    public void enterTrade(Order order) throws UIOperationFailureException {
        log.info("Entered trade");
        tradeMode=TradeMode.ACTIVE;
        this.activeTradeProfitorLoss=BigDecimal.ZERO;
        this.activeTradePriceDiff=0.0;
        activeOrder=order;
        activeOrder.setOpenPrice(currentMarketPrice);
        tradePlatform.placeOrder(activeOrder);
    }

    public void endTrade() throws UIOperationFailureException {
        log.info("Ended traded");
        stopLoss();
        endTrade=true;
    }

    private void updateProfitLossCounters(){
        //Update the profit loss
        activeTradePriceDiff = activeTradePriceDiff + cummulativePriceDiff;
        activeTradeProfitorLoss = BigDecimal.valueOf(activeTradePriceDiff * activeOrder.getOrderQty().doubleValue());
        if (tradeMode.equals(TradeMode.ACTIVE)) {
            //Update the profit
            totalPriceDiff = totalPriceDiff + cummulativePriceDiff;
            totalProfitorLoss = BigDecimal.valueOf(totalPriceDiff * activeOrder.getOrderQty().doubleValue());
        }
        //Update the trailers
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

    private void stopLoss() throws UIOperationFailureException {
        log.info("Stoploss has reached");
        tradeMode=tradeSettings.getPostStoplossMode();
        this.trailingStopLoss=BigDecimal.ZERO;
        this.trailingStoplossOnPrice=0.0;
        this.activeTradePriceDiff=0.0;
        this.activeTradeProfitorLoss=BigDecimal.ZERO;
        this.trailingTrendReversalOnPrice=0.0;
        this.trailingTrendReversal=BigDecimal.ZERO;
        if(tradeMode.equals(TradeMode.ACTIVE)){

            tradePlatform.placeOrder(this.activeOrder.getStoplossOrder());
        }

    }

    private void reverseTrade() throws UIOperationFailureException {
        log.info("Trade reverse");
        this.trailingTrendReversalOnPrice=0.0;
        this.trailingTrendReversal=BigDecimal.ZERO;
        if(tradeMode.equals(TradeMode.ACTIVE)){
            tradePlatform.placeOrder(this.activeOrder.getReversalOrder());
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

    private Double getMarketPrice() throws Exception {
            return (tradePlatform.getMarketPrice(activeOrder));
    }

    private boolean isMarketEndTime() throws ParseException {
        Date marketEndTime=activeOrder.getAssetClass().getMarketCloseTime();
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
