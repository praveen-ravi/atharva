package com.atharva.trade;

import java.util.Date;

/**
 * Created by 16733 on 28/01/17.
 */
public class AssetClass {
    private String exchange;
    private Date marketOpenTime;
    private Date marketCloseTime;

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public Date getMarketOpenTime() {
        return marketOpenTime;
    }

    public void setMarketOpenTime(Date marketOpenTime) {
        this.marketOpenTime = marketOpenTime;
    }

    public Date getMarketCloseTime() {
        return marketCloseTime;
    }

    public void setMarketCloseTime(Date marketCloseTime) {
        this.marketCloseTime = marketCloseTime;
    }
}
