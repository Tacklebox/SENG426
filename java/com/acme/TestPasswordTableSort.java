package com.acme;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TestPasswordTableSort {
    private WebDriver driver;
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
    public void tableExists() throws Exception {
        WebElement table = getTable();
        assertEquals(true, table.isDisplayed());
    }

    @Test
    public void tableHeadingsCorrect() throws Exception {
        WebElement table = getTable();
        List<WebElement> headings = table.findElements(By.tagName("th"));
        List<String> headings_text = headings.stream().map(WebElement::getText).collect(Collectors.toList());

        // Last entry is an empty string to represent the delete button column
        String[] expected_heading = {"ID", "Site", "Login", "Password", "Created Date", "Last Modified Date", ""};
        assertArrayEquals("Headings for password table do not match expected.",
                expected_heading,
                headings_text.toArray()
        );
    }

    @Test
    public void sortHeaders() throws Exception {
        // Verify there's some passwords to sort
        WebElement table = getTable();

        WebElement table_body = table.findElement(By.tagName("tbody"));
        List<WebElement> rows = table_body.findElements(By.tagName("tr"));

        // Make passwords until there's 5 entries
        int numTestPasswordsCreated = 0;
        if (rows.size() < 5) {
            numTestPasswordsCreated = 5 - rows.size();
        }
        createPasswords(numTestPasswordsCreated);

        // Test sorting in both directions

        // ID IS DEFAULTED TO SORT IN ASCENDING ON PAGE LOAD
        // Therefore we'll test both in the opposite order
        sortByHeader(0, true); // ID
        sortByHeader(0, false);

        sortByHeader(1, false); // Site
        sortByHeader(1, true);
        sortByHeader(2, false); // Login
        sortByHeader(2, true);
        sortByHeader(3, false); // Password
        sortByHeader(3, true);
        sortByHeader(4, false); // Created Date
        sortByHeader(4, true);
        sortByHeader(5, false); // Last Modified Date
        sortByHeader(5, true); 

        // Remove the passwords we made
        cleanupPasswords(numTestPasswordsCreated);
    }


    private void sortByHeader(int headerIndex, boolean isReverse) throws Exception {
        WebElement table = getTable();
        WebElement table_head = table.findElement(By.cssSelector("thead"));
        List<WebElement> headings = table_head.findElements(By.tagName("th"));
        WebElement heading = headings.get(headerIndex);
        heading.click();

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

        List<String> sorted = new ArrayList<>(column);
        Collections.sort(sorted);

        if (isReverse) {
            Collections.reverse(sorted);
        }

        assertArrayEquals(sorted.toArray(), column.toArray());
    }

    private void login() throws Exception {
        driver.get(url);
        driver.findElement(By.className("glyphicon-log-in")).click();
        driver.findElement(By.id("username")).sendKeys("jo.thomas@acme.com");
        driver.findElement(By.id("password")).sendKeys("mustang");
        driver.findElement(By.className("btn-primary")).click();
    }

    private void navigateToACMEPass() {
        WebElement element = driver.findElement(By.xpath("//a[contains(text(),'ACMEPass')]"));

        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", element);
    }

    private WebElement getTable() {
        return (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.presenceOfElementLocated(
                        By.cssSelector("div[class=\"table-responsive\"]")
                ));
    }

    private void createPasswords(int numPasswords){
        for (int i = 0; i < numPasswords; i += 1) {
            WebElement createButton = (new WebDriverWait(driver, 10))
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

    private void cleanupPasswords(int numPasswords) {
        for(int i = 0; i < numPasswords; i += 1) {
            //get the table row/then click the delete button

            WebElement deleteButton = (new WebDriverWait(driver, 10))
                    .until(ExpectedConditions.elementToBeClickable(
                        driver.findElement(By.xpath("//td[contains(text(),'TestSite" + String.valueOf(i) + "')]"))
                              .findElement(By.xpath("./..//button[contains(@class, 'btn btn-danger btn-sm')]"))
                    ));
            deleteButton.click();

            //confirm delete
            driver.findElement(By.xpath("//span[contains(text(), 'Delete')]")).click();
        }
    }
}
