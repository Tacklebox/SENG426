package com.acme;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by justinmacaulay on 2017-06-13.
 */
public class ViewACMEPassTest {

    private WebDriver driver;
    private String url;
    private StringBuffer verificationErrors = new StringBuffer();

    @Before
    public void setUp() throws Exception {

        driver = new FirefoxDriver();
        url = "http://localhost:8080/#/";
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

    }

    @Test
    public void viewACMEPass(){
        login();

        WebElement element = driver.findElement(By.xpath("//a[contains(text(),'ACMEPass')]"));

        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", element);

        assertEquals("Create new ACME Pass" ,driver.findElement(By.xpath("//span[contains(text(),'Create new ACME Pass')]")).getText());
    }

    @Test
    public void tryToViewPassNotLoggedIn(){
        driver.get(url);
        List<WebElement> element = driver.findElements(By.xpath("//a[contains(text(),'ACMEPass')]"));

        assertTrue(element.isEmpty());
    }

    @Test
    public void ensureTableIsOnThePage(){
        login();

        WebElement element = driver.findElement(By.xpath("//a[contains(text(),'ACMEPass')]"));

        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", element);

        List<WebElement> tableHeadings = driver.findElements(By.xpath("//th"));

        assertEquals("ID", tableHeadings.get(0).getText());
        assertEquals("Site", tableHeadings.get(1).getText());
        assertEquals("Login", tableHeadings.get(2).getText());
        assertEquals("Password", tableHeadings.get(3).getText());
        assertEquals("Created Date", tableHeadings.get(4).getText());
        assertEquals("Last Modified Date", tableHeadings.get(5).getText());



    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }

    public void login(){
        driver.get(url);
        driver.findElement(By.className("glyphicon-log-in")).click();
        driver.findElement(By.id("username")).sendKeys("jo.thomas@acme.com");
        driver.findElement(By.id("password")).sendKeys("mustang");
        driver.findElement(By.className("btn-primary")).click();
    }
}
