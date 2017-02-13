package com.atharva.ui.pages;

import com.atharva.exceptions.UIOperationFailureException;
import com.atharva.trade.Order;
import com.atharva.ui.ObjectProperties;
import com.atharva.ui.OrderType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Created by 16733 on 26/01/17.
 */
public class MyTradePage extends Actions implements WebPage {
    private ObjectProperties objectProperties;
    Logger logger = LogManager.getLogger(LoginPage.class);

    public By tradeNowLink;
    public By exchangeDropDown;
    public By scripTextBox;
    public By buySellDropDown;
    public By orderQtyTextBox;
    public By gFDDropDown;
    public By disclosedQtyTextBox;
    public By stopLossTriggerTextBox;
    public By limitPriceTextBox;
    public By marketOrderRadioButton;
    public By limitOrderRadioButton;
    public By advancedOrderDropDown;
    public By dPAccountDropDown;
    public By placeOrderButton;
    public By confirmOrderButton;
    public By firstOptionInDropdown;

    public OrderConfirmationPage orderConfirmationPage;


    public MyTradePage(WebDriver driver, ObjectProperties objectProperties) {
        super(driver);
        this.driver = driver;
        this.objectProperties = objectProperties;
        orderConfirmationPage=new OrderConfirmationPage(driver,objectProperties);
        tradeNowLink = By.xpath(objectProperties.getProperty("myTradePage.TradeNowLink.xpath"));
        exchangeDropDown = By.id(objectProperties.getProperty("myTradePage.ExchangeDropDown.id"));
        scripTextBox = By.id(objectProperties.getProperty("myTradePage.ScripTextBox.id"));
        buySellDropDown = By.id(objectProperties.getProperty("myTradePage.BuySellDropDown.id"));
        orderQtyTextBox = By.id(objectProperties.getProperty("myTradePage.OrderQtyTextBox.id"));
        gFDDropDown = By.id(objectProperties.getProperty("myTradePage.GFDDropDown.id"));
        disclosedQtyTextBox = By.cssSelector(objectProperties.getProperty("myTradePage.DisclosedQtyTextBox.cssPath"));
        stopLossTriggerTextBox = By.id(objectProperties.getProperty("myTradePage.StopLossTriggerTextBox.id"));
        limitPriceTextBox = By.id(objectProperties.getProperty("myTradePage.LimitPriceTextBox.id"));
        marketOrderRadioButton = By.id(objectProperties.getProperty("myTradePage.MarketOrderRadioButton.id"));
        limitOrderRadioButton = By.id(objectProperties.getProperty("myTradePage.LimitOrderRadioButton.id"));
        advancedOrderDropDown = By.id(objectProperties.getProperty("myTradePage.AdvancedOrderDropDown.id"));
        dPAccountDropDown = By.xpath(objectProperties.getProperty("myTradePage.DPAccountDropDown.xpath"));
        placeOrderButton = By.id(objectProperties.getProperty("myTradePage.PlaceOrderButton.id"));
        confirmOrderButton = By.id(objectProperties.getProperty("myTradePage.ConfirmOrderButton.id"));
        firstOptionInDropdown = By.xpath(objectProperties.getProperty("myTradePage.firstOptionInDropdown.xpath"));
    }


