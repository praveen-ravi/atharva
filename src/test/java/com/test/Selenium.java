package com.test;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * Created by 16733 on 04/02/17.
 */
public class Selenium {

    public static void main(String[] args){

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


        driver1.get("http://demo.guru99.com/V4/index.php");
        driver1.findElement(By.xpath("//input[@name='uid']")).sendKeys("sdafw");
        driver1.findElement(By.xpath("//input[@name='password']")).sendKeys("sdafw");
        driver1.findElement(By.xpath("//input[@name='btnLogin']")).click();

        Alert alert=driver1.switchTo().alert();
        alert.accept();
        driver1.findElement(By.xpath("//input[@name='uid']")).sendKeys("sdafw");

        //WebDriver driver2= new FirefoxDriver(firefoxCapabilities);
       //UiAutoThread thread1=new UiAutoThread(driver1,"http://www.sharekhan.com/stock-market/HomePage.aspx");
        //UiAutoThread thread2=new UiAutoThread(driver2,"https://www.sharekhan.com/stock-market/HomePage.aspx");
        //thread1.setName("yahooThread");
        //thread2.setName("youtubeThread");
        //thread1.start();
        //thread2.start();
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