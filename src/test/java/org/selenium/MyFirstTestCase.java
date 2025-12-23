package org.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.selenium.pom.Objects.BillingAddress;
import org.selenium.pom.base.BaseTest;
import org.selenium.pom.pages.CartPage;
import org.selenium.pom.pages.CheckoutPage;
import org.selenium.pom.pages.HomePage;
import org.selenium.pom.pages.StorePage;
import org.selenium.pom.utils.JacksonUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;

public class MyFirstTestCase extends BaseTest {

    @Test
    public void guestCheckoutUsingDirectBankTransfer() throws InterruptedException, IOException {
        BillingAddress billingAddress = new BillingAddress();
        InputStream is = getClass().getClassLoader().getResourceAsStream("myBillingAddress.json");
        billingAddress = JacksonUtils.deserializeJson(is, billingAddress);

        StorePage storePage = new HomePage(driver).
                load().
                navigateToStoreUsingMenu().
                search("Blue");
        Thread.sleep(5000);
        Assert.assertEquals(storePage.getTitle(), "Search results: “Blue”");

        storePage.clickAddToCartBtn("Blue Shoes");
        Thread.sleep(6000);
        CartPage cartPage = storePage.clickViewCart();
        Thread.sleep(5000);
        Assert.assertEquals(cartPage.getProductName(),"Blue Shoes");

        CheckoutPage checkoutPage = cartPage.
                clickCheckoutBtn().
                setBillingAddress(billingAddress);
        Thread.sleep(5000);
        driver.findElement(By.cssSelector("#place_order")).click();
        Thread.sleep(5000);
        Assert.assertEquals(checkoutPage.getNotice(), "Thank you. Your order has been received.");

    }

    @Test
    public void LoginAndCheckoutUsingDirectBankTransfer() throws InterruptedException {
        // System.setProperty("webdriver.chrome.driver", "path/to/chromedriver.exe"); ko can, do da setup home_variable
        WebDriver driver = new ChromeDriver();
        driver.get("https://askomdch.com");
        driver.manage().window().maximize();
        driver.findElement(By.cssSelector("#menu-item-1227 > a")).click();
        driver.findElement(By.id("woocommerce-product-search-field-0")).sendKeys("Blue");
        driver.findElement(By.cssSelector("button[value='Search']")).click();
        Assert.assertEquals(
                driver.findElement(By.cssSelector(".woocommerce-products-header__title.page-title")).getText(),
                "Search results: “Blue”"
        );
        driver.findElement(By.cssSelector("a[aria-label='Add “Blue Shoes” to your cart']")).click();
        Thread.sleep(5000);
        driver.findElement(By.cssSelector("a[title='View cart']")).click();
        Assert.assertEquals(
                driver.findElement(By.cssSelector("td[class='product-name'] a")).getText(),
                "Blue Shoes"
        );
        driver.findElement(By.cssSelector(".checkout-button")).click();
        driver.findElement(By.className("showlogin")).click();
        Thread.sleep(3000);
        driver.findElement(By.id("username")).sendKeys("thao2");
        driver.findElement(By.id("password")).sendKeys("thao123");
        driver.findElement(By.name("login")).click();
        driver.findElement(By.cssSelector("#billing_first_name")).sendKeys("Thao");
        driver.findElement(By.cssSelector("#billing_last_name")).sendKeys("Lam");
        driver.findElement(By.cssSelector("#billing_address_1")).sendKeys("123 A. B, C");
        driver.findElement(By.cssSelector("#billing_city")).sendKeys("HCM");
        driver.findElement(By.cssSelector("#billing_postcode")).sendKeys("12345");
        driver.findElement(By.cssSelector("#billing_email")).clear();
        driver.findElement(By.cssSelector("#billing_email")).sendKeys("thaothao@gmail.com");
        driver.findElement(By.cssSelector("#place_order")).click();
        Thread.sleep(6000);
        Assert.assertEquals(
                driver.findElement(By.cssSelector(".woocommerce-notice")).getText(),
                "Thank you. Your order has been received."
        );
    driver.quit();
    }
}