    public WebPage placeOrder(Order order) throws UIOperationFailureException {

        if(this.syncForObject(tradeNowLink)){
            if(click(tradeNowLink)){
                logger.info("Clicked on tradeNow Link");
                syncForObject(exchangeDropDown);
            }else{
                logger.error("Failed to click on trade now link");
                throw new UIOperationFailureException("Failed to click on trade now link");

            }

            if(selectByVisibleText(exchangeDropDown,order.getAssetClass().getExchange())){
                logger.info("Selected the Exchange: "+order.getAssetClass().getExchange());
            }else{
                logger.error("Failed to select Exchange", new UIOperationFailureException("Failed to select Exchange"));
                throw new UIOperationFailureException("Failed to select Exchange");
            }

            if(sendKeys(scripTextBox,order.getScrip())){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(click(firstOptionInDropdown)){
                        logger.info("Selected the scrip from dropdown ");
                    }else{
                        logger.error("Failed to select the scrip", new UIOperationFailureException("Failed to select the scrip"));
                        throw new UIOperationFailureException("Failed to select the scrip");
                    }
                logger.info("Entered the scrip : "+order.getScrip());
            }else{
                logger.error("Failed to enter scrip", new UIOperationFailureException("Failed to enter scrip"));
                throw new UIOperationFailureException("Failed to enter scrip");
            }

            if(selectByVisibleText(buySellDropDown,order.getTradeType().getUiText())){
                logger.info("Selected buy/sell: "+order.getTradeType().getUiText());
            }else{
                logger.error("Failed to select buy/sell", new UIOperationFailureException("Failed to select buy/sell"));
                throw new UIOperationFailureException("Failed to select buy/sell");
            }

            handleAlert(true);

            if(clearAndSendKeys(orderQtyTextBox,order.getOrderQty().toString())){
                logger.info("Entered the quantity: "+order.getOrderQty());
            }else{
                logger.error("Failed to enter order quantit", new UIOperationFailureException("Failed to enter order quantit"));
                throw new UIOperationFailureException("Failed to enter order quantity");
            }
            if(order.getStoplossTrigger()!=null){
                if(clearAndSendKeys(stopLossTriggerTextBox,order.getStoplossTrigger().toString())){
                    logger.info("Entered the stoploss trigger: "+order.getStoplossTrigger());
                }else{
                    logger.error("Failed to enter Stoploss trigger", new UIOperationFailureException("Failed to enter Stoploss trigger"));
                    throw new UIOperationFailureException("Failed to enter Stoploss trigger");
                }
            }

            if(order.getOrderType()!=null){
                if(order.getOrderType().equals(OrderType.LIMIT_ORDER)){
                    if(click(limitOrderRadioButton)){
                        logger.info("Selected limit order radio button");
                    }else{
                        logger.error("Failed to select limit order radio button",new UIOperationFailureException("Failed to select limit order radio button"));
                        throw new UIOperationFailureException("Failed to select limit order radio button");
                    }
                }else{
                    if(click(marketOrderRadioButton)){
                        logger.info("Selected market order radio button");
                    }else{
                        logger.error("Failed to select market order radio button",new UIOperationFailureException("Failed to select market order radio button"));
                        throw new UIOperationFailureException("Failed to select market order radio button");
                    }
                }
            }
            String limitPrice="0.0";
            if(order.getLimitPrice()!=null) {
                limitPrice=order.getLimitPrice().toString();
            }

            if(clearAndSendKeys(limitPriceTextBox,limitPrice)){
                logger.info("Entered the limit price: "+limitPrice);
            }else{
                logger.error("Failed to enter limit price", new UIOperationFailureException("Failed to enter limit price"));
                throw new UIOperationFailureException("Failed to enter limit price");
            }


            if(selectByVisibleText(dPAccountDropDown,order.getUser().getSkDpAccountNo())){
                    logger.info("Selected the  DP Account: "+order.getUser().getSkAccountNo());
            }else{
                logger.error("Failed to select account no.", new UIOperationFailureException("Failed to select account no."));
                throw new UIOperationFailureException("Failed to select account no.");
            }

            if(click(placeOrderButton)){
                logger.info("Clicked on place order button");
            }else{
                UIOperationFailureException e=new UIOperationFailureException("Failed to click on place order button");
                logger.error("Failed to click on place order button", e);
                throw e;
            }

            if(click(confirmOrderButton)){
                logger.info("Clicked on order confirm button");
            }else{
                UIOperationFailureException e=new UIOperationFailureException("Failed to click on order confirm  button");
                logger.error("Failed to click on order confirm button", e);
                throw e;
            }

            if(syncForObject(orderConfirmationPage.confirmationHeader)){
                logger.info("Order confirmation page is displayed");
            }

            return(orderConfirmationPage);
        }else {
            logger.fatal("Failed to place order due to Element not found in page");
            throw new UIOperationFailureException("Unable to find the My Trade page");
        }

    }


}
