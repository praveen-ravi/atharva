package com.atharva;

import com.atharva.exceptions.NetworkCallFailedException;
import com.atharva.exceptions.UIOperationFailureException;
import com.atharva.trade.Order;

/**
 * Created by 16733 on 11/02/17.
 */
public interface TradePlatform {
    public boolean placeOrder(Order order) throws UIOperationFailureException;
    public Double getMarketPrice(Order order) throws NetworkCallFailedException;
}
