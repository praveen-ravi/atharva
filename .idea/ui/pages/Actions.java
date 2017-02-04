package com.atharva.ui.pages;


import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Created by 16733 on 26/01/17.
 */
public class Actions {
    WebDriver driver;
    Logger logger= Logger.getLogger(Actions.class);
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
}
