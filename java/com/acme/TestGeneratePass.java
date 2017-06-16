package com.acme;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TestGeneratePass {
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
        openGeneratePasswordModal();
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
    public void haveGeneratePasswordModal() throws Exception {
        WebElement pwGenForm = (new WebDriverWait(driver, 10, 500))
                .until(ExpectedConditions.elementToBeClickable(
                        By.cssSelector("form[name=\"pdwGenForm\"]"))
                );
        assertEquals(true, pwGenForm.isDisplayed());
    }

    @Test
    public void testPWLengthWITHRepetition() throws Exception {
        Map<String, WebElement> formElements = getPWGenFormElements();
        setCheckboxValue(formElements.get("lowercase"), true);
        setCheckboxValue(formElements.get("uppercase"), true);
        setCheckboxValue(formElements.get("digits"), true);
        setCheckboxValue(formElements.get("special"), true);
        setCheckboxValue(formElements.get("repetition"), false);

        int[] test_lengths = {0, 1, 10, 100, 1000};
        for (Integer length : test_lengths) {
            generateAndValidateLength(formElements, length);
        }
    }

    @Test
    public void testPWLengthWITHOUTRepetition() throws Exception {
        Map<String, WebElement> formElements = getPWGenFormElements();
        setCheckboxValue(formElements.get("lowercase"), true);
        setCheckboxValue(formElements.get("uppercase"), true);
        setCheckboxValue(formElements.get("digits"), true);
        setCheckboxValue(formElements.get("special"), true);
        setCheckboxValue(formElements.get("repetition"), true);

        // Here we only test up to 56 items since the without repetition
        // we only have so much entropy
        // 56 == len([a-z] + [A-Z] + [0-9] + [!@#$%-_])
        int[] test_lengths = {0, 1, 10, 56};
        for (Integer length : test_lengths) {
            generateAndValidateLength(formElements, length);
            assertEquals(
                    true,
                    formElements.get("output").getAttribute("value").matches("(?:([A-Za-z0-9!@#$%\\-_])(?!.*\\1))*")
            );
        }
    }

    private void generateAndValidateLength(Map<String, WebElement> formElements, Integer length) {
        formElements.get("length").clear();

        // Set the output to "loading..." so that we can wait until the result is generated
        // even if the length is zero. A previous version used .clear() but then couldn't
        // detect when the output was generated.
        formElements.get("output").sendKeys("loading...");
        formElements.get("length").sendKeys(String.valueOf(length));
        formElements.get("generate").click();

        // Wait until output is generated
        (new WebDriverWait(driver, 10, 500)).until(
                (ExpectedCondition<Boolean>) webDriver -> webDriver != null &&
                        !webDriver.findElement(
                                By.cssSelector("input#field_password[type=\"text\"]")
                        ).getAttribute("value").equals("loading...")
        );

        assertEquals("Generated password: `" + formElements.get("output").getAttribute("value")
                        + "` is not " + String.valueOf(length) + " characters long.",
                (long)length, formElements.get("output").getAttribute("value").length());
    }

    @Test
    public void testlowercase() throws Exception {
        Map<String, WebElement> formElements = getPWGenFormElements();
        setCheckboxValue(formElements.get("repetition"), false);

        setCheckboxValue(formElements.get("lowercase"), true);
        setCheckboxValue(formElements.get("uppercase"), false);
        setCheckboxValue(formElements.get("digits"), false);
        setCheckboxValue(formElements.get("special"), false);


        generateAndValidateLength(formElements, 10);

        assertEquals(true, formElements.get("output").getAttribute("value").matches("[a-z]{10}"));

        setCheckboxValue(formElements.get("lowercase"), false);
        setCheckboxValue(formElements.get("uppercase"), true);
        setCheckboxValue(formElements.get("digits"), true);
        setCheckboxValue(formElements.get("special"), true);

        generateAndValidateLength(formElements, 10);

        assertEquals(true, formElements.get("output").getAttribute("value").matches("[^a-z]{10}"));

    }

    @Test
    public void testuppercase() throws Exception {
        Map<String, WebElement> formElements = getPWGenFormElements();
        setCheckboxValue(formElements.get("repetition"), false);

        setCheckboxValue(formElements.get("lowercase"), false);
        setCheckboxValue(formElements.get("uppercase"), true);
        setCheckboxValue(formElements.get("digits"), false);
        setCheckboxValue(formElements.get("special"), false);


        generateAndValidateLength(formElements, 10);

        assertEquals(true, formElements.get("output").getAttribute("value").matches("[A-Z]{10}"));

        setCheckboxValue(formElements.get("lowercase"), true);
        setCheckboxValue(formElements.get("uppercase"), false);
        setCheckboxValue(formElements.get("digits"), true);
        setCheckboxValue(formElements.get("special"), true);

        generateAndValidateLength(formElements, 10);

        assertEquals(true, formElements.get("output").getAttribute("value").matches("[^A-Z]{10}"));
    }

    @Test
    public void testnumbers() throws Exception {
        Map<String, WebElement> formElements = getPWGenFormElements();
        setCheckboxValue(formElements.get("repetition"), false);

        setCheckboxValue(formElements.get("lowercase"), false);
        setCheckboxValue(formElements.get("uppercase"), false);
        setCheckboxValue(formElements.get("digits"), true);
        setCheckboxValue(formElements.get("special"), false);


        generateAndValidateLength(formElements, 10);

        assertEquals(true, formElements.get("output").getAttribute("value").matches("[0-9]{10}"));

        setCheckboxValue(formElements.get("lowercase"), true);
        setCheckboxValue(formElements.get("uppercase"), true);
        setCheckboxValue(formElements.get("digits"), false);
        setCheckboxValue(formElements.get("special"), true);

        generateAndValidateLength(formElements, 10);

        assertEquals(true, formElements.get("output").getAttribute("value").matches("[^0-9]{10}"));
    }

    @Test
    public void testspecial() throws Exception {
        Map<String, WebElement> formElements = getPWGenFormElements();
        setCheckboxValue(formElements.get("repetition"), false);

        setCheckboxValue(formElements.get("lowercase"), false);
        setCheckboxValue(formElements.get("uppercase"), false);
        setCheckboxValue(formElements.get("digits"), false);
        setCheckboxValue(formElements.get("special"), true);


        generateAndValidateLength(formElements, 10);

        assertEquals(true, formElements.get("output").getAttribute("value").matches("[!@#$%\\-_]{10}"));

        setCheckboxValue(formElements.get("lowercase"), true);
        setCheckboxValue(formElements.get("uppercase"), true);
        setCheckboxValue(formElements.get("digits"), true);
        setCheckboxValue(formElements.get("special"), false);

        generateAndValidateLength(formElements, 10);

        assertEquals(true, formElements.get("output").getAttribute("value").matches("[^!@#$%\\-_]{10}"));
    }

    private void login() throws Exception {
        driver.get(url);
        driver.findElement(By.className("glyphicon-log-in")).click();
        driver.findElement(By.id("username")).sendKeys("jo.thomas@acme.com");
        driver.findElement(By.id("password")).sendKeys("mustang");
        driver.findElement(By.className("btn-primary")).click();
    }

    private void setCheckboxValue(WebElement checkbox, boolean checked) {
        if (checked != checkbox.isSelected())
        {
            checkbox.click();
        }
    }

    private void openGeneratePasswordModal() {
        WebElement element = driver.findElement(By.xpath("//a[contains(text(),'ACMEPass')]"));

        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", element);

        WebElement createButton = (new WebDriverWait(driver, 10, 500))
                .until(ExpectedConditions.elementToBeClickable(
                        By.cssSelector("button[ui-sref=\"acme-pass.new\"]"))
                );
        createButton.click();

        WebElement generateButton = (new WebDriverWait(driver, 10, 500))
                .until(ExpectedConditions.elementToBeClickable(
                        By.cssSelector("button[ng-click=\"vm.openPwdGenModal()\"]"))
                );
        generateButton.click();
    }

    private Map<String, WebElement> getPWGenFormElements() {
        Map<String, WebElement> formelems = new HashMap<>(8);

        formelems.put("lowercase", driver.findElement(By.cssSelector("input#field_lower[type=\"checkbox\"]")));
        formelems.put("uppercase", driver.findElement(By.cssSelector("input#field_upper[type=\"checkbox\"]")));
        formelems.put("digits", driver.findElement(By.cssSelector("input#field_digits[type=\"checkbox\"]")));
        formelems.put("special", driver.findElement(By.cssSelector("input#field_special[type=\"checkbox\"]")));
        formelems.put("repetition", driver.findElement(By.cssSelector("input#field_repetition[type=\"checkbox\"]")));
        formelems.put("length", driver.findElement(By.cssSelector("input#field_length[type=\"number\"]")));
        formelems.put("generate", driver.findElement(By.cssSelector("button[ng-click=\"vm.generate()\"]")));
        formelems.put("output", driver.findElement(By.cssSelector("input#field_password[type=\"text\"]")));

        return formelems;
    }


}
