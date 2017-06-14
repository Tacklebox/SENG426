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

import static org.junit.Assert.*;

/**
 * Created by justinmacaulay on 2017-06-13.
 * Modified by jasonsyrotuck on 2017-06-13
 */
public class CreateACMEPassTest {

    private WebDriver driver;
    private String url;
    private StringBuffer verificationErrors = new StringBuffer();

    @Before
    public void setUp() throws Exception {

        driver = new FirefoxDriver();
        url = "http://localhost:8080/#/";
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        login();

        WebElement element = driver.findElement(By.xpath("//a[contains(text(),'ACMEPass')]"));

        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", element);
    }

    @Test
    public void createACMEPass() throws InterruptedException {
        driver.findElement(By.xpath("//span[contains(text(),'Create new ACME Pass')]")).click();
        //populate fields
        driver.findElement(By.id("field_site")).sendKeys("TestSite");
        driver.findElement(By.id("field_login")).sendKeys("TestLogin");
        driver.findElement(By.id("field_password")).sendKeys("TestPass");

        //save ACMEPass
        driver.findElement(By.xpath("//span[contains(text(),'Save')]")).click();

        //ensure the newly added password exists in the list
        //driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        WebElement ACMEPassLoginLabel = driver.findElement(By.xpath("//td[contains(text(),'TestSite')]"));
        assertEquals("TestSite", ACMEPassLoginLabel.getText());
        //CLEANUP
        //get the table row/then click the delete button
        ACMEPassLoginLabel.findElement(By.xpath("./..//button[contains(@class, 'btn btn-danger btn-sm')]")).click();

        //confirm delete
        driver.findElement(By.xpath("//span[contains(text(), 'Delete')]")).click();
    }

    @Test
    public void createACMEPassWithoutSite(){
        driver.findElement(By.xpath("//span[contains(text(),'Create new ACME Pass')]")).click();
        //populate fields
        //driver.findElement(By.id("field_site")).sendKeys("TestSite");
        driver.findElement(By.id("field_login")).sendKeys("TestLogin");
        driver.findElement(By.id("field_password")).sendKeys("TestPass");

        //ASSERT save button is disabled
        assertNotNull(driver.findElement(By.xpath("//span[contains(text(),'Save')]/..")).getAttribute("disabled"));
    }

    @Test
    public void createACMEPassWithoutLogin(){
        driver.findElement(By.xpath("//span[contains(text(),'Create new ACME Pass')]")).click();

        //populate fields
        driver.findElement(By.id("field_site")).sendKeys("TestSite");
        //driver.findElement(By.id("field_login")).sendKeys("TestLogin");
        driver.findElement(By.id("field_password")).sendKeys("TestPass");

        //ASSERT save button is disabled
        assertNotNull(driver.findElement(By.xpath("//span[contains(text(),'Save')]/..")).getAttribute("disabled"));

    }

    @Test
    public void createACMEPassWithoutPassword(){
        driver.findElement(By.xpath("//span[contains(text(),'Create new ACME Pass')]")).click();

        //populate fields
        driver.findElement(By.id("field_site")).sendKeys("TestSite");
        driver.findElement(By.id("field_login")).sendKeys("TestLogin");
        //driver.findElement(By.id("field_password")).sendKeys("TestPass");

        //ASSERT save button is disabled
        assertNotNull(driver.findElement(By.xpath("//span[contains(text(),'Save')]/..")).getAttribute("disabled"));
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
