package com.atharva.trade;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Created by 16733 on 26/01/17.
 */
public class Order {
    private String scrip;
    private BigDecimal Capital;
    private TradeType tradeType;
    private Long orderQty;
    private String validity;
    private BigInteger disclosedQty;
    private String orderType;
    private BigInteger accountNo;
    private Double openPrice;
    private Double closedPrice;
    private BigDecimal profitLoss;
    private AssetClass assetClass;

    public BigDecimal getCapital() {
        return Capital;
    }

    public void setCapital(BigDecimal capital) {
        Capital = capital;
    }
    public void setClosedPrice(Double closedPrice) {
        this.closedPrice = closedPrice;
    }

    public AssetClass getAssetClass() {
        return assetClass;
    }

    public void setAssetClass(AssetClass assetClass) {
        this.assetClass = assetClass;
    }

    public BigDecimal getProfitLoss() {
        return profitLoss;
    }

    public void setProfitLoss(BigDecimal profitLoss) {
        this.profitLoss = profitLoss;
    }

    public String getScrip() {
        return scrip;
    }

    public void setScrip(String scrip) {
        this.scrip = scrip;
    }

    public TradeType getTradeType() {
        return tradeType;
    }

    public void setTradeType(TradeType tradeType) {
        this.tradeType = tradeType;
    }

    public Long getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(Long orderQty) {
        this.orderQty = orderQty;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public BigInteger getDisclosedQty() {
        return disclosedQty;
    }

    public void setDisclosedQty(BigInteger disclosedQty) {
        this.disclosedQty = disclosedQty;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }


    public BigInteger getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(BigInteger accountNo) {
        this.accountNo = accountNo;
    }

    public Double getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(Double openPrice) {
        this.openPrice = openPrice;
    }

    public Double getClosedPrice() {
        return closedPrice;
    }

    public void setClosedtPrice(Double currentPrice) {
        this.closedPrice = currentPrice;
    }

}
