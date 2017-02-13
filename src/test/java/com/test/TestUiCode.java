package com.test;

import com.atharva.exceptions.UIOperationFailureException;
import com.atharva.trade.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 16733 on 12/02/17.
 */
public class TestUiCode {
    public static void main(String[] args) throws ParseException, IOException, UIOperationFailureException {


    AssetClass asset1 = new AssetClass();
            asset1.setExchange("NSE");
    SimpleDateFormat format = new SimpleDateFormat("HH:mm");
    Date marketCloseTime = format.parse("15:10");
            asset1.setMarketCloseTime(marketCloseTime);
            asset1.setTradeingInterface("com.atharva.ui.Sharekhan.getInstance");
    Order order1 = new Order();
            order1.setTradeType(TradeType.SELLBIGTRADE);
            order1.setScrip("AXISBANK");
            order1.setCapital(BigDecimal.valueOf(2500));
            order1.setAssetClass(asset1);
            order1.setFlagAverage(1.0);

        User user1=new User();
            user1.setName("PraveenRavi");
            user1.setIduser("1832188");
            user1.setSkLoginId("pravi86");
            user1.setCkMembershipPassword("Tiger@5683");
            user1.setSkTradingPassword("Cinde@6283");
            user1.setSkAccountNo("1832188");
            user1.setSkDpAccountNo("2472182");

            order1.setUser(user1);


    TradeSettings settings1 = new TradeSettings();
            //order1.setOrderQty((long)((order1.getCapital().longValue()*0.05)/settings1.getStoploss()));

        System.setProperty("sharekhansite","https://strade.sharekhan.com/rmmweb/");
        System.setProperty("webdriver.chrome.driver","src/main/resources/chromedriver");
        System.setProperty("webdriver.gecko.driver","src/main/resources/geckodriver");


        TradeHandler tradeHandler=new TradeHandler();

        tradeHandler.setTradeSettings(settings1);
        tradeHandler.setOrder(order1);
        tradeHandler.start();
}
}
