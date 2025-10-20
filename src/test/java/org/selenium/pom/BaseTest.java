package org.selenium.pom;

import org.openqa.selenium.WebDriver;
import org.selenium.factory.DriverManager;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseTest {
    protected WebDriver driver;

    @BeforeMethod
    public void starDriver(){
        driver = new DriverManager().initializeDriver();
    }

    @AfterMethod
    public void quitDriver(){
        driver.quit();
    }
}