package com.atharva;

import com.atharva.trade.TradeSettings;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

import javax.validation.constraints.NotNull;

/**
 * Created by 16733 on 12/02/17.
 */
public class AtharvaConfiguration extends Configuration {
    @NotNull
    Double trendReversalStrategy;
    @NotNull
    Double stopLossStrategy;
    @NotNull
    Double riskOnCapital;
    @NotNull
    long timeInterval;
    @NotNull
    Double trendReversalCriteria;

    @JsonProperty
    public Double getTrendReversalStrategy() {
        return trendReversalStrategy;
    }

    @JsonProperty
    public Double getStopLossStrategy() {
        return stopLossStrategy;
    }

    @JsonProperty
    public Double getRiskOnCapital() {
        return riskOnCapital;
    }

    @JsonProperty
    public long getTimeInterval() {
        return timeInterval;
    }

    @JsonProperty
    public Double getTrendReversalCriteria() {
        return trendReversalCriteria;
    }

    private TradeSettings tradeSettings=new TradeSettings();



    public TradeSettings getTradeSettings() {
        tradeSettings.setStopLossStrategy(this.stopLossStrategy);
        tradeSettings.setRiskOnCapital(this.riskOnCapital);
        tradeSettings.setTimeInterval(this.timeInterval);
        tradeSettings.setTrendReversalStrategy(this.trendReversalStrategy);
        tradeSettings.setTrendReversalCriteria(this.trendReversalCriteria);
        return tradeSettings;
    }


}
