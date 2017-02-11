package com.atharva.ui.pages;


import com.atharva.exceptions.UIOperationFailureException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

/**
 * Created by 16733 on 26/01/17.
 */
public class Actions {
    WebDriver driver;
    Logger logger= LogManager.getLogger(Actions.class);
    int defaultWaitTime = 10000;
    public Actions(WebDriver driver) {
        this.driver = driver;
    }

    public boolean click(By by){
        try{
            WebElement element = driver.findElement(by);
            element.click();
            return(true);
        }catch (Exception e){
            logger.error("Error encountered "+e);
            return false;
        }
    }

    public boolean sendKeys(By by,String value){
        try{
            WebElement element = driver.findElement(by);
            element.sendKeys(value);
            return(true);
        }catch (Exception e){
            logger.error("Error encountered "+e);
            return false;
        }
    }

    public boolean exists(By by){
        try{
            WebElement element = driver.findElement(by);
            return(true);
        }catch (Exception e){
            return false;
        }
    }

    public boolean isVisible(By by){
        try{
            WebElement element = driver.findElement(by);
            return(element.isDisplayed());
        }catch (Exception e){
            return false;
        }
    }

    public String getText(By by) throws UIOperationFailureException {
        if(isVisible(by)){
            WebElement element = driver.findElement(by);
            return(element.getText());
        }else{
            throw (new UIOperationFailureException("Element is not visible"));
        }
    }

    public boolean syncForObject(By by){
        boolean exists=false;
        long systemTime = System.currentTimeMillis();
        do{
            exists=this.exists(by);
        }while(!exists && System.currentTimeMillis()<this.defaultWaitTime+systemTime);
        return(exists);
    }

    public boolean syncForObject(By by, int miliSeconds){
        boolean exists=false;
        long systemTime = System.currentTimeMillis();
        do{
            exists=this.exists(by);
        }while(!exists && System.currentTimeMillis()<miliSeconds+systemTime);
        return(exists);
    }

    public boolean selectByVisibleText(By by, String value){
        if(syncForObject(by)){
            WebElement element=driver.findElement(by);
            Select select = new Select(element);
            select.selectByVisibleText(value);
            return (true);
        }else{
            logger.fatal("Element not found for select");
            return (false);
        }
    }

    public boolean handleAlert(boolean accept){
        try {
            Alert alert=driver.switchTo().alert();
            if(accept){
                alert.accept();
            }else {
                alert.dismiss();
            }
            return (true);
        }catch (Exception e){
            return (false);
        }
    }
}