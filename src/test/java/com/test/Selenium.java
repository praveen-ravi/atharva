package com.test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.Select;

/**
 * Created by 16733 on 04/02/17.
 */
public class Selenium {

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("webdriver.chrome.driver","src/main/resources/chromedriver");
        System.setProperty("webdriver.gecko.driver","src/main/resources/geckodriver");
        ChromeOptions chromeOptions=new ChromeOptions();
        chromeOptions.addArguments("-incognito");
        DesiredCapabilities chromeCapabilities=new DesiredCapabilities();

        chromeCapabilities.setCapability(ChromeOptions.CAPABILITY,chromeOptions);
        chromeCapabilities.setBrowserName("chrome");

        DesiredCapabilities firefoxCapabilities=new DesiredCapabilities();
        firefoxCapabilities.setVersion("49.0.2");

        WebDriver driver1 =new ChromeDriver(chromeCapabilities);


        driver1.get("https://strade.sharekhan.com/rmmweb/");
        //driver1.findElement(By.xpath("//td[contains(text(),'MARKET TODAY')]")).click();

        driver1.findElement(By.id("loginid")).sendKeys("pravi86");
        driver1.findElement(By.id("pwSignup")).sendKeys("Tiger@5683");
        driver1.findElement(By.id("pwSignup1")).sendKeys("Cinde@6283");
        driver1.findElement(By.xpath("//input[@name='Login']")).click();
        driver1.findElement(By.xpath("//span[@id='teqmtm']")).click();
        Thread.sleep(2000);
        WebElement element=driver1.findElement(By.xpath("//select[@name='requeststatus']"));
        System.out.println(element.isDisplayed());
        Select select=new Select(element);
        select.selectByVisibleText("Order");

        WebElement element1=driver1.findElement(By.xpath("//a[contains(text(), '651372836')]/parent::td/following-sibling::td[12]"));
        System.out.println(element1.isDisplayed());

    }

}

class UiAutoThread extends Thread{
    String url;
    WebDriver driver;
    public UiAutoThread(WebDriver driver,String url){
        this.driver=driver;
        this.url=url;

    }
    @Override
    public void run(){
        driver.get(url);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(this.getName()+":"+driver.getTitle());
       // driver.findElement(By.xpath("//a[1]")).click();
    }
}