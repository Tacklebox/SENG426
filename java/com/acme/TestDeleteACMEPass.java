package com.acme;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import static org.junit.Assert.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class TestDeleteACMEPass {
    private WebDriver driver;
    private String url;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();

    @Before
    public void setUp() throws Exception {
      System.setProperty("webdriver.gecko.driver", "/home/kelvin/seng426/geckodriver");

//      driver = new HtmlUnitDriver(true);
  	driver = new FirefoxDriver();
      url = "http://localhost:8080/#/";
      driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
      driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
      driver.manage().timeouts().setScriptTimeout(10,TimeUnit.SECONDS);
    }

    @Test
    public void deleteACMEPass() throws Exception {
    	String username = "jo.thomas@acme.com";
    	String userpass = "mustang";
    	
    	driver.get(url);
        driver.findElement(By.className("glyphicon-log-in")).click();
        driver.findElement(By.id("username")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(userpass);
        driver.findElement(By.className("btn-primary")).click();
        
        WebElement element = driver.findElement(By.xpath("//a[contains(text(),'ACMEPass')]"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);

        driver.findElement(By.xpath("//span[contains(text(),'Create new ACME Pass')]")).click();
        driver.findElement(By.id("field_site")).sendKeys("TestSiteD");
        driver.findElement(By.id("field_login")).sendKeys("TestLoginD");
        driver.findElement(By.id("field_password")).sendKeys("TestPassD");
        //save ACMEPass
        driver.findElement(By.xpath("//span[contains(text(),'Save')]")).click();

        //ensure the newly added password exists in the list
        WebElement ACMEPassLoginLabel = driver.findElement(By.xpath("//td[contains(text(),'TestSiteD')]"));
        //get the table row/then click the delete button
        ACMEPassLoginLabel.findElement(By.xpath("./..//button[contains(@class, 'btn btn-danger btn-sm')]")).click();

        //get the rows of the table
        //each row should be a different password
        List<WebElement> rows = driver.findElements(By.tagName("tr"));
        
        //should be a list object not null
    	System.err.println(rows);

    	String[] acmeListBefore = new String[100];
        String[] acmeListAfter = new String[100];
        int i =0;
        for(WebElement e : rows){
        	List<WebElement> cell = e.findElements(By.tagName("td"));
        	//should be a list object not null
        	System.err.println(acmeListAfter);

        	try{acmeListBefore[i] = cell.get(0).toString();}
        	catch (IndexOutOfBoundsException err){
            	System.err.println("reached out of bounds, index at "+ i);        		
        		break;
        	}
        	i++;
        	//arbitrary max on the iteration since long lists are not part of the test
        	if(i>99) break;

        }
        //this located the element based on the button text
        //might not be working perfectly since page seems to hang and does not show the updated table after deletion
        driver.findElement(By.xpath("//span[contains(text(), 'Delete')]")).click();


    int j =0;
    boolean atLeastOneDiff = false;
    for(WebElement m : rows){
    	//try to find cells within each row of the table
    	List<WebElement> cell = m.findElements(By.tagName("td"));
    	System.err.println(acmeListAfter);
    	//get cell 1 which should be the id number
    	acmeListAfter[j] = cell.get(0).toString();
    	System.err.println(acmeListAfter[j]);
        if(acmeListBefore[j].equals(acmeListAfter[j])){
        	
        }
    	j++;
    	if(j>99) break;

    }
    
  assertEquals(true,false);  
    }    
    @After
    public void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }

    private boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private boolean isAlertPresent() {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    private String closeAlertAndGetItsText() {
        try {
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            if (acceptNextAlert) {
                alert.accept();
            } else {
                alert.dismiss();
            }
            return alertText;
        } finally {
            acceptNextAlert = true;
        }
    }
}
