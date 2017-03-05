package com.atharva.simulator;

import com.atharva.TradePlatform;
import com.atharva.exceptions.NetworkCallFailedException;
import com.atharva.trade.Order;
import com.atharva.trade.OrderConfirmation;
import com.jayway.jsonpath.JsonPath;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by 16733 on 22/02/17.
 */
public class TradeSimulator implements TradePlatform {

    Logger log = LogManager.getFormatterLogger(TradeSimulator.class);

    public static TradeSimulator getInstance(){
        return (new TradeSimulator());
    }
    @Override
    public OrderConfirmation placeOrder(Order order) {
        OrderConfirmation orderConfirmation = new OrderConfirmation();
        try {
            orderConfirmation.setExecutedPrice(getMarketPrice(order));
            orderConfirmation.setOrderStatus("success");
            orderConfirmation.setOrderId("Dummy_"+System.currentTimeMillis());
            order.setExecutedPrice(orderConfirmation.getExecutedPrice());
            order.setOrderId(orderConfirmation.getOrderId());
            tpLog.log(ORDER,"Order :"+order);
        } catch (NetworkCallFailedException e) {
            log.error("Error while getting the price ",e);
        }
        return (orderConfirmation);
    }

    @Override
    public Double getMarketPrice(Order order) throws NetworkCallFailedException {
        int retryCount=5;
        Exception err;
        do {
            try {
                Client client= ClientBuilder.newClient();
                WebTarget target=client.target("http://finance.google.com/finance/info");
                WebTarget targetWithQueryParam = target.queryParam("client","ig");
                targetWithQueryParam=targetWithQueryParam.queryParam("q",order.getAssetClass().getExchange()+":"+order.getScrip());
                Invocation.Builder invocationBuilder = targetWithQueryParam.request(MediaType.APPLICATION_JSON);
                Response response=invocationBuilder.get();
                String responseJson = response.readEntity(String.class);
                responseJson = responseJson.replace("//", "");
                responseJson = responseJson.replace("[", "");
                responseJson = responseJson.replace("]", "");
                String currentP = JsonPath.read(responseJson, "$.l");
                currentP = currentP.replace(",", "");
                //log.info("Current market Price "+currentP);
                return Double.parseDouble(currentP);

            } catch (Exception e) {
                err=e;
                retryCount--;
            }
        }while (retryCount>0);
        throw new NetworkCallFailedException("Failed to probe the market price due to error "+err);
    }
}
