package com.atharva.trade;

import java.util.Date;

/**
 * Created by 16733 on 28/01/17.
 */
public class AssetClass {


    private String assetClass;
    private String exchange;
    private Date marketCloseTime;
    private String tradeingInterface;

    public String getAssetClass() {
        return assetClass;
    }

    public void setAssetClass(String assetClass) {
        this.assetClass = assetClass;
    }

    public String getTradeingInterface() {
        return tradeingInterface;
    }

    public void setTradeingInterface(String tradeingInterface) {
        this.tradeingInterface = tradeingInterface;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public Date getMarketCloseTime() {
        return marketCloseTime;
    }

    public void setMarketCloseTime(Date marketCloseTime) {
        this.marketCloseTime = marketCloseTime;
    }
}
