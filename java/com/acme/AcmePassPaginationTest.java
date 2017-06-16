package com.acme;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by justinmacaulay on 2017-06-15.
 */
public class AcmePassPaginationTest {
    private WebDriver driver;
    private int numberOfUsers = 0;
    private String url;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();

    @Before
    public void setUp() throws Exception {
        //System.setProperty("webdriver.gecko.driver");

        driver = new FirefoxDriver();
        url = "http://localhost:8080/#/";
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

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
    public void ForwardPaginationTest() throws Exception{
        int number = 21;
        createPasswords(number);

        WebElement pageNextButton = (new WebDriverWait(driver, 10, 500))
                .until(ExpectedConditions.elementToBeClickable(
                        driver.findElement(By.xpath("//li[contains(@class, 'next')]/a"))
                ));
        pageNextButton.click();

        assertEquals("TestLogin20", driver.findElement(By.xpath("//td[contains(text(),'TestLogin20')]")).getText());

        WebElement pagePreviousButton = (new WebDriverWait(driver, 10, 500))
                .until(ExpectedConditions.elementToBeClickable(
                        driver.findElement(By.xpath("//li[contains(@class, 'previous')]/a"))
                ));
        pagePreviousButton.click();

        cleanupPasswords(number);

    }

    @Test
    public void ForwardThenBackPaginationTest() throws Exception{
        int number = 21;
        createPasswords(number);

        WebElement pageNextButton = (new WebDriverWait(driver, 10, 500))
                .until(ExpectedConditions.elementToBeClickable(
                        driver.findElement(By.xpath("//li[contains(@class, 'next')]/a"))
                ));
        pageNextButton.click();

        assertEquals("TestLogin20", driver.findElement(By.xpath("//td[contains(text(),'TestLogin20')]")).getText());

        WebElement pagePreviousButton = (new WebDriverWait(driver, 10, 500))
                .until(ExpectedConditions.elementToBeClickable(
                        driver.findElement(By.xpath("//li[contains(@class, 'previous')]/a"))
                ));
        pagePreviousButton.click();

        assertEquals("TestLogin0", driver.findElement(By.xpath("//td[contains(text(),'TestLogin0')]")).getText());

        cleanupPasswords(number);

    }

    @Test
    public void ForwardPaginationWithSortTest() throws Exception{
        int number = 21;
        createPasswords(number);

        List<String> table = new ArrayList<>(sortByHeader(1, true));

        WebElement pageNextButton = (new WebDriverWait(driver, 10, 500))
                .until(ExpectedConditions.elementToBeClickable(
                        driver.findElement(By.xpath("//li[contains(@class, 'next')]/a"))
                ));
        pageNextButton.click();

        table.addAll(sortByHeader(1, false));

        List<String> sorted = new ArrayList<>(table);

        Collections.sort(sorted);

        assertArrayEquals(sorted.toArray(), table.toArray());

        WebElement pagePreviousButton = (new WebDriverWait(driver, 10, 500))
                .until(ExpectedConditions.elementToBeClickable(
                        driver.findElement(By.xpath("//li[contains(@class, 'previous')]/a"))
                ));
        pagePreviousButton.click();

        cleanupPasswords(number);

    }

    private void login() throws Exception {
        driver.get(url);
        driver.findElement(By.className("glyphicon-log-in")).click();
        driver.findElement(By.id("username")).sendKeys("jo.thomas@acme.com");
        driver.findElement(By.id("password")).sendKeys("mustang");
        driver.findElement(By.className("btn-primary")).click();
    }

    private void navigateToACMEPass() throws Exception{
        WebElement element = driver.findElement(By.xpath("//a[contains(text(),'ACMEPass')]"));

        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", element);
    }

    private void createPasswords(int numPasswords) throws Exception{
        for (int i = 0; i < numPasswords; i += 1) {
            WebElement createButton = (new WebDriverWait(driver, 10, 500))
                    .until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[ui-sref=\"acme-pass.new\"]")));
            createButton.click();
            //populate fields
            driver.findElement(By.id("field_site")).sendKeys("TestSite" + String.valueOf(i));
            driver.findElement(By.id("field_login")).sendKeys("TestLogin" + String.valueOf(i));
            driver.findElement(By.id("field_password")).sendKeys("TestPass" + String.valueOf(i));
            //save ACMEPass
            driver.findElement(By.cssSelector("button[type=\"submit\"]")).click();
            driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        }
    }

    private void cleanupPasswords(int numPasswords) throws Exception {
        for(int i = 0; i < numPasswords; i += 1) {
            //get the table row/then click the delete button

            WebElement deleteButton = (new WebDriverWait(driver, 10, 500))
                    .until(ExpectedConditions.elementToBeClickable(
                            driver.findElement(By.xpath("//td[contains(text(),'TestSite" + String.valueOf(i) + "')]"))
                                    .findElement(By.xpath("./..//button[contains(@class, 'btn btn-danger btn-sm')]"))
                    ));
            deleteButton.click();

            //confirm delete
            driver.findElement(By.xpath("//span[contains(text(), 'Delete')]")).click();
        }
    }

    private List<String> sortByHeader(int headerIndex, boolean doSort) throws Exception {

        WebElement table = getTable();

        if(doSort) {
            WebElement table_head = table.findElement(By.cssSelector("thead"));
            List<WebElement> headings = table_head.findElements(By.tagName("th"));
            WebElement heading = headings.get(headerIndex);
            heading.click();
        }
        // Sorting reinitialized the table
        table = getTable();

        WebElement table_body = table.findElement(By.tagName("tbody"));
        List<WebElement> rows = table_body.findElements(By.tagName("tr"));

        List<String> column = new ArrayList<>();
        for (WebElement row : rows) {
            List<WebElement> row_elems = row.findElements(By.cssSelector("td"));
            String text = row_elems.get(headerIndex).getText();
            // Force append by using an index argument.
            column.add(column.size(), text);
        }

        return column;
    }

    private WebElement getTable() throws Exception{
        return (new WebDriverWait(driver, 10, 500))
                .until(ExpectedConditions.presenceOfElementLocated(
                        By.cssSelector("div[class=\"table-responsive\"]")
                ));
    }
}
