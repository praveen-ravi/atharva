package com.atharva.trade;

/**
 * Created by 16733 on 16/02/17.
 */
public class OrderConfirmation {
    private String orderId;
    private Double executedPrice;
    private boolean orderStatus;

    public boolean isOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(boolean orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Double getExecutedPrice() {
        return executedPrice;
    }

    public void setExecutedPrice(Double executedPrice) {
        this.executedPrice = executedPrice;
    }
}
