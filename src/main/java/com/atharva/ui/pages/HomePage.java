package com.atharva.ui.pages;

import com.atharva.exceptions.UIOperationFailureException;
import com.atharva.ui.ObjectProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import java.util.Set;


/**
 * Created by 16733 on 26/01/17.
 */
public class HomePage extends Actions implements WebPage  {
    public String tradeNowLink;
    public LoginPage loginPage;
    Logger logger= LogManager.getLogger(HomePage.class);
    public HomePage(WebDriver driver,ObjectProperties objectProperties) {
        super(driver,objectProperties);
        tradeNowLink="homePage.visitHomePageLink.Text";

        loginPage=new LoginPage(driver,objectProperties);
    }

    public WebPage clickVisitHomePage() throws UIOperationFailureException {
        if(this.click(tradeNowLink)){
            logger.info("Clicked on Visit Home Page link");
        }else{
            logger.error("Failed to click on Visit Home Page link",new UIOperationFailureException("Failed to click on Visit Home Page link"));
            throw new UIOperationFailureException("Failed to click on Visit Home Page link");
        }

        Set<String> windowHandles = driver.getWindowHandles();
        boolean switched=false;
        for(String handle:windowHandles) {
            driver.switchTo().window(handle);
            if(exists(loginPage.loginLink)){
                break;
            }
        }
        if(switched){
            return (loginPage);
        }else{
            throw new UIOperationFailureException("Failed to switch control to Login screen");
        }
    }
}
