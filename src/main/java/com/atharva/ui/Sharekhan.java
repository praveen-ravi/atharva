package com.atharva.ui;

import com.atharva.exceptions.UIOperationFailureException;
import com.atharva.trade.Order;
import com.atharva.ui.pages.LoginPage;
import com.atharva.ui.pages.MyTradePage;
import com.atharva.ui.pages.OrderConfirmationPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.IOException;

/**
 * Created by 16733 on 07/02/17.
 */
public class Sharekhan {
    WebDriver driver;
    ObjectProperties objectProperties;

     private static Sharekhan sharekhan;



    private Sharekhan() throws IOException {
        driver=new ChromeDriver();
        objectProperties=new ObjectProperties("objectProperties.properties");
    }

    public static Sharekhan getInstance() throws IOException {
        if(sharekhan==null){
            sharekhan=new Sharekhan();
        }
        return (sharekhan);
    }

    public boolean placeOrder(Order order) throws UIOperationFailureException {
        synchronized (sharekhan) {
            LoginPage loginPage = new LoginPage(driver, objectProperties);

            MyTradePage myTradePage = (MyTradePage) loginPage.verifiyAndLogin(order.getUser());

            OrderConfirmationPage orderConfirmationPage = (OrderConfirmationPage) myTradePage.placeOrder(order);

            return (orderConfirmationPage.confirmOrder(order));
        }
    }

}
