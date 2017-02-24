package com.atharva.ui.pages;

import com.atharva.exceptions.UIOperationFailureException;
import com.atharva.trade.Order;
import com.atharva.trade.OrderConfirmation;
import com.atharva.ui.ObjectProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

/**
 * Created by 16733 on 07/02/17.
 */
public class OrderConfirmationPage extends Actions implements WebPage{
    Logger logger = LogManager.getLogger(LoginPage.class);

    public String confirmationHeader;
    public String orderStatusTableCell;
    public String orderQtyTableCell;
    public String skOrderIDCell;
    public String mtmLink;
    public ReportsPage reportsPage;

    public OrderConfirmationPage(WebDriver driver,ObjectProperties objectProperties) {
        super(driver,objectProperties);
        reportsPage=new ReportsPage(driver,objectProperties);
        confirmationHeader="orderConfirmationPage.ConfirmationHeader.xpath";
        orderStatusTableCell="orderConfirmationPage.OrderStatusTableCell.xpath";
        orderQtyTableCell="orderConfirmationPage.OrderQtyTableCell.xpath";
        skOrderIDCell="orderConfirmationPage.SkOrderIDCell.xpath";
        mtmLink="mainPage.MTMlink.xpath";

    }

    public OrderConfirmation confirmOrder(Order order) throws UIOperationFailureException {
        OrderConfirmation orderConfirmation=new OrderConfirmation();
        if(syncForVisible(confirmationHeader)){
            String status = getText(orderStatusTableCell);
            String orderQty = getText(orderQtyTableCell);
            String skOrderId = getText(skOrderIDCell);
            Double execPrice = null;

            orderConfirmation.setOrderId(skOrderId);

            if(!status.equalsIgnoreCase("rejected")){

                if(sycnForText(orderStatusTableCell,"FullyExecuted",20000)){
                    status = getText(orderStatusTableCell);
                    logger.info("Order status : "+status);
                    orderConfirmation.setOrderStatus(true);
                }else{
                    orderConfirmation.setOrderStatus(false);
                }
                try {
                    execPrice = reportsPage.getExecPrice(order,skOrderId);
                }catch (UIOperationFailureException e){
                    logger.error("Failed to get executed price");
                }
                orderConfirmation.setExecutedPrice(execPrice);
            }else {
                orderConfirmation.setOrderStatus(false);
            }
        }else{
            orderConfirmation.setOrderStatus(false);

        }
        return(orderConfirmation);

    }
}
