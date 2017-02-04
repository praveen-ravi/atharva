package com.atharva.ui.pages;

import com.atharva.ui.ObjectProperties;
import com.atharva.exceptions.UIOperationFailureException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.Set;


/**
 * Created by 16733 on 26/01/17.
 */
public class HomePage extends Actions implements WebPage  {
    private ObjectProperties objectProperties;
    public By tradeNowLink;
    public LoginPage loginPage;
    Logger logger= Logger.getLogger(HomePage.class);
    public HomePage(WebDriver driver,ObjectProperties objectProperties) {
        super(driver);
        this.driver=driver;
        this.objectProperties=objectProperties;
        tradeNowLink=By.linkText(objectProperties.getProperty("homePage.visitHomePageLink.Text"));

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
