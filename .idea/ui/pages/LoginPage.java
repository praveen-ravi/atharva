package com.atharva.ui.pages;

import com.atharva.exceptions.UIOperationFailureException;
import com.atharva.ui.ObjectProperties;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Created by 16733 on 26/01/17.
 */
public class LoginPage extends Actions implements WebPage {
    private ObjectProperties objectProperties;
    Logger logger= LogManager.getLogger(LoginPage.class);

    public By loginLink;
    public By loginIdTextBox;
    public By memberShipPasswordTextbox;
    public By tradingPasswordTextbox;
    public By loginButton;
    public MyTradePage myHomePage;
    public LoginPage(WebDriver driver,ObjectProperties objectProperties) {
        super(driver);
        myHomePage=new MyTradePage(driver,objectProperties);

        this.driver=driver;
        this.objectProperties=objectProperties;
        loginIdTextBox = By.id(objectProperties.getProperty("loginPage.loginIDTextBox.cssPath"));
        memberShipPasswordTextbox=By.id(objectProperties.getProperty("loginPage.memberShipPasswordTextBox.cssPath"));
        tradingPasswordTextbox=By.id(objectProperties.getProperty("loginPage.tradingPasswordTextBox.cssPath"));
        loginButton=By.cssSelector(objectProperties.getProperty("loginPage.loginButton.cssPath"));
        loginLink=By.xpath("loginPage.loginLink.xpath");

    }

    public WebPage login(String loginId,String memberShipPassword,String tradingPassword) throws UIOperationFailureException {
        if(click(loginLink)){
            logger.info("Clicked login link (for login fields)");
        }else {
            logger.error("Failed to click on login link (for login fields)", new UIOperationFailureException("Failed to click on login link (for login fields)"));
            throw new UIOperationFailureException("Failed to click on login link (for login fields)");
        }

        if (sendKeys(loginIdTextBox,loginId)){
            logger.info("Entered login id");
        }else {
            logger.error("Failed to enter login id", new UIOperationFailureException("Failed to enter login id"));
            throw new UIOperationFailureException("Failed to enter login id");
        }

        if(sendKeys(memberShipPasswordTextbox,memberShipPassword)){
            logger.info("Entered membership password");
        }else {
            logger.error("Failed to enter membership password", new UIOperationFailureException("Failed to enter membership password"));
            throw new UIOperationFailureException("Failed to enter membership password");
        }

        if(sendKeys(tradingPasswordTextbox,tradingPassword)){
            logger.info("Entered trading password");
        }else {
            logger.error("Failed to enter trading password", new UIOperationFailureException("Failed to enter trading password"));
            throw new UIOperationFailureException("Failed to enter trading password");
        }

        if(click(loginButton)){
            logger.info("Clicked on login button");
        }else {
            logger.error("Failed to click on login button", new UIOperationFailureException("Failed to click on login button"));
            throw new UIOperationFailureException("Failed to click on login button");
        }

        if(syncForObject(myHomePage.tradeNowLink)){
            logger.info("Login Successful");
        }else {
            logger.error("Failed to login", new UIOperationFailureException("Failed to login"));
            throw new UIOperationFailureException("Failed to login");
        }

        return(myHomePage);
    }


}
