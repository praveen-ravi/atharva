package com.atharva.trade;

/**
 * Created by 16733 on 15/02/17.
 */
public class TradeSettingValues {
    private Double stoploss;
    private Double trendReversal;
    private Double reEntry;
    private Double reEntryLimit;
    private Double trendReversalLimit;
    private Double flagAverage=0.0;

    public Double getStoploss() {
        return stoploss;
    }

    public Double getTrendReversal() {

        return trendReversal;
    }

    public Double getTrendReversalLimit() {
        return trendReversalLimit;
    }

    public Double getReEntry() {
        return reEntry;
    }

    public Double getReEntryLimit() {
        return reEntryLimit;
    }

    public Double getFlagAverage() {
        return flagAverage;
    }

    public TradeSettingValues(Double flagAverage,TradeSettings tradeSettings){
        this.flagAverage=flagAverage;

        //Calculate the stoploss limit
        this.stoploss=this.flagAverage*tradeSettings.getStopLossStrategy();
        this.trendReversal=this.flagAverage*tradeSettings.getTrendReversalStrategy();
        this.reEntry=this.flagAverage*tradeSettings.getReEntryStrategy();
        reEntryLimit=tradeSettings.getReEntryCriteria()*this.flagAverage;
        trendReversalLimit=tradeSettings.getTrendReversalCriteria()*this.flagAverage;

    }



}
