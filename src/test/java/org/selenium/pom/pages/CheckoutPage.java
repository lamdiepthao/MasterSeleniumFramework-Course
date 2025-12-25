package org.selenium.pom.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.selenium.pom.objects.BillingAddress;
import org.selenium.pom.base.BasePage;

import java.time.Duration;
import java.util.List;

public class CheckoutPage extends BasePage {
    private final By firstnameFld = By.cssSelector("#billing_first_name");
    private final By lastnameFld = By.cssSelector("#billing_last_name");
    private final By addressLineOneFld = By.cssSelector("#billing_address_1");
    private final By billingCityFld = By.cssSelector("#billing_city");
    private final By getBillingPostCodeFld = By.cssSelector("#billing_postcode");
    private final By billingEmailFld = By.cssSelector("#billing_email");
    private final By placeOrderBtn = By.cssSelector("#place_order");
    private final By successNotice = By.cssSelector(".woocommerce-notice");

    //Missing 4 private final (ShowLogin)
    private final By overlay = By.cssSelector(".blockUI blockOverlay");

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    public CheckoutPage enterFirstName(String firstname){
        driver.findElement(firstnameFld).sendKeys(firstname);
        return this;
    }
    public CheckoutPage enterLastName(String lastname){
        driver.findElement(lastnameFld).sendKeys(lastname);
        return this;
    }

    public CheckoutPage enterAddressLineOne (String addressLineOne){
        driver.findElement(addressLineOneFld).sendKeys(addressLineOne);
        return this;
    }
    public CheckoutPage enterCity (String billingCity){
        driver.findElement(billingCityFld).sendKeys(billingCity);
        return this;
    }

    public CheckoutPage enterPostCode (String getBillingPostCode){
        driver.findElement(getBillingPostCodeFld).sendKeys(getBillingPostCode);
        return this;
    }
    public CheckoutPage enterEmail (String billingEmail){
        driver.findElement(billingEmailFld).sendKeys(billingEmail);
        return this;
    }
    public CheckoutPage placeOrder (){
        List<WebElement> overlays =  driver.findElements(overlay);
        System.out.println("OVERLAY SIZE: " + overlays.size());
        if(overlays.size() > 0){
            new WebDriverWait(driver, Duration.ofSeconds(15)).until(
                    ExpectedConditions.invisibilityOfAllElements()
            );

            System.out.println("OVERLAYS ARE INVISIBLE: ");
        }
        driver.findElement(placeOrderBtn).click();
        return new CheckoutPage(driver);
        //return this;
    }

    public CheckoutPage setBillingAddress(BillingAddress billingAddress){
        return enterFirstName(billingAddress.getFirstName()).
                enterLastName(billingAddress.getLastName()).
                enterAddressLineOne(billingAddress.getAddressLineOne()).
                enterCity(billingAddress.getCity()).
                enterPostCode(billingAddress.getPostCode()).
                enterEmail(billingAddress.getEmail());
    }

    public String getNotice (){
       return driver.findElement(successNotice).getText();
    }


}
