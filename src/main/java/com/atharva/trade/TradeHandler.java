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
import java.util.*;

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
    private Double cummulativePriceDiff=0.0;        //Difference between lastseen price and current price
    private Double trailingStoplossOnPrice=0.0;
    private Double trailingFlagAverageOnPrice =0.0;
    private Order activeOrder;
    private TradeSettings tradeSettings;
    private TradeSettingValues tradeSettingValues;
    private TradeMode tradeMode;
    private TradeType onGoingTradeType;
    private Double currentMarketPrice=0.0;
    private Double lastSeenMarketPrice=0.0;
    private Double currentTrendProfit = 0.0;
    private Double reEntryCounter=0.0;
    private List<Boolean> profitLossEntries=new ArrayList<>();
    private TradePlatform tradePlatform;
    private Double currentEntryProfitLoss=0.0;
    private long currentPositions=0;
    private ArrayList<Double> prices=new ArrayList();
    private Double smallMovingAvg;
    private Double largeMovingAvg;


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
            activeOrder.setOrderQty((long) ((activeOrder.getCapital().longValue()*tradeSettingValues.getRiskOnCapital())/tradeSettingValues.getStoploss()));
            if(activeOrder.getOrderQty()>=1) {
                startTrade();
            }else{
                log.error("Insufficient capital for starting the trade");
            }
        } catch (Exception e) {
            try {
                if(currentPositions!=0) {
                    log.fatal("ENDING TRADE DUE TO EXCEPTION",e);
                    endTrade();
                }
            } catch (OrderPlacementExecption orderPlacementExecption) {
                log.fatal("CRITICAL EXCEPTION DURING END TRADE",orderPlacementExecption);
            }
            log.fatal("EXECUTION OF TRADE INTURRUPTED",e);
        }
    }

    public void startTrade() throws Exception {
        onGoingTradeType=activeOrder.getTradeType();
        instanciateTradePlatform();
        lastSeenMarketPrice=0.0;
        //Place the order and enter trade

        log.info("Entering trade for order "+activeOrder);
        log.info("With trade settings"+tradeSettings);
        enterTrade();
        tradeMode = TradeMode.ACTIVE;
        this.activeTradePriceDiff = 0.0;
        updateCurrentPrice();
        updateProfitLossCounters();

        Long processStartTime = System.currentTimeMillis();
        do {
            //Cancelling the processing time of the logic

            Long waitTime =   (tradeSettingValues.getTimeInterval()-(System.currentTimeMillis() - processStartTime));
            if (waitTime > 0) {
                try {
                    Thread.sleep(waitTime);
                } catch (InterruptedException e) {
                    log.warn("Encountered exception " + e);
                }
            }
            processStartTime = System.currentTimeMillis();

            //Check if the market close time has reached.
            if (isMarketEndTime()) {
                endTrade();
            }
            updateCurrentPrice();
            updateProfitLossCounters();
            this.monitorTrade();

            log.log(REPORT, this.toString()+"\n\n");



        } while (!this.tradeMode.equals(TradeMode.ENDTRADE));

    }

    private Order lastBuyOrder;
    private Order lastSellOrder;
    private void monitorTrade() throws Exception {
        //Check if StoplossCondition has reached
        if (isStopLossContitionReached()) {
            log.info("Stoploss condition has reached for order : " + activeOrder);
            stopLoss();
            log.debug("Updating the porfit loss counters after placing order :" + activeOrder);
            updateCummulativePriceDiff();
            updateProfitLossCounters();
            tradeMode = tradeSettings.getPostStoplossMode();
            this.trailingStoplossOnPrice = 0.0;
            this.activeTradePriceDiff = 0.0;
            this.trailingFlagAverageOnPrice = 0.0;
        } else if (isMovingAvgCrossOver()) {
            log.info("Trade reversal condition has reached for order " + activeOrder);
            reverseTrade();
            if(tradeMode.equals(tradeMode.INACTIVE)){
                log.debug("Updating current price");
                tradeMode=tradeMode.ACTIVE;
                updateCurrentPrice();
            }else {
                log.debug("Updating the porfit loss counters after placing order :" + activeOrder);
                this.updateCummulativePriceDiff();
            }
            updateProfitLossCounters();
            this.trailingFlagAverageOnPrice = 0.0;
            this.currentTrendProfit = 0.0;
            Double brokeragePrice=0.0;
            if(lastBuyOrder!=null && lastBuyOrder.getExecutedPrice()!=null) {
                brokeragePrice=lastBuyOrder.getExecutedPrice() * tradeSettingValues.getBrokerage();
            }
            if (currentEntryProfitLoss > brokeragePrice) {
                profitLossEntries.add(true);
            } else {
                profitLossEntries.add(false);
            }
            currentEntryProfitLoss = 0.0;
            activeOrder.setTradeType(onGoingTradeType.getOppositeDirection());
            onGoingTradeType = onGoingTradeType.getOppositeDirection();
            log.debug("Order :" + activeOrder + ", TrailingTrendReversal:" + trailingFlagAverageOnPrice + ", TrailingStoploss:" + trailingStoplossOnPrice);
        } else if (tradeMode.equals(TradeMode.ACTIVE) && isFlagAverageReached()){
            log.info("Flag average has reached for order :" + activeOrder);
            stopLoss();
            log.debug("Updating the porfit loss counters after placing order :" + activeOrder);
            updateCummulativePriceDiff();
            updateProfitLossCounters();
            Double brokeragePrice=0.0;
            if(lastBuyOrder!=null && lastBuyOrder.getExecutedPrice()!=null) {
                brokeragePrice=lastBuyOrder.getExecutedPrice() * tradeSettingValues.getBrokerage();
            }
            if (currentEntryProfitLoss > brokeragePrice) {
                profitLossEntries.add(true);
            } else {
                profitLossEntries.add(false);
            }
            this.trailingFlagAverageOnPrice = 0.0;
            currentEntryProfitLoss = 0.0;

            tradeMode = TradeMode.INACTIVE;

        }


        if (tradeMode.equals(TradeMode.INACTIVE)) {
            log.debug("Looking for reentry");
            //check for reEntry
            log.info("reEntryCounter: {} trendFlagAverageValue: {}" +
                    "\n{} > {} --> {}",reEntryCounter,tradeSettingValues.getTrendFlagAverageValue(),reEntryCounter,tradeSettingValues.getTrendFlagAverageValue(),reEntryCounter>tradeSettingValues.getTrendFlagAverageValue());
            if(reEntryCounter>tradeSettingValues.getTrendFlagAverageValue()){
                //Check if the proftiablity has reached the re-entry criteria
                if (isMarketEndTime()) {
                    endTrade();
                } else {
                    log.info("Re-Entering the trade for order :"+activeOrder);
                    enterTrade();
                    reEntryCounter=0.0;
                    tradeMode=TradeMode.ACTIVE;
                    log.debug("Updating the porfit loss counters after placing order :"+activeOrder);
                    updateCurrentPrice();
                    updateProfitLossCounters();
                }
            }


        }else if(tradeMode.equals(TradeMode.MARKETWATCH)) {
            if(activeTradePriceDiff>tradeSettingValues.getReEntry()) {
                log.info("Report profitiablity");
            }
        }

    }

    private boolean isFlagAverageReached(){
        log.info("Comparing trend reversal : {} TrendFlagAverageSetting :{}" +
                "\n{}>{}---{}",Math.abs(trailingFlagAverageOnPrice), Math.abs(tradeSettingValues.getTrendFlagAverageValue()),Math.abs(trailingFlagAverageOnPrice), Math.abs(tradeSettingValues.getTrendFlagAverageValue()),Math.abs(trailingFlagAverageOnPrice) > Math.abs(tradeSettingValues.getTrendFlagAverageValue()));
        if(Math.abs(trailingFlagAverageOnPrice) > Math.abs(tradeSettingValues.getTrendFlagAverageValue())){
            return true;
        }else{
            return false;
        }
    }

    private boolean isMovingAvgCrossOver(){
        //Small Moving avg is greater than Large moving avg and current trend is short

        if(largeMovingAvg>0) {
            log.debug("Comparing moving avg {},{}",smallMovingAvg,largeMovingAvg);
            if (smallMovingAvg > (largeMovingAvg + (tradeSettingValues.getMovingAvgCrossOverDifferenceForReversal() * smallMovingAvg)) && onGoingTradeType.getTradeDirection() == -1) {
                log.debug("Moving avg crossed over ({}>{} AND {}==BUY)-->true", largeMovingAvg, (largeMovingAvg + (tradeSettingValues.getMovingAvgCrossOverDifferenceForReversal() * smallMovingAvg)), onGoingTradeType);
                return (true);
            } else if (smallMovingAvg < (largeMovingAvg - (tradeSettingValues.getMovingAvgCrossOverDifferenceForReversal() * smallMovingAvg)) && onGoingTradeType.getTradeDirection() == 1) {
                log.debug("Moving avg crossed over ({}<{} AND {}==SELL)-->true", largeMovingAvg, (largeMovingAvg - (tradeSettingValues.getMovingAvgCrossOverDifferenceForReversal() * smallMovingAvg)), onGoingTradeType);
                return (true);
            } else {
                return false;
            }
        }else{
            return(false);
        }
    }

    private boolean isStopLossContitionReached(){

        if(this.trailingStoplossOnPrice<0){

            int consecutiveLossEntries=0;
            for(int i=profitLossEntries.size()-1;i>=(profitLossEntries.size()-tradeSettingValues.getMaxConsecutiveLossEntries());i--){
                if(i<0){
                    break;
                }
                if(!profitLossEntries.get(i)){
                    consecutiveLossEntries++;
                }
            }
            log.info("Comparing Trailing stoploss: {} StoplossSetting:{} consecutiveLossEntries:{} maxStopLossEntries:{} Order:{}" +
                    "\n{}>{} OR {}>={} --> {}",trailingStoplossOnPrice*-1,tradeSettingValues.getStoploss(),consecutiveLossEntries,tradeSettingValues.getMaxConsecutiveLossEntries(),activeOrder,trailingStoplossOnPrice*-1,tradeSettingValues.getStoploss(),consecutiveLossEntries,tradeSettingValues.getMaxConsecutiveLossEntries(),(trailingStoplossOnPrice*-1)>=tradeSettingValues.getStoploss() || consecutiveLossEntries>=tradeSettingValues.getMaxConsecutiveLossEntries());

            if((trailingStoplossOnPrice*-1)>=tradeSettingValues.getStoploss() || consecutiveLossEntries>=tradeSettingValues.getMaxConsecutiveLossEntries()){
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
        String trade=this.uniqueID +"\t"+this.onGoingTradeType+"\t"+this.activeOrder.getScrip()+"\t"+this.currentMarketPrice+"\t"+this.cummulativePriceDiff+"\t"+this.activeTradePriceDiff+"\t"+this.trailingFlagAverageOnPrice +"\t"+this.trailingStoplossOnPrice+"\t"+this.tradeMode+"\t"+totalPriceDiff+"\t"+totalProfitorLoss;
        return(trade);
    }

    public void enterTrade() throws OrderPlacementExecption {
        try {
            this.placeOrder(activeOrder);
            log.info("Enter order executed for : "+activeOrder);
        } catch (OrderPlacementExecption orderPlacementExecption) {
            log.fatal("FAILED TO ENTER THE TRADE");
            throw orderPlacementExecption;
        }

    }

    private void placeOrder(Order order) throws OrderPlacementExecption {
        String orderStatus="failed";
        OrderConfirmation orderConfirmation;
        int orderPlacementRetryCount=3;
        do {
            orderConfirmation= tradePlatform.placeOrder(order);
            orderStatus = orderConfirmation.getOrderStatus();
            if (orderConfirmation.getExecutedPrice() != null) {
                updatePrice(orderConfirmation.getExecutedPrice());
            }
        }while(orderStatus.equalsIgnoreCase("failed") && orderPlacementRetryCount-->0);
        if(orderStatus.equalsIgnoreCase("success")) {
            currentPositions=currentPositions+order.getOrderQty();
            activeOrder.setOrderId(orderConfirmation.getOrderId());
            activeOrder.setExecutedPrice(orderConfirmation.getExecutedPrice());
            if(order.getTradeType().getTradeDirection()==1){
                lastBuyOrder=order;
            }else{
                lastSellOrder=order;
            }
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
        if (tradeMode.equals(TradeMode.ACTIVE)) {
            activeTradePriceDiff = activeTradePriceDiff + cummulativePriceDiff;
            currentTrendProfit= currentTrendProfit+cummulativePriceDiff;
            currentEntryProfitLoss=currentEntryProfitLoss+cummulativePriceDiff;
            totalPriceDiff = totalPriceDiff + cummulativePriceDiff;
            totalProfitorLoss = BigDecimal.valueOf(totalPriceDiff * activeOrder.getOrderQty().doubleValue());
            trailingStoplossOnPrice=trailingStoplossOnPrice+this.cummulativePriceDiff;
            trailingFlagAverageOnPrice = trailingFlagAverageOnPrice +this.cummulativePriceDiff;
        }else if(tradeMode.equals(TradeMode.INACTIVE)) {
            reEntryCounter = reEntryCounter + cummulativePriceDiff;
            if (reEntryCounter < 0) {
                reEntryCounter = 0.0;
            }
        }

        if(trailingStoplossOnPrice.doubleValue()>0){
            this.trailingStoplossOnPrice=0.0;
        }

        if(trailingFlagAverageOnPrice.doubleValue()>0){
            this.trailingFlagAverageOnPrice =0.0;
        }

        //Calculate moving averages
        int indexIntervals= (int) (tradeSettingValues.getMovingAvgInterval()/tradeSettingValues.getTimeInterval());
        indexIntervals=(indexIntervals==0)? 1:indexIntervals;
        if(prices.size()>=indexIntervals*tradeSettingValues.getMovingAvgLargePeriod()){
            int j=0;
            Double priceSummation=0.0;
            for(int i=prices.size()-1;i>=0;i=i-indexIntervals){
                priceSummation=priceSummation+prices.get(i);
                j++;
                if(j==tradeSettingValues.getMovingAvgSmallPeriod()){
                    smallMovingAvg=priceSummation/tradeSettingValues.getMovingAvgSmallPeriod();
                }
                if(j==tradeSettingValues.getMovingAvgLargePeriod()){
                    largeMovingAvg=priceSummation/tradeSettingValues.getMovingAvgLargePeriod();
                    break;
                }

            }
        }



        log.debug("Order :"+activeOrder+", " +
                        "\ncummulativePriceDiff: "+cummulativePriceDiff+
                        "\ntrailingFlagAverageOnPrice:"+ trailingFlagAverageOnPrice +
                        "\nTrailingStoploss:"+trailingStoplossOnPrice+
                        "\nactiveTradePriceDiff: "+activeTradePriceDiff+
                        "\ncurrentTrendProfit: "+currentTrendProfit+
                        "\ncurrentEntryProfitLoss: "+currentEntryProfitLoss+
                        "\ntotalPriceDiff: "+totalPriceDiff+
                        "\nreEntryCounter:"+reEntryCounter+
                        "\nsmallMovingAvg:"+smallMovingAvg+
                        "\nlargeMovingAvg:"+largeMovingAvg);

    }

    private void stopLoss() throws OrderPlacementExecption {
        log.info("Performing stoploss "+activeOrder);

        if(tradeMode.equals(TradeMode.ACTIVE)){
            Order stoplossOrder=this.activeOrder.getStoplossOrder();
            try {
                this.placeOrder(stoplossOrder);

            } catch (OrderPlacementExecption orderPlacementExecption) {
                log.fatal("FAILED TO PLACE THE STOPLOSS ORDER!!!");
                throw orderPlacementExecption;
            }


        }

    }

    private void reverseTrade() throws OrderPlacementExecption {
        log.info("Trade reverse for order "+this.activeOrder);
        try {
            Order reversalOrder = this.activeOrder.getReversalOrder();
            if(tradeMode.equals(TradeMode.ACTIVE)){
                reversalOrder.setOrderQty(reversalOrder.getOrderQty()*2);
            }
            this.placeOrder(reversalOrder);
        } catch (OrderPlacementExecption orderPlacementExecption) {
            log.fatal("FAILED TO REVERSE THE TRADE!!!");
            throw orderPlacementExecption;
        }


    }

    private void updatePrice(Double price){
        this.lastSeenMarketPrice=this.currentMarketPrice;
        this.currentMarketPrice=price;
        prices.add(price);
    }

    private void updateCurrentPrice() throws Exception {
        updatePrice(getMarketPrice());
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
