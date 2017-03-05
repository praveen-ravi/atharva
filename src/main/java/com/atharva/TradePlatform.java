package com.atharva;

import com.atharva.exceptions.NetworkCallFailedException;
import com.atharva.trade.Order;
import com.atharva.trade.OrderConfirmation;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by 16733 on 11/02/17.
 */
public interface TradePlatform {
    static final Level ORDER=Level.forName("ORDER",25);
    public Logger tpLog =LogManager.getLogger(TradePlatform.class);
    public OrderConfirmation placeOrder(Order order);
    public Double getMarketPrice(Order order) throws NetworkCallFailedException;
}
