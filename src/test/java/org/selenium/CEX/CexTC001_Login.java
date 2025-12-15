package org.selenium.CEX;

import org.openqa.selenium.By;
import org.selenium.pom.base.BaseTest;
import org.testng.annotations.Test;


public class CexTC001_Login extends BaseTest {

    @Test
    public void logincex() throws InterruptedException {
        driver.get("https://merchant-bo-sit.mqbc21.com/backofficeV2/login");
        Thread.sleep(3000);
       driver.findElement(By.xpath("//input[@id='loginId']")).sendKeys("tannieaq");
       driver.findElement(By.xpath("//input[@id='password']")).sendKeys("Xhkn@1707");
       driver.findElement(By.xpath("//button[@type='submit']")).click();

    }
}
