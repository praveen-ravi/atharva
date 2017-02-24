package com.atharva.trade;

import com.atharva.TradePlatform;
import com.atharva.exceptions.OrderPlacementExecption;
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
    private TradeSettingValues tradeSettingValues;
    private TradeMode tradeMode;
    private TradeType onGoingTradeType;
    private Double currentMarketPrice=0.0;
    private Double lastSeenMarketPrice=0.0;
    private Double currentTrendProfit = 0.0;

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
        log.debug("Instanciating platform with "+interfaceInstanciationString);
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
            tradeSettingValues=new TradeSettingValues(activeOrder.getFlagAverage(),tradeSettings);
            activeOrder.setOrderQty((long) ((activeOrder.getCapital().longValue()*tradeSettings.getRiskOnCapital())/tradeSettingValues.getStoploss()));
            if(activeOrder.getOrderQty()>=1) {
                startTrade();
            }else{
                log.error("Insufficient capital for starting the trade");
            }
        } catch (Exception e) {
            try {
                if(tradeMode!=null && tradeMode.equals(TradeMode.ACTIVE))
                    endTrade();
            } catch (OrderPlacementExecption orderPlacementExecption) {
                log.fatal("CRITICAL EXCEPTION DURING END TRADE",orderPlacementExecption);
            }
            log.fatal("EXECUTION OF TRADE INTURRUPTED",e);
        }
    }
    private boolean orderPlaced=false;
    public void startTrade() throws Exception {

            onGoingTradeType=activeOrder.getTradeType();
            instanciateTradePlatform();
            lastSeenMarketPrice=0.0;
            //Place the order and enter trade
            enterTrade(activeOrder);
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

                    if(activeTradePriceDiff>=tradeSettingValues.getReEntryLimit()){
                        stopLoss();
                    }else{
                        endTrade();
                    }
                }

                if(isTradeReversalReached()) {
                    reverseTrade();
                }


                    //Check of the trade is active in case of profit
                if (activeTradePriceDiff > tradeSettingValues.getReEntry()) {
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
                log.log(REPORT, this.toString()+"\n\n");

                //Check if the market close time has reached.
                if (isMarketEndTime()) {
                    endTrade();
                }

            } while (!this.tradeMode.equals(TradeMode.ENDTRADE));

    }

    private boolean isTradeReversalReached(){
        log.info("Comparing trend reversal : {}>{} && {}>{}",currentTrendProfit,tradeSettingValues.getTrendReversalLimit(),Math.abs(trailingTrendReversalOnPrice), Math.abs(tradeSettingValues.getTrendReversal()));
        if((currentTrendProfit>tradeSettingValues.getTrendReversalLimit()) && (Math.abs(trailingTrendReversalOnPrice) > Math.abs(tradeSettingValues.getTrendReversal()))) {
            return true;
        }else{
            return false;
        }
    }

    private boolean isStopLossContitionReached(){

        if(this.trailingStoplossOnPrice<0){
            log.info("Comparing Trailing stoploss: "+trailingStoplossOnPrice*-1+" StoplossSetting: "+tradeSettingValues.getStoploss()+" for order "+activeOrder+"\n"
                    +(trailingStoplossOnPrice*-1)+" > "+tradeSettingValues.getStoploss()+"-->"+((trailingStoplossOnPrice*-1)>=tradeSettingValues.getStoploss()));
            if((trailingStoplossOnPrice*-1)>=tradeSettingValues.getStoploss()){
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

    public void enterTrade(Order order) throws OrderPlacementExecption {
        log.info("Entered trade for order "+order);
        log.info("With trade settings"+tradeSettings);
        activeOrder=order;
        try {
            this.placeOrder(activeOrder);
        } catch (OrderPlacementExecption orderPlacementExecption) {
            log.fatal("FAILED TO ENTER THE TRADE");
            throw orderPlacementExecption;
        }

        tradeMode = TradeMode.ACTIVE;
        this.activeTradeProfitorLoss = BigDecimal.ZERO;
        this.activeTradePriceDiff = 0.0;



    }

    private void placeOrder(Order order) throws OrderPlacementExecption {
        OrderConfirmation orderConfirmation = tradePlatform.placeOrder(order);
        orderPlaced=orderConfirmation.isOrderStatus();
        if(orderConfirmation.getExecutedPrice()!=null) {
            this.lastSeenMarketPrice=currentMarketPrice;
            currentMarketPrice = orderConfirmation.getExecutedPrice();

        }
        if(orderPlaced) {
            activeOrder.setOrderId(orderConfirmation.getOrderId());
            activeOrder.setExecutedPrice(orderConfirmation.getExecutedPrice());
        }else{
            throw new OrderPlacementExecption("Failed to place order "+order);
        }
        log.info("Order placed : "+order);
    }

    public void endTrade() throws OrderPlacementExecption {
        log.info("Ended traded for order "+activeOrder);
        stopLoss();
        this.tradeMode=TradeMode.ENDTRADE;
    }

    private void updateProfitLossCounters(){
        //Update the profit loss
        activeTradePriceDiff = activeTradePriceDiff + cummulativePriceDiff;
        currentTrendProfit= currentTrendProfit+cummulativePriceDiff;
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
            log.info("Order :"+activeOrder+", TrailingTrendReversal:"+trailingTrendReversalOnPrice+", TrailingStoploss:"+trailingStoplossOnPrice);

    }

    private void stopLoss() throws OrderPlacementExecption {
        log.info("Stoploss has reached for order "+activeOrder);

        if(tradeMode.equals(TradeMode.ACTIVE)){
            Order stoplossOrder=this.activeOrder.getStoplossOrder();
            try {
                this.placeOrder(stoplossOrder);
                updateCummulativePriceDiff();
                updateProfitLossCounters();
            } catch (OrderPlacementExecption orderPlacementExecption) {
                log.fatal("FAILED TO PLACE THE STOPLOSS ORDER!!!");
                throw orderPlacementExecption;
            }


        }
        log.debug("Updating the porfit loss counters after placing order");
        updateProfitLossCounters();
        tradeMode=tradeSettings.getPostStoplossMode();
        this.trailingStopLoss=BigDecimal.ZERO;
        this.trailingStoplossOnPrice=0.0;
        this.activeTradePriceDiff=0.0;
        this.activeTradeProfitorLoss=BigDecimal.ZERO;
        this.trailingTrendReversalOnPrice=0.0;
        this.trailingTrendReversal=BigDecimal.ZERO;
    }

    private void reverseTrade() throws OrderPlacementExecption {
        log.info("Trade reverse for order "+this.activeOrder);
        this.trailingTrendReversalOnPrice=0.0;
        this.trailingTrendReversal=BigDecimal.ZERO;
        this.currentTrendProfit = 0.0;

        if(tradeMode.equals(TradeMode.ACTIVE)){

            try {
                this.placeOrder(this.activeOrder.getReversalOrder());
            } catch (OrderPlacementExecption orderPlacementExecption) {
                log.fatal("FAILED TO REVERSE THE TRADE!!!");
                throw orderPlacementExecption;
            }
        }
        log.debug("Updating the porfit loss counters after placing order");
        this.updateCummulativePriceDiff();
        updateProfitLossCounters();

        activeOrder.setTradeType(onGoingTradeType.getOppositeDirection());
        onGoingTradeType=onGoingTradeType.getOppositeDirection();
        log.debug("Order :"+activeOrder+", TrailingTrendReversal:"+trailingTrendReversalOnPrice+", TrailingStoploss:"+trailingStoplossOnPrice);
    }

    private void updateCurrentPrice() throws Exception {
        this.lastSeenMarketPrice=this.currentMarketPrice;
        this.currentMarketPrice=getMarketPrice();
        log.info("Current price for order "+activeOrder+": "+currentMarketPrice);
        updateCummulativePriceDiff();
    }

    private void updateCummulativePriceDiff(){
        this.cummulativePriceDiff=(currentMarketPrice-lastSeenMarketPrice)*this.onGoingTradeType.getTradeDirection();
        log.info("Cummulative price difference for order "+activeOrder+": "+cummulativePriceDiff);
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
