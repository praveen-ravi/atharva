package com.atharva.trade;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

/**
 * Created by 16733 on 29/01/17.
 */
public class TradeSettings {

    @NotNull
    private Double trendFlagAvgStrategy =1.0;
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
    private int movingAvgSmallPeriod =20;
    @NotNull
    private int movingAvgLargePeriod =40;
    @NotNull
    private long movingAvgInterval=60000;
    @NotNull
    private Double movingAvgCrossOverDifferenceForStoploss=0.00005;
    @NotNull
    private Double movingAvgCrossOverDifferenceForReversal=0.0001;
    private TradeMode postStoplossMode = TradeMode.MARKETWATCH;
    @NotNull
    private boolean followOppositeTrend =true;

    @JsonProperty
    public boolean isFollowOppositeTrend() {
        return followOppositeTrend;
    }

    public Double getBrokerage() {
        return brokerage;
    }

    public void setBrokerage(Double brokerage) {
        this.brokerage = brokerage;
    }

    @Override
    public String toString(){
        String tradeSettingsString="trendFlagAvgStrategy:"+ trendFlagAvgStrategy +"\tstopLossStrategy:"+stopLossStrategy+"\ttimeInterval:"+timeInterval+"\triskOnCapital:"+riskOnCapital+"\tpostStoplossMode:"+postStoplossMode;
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
    public Double getTrendFlagAvgStrategy() {
        return trendFlagAvgStrategy;
    }

    public void setTrendFlagAvgStrategy(Double trendFlagAvgStrategy) {
        this.trendFlagAvgStrategy = trendFlagAvgStrategy;
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
    @JsonProperty
    public int getMovingAvgSmallPeriod() {
        return movingAvgSmallPeriod;
    }
    @JsonProperty
    public int getMovingAvgLargePeriod() {
        return movingAvgLargePeriod;
    }
    @JsonProperty
    public long getMovingAvgInterval() {
        return movingAvgInterval;
    }
    @JsonProperty
    public Double getMovingAvgCrossOverDifferenceForStoploss() {
        return movingAvgCrossOverDifferenceForStoploss;
    }
    @JsonProperty
    public Double getMovingAvgCrossOverDifferenceForReversal() {
        return movingAvgCrossOverDifferenceForReversal;
    }
}
