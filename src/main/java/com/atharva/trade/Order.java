package com.atharva.trade;

import com.atharva.ui.OrderType;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Created by 16733 on 26/01/17.
 */
public class Order {
    private String scrip;
    private BigDecimal capital;
    private TradeType tradeType;
    private Long orderQty;
    private String validity;
    private BigInteger disclosedQty;
    private OrderType orderType;
    private Double openPrice;
    private Double closedPrice;
    private AssetClass assetClass;
    private Double stoplossTrigger;
    private Double limitPrice;
    private User user;

    public Order clone(){
        Order cloneOrder = new Order();
        cloneOrder.scrip=this.scrip;
        cloneOrder.capital=this.capital;
        cloneOrder.tradeType=this.tradeType;
        cloneOrder.orderQty=this.orderQty;
        cloneOrder.validity=this.validity;
        cloneOrder.disclosedQty=this.disclosedQty;
        cloneOrder.orderQty=this.orderQty;
        cloneOrder.openPrice=this.openPrice;
        cloneOrder.closedPrice=this.closedPrice;
        cloneOrder.assetClass=this.assetClass;
        cloneOrder.stoplossTrigger=this.stoplossTrigger;
        cloneOrder.limitPrice=this.limitPrice;
        cloneOrder.user=this.user;

        return (cloneOrder);
    }

    public Order getStoplossOrder(){
        Order stoplossOrder = this.clone();
        stoplossOrder.setTradeType(this.getTradeType().getOppositeDirection());
        stoplossOrder.setOpenPrice(0.0);
        //TODO:Make it configurable
        stoplossOrder.setValidity("GFD");
        stoplossOrder.setDisclosedQty(BigInteger.ZERO);
        stoplossOrder.setOrderType(OrderType.MARKET_ORDER);
        stoplossOrder.setOpenPrice(0.0);
        stoplossOrder.setClosedPrice(0.0);
        stoplossOrder.setStoplossTrigger(0.0);
        stoplossOrder.setLimitPrice(0.0);
        return (stoplossOrder);
    }

    public Order getReversalOrder(){
        Order reversalOrder = this.clone();
        reversalOrder.setTradeType(this.getTradeType().getOppositeDirection());
        reversalOrder.setOpenPrice(0.0);
        //TODO:Make it configurable
        reversalOrder.setValidity("GFD");
        reversalOrder.setDisclosedQty(BigInteger.ZERO);
        reversalOrder.setOrderType(OrderType.MARKET_ORDER);
        reversalOrder.setOpenPrice(0.0);
        reversalOrder.setClosedPrice(0.0);
        reversalOrder.setStoplossTrigger(0.0);
        reversalOrder.setLimitPrice(0.0);
        reversalOrder.setOrderQty(this.orderQty*2);
        return (reversalOrder);
    }

    public Double getLimitPrice() {
        return limitPrice;
    }

    public void setLimitPrice(Double triggerPrice) {
        this.limitPrice = triggerPrice;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Double getStoplossTrigger() {
        return stoplossTrigger;
    }

    public void setStoplossTrigger(Double stoplossTrigger) {
        this.stoplossTrigger = stoplossTrigger;
    }

    public BigDecimal getCapital() {
        return capital;
    }

    public void setCapital(BigDecimal capital) {
        this.capital = capital;
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

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
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
