package com.atharva.ui.pages;

import com.atharva.exceptions.UIOperationFailureException;
import com.atharva.trade.Order;
import com.atharva.ui.ObjectProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

/**
 * Created by 16733 on 16/02/17.
 */
public class ReportsPage extends Actions implements WebPage{
    Logger logger = LogManager.getLogger(ReportsPage.class);

    public String reportsTypeDropdown;
    private String variableProperty_ExecPriceCell;
    public String mtmLink;


    public ReportsPage(WebDriver driver, ObjectProperties objectProperties) {
        super(driver,objectProperties);
        reportsTypeDropdown="reportsPage.ReportsTypeDropdown.xpath";
        variableProperty_ExecPriceCell="reportsPage.Order.ExecPriceCell.xpath";
        mtmLink="mainPage.MTMlink.xpath";

    }

    public Double getExecPrice(Order order, String skOrderId) throws UIOperationFailureException {
        if(isVisible(mtmLink)){
            if(click(mtmLink)){
                logger.info("Successfully clicked on MTM link");
            }else{
                logger.fatal("Failed to click on MTM link", new UIOperationFailureException("Failed to click on MTM link"));
                throw new UIOperationFailureException("Failed to click on MTM link");
            }
        }
        if(syncForObject(reportsTypeDropdown)){
            if(selectByVisibleText(reportsTypeDropdown,"Order")){
                logger.info("Selected order from the dropdown");
            }else{
                logger.fatal("Failed to select the orders from the dropdown", new UIOperationFailureException("Failed to select the order from the dropdown"));
                throw new UIOperationFailureException("Failed to select the order from the dropdown");
            }
        }else {
            logger.fatal("Failed to locate the reports type dropdown", new UIOperationFailureException("Failed to locate the reports type dropdown"));
            throw new UIOperationFailureException("Failed to locate the reports type dropdown");
        }

        if(syncForObject(variableProperty_ExecPriceCell,skOrderId)){
//            long devisorForExecPrice;
//            if(order.isReversalOrder()){
//                devisorForExecPrice= (long) (order.getOrderQty()/2);
//            }else {
//                devisorForExecPrice=order.getOrderQty();
//            }
            Double execPrice = Double.parseDouble(getText(variableProperty_ExecPriceCell,skOrderId));
            logger.info("Executed price : "+execPrice);
            return (execPrice);
        }else {
            logger.fatal("Failed to locate exeucted price for orderId "+skOrderId, new UIOperationFailureException("Failed to locate exeucted price for orderId "+skOrderId));
            throw new UIOperationFailureException("Failed to locate exeucted price for orderId "+skOrderId);
        }
    }
}
