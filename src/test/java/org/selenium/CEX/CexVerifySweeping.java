package org.selenium.CEX;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.jboss.aerogear.security.otp.Totp;
import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebElement;

public class CexVerifySweeping {

    private WebDriver driver;
    // Declaring WebDriverWait at the class level
    private WebDriverWait wait;

    // Replace secret key of user account
    private static final String SECRET_KEY = "HZZABH7ASYFPJYGJI454CDVS";
    private static final String USERNAME = "tanniempg";
    private static final String PASSWORD = "Test@123";
    private static final String LOGIN_URL = "https://merchant-bo-sit.mqbc21.com/backofficeV2/login";

    private String get2FACode() {
        Totp totp = new Totp(SECRET_KEY);
        return totp.now();
    }

    @BeforeEach
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        // Initialize WebDriverWait with a higher timeout to ensure stability
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    @Test
    public void testSuccessfulLoginWith2FA() throws InterruptedException {
        // ... (LOGIN STEP) ...

        driver.get(LOGIN_URL);
        Thread.sleep(3000);
        driver.findElement(By.xpath("//input[@id='loginId']")).sendKeys(USERNAME);
        driver.findElement(By.xpath("//input[@id='password']")).sendKeys(PASSWORD);
        driver.findElement(By.xpath("//button[@type='submit']")).click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//input[@type='text'])[2]")));
        String twoFactorCode = get2FACode();
        driver.findElement(By.xpath("(//input[@type='text'])[2]")).sendKeys(twoFactorCode);
        Thread.sleep(5000);
        System.out.println("Login success");

        // =========================================================================
        // DECLARE FIXED INPUT DATA (Expected Values)
        // =========================================================================
        String inputAmount = "1";

        // Expected values based on the selection/system
        String expectedNetwork = "ETHSEP";
        String expectedCrypto = "ETHSEP";
        String expectedWalletType = "HD";
        String expectedColdWalletName = "TestETH"; // Used for input and logging

        // =========================================================================
        // CREATE SWEEPING FLOW
        // =========================================================================
        Thread.sleep(1000);
        driver.findElement(By.xpath("//span[normalize-space()='Merchant Wallet']")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("//a[normalize-space()='Sweeping']")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("//span[normalize-space()='Create Sweeping']")).click();
        Thread.sleep(1000);

        // --- INPUT DATA ---
        driver.findElement(By.xpath("//span[normalize-space()='Find Address']")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("//input[@id='networkCode']")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("//div[contains(text(),'ETHSEP (Test)')]")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("//input[@id='assetCode']")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("//div[@class='ant-select-item-option-content'][normalize-space()='" + expectedCrypto + "']")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("//input[@id='walletType']")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("//div[contains(text(),'" + expectedWalletType + "')]")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("//input[@id='coldWallet']")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("//div[contains(text(),'" + expectedColdWalletName + "')]")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("//input[@id='limit']")).sendKeys(inputAmount);
        Thread.sleep(1000);

        // --- SUBMIT AND CONFIRM ---
        driver.findElement(By.xpath("//div[@class='ant-drawer-extra']//span[contains(text(),'Find Address')]")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("//span[normalize-space()='Confirm to Sweep']")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("//div[@class='ant-modal-footer']//button[1]")).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//input[@type='text'])[1]")));
        driver.findElement(By.xpath("(//input[@type='text'])[1]")).sendKeys(twoFactorCode);

        // DELAY: Wait for the system to process
        Thread.sleep(2000);
        System.out.println("Sweeping command sent. Proceeding to verification...");

        // =========================================================================
        // IMPORTANT STEP: CLICK SUBMIT TO REFRESH LIST
        // =========================================================================
        driver.findElement(By.xpath("//button[normalize-space()='Submit']")).click();
        Thread.sleep(1000);

        // =========================================================================
        // START TABLE DATA VERIFICATION
        // =========================================================================

        // Stable XPath of the first row (tr[1])
        String xpathForTargetRowBase = "(//tr[@class='ant-table-row ant-table-row-level-0'])[1]";

        // XPath to directly target the DIV containing the Network Code text inside td[3]
        // This is the most reliable wait condition
        String xpathForNetworkTextDiv = xpathForTargetRowBase + "//td[3]//div[text()='" + expectedNetwork + "']";

        WebElement targetRow = null;
        WebElement networkTextDiv = null;
        try {
            // 1. WAIT FOR THE DIV CONTAINING NETWORK CODE TEXT TO BE VISIBLE
            networkTextDiv = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathForNetworkTextDiv))
            );

            // 2. Get the parent row (tr) from the found text div
            // Traverse up to the nearest ancestor tr
            targetRow = networkTextDiv.findElement(By.xpath("./ancestor::tr[1]"));

            System.out.println("Found the latest transaction row and confirmed Network Code.");

        } catch (Exception e) {
            System.err.println("Selenium Error: " + e.getMessage());
            // Assert failure if the row isn't found within the timeout
            Assertions.fail("Error: Could not find Network Code '" + expectedNetwork + "' in the first row after 30 seconds. Check XPath or data load time.");
        }

        // --- FETCH DATA AND ASSERTIONS ---

        // 1. Fetch Sweeping ID (td[2] - Log only)
        String actualSweepingId = targetRow.findElement(By.xpath("./td[2]")).getText().trim();

        // 2. Assert Network Code (td[3]) - Use the element found during wait
        String actualNetwork = networkTextDiv.getText().trim();
        Assertions.assertEquals(expectedNetwork, actualNetwork,
                "Error: Network Code does not match.");

        // 3. Assert Crypto/Asset Code (td[4])
        String actualCrypto = targetRow.findElement(By.xpath("./td[4]")).getText().trim();
        Assertions.assertEquals(expectedCrypto, actualCrypto,
                "Error: Asset Code (Crypto) does not match.");

        // 4. Assert Wallet Type (td[7])
        String actualWalletType = targetRow.findElement(By.xpath("./td[7]")).getText().trim();
        Assertions.assertEquals(expectedWalletType, actualWalletType,
                "Error: Wallet Type does not match.");

        // 5. Assert Name (td[6]) - Verify it is empty as observed
        String actualName = targetRow.findElement(By.xpath("./td[6]")).getText().trim();
        Assertions.assertEquals("", actualName,
                "Error: Name column (td[6]) is not empty as expected after creation.");

        // 6. Log Cold Wallet Address (td[8])
        String actualColdWalletAddress = targetRow.findElement(By.xpath("./td[8]")).getText().trim();


        System.out.println("--- VERIFICATION RESULTS ---");
        System.out.println("Sweeping ID: " + actualSweepingId + " (Fetched from table)");
        System.out.println("Network Code: " + actualNetwork + " - VERIFIED");
        System.out.println("Crypto/Asset Code: " + actualCrypto + " - VERIFIED");
        System.out.println("Wallet Type: " + actualWalletType + " - VERIFIED");
        System.out.println("Cold Wallet Address (Log): " + actualColdWalletAddress);
        System.out.println("Verification successful! Input data has been confirmed.");
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}