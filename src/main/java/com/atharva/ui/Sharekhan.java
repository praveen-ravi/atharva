package com.atharva.ui;

import com.atharva.TradePlatform;
import com.atharva.exceptions.NetworkCallFailedException;
import com.atharva.exceptions.TradeAssetNotFoundException;
import com.atharva.trade.Order;
import com.atharva.trade.OrderConfirmation;
import com.atharva.ui.pages.LoginPage;
import com.atharva.ui.pages.MyTradePage;
import com.atharva.ui.pages.OrderConfirmationPage;
import com.jayway.jsonpath.JsonPath;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;


/**
 * Created by 16733 on 07/02/17.
 */
public class Sharekhan implements TradePlatform {
    Logger log = LogManager.getLogger(Sharekhan.class);
    WebDriver driver;
    ObjectProperties objectProperties;


    private Client client = ClientBuilder.newClient();



    private Sharekhan() throws IOException {
        objectProperties=new ObjectProperties("objectProperties.properties");
    }

    public static Sharekhan getInstance() throws IOException {
        Sharekhan sharekhan=new Sharekhan();
        return (sharekhan);
    }

    @Override
    public OrderConfirmation placeOrder(Order order) {
        OrderConfirmation orderConfirmation=null;
        try {
            log.info("Placing order : " + order);
            driver = new ChromeDriver();
            LoginPage loginPage = new LoginPage(driver, objectProperties);

            MyTradePage myTradePage = (MyTradePage) loginPage.verifiyAndLogin(order.getUser());

            OrderConfirmationPage orderConfirmationPage = (OrderConfirmationPage) myTradePage.placeOrder(order);
            orderConfirmation = orderConfirmationPage.confirmOrder(order);
            order.setExecutedPrice(orderConfirmation.getExecutedPrice());
            order.setOrderId(orderConfirmation.getOrderId());
            tpLog.log(ORDER,"Order :"+order);

            return (orderConfirmation);
        }catch (TradeAssetNotFoundException e){
            orderConfirmation=new OrderConfirmation();
            orderConfirmation.setOrderStatus("notfound");
            log.fatal("Failed to place order due to exception",e);
            return (orderConfirmation);
        }
        catch (Exception e){
            orderConfirmation=new OrderConfirmation();
            orderConfirmation.setOrderStatus("failed");
            log.fatal("Failed to place order due to exception",e);
            return (orderConfirmation);
        }finally {
            driver.close();
            driver.quit();
        }

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
