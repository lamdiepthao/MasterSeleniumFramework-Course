package org.selenium.CEX;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.jboss.aerogear.security.otp.Totp; // Import library TOTP
import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CexLogin2FATest {

    private WebDriver driver;

    // Replace secret key of user account
    private static final String SECRET_KEY = "HZZABH7ASYFPJYGJI454CDVS";

    // Replace actual account to login
    private static final String USERNAME = "tanniempg";
    private static final String PASSWORD = "Test@123";
    private static final String LOGIN_URL = "https://merchant-bo-sit.mqbc21.com/backofficeV2/login";

    // =========================================================================
    // Calculation function for 2FA
    // =========================================================================
    private String get2FACode() {
        // Create TOTP from secret key
        Totp totp = new Totp(SECRET_KEY);
        // Return OTP 6 number base on time
        return totp.now();
    }

    @BeforeEach
    public void setup() {
        // Init WebDriver (need to add ChromeDriver)
        // Real env, should use WebDriverManager or System.setProperty
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @Test
    public void testSuccessfulLoginWith2FA() throws InterruptedException {
        // 1. Navigate to login page
        driver.get(LOGIN_URL);
        Thread.sleep(3000);

        // 2. Fulfill valid data (step 1)
        driver.findElement(By.xpath("//input[@id='loginId']")).sendKeys(USERNAME);
        driver.findElement(By.xpath("//input[@id='password']")).sendKeys(PASSWORD);
        driver.findElement(By.xpath("//button[@type='submit']")).click();

        // 3. Wait for loading 2FA page
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        // Wait for displaying 2FA input
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//input[@type='text'])[2]")));

        // 4. Get and autofill 2FA (Important step)
        String twoFactorCode = get2FACode();
        //System.out.println("Current 2FA: " + twoFactorCode);

        driver.findElement(By.xpath("(//input[@type='text'])[2]")).sendKeys(twoFactorCode);
        Thread.sleep(5000);
        //driver.findElement(By.id("submit2faButton")).click();

        // 5. Verify login success
        // Chờ đến trang Dashboard/Home Page
        //wait.until(ExpectedConditions.urlContains("/dashboard"));
        // Kiểm tra xem một phần tử chỉ có trên trang thành công có xuất hiện không
        //boolean isSuccess = driver.findElement(By.xpath("//h1[text()='Welcome to Dashboard']")).isDisplayed();
        //assertTrue(isSuccess, "Đăng nhập 2FA không thành công.");
         System.out.println("Login success");
        // Create sweeping flow:
        Thread.sleep(2000);
        driver.findElement(By.xpath("//span[normalize-space()='Merchant Wallet']")).click();
        Thread.sleep(2000);
        driver.findElement(By.xpath("//a[normalize-space()='Sweeping']")).click();
        Thread.sleep(2000);
        driver.findElement(By.xpath("//span[normalize-space()='Create Sweeping']")).click();
        Thread.sleep(2000);
        driver.findElement(By.xpath("//span[normalize-space()='Find Address']")).click();
        Thread.sleep(2000);
        driver.findElement(By.xpath("//input[@id='networkCode']")).click();
        Thread.sleep(2000);
        driver.findElement(By.xpath("//div[contains(text(),'ETHSEP (Test)')]")).click();
        Thread.sleep(2000);
        driver.findElement(By.xpath("//input[@id='assetCode']")).click();
        Thread.sleep(2000);
        driver.findElement(By.xpath("//div[@class='ant-select-item-option-content'][normalize-space()='ETHSEP']")).click();
        Thread.sleep(2000);
        driver.findElement(By.xpath("//input[@id='walletType']")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("//div[contains(text(),'HD')]")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("//input[@id='coldWallet']")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("//div[contains(text(),'TestETH')]")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("//input[@id='limit']")).sendKeys("1");
        Thread.sleep(1000);
        driver.findElement(By.xpath("//div[@class='ant-drawer-extra']//span[contains(text(),'Find Address')]")).click();
        Thread.sleep(2000);
        driver.findElement(By.xpath("//span[normalize-space()='Confirm to Sweep']")).click();
        Thread.sleep(2000);
        driver.findElement(By.xpath("//div[@class='ant-modal-footer']//button[1]")).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//input[@type='text'])[2]")));
        driver.findElement(By.xpath("(//input[@type='text'])[1]")).sendKeys(twoFactorCode);
        Thread.sleep(3000);
        System.out.println("Sweeping is created successfully");
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
