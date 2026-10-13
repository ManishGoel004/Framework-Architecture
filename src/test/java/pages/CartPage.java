package pages;

import constants.TestConstants;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CartPage extends BasePage {

    private By pageTitle =
            By.className("title");

    private By inventoryItemName =
            By.className("inventory_item_name");

    public CartPage(WebDriver driver) {

        super(driver);

        waitForUrlContains(
                TestConstants.CART_PATH
        );

        waitForText(
                pageTitle,
                TestConstants.CART_PAGE_TITLE
        );
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