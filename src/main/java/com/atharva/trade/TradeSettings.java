package com.atharva.trade;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

/**
 * Created by 16733 on 29/01/17.
 */
public class TradeSettings {

    @NotNull
    private Double trendReversalStrategy=1.0;
    @NotNull
    private Double stopLossStrategy=1.6;
    @NotNull
    private Double reEntryStrategy=0.03;
    @NotNull
    private Double reEntryCriteria=0.05;
    @NotNull
    private long timeInterval=15000;
    @NotNull
    private Double riskOnCapital=0.05;
    @NotNull
    private Double trendReversalCriteria=1.6;
    @NotNull
    private Double brokerage=0.0005;
    @NotNull
    private int movingAvgSmall=10;
    @NotNull
    private int movingAvgLarge=20;
    @NotNull
    private long movingAvgInterval=60000;
    @NotNull
    private Double movingAvgCrossOverDifferenceForStoploss=0.00005;
    @NotNull
    private Double movingAvgCrossOverDifferenceForReversal=0.0001;
    private TradeMode postStoplossMode = TradeMode.MARKETWATCH;

    public Double getBrokerage() {
        return brokerage;
    }

    public void setBrokerage(Double brokerage) {
        this.brokerage = brokerage;
    }

    @Override
    public String toString(){
        String tradeSettingsString="trendReversalStrategy:"+trendReversalStrategy+"\tstopLossStrategy:"+stopLossStrategy+"\ttimeInterval:"+timeInterval+"\triskOnCapital:"+riskOnCapital+"\tpostStoplossMode:"+postStoplossMode;
        return(tradeSettingsString);
    }

    public TradeMode getPostStoplossMode() {
        return postStoplossMode;
    }

    public void setPostStoplossMode(TradeMode postStoplossMode) {
        this.postStoplossMode = postStoplossMode;
    }



    @JsonProperty
    public Double getRiskOnCapital() {
        return riskOnCapital;
    }

    public void setRiskOnCapital(Double riskOnCapital) {
        this.riskOnCapital = riskOnCapital;
    }





    public TradeSettings(){

    }


    @JsonProperty
    public long getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(long timeInterval) {
        this.timeInterval = timeInterval;
    }



    @JsonProperty
    public Double getTrendReversalStrategy() {
        return trendReversalStrategy;
    }

    public void setTrendReversalStrategy(Double trendReversalStrategy) {
        this.trendReversalStrategy = trendReversalStrategy;
    }

    @JsonProperty
    public Double getStopLossStrategy() {
        return stopLossStrategy;
    }

    public void setStopLossStrategy(Double stopLossStrategy) {
        this.stopLossStrategy = stopLossStrategy;
    }

    public Double getReEntryStrategy() {
        return reEntryStrategy;
    }

    public void setReEntryStrategy(Double reEntryStrategy) {
        this.reEntryStrategy = reEntryStrategy;
    }

    public Double getReEntryCriteria() {
        return reEntryCriteria;
    }

    public void setReEntryCriteria(Double reEntryCriteria) {
        this.reEntryCriteria = reEntryCriteria;
    }
    @JsonProperty
    public Double getTrendReversalCriteria() {
        return trendReversalCriteria;
    }

    public void setTrendReversalCriteria(Double trendReversalCriteria) {
        this.trendReversalCriteria = trendReversalCriteria;
    }
}
