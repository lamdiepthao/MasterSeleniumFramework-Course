package org.selenium.pom.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
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

    private final By countryDropDown = By.id("billing_country");
    private final By stateDropDown = By.id("billing_state");
    private final By directBankTransferRadioBtn = By.id("payment_method_bacs");


    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    public CheckoutPage enterFirstName(String firstname){
        WebElement e = wait.until(ExpectedConditions.visibilityOfElementLocated(firstnameFld));
        e.clear();
        e.sendKeys(firstname);
        return this;
    }
    public CheckoutPage enterLastName(String lastname){
        driver.findElement(lastnameFld).sendKeys(lastname);
        return this;
    }
    // handle select dropdown. easy maintain
    public CheckoutPage selectCountry(String countryName)
    {
        Select select = new Select(driver.findElement(countryDropDown));
        select.selectByVisibleText(countryName);
        return this;
    }
    public CheckoutPage selectState(String stateName)
    {
        Select select = new Select(driver.findElement(stateDropDown));
        select.selectByVisibleText(stateName);
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
        waitForOverlaysToDisappear(overlay);
        driver.findElement(placeOrderBtn).click();
        //return new CheckoutPage(driver);
        return this;
    }

    public CheckoutPage setBillingAddress(BillingAddress billingAddress){
        return enterFirstName(billingAddress.getFirstName()).
                enterLastName(billingAddress.getLastName()).
                selectCountry(billingAddress.getCountry()).
                enterAddressLineOne(billingAddress.getAddressLineOne()).
                enterCity(billingAddress.getCity()).
                selectState(billingAddress.getState()).
                enterPostCode(billingAddress.getPostCode()).
                enterEmail(billingAddress.getEmail());
    }

    public String getNotice (){
       return wait.until(ExpectedConditions.visibilityOfElementLocated(successNotice)).getText();
        //return driver.findElement(successNotice).getText();
    }
    //Handle default radio button
    public CheckoutPage selectDirectTransferBank(){
        WebElement e = wait.until(ExpectedConditions.elementToBeClickable(directBankTransferRadioBtn));
        if(e.isSelected()){
            e.click();}
        return this;

    }
}
