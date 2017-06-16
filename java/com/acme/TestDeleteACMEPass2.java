package com.acme;

import org.apache.xpath.operations.Bool;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class TestDeleteACMEPass2 {
    private WebDriver driver;
    private String url;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();

    @Before
    public void setUp() throws Exception {
      //System.setProperty("webdriver.gecko.driver", "/home/kelvin/seng426/geckodriver");

  	  driver = new FirefoxDriver();
      url = "http://localhost:8080/#/";
      driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
      driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
      driver.manage().timeouts().setScriptTimeout(10,TimeUnit.SECONDS);

      login();
      navigateToACMEPass();
    }


    @After
    public void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }

    @Test
    public void haveDeleteModal() throws Exception {
        String site_name = createPassword();

        WebElement deleteButton = (new WebDriverWait(driver, 10, 500))
                .until(ExpectedConditions.elementToBeClickable(
                        driver.findElement(By.xpath("//tr[td//text()[contains(., '"+site_name+"')]]"))
                              .findElement(By.cssSelector("button[ui-sref=\"acme-pass.delete({id:acmePass.id})\"]"))
                ));
        deleteButton.click();

        WebElement deleteModal = (new WebDriverWait(driver, 10, 500))
                .until(ExpectedConditions.elementToBeClickable(
                        By.cssSelector("form[name=\"deleteForm\"]"))
                );
        assertEquals(true, deleteModal.isDisplayed());

        driver.findElement(By.cssSelector("body")).sendKeys(Keys.ESCAPE);

        cleanupPassword(site_name);
    }


    @Test
    public void deleteACMEPass() throws Exception {
        String site_name = createPassword();

        cleanupPassword(site_name);

        WebElement table = getTable();
        WebElement table_body = table.findElement(By.tagName("tbody"));
        List<WebElement> rows = table_body.findElements(By.tagName("tr"));

        List<String> column = new ArrayList<>();
        for (WebElement row : rows) {
            List<WebElement> row_elems = row.findElements(By.tagName("td"));
            String text = row_elems.get(1 /* Index of the heading for Site*/).getText();
            // Force append by using an index argument.
            column.add(column.size(), text);
        }

        assertEquals(false, column.contains(site_name));
    }    
    
    @Test
    public void deleteACMEPassCancel() throws Exception {
        String site_name = createPassword();

        //get the table row/then click the delete button
        WebElement deleteButton = (new WebDriverWait(driver, 10, 500))
                .until(ExpectedConditions.elementToBeClickable(
                        driver.findElement(By.xpath("//tr[td//text()[contains(., '"+site_name+"')]]"))
                                .findElement(By.cssSelector("button[ui-sref=\"acme-pass.delete({id:acmePass.id})\"]"))
                ));
        deleteButton.click();

        //confirm delete
        driver.findElement(By.xpath("//span[contains(text(), 'Cancel')]")).click();

        WebElement table = getTable();
        WebElement table_body = table.findElement(By.tagName("tbody"));
        List<WebElement> rows = table_body.findElements(By.tagName("tr"));

        List<String> column = new ArrayList<>();
        for (WebElement row : rows) {
            List<WebElement> row_elems = row.findElements(By.tagName("td"));
            String text = row_elems.get(1 /* Index of the heading for Site*/).getText();
            // Force append by using an index argument.
            column.add(column.size(), text);
        }

        assertEquals(true, column.contains(site_name));

        cleanupPassword(site_name);
    }    


    private String createPassword() {
        WebElement createButton = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[ui-sref=\"acme-pass.new\"]")));
        createButton.click();


        String site_name = UUID.randomUUID().toString().replaceAll("-", "");

        //populate fields
        driver.findElement(By.id("field_site")).sendKeys(site_name);
        driver.findElement(By.id("field_login")).sendKeys("TestLogin");
        driver.findElement(By.id("field_password")).sendKeys("TestPass");

        //save ACMEPass
        driver.findElement(By.cssSelector("button[type=\"submit\"]")).click();

        return site_name;
    }

    private void cleanupPassword(String site_name) {
        //get the table row/then click the delete button
        WebElement deleteButton = (new WebDriverWait(driver, 10, 500))
                .until(ExpectedConditions.elementToBeClickable(
                        driver.findElement(By.xpath("//tr[td//text()[contains(., '"+site_name+"')]]"))
                                .findElement(By.cssSelector("button[ui-sref=\"acme-pass.delete({id:acmePass.id})\"]"))
                ));
        deleteButton.click();

        //confirm delete
        driver.findElement(By.xpath("//span[contains(text(), 'Delete')]")).click();
    }

    private void login() throws Exception {
        driver.get(url);
        driver.findElement(By.className("glyphicon-log-in")).click();
        driver.findElement(By.id("username")).sendKeys("jo.thomas@acme.com");
        driver.findElement(By.id("password")).sendKeys("mustang");
        driver.findElement(By.className("btn-primary")).click();
    }

    private WebElement getTable() {

        return (new WebDriverWait(driver, 10, 500))
                .until(ExpectedConditions.presenceOfElementLocated(
                        By.cssSelector("div[class=\"table-responsive\"]")
                ));
    }

    private void navigateToACMEPass() {
        WebElement element = driver.findElement(By.xpath("//a[contains(text(),'ACMEPass')]"));

        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", element);
    }

}
