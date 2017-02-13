package com.atharva.ui.pages;

import com.atharva.exceptions.UIOperationFailureException;
import com.atharva.trade.Order;
import com.atharva.ui.ObjectProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Created by 16733 on 07/02/17.
 */
public class OrderConfirmationPage extends Actions implements WebPage{
    private ObjectProperties objectProperties;
    Logger logger = LogManager.getLogger(LoginPage.class);

    public By confirmationHeader;
    public By orderStatusTableCell;
    public By orderQtyTableCell;
    public OrderConfirmationPage(WebDriver driver,ObjectProperties objectProperties) {
        super(driver);
        this.driver = driver;
        this.objectProperties = objectProperties;
        confirmationHeader=By.xpath(objectProperties.getProperty("orderConfirmationPage.ConfirmationHeader.xpath"));
        orderStatusTableCell=By.xpath(objectProperties.getProperty("orderConfirmationPage.OrderStatusTableCell.xpath"));
        orderQtyTableCell=By.xpath(objectProperties.getProperty("orderConfirmationPage.OrderQtyTableCell.xpath"));

    }

    public boolean confirmOrder(Order order) throws UIOperationFailureException {

        if(syncForVisible(confirmationHeader)){
            String status = getText(orderStatusTableCell);
            String orderQty = getText(orderQtyTableCell);

            if(orderQty.equals(order.getOrderQty())){
                return(true);
            }else {
                return (false);
            }
        }else{
            return(false);
        }
    }
}
