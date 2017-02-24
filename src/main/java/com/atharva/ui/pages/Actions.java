package com.atharva.ui.pages;


import com.atharva.exceptions.UIOperationFailureException;
import com.atharva.ui.ObjectProperties;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 16733 on 26/01/17.
 */
public class Actions {
    WebDriver driver;
    private ObjectProperties objectProperties;
    Logger logger= LogManager.getLogger(Actions.class);
    int defaultWaitTime = 10000;
    public Actions(WebDriver driver, ObjectProperties objectProperties) {
        this.driver = driver;
        this.objectProperties=objectProperties;
    }

    private WebElement getElement(String objectPropertyKey,String... parameters) throws UIOperationFailureException {
        String byMethod = objectPropertyKey.substring(objectPropertyKey.lastIndexOf(".")+1);
        switch (byMethod.toLowerCase()){
            case "xpath" : return(driver.findElement(By.xpath(getParameterizedProperty(objectProperties.getProperty(objectPropertyKey),parameters))));
            case "csspath" : return(driver.findElement(By.cssSelector(getParameterizedProperty(objectProperties.getProperty(objectPropertyKey),parameters))));
            case "id" : return(driver.findElement(By.id(getParameterizedProperty(objectProperties.getProperty(objectPropertyKey),parameters))));
            case "text" : return (driver.findElement(By.linkText(getParameterizedProperty(objectProperties.getProperty(objectPropertyKey),parameters))));
            default:throw new UIOperationFailureException("method not defined for "+ byMethod);
        }
    }

    public String getParameterizedProperty(String property, String[] parameters) {
        Map<String, String> valuesMap = new HashMap<>();
        if(parameters.length>0) {
            int paramnumber = 0;
            for (String param : parameters) {
                valuesMap.put(Integer.toString(paramnumber), param);
                paramnumber++;
            }
            String templateString = property;
            StrSubstitutor sub = new StrSubstitutor(valuesMap);
            String resolvedString = sub.replace(templateString);
            return resolvedString;
        }else {
            return property;
        }
    }

    public boolean click(String objectPropertyKey,String... parameters) throws UIOperationFailureException {
        if(syncForObject(objectPropertyKey,parameters)){
            WebElement element = getElement(objectPropertyKey,parameters);
            element.click();
            return(true);
        }else{
            logger.error("Failed to click on element :"+objectPropertyKey);
            return false;
        }
    }

    public boolean sendKeys(String objectPropertyKey,String value, String... parameters) throws UIOperationFailureException {
        if(syncForObject(objectPropertyKey,parameters)){
            WebElement element = getElement(objectPropertyKey,parameters);
            element.sendKeys(value);
            return(true);
        }else{
            logger.error("Failed to sendKeys to element :"+objectPropertyKey);
            return false;
        }
    }

    public boolean clearAndSendKeys(String objectPropertyKey,String value, String... parameters) throws UIOperationFailureException {
        if(syncForObject(objectPropertyKey,parameters)){
            WebElement element = getElement(objectPropertyKey,parameters);
            element.clear();
            element.sendKeys(value);
            return(true);
        }else{
            logger.error("Failed to send keys to element :"+objectPropertyKey);
            return false;
        }
    }

    public boolean switchToFrame(String objectPropertyKey,String... parameters ) throws UIOperationFailureException {
        if(syncForObject(objectPropertyKey,parameters)){
            WebElement element=getElement(objectPropertyKey,parameters);
            driver.switchTo().frame(element);
            return(true);
        }else{
            logger.error("Failed to switch to frame "+objectPropertyKey);
            return false;
        }
    }

    public void switchToDefault(){
        driver.switchTo().defaultContent();
    }

    public boolean exists(String objectPropertyKey,String... parameters ){
        try{
            WebElement element = getElement(objectPropertyKey,parameters);
            return(true);
        }catch (Exception e){
            return false;
        }
    }

    public boolean isVisible(String objectPropertyKey,String... parameters ){
        try{
            WebElement element = getElement(objectPropertyKey,parameters);
            return(element.isDisplayed());
        }catch (Exception e){
            return false;
        }
    }

