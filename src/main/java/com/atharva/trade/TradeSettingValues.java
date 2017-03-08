package com.atharva.trade;

/**
 * Created by 16733 on 15/02/17.
 */
public class TradeSettingValues {
    private Double stoploss;
    private Double trendFlagAverageValue;
    private Double reEntry;
    private Double reEntryLimit;
    private Double trendReversalLimit;
    private Double flagAverage=0.0;
    private int maxConsecutiveLossEntries=2;
    private Double riskOnCapital=0.05;
    private long timeInterval=15000;
    private TradeMode postStoplossMode = TradeMode.MARKETWATCH;
    private Double brokerage = 0.0005;
    private int movingAvgSmallPeriod =20;
    private int movingAvgLargePeriod =40;
    private long movingAvgInterval=60000;
    private Double movingAvgCrossOverDifferenceForStoploss=0.00005;
    private Double movingAvgCrossOverDifferenceForReversal=0.0001;
    private boolean followOppositeTrend;

    public boolean isFollowOppositeTrend() {
        return followOppositeTrend;
    }

    public Double getBrokerage() {
        return brokerage;
    }

    public void setBrokerage(Double brokerage) {
        this.brokerage = brokerage;
    }

    public Double getRiskOnCapital() {
        return riskOnCapital;
    }

    public int getMaxConsecutiveLossEntries() {
        return maxConsecutiveLossEntries;
    }

    public void setMaxConsecutiveLossEntries(int maxConsecutiveLossEntries) {
        this.maxConsecutiveLossEntries = maxConsecutiveLossEntries;
    }

    public Double getStoploss() {
        return stoploss;
    }

    public Double getTrendFlagAverageValue() {

        return trendFlagAverageValue;
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

    public long getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(long timeInterval) {
        this.timeInterval = timeInterval;
    }

    public TradeMode getPostStoplossMode() {
        return postStoplossMode;
    }

    public void setPostStoplossMode(TradeMode postStoplossMode) {
        this.postStoplossMode = postStoplossMode;
    }

    public int getMovingAvgSmallPeriod() {
        return movingAvgSmallPeriod;
    }

    public int getMovingAvgLargePeriod() {
        return movingAvgLargePeriod;
    }

    public long getMovingAvgInterval() {
        return movingAvgInterval;
    }

    public Double getMovingAvgCrossOverDifferenceForStoploss() {
        return movingAvgCrossOverDifferenceForStoploss;
    }

    public Double getMovingAvgCrossOverDifferenceForReversal() {
        return movingAvgCrossOverDifferenceForReversal;
    }

    public TradeSettingValues(Double flagAverage, TradeSettings tradeSettings){
        this.flagAverage=flagAverage;

        //Calculate the stoploss limit
        this.stoploss=this.flagAverage*tradeSettings.getStopLossStrategy();
        this.trendFlagAverageValue =this.flagAverage*tradeSettings.getTrendFlagAvgStrategy();
        this.reEntry=this.flagAverage*tradeSettings.getReEntryStrategy();
        reEntryLimit=tradeSettings.getReEntryCriteria()*this.flagAverage;
        trendReversalLimit=tradeSettings.getTrendReversalCriteria()*this.flagAverage;
        this.riskOnCapital=tradeSettings.getRiskOnCapital();
        this.timeInterval=tradeSettings.getTimeInterval();
        this.postStoplossMode=tradeSettings.getPostStoplossMode();
        this.brokerage=tradeSettings.getBrokerage();
        this.movingAvgLargePeriod =tradeSettings.getMovingAvgLargePeriod();
        this.movingAvgSmallPeriod =tradeSettings.getMovingAvgSmallPeriod();
        this.movingAvgCrossOverDifferenceForReversal=tradeSettings.getMovingAvgCrossOverDifferenceForReversal();
        this.movingAvgCrossOverDifferenceForStoploss=tradeSettings.getMovingAvgCrossOverDifferenceForStoploss();
        this.movingAvgInterval=tradeSettings.getMovingAvgInterval();
        this.followOppositeTrend=tradeSettings.isFollowOppositeTrend();

    }



}
