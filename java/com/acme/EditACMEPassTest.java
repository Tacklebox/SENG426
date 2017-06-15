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

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.fail;

/**
 * Created by justinmacaulay on 2017-06-13.
 */
public class EditACMEPassTest {

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
    public void editExistingPasswordChangePassword(){
        int number = 1;
        login();
        navigateToACMEPass();
        createPasswords(number);


        driver.findElement(By.xpath("//span[contains(@class, 'glyphicon-pencil')]")).click();
        WebElement password = driver.findElement(By.id("field_password"));
        password.clear();
        password.sendKeys("TestingPassword");
        driver.findElement(By.xpath("//span[contains(text(),'Save')]")).click();

        driver.findElement(By.xpath("//span[contains(@class, 'glyphicon-eye-open')]")).click();
        WebElement changedPass = driver.findElement(By.cssSelector("input.acmepass-password"));

        assertEquals("TestingPassword", changedPass.getAttribute("value"));

        cleanupPasswords(number);
    }

    @Test
    public void editExistingPasswordChangePasswordButCancel(){
        int number = 1;
        login();
        navigateToACMEPass();
        createPasswords(number);


        driver.findElement(By.xpath("//span[contains(@class, 'glyphicon-pencil')]")).click();
        WebElement password = driver.findElement(By.id("field_password"));
        password.clear();
        password.sendKeys("TestingPassword");
        driver.findElement(By.xpath("//span[contains(text(),'Cancel')]")).click();

        driver.findElement(By.xpath("//span[contains(@class, 'glyphicon-eye-open')]")).click();
        WebElement changedPass = driver.findElement(By.cssSelector("input.acmepass-password"));

        assertNotSame("TestingPassword",changedPass.getAttribute("value"));
        cleanupPasswords(number);
    }

    @Test
    public void editExistingPasswordChangeSite(){
        int number = 1;
        login();
        navigateToACMEPass();
        createPasswords(number);


        driver.findElement(By.xpath("//span[contains(@class, 'glyphicon-pencil')]")).click();
        WebElement Site = driver.findElement(By.id("field_site"));
        Site.clear();
        Site.sendKeys("TestingSite");
        driver.findElement(By.xpath("//span[contains(text(),'Save')]")).click();

        WebElement ACMEPassSiteLabel = driver.findElement(By.xpath("//td[contains(text(),'TestingSite')]"));
        assertEquals("TestingSite", ACMEPassSiteLabel.getText());

        driver.findElement(By.xpath("//span[contains(@class, 'glyphicon-pencil')]")).click();
        Site = driver.findElement(By.id("field_site"));
        Site.clear();
        Site.sendKeys("TestSite0");
        driver.findElement(By.xpath("//span[contains(text(),'Save')]")).click();

        cleanupPasswords(number);
    }

    @Test
    public void editExistingPasswordChangeSiteButCancel(){
        int number = 1;
        login();
        navigateToACMEPass();
        createPasswords(number);


        driver.findElement(By.xpath("//span[contains(@class, 'glyphicon-pencil')]")).click();
        WebElement password = driver.findElement(By.id("field_site"));
        password.clear();
        password.sendKeys("TestingSite");
        driver.findElement(By.xpath("//span[contains(text(),'Cancel')]")).click();
        driver.findElement(By.xpath("//span[contains(@class, 'glyphicon-eye-open')]")).click();

        WebElement site = driver.findElement(By.xpath("//td[contains(text(),'TestSite0')]"));

        assertNotSame("TestingSite", site.getText());

        cleanupPasswords(number);
    }

    @Test
    public void editExistingPasswordChangeLogin(){
        int number = 1;
        login();
        navigateToACMEPass();
        createPasswords(number);


        driver.findElement(By.xpath("//span[contains(@class, 'glyphicon-pencil')]")).click();
        WebElement password = driver.findElement(By.id("field_login"));
        password.clear();
        password.sendKeys("TestingLogin");
        driver.findElement(By.xpath("//span[contains(text(),'Save')]")).click();

        assertEquals("TestingLogin", driver.findElement(By.xpath("//td[contains(text(),'TestingLogin')]")).getText());

        cleanupPasswords(number);
    }

    @Test
    public void editExistingPasswordChangeLoginButCancel(){
        int number = 1;
        login();
        navigateToACMEPass();
        createPasswords(number);


        driver.findElement(By.xpath("//span[contains(@class, 'glyphicon-pencil')]")).click();
        WebElement password = driver.findElement(By.id("field_login"));
        password.clear();
        password.sendKeys("TestingLogin");
        driver.findElement(By.xpath("//span[contains(text(),'Cancel')]")).click();

        assertNotSame("TestingLogin", driver.findElement(By.xpath("//td[contains(text(),'TestLogin0')]")).getText());

        cleanupPasswords(number);
    }

    @Test
    public void editExistingPasswordChangePasswordByGenerateButton(){
        int number = 1;
        String genPass = "";
        login();
        navigateToACMEPass();
        createPasswords(number);

        driver.findElement(By.xpath("//span[contains(@class, 'glyphicon-pencil')]")).click();
        driver.findElement(By.xpath("//span[contains(@class,'glyphicon-refresh')]")).click();
        driver.findElement(By.xpath("//span[contains(@class,'glyphicon-refresh')]")).click();
        genPass = driver.findElement(By.cssSelector("input#field_password")).getAttribute("value");

        driver.findElement(By.xpath("//span[contains(text(),'Use')]")).click();
        driver.findElement(By.xpath("//span[contains(text(),'Save')]")).click();

        driver.findElement(By.xpath("//span[contains(@class, 'glyphicon-eye-open')]")).click();
        WebElement changedPass = driver.findElement(By.cssSelector("input.acmepass-password"));

        assertEquals(genPass, changedPass.getAttribute("value"));



        cleanupPasswords(number);
    }

    @Test
    public void editExistingPasswordChangePasswordByGenerateButtonButCancel(){
        int number = 1;
        String genPass = "";
        login();
        navigateToACMEPass();
        createPasswords(number);

        driver.findElement(By.xpath("//span[contains(@class, 'glyphicon-pencil')]")).click();
        driver.findElement(By.xpath("//span[contains(@class,'glyphicon-refresh')]")).click();
        driver.findElement(By.xpath("//span[contains(@class,'glyphicon-refresh')]")).click();
        genPass = driver.findElement(By.cssSelector("input#field_password")).getAttribute("value");

        driver.findElement(By.xpath("//span[contains(text(),'Use')]")).click();
        driver.findElement(By.xpath("//span[contains(text(),'Cancel')]")).click();

        driver.findElement(By.xpath("//span[contains(@class, 'glyphicon-eye-open')]")).click();
        WebElement changedPass = driver.findElement(By.cssSelector("input.acmepass-password"));

        assertNotSame(genPass, changedPass.getAttribute("value"));



        cleanupPasswords(number);
    }




    @After
    public void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
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

    public void login(){
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
}
