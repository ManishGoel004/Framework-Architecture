package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class InventoryPage extends BasePage {

    private By pageTitle =
            By.className("title");

    private By backpackAddToCartButton =
            By.id("add-to-cart-sauce-labs-backpack");

    private By cartLink =
            By.className("shopping_cart_link");

    public InventoryPage(WebDriver driver) {

        super(driver);

        waitForVisibility(pageTitle);
    }

    public String getPageTitle() {
        return getText(pageTitle);
    }

    public String getCurrentUrl() {

        return driver.getCurrentUrl();
    }

    public void addBackpackToCart() {
        click(backpackAddToCartButton);
    }

    public CartPage openCart() {

        click(cartLink);

        return new CartPage(driver);
    }
}