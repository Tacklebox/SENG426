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
        
        Thread.sleep(5000);

        //get the rows of the table
        //each row should be a different password
        List<WebElement> rows = driver.findElements(By.tagName("tr"));
        boolean atLeastOneDiff = false;

        //should be a list object not null
    	System.err.println("(object) list of rows:>"+rows);

    	int numRowsBefore;
    	int numRowsAfter;

    	/*
    	String[] acmeListBefore = new String[100];
        String[] acmeListAfter = new String[100];
        int i =0;
        for(WebElement e : rows){
        	List<WebElement> cell = e.findElements(By.tagName("td"));
        	//should be a list object not null
        	System.err.println("(object) list of colmns"+cell);

        	try{acmeListBefore[i] = cell.get(0).toString();}
        	catch (IndexOutOfBoundsException err){
            	System.err.println("reached out of bounds, index at "+ i);        		
        		break;
        	}
        	i++;
        	//arbitrary max on the iteration since long lists are not part of the test
        	if(i>99) break;

        }
*/ 
    	numRowsBefore = rows.size();
    	//this located the element based on the button text
        //might not be working perfectly since page seems to hang and does not show the updated table after deletion
        WebElement ACMEPassLoginLabel = driver.findElement(By.xpath("//td[contains(text(),'TestSiteD')]"));
        ACMEPassLoginLabel.findElement(By.xpath("./..//button[contains(@class, 'btn btn-danger btn-sm')]")).click();

    	driver.findElement(By.xpath("//span[contains(text(), 'Delete')]")).click();
        driver.navigate().refresh();
    	Thread.sleep(5000);
    	//remake rows object after refresh
    	rows = driver.findElements(By.tagName("tr"));
        numRowsAfter = rows.size();
    	
/*
    int j =0;
    for(WebElement m : rows){
    	System.err.println("A _ " + m.getTagName());
    	
    	//try to find cells within each row of the table
    	List<WebElement> cell = m.findElements(By.tagName("td"));
    	System.err.println("B _ "+acmeListAfter);
    	//get cell 1 which should be the id number
    	acmeListAfter[j] = cell.get(0).toString();
    	System.err.println("C _ "+acmeListAfter[j]);
        if(acmeListBefore[j].equals(acmeListAfter[j])){
        	
        }
    	j++;
    	if(j>99) break;

    }
*/    
    	System.err.println(numRowsBefore);
    	System.err.println(numRowsAfter);

  assertEquals(numRowsBefore,numRowsAfter+1);  
    }    
    
    @Test
    public void deleteACMEPassCancel() throws Exception {
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
        driver.findElement(By.id("field_site")).sendKeys("TestSiteC");
        driver.findElement(By.id("field_login")).sendKeys("TestLoginC");
        driver.findElement(By.id("field_password")).sendKeys("TestPassC");
        //save ACMEPass
        driver.findElement(By.xpath("//span[contains(text(),'Save')]")).click();
        
        Thread.sleep(5000);

        //get the rows of the table
        //each row should be a different password
        List<WebElement> rows = driver.findElements(By.tagName("tr"));
        boolean atLeastOneDiff = false;

        //should be a list object not null
    	System.err.println("(object) list of rows:>"+rows);

    	int numRowsBefore;
    	int numRowsAfter;

    	numRowsBefore = rows.size();
    	//this located the element based on the button text
        //might not be working perfectly since page seems to hang and does not show the updated table after deletion
        WebElement ACMEPassLoginLabel = driver.findElement(By.xpath("//td[contains(text(),'TestSiteC')]"));
        ACMEPassLoginLabel.findElement(By.xpath("./..//button[contains(@class, 'btn btn-danger btn-sm')]")).click();

    	driver.findElement(By.xpath("//span[contains(text(), 'Cancel')]")).click();
        driver.navigate().refresh();
    	Thread.sleep(5000);
    	//remake rows object after refresh
    	rows = driver.findElements(By.tagName("tr"));
        numRowsAfter = rows.size();

        System.err.println(numRowsBefore);
    	System.err.println(numRowsAfter);

  assertEquals(numRowsBefore,numRowsAfter);  
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
