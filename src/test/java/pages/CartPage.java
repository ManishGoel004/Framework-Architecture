package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CartPage extends BasePage {

    private By pageTitle =
            By.className("title");

    private By inventoryItemName =
            By.className("inventory_item_name");

    public CartPage(WebDriver driver) {

        super(driver);

        waitForVisibility(pageTitle);
    }

    public String getPageTitle() {
        return getText(pageTitle);
    }

    public String getItemName() {
        return getText(inventoryItemName);
    }

    public String getCurrentUrl() {

        return driver.getCurrentUrl();
    }
}