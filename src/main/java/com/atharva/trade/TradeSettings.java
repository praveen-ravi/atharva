package com.atharva.trade;

/**
 * Created by 16733 on 29/01/17.
 */
public class TradeSettings {
    private Double stoploss;
    private Double trendReversal;
    private Double reEntry;
    private Double reEntryLimit;
    private Double flagAverage;
    private Double trendReversalStrategy=1.0;
    private Double stopLossStrategy=1.6;
    private Double reEntryStrategy;
    private Double reEntryCriteria;
    private long timeInterval=15000;
    private Double riskOnCapital=0.05;


    public Double getStoploss() {
        return stoploss;
    }

    public Double getRiskOnCapital() {
        return riskOnCapital;
    }

    public void setRiskOnCapital(Double riskOnCapital) {
        this.riskOnCapital = riskOnCapital;
    }

    public Double getTrendReversal() {

        return trendReversal;
    }

    public Double getReEntry() {
        return reEntry;
    }

    public Double getReEntryLimit() {
        return reEntryLimit;
    }

    public TradeSettings(Double flagAverage){
        this.flagAverage=flagAverage;

        //Calculate the stoploss limit
        this.stoploss=this.flagAverage*stopLossStrategy;
        reEntryStrategy=trendReversalStrategy;
        this.trendReversal=this.flagAverage*trendReversalStrategy;
        this.reEntry=this.flagAverage*this.reEntryStrategy;
        reEntryCriteria=0.0;
        reEntryLimit=reEntryCriteria*this.flagAverage;

    }

    public long getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(long timeInterval) {
        this.timeInterval = timeInterval;
    }

    public Double getFlagAverage() {
        return flagAverage;
    }

    public void setFlagAverage(Double flagAverage) {
        this.flagAverage = flagAverage;
        this.stoploss=this.flagAverage*stopLossStrategy;
        this.trendReversal=this.flagAverage*trendReversalStrategy;
        this.reEntry=this.flagAverage*this.reEntryStrategy;
        reEntryLimit=reEntryCriteria*this.flagAverage;
    }

    public Double getTrendReversalStrategy() {
        return trendReversalStrategy;
    }

    public void setTrendReversalStrategy(Double trendReversalStrategy) {
        this.trendReversalStrategy = trendReversalStrategy;
        this.trendReversal=this.flagAverage*trendReversalStrategy;
    }

    public Double getStopLossStrategy() {
        return stopLossStrategy;
    }

    public void setStopLossStrategy(Double stopLossStrategy) {
        this.stopLossStrategy = stopLossStrategy;
        this.stoploss=this.flagAverage*this.stopLossStrategy;
    }

    public Double getReEntryStrategy() {
        return reEntryStrategy;
    }

    public void setReEntryStrategy(Double reEntryStrategy) {
        this.reEntryStrategy = reEntryStrategy;
        this.reEntry=this.flagAverage*this.reEntryStrategy;
    }

    public Double getReEntryCriteria() {
        return reEntryCriteria;
    }

    public void setReEntryCriteria(Double reEntryCriteria) {
        this.reEntryCriteria = reEntryCriteria;
        reEntryLimit=this.reEntryCriteria*this.flagAverage;
    }
}
