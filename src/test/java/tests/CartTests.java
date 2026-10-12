package tests;

import base.BaseTest;
import constants.TestConstants;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.InventoryPage;
import pages.LoginPage;

@Epic("SauceDemo Web Application")
@Feature("Shopping Cart")
public class CartTests extends BaseTest {

    @Test(
            testName = "Add Sauce Labs Backpack to cart"
    )
    @Story("Add Product To Cart")
    @Description(
            "Verifies that a logged-in user can add "
                    + "Sauce Labs Backpack and see it in the cart."
    )
    public void addBackpackToCartTest() {

        LoginPage loginPage =
                new LoginPage(getDriver());

        InventoryPage inventoryPage =
                Allure.step(
                        "Login as standard user",
                        () -> loginPage.login(
                                "standard_user",
                                "secret_sauce"
                        )
                );

        Allure.step(
                "Add Sauce Labs Backpack to cart",
                () ->
                        inventoryPage.addBackpackToCart()
        );

        CartPage cartPage =
                Allure.step(
                        "Open the shopping cart",
                        () ->
                                inventoryPage.openCart()
                );

        Allure.step(
                "Verify cart page title",
                () -> {

                    Assert.assertEquals(
                            cartPage.getPageTitle(),
                            TestConstants.CART_PAGE_TITLE
                    );
                }
        );

        Allure.step(
                "Verify Sauce Labs Backpack is present in cart",
                () -> {

                    Assert.assertEquals(
                            cartPage.getItemName(),
                            TestConstants.BACKPACK_NAME
                    );
                }
        );
    }
}