    public String getText(String objectPropertyKey,String... parameters) throws UIOperationFailureException {
        if(syncForVisible(objectPropertyKey,parameters)){
            WebElement element = getElement(objectPropertyKey,parameters);
            return(element.getText());
        }else{
            throw (new UIOperationFailureException("Element is not visible"));
        }
    }

    public boolean syncForObject(String objectPropertyKey,String... parameters){
        boolean exists=false;
        long systemTime = System.currentTimeMillis();
        do{
            exists=this.exists(objectPropertyKey,parameters);
        }while(!exists && System.currentTimeMillis()<this.defaultWaitTime+systemTime);
        return(exists);
    }

    public boolean syncForObject(String objectPropertyKey,int miliSeconds, String... parameters ){
        boolean exists=false;
        long systemTime = System.currentTimeMillis();
        do{
            exists=this.exists(objectPropertyKey,parameters);
        }while(!exists && System.currentTimeMillis()<miliSeconds+systemTime);
        return(exists);
    }

    public boolean syncForVisible(String objectPropertyKey,int miliSeconds, String... parameters ){
        boolean visible=false;
        long systemTime = System.currentTimeMillis();
        do{
            visible=this.isVisible(objectPropertyKey,parameters);
        }while(!visible && System.currentTimeMillis()<miliSeconds+systemTime);
        return(visible);
    }

    public boolean syncForVisible(String objectPropertyKey,String... parameters){
        boolean visible=false;
        long systemTime = System.currentTimeMillis();
        do{
            visible=this.isVisible(objectPropertyKey,parameters);
        }while(!visible && System.currentTimeMillis()<this.defaultWaitTime+systemTime);
        return(visible);
    }

    public boolean selectByVisibleText(String objectPropertyKey, String value,String... parameters) throws UIOperationFailureException {
        if(syncForObject(objectPropertyKey,parameters)){
            WebElement element=getElement(objectPropertyKey,parameters);
            Select select = new Select(element);
            select.selectByVisibleText(value);
            return (true);
        }else{
            logger.fatal("Element not found for select");
            return (false);
        }
    }

    public String getAttributeValue(String objectPropertyKey, String attribute,String... parameters) throws UIOperationFailureException {
        if(syncForObject(objectPropertyKey,parameters)){
            String value = getElement(objectPropertyKey,parameters).getAttribute(attribute);
            return(value);
        }else{
            logger.fatal("Failed to get the element attribute "+attribute);
            return (null);
        }
    }

    public boolean sycnForProperty(String objectPropertyKey,String attribute,String value,String... parameters) throws UIOperationFailureException {
        long systemTime = System.currentTimeMillis();
        do{
            String elementValue = getAttributeValue(objectPropertyKey,attribute,parameters);
            if(elementValue.equalsIgnoreCase(value)){
                return (true);
            }
        }while (System.currentTimeMillis()<this.defaultWaitTime+systemTime);
        return (false);
    }

    public boolean sycnForProperty(String objectPropertyKey,String attribute,String value ,long waitTime,String... parameters) throws UIOperationFailureException {
        long systemTime = System.currentTimeMillis();
        do{
            String elementValue = getAttributeValue(objectPropertyKey,attribute,parameters);
            if(elementValue.equalsIgnoreCase(value)){
                return (true);
            }
        }while (System.currentTimeMillis()<waitTime+systemTime);
        return (false);
    }

    public boolean sycnForText(String objectPropertyKey,String value,String... parameters) throws UIOperationFailureException {
        long systemTime = System.currentTimeMillis();
        do{
            String elementValue = getText(objectPropertyKey,parameters);
            if(elementValue.equalsIgnoreCase(value)){
                return (true);
            }
        }while (System.currentTimeMillis()<this.defaultWaitTime+systemTime);
        return (false);
    }

    public boolean sycnForText(String objectPropertyKey,String value ,long waitTime,String... parameters) throws UIOperationFailureException {
        long systemTime = System.currentTimeMillis();
        do{
            String elementValue = getText(objectPropertyKey,parameters);
            if(elementValue.equalsIgnoreCase(value)){
                return (true);
            }
        }while (System.currentTimeMillis()<waitTime+systemTime);
        return (false);
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
