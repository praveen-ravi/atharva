package com.atharva.ui.pages;

import com.atharva.ui.ObjectProperties;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.math.BigInteger;

/**
 * Created by 16733 on 26/01/17.
 */
public class MyTradePage extends Actions implements WebPage {
    private ObjectProperties objectProperties;
    Logger logger = LogManager.getLogger(LoginPage.class);

    public By tradeNowLink;


    public MyTradePage(WebDriver driver, ObjectProperties objectProperties) {
        super(driver);
        this.driver = driver;
        this.objectProperties = objectProperties;

        tradeNowLink = By.xpath(objectProperties.getProperty("myTradePage.TradeNowLink.xpath"));

    }


}
