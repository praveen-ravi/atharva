package com.atharva.trade;

import javax.validation.constraints.NotNull;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 16733 on 28/01/17.
 */
public class AssetClass {

    @NotNull
    private String assetClass;
    @NotNull
    private String exchange;
    private Date marketCloseTime;
    @NotNull
    private String tradeingInterface;
    @NotNull
    private String marketClose;

    public String getMarketClose() {
        return marketClose;
    }

    public void setMarketClose(String marketClose) {
        this.marketClose = marketClose;
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Date marketCloseTime = null;
        try {
            marketCloseTime = format.parse(marketClose);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.setMarketCloseTime(marketCloseTime);

    }

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
