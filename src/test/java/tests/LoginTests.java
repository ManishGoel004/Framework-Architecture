package tests;

import base.BaseTest;
import constants.TestConstants;
import data.LoginTestData;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.InventoryPage;
import pages.LoginPage;
import utils.ConfigReader;

@Epic("SauceDemo Web Application")
@Feature("Authentication")
public class LoginTests extends BaseTest {

    @Test(
            testName = "Valid user can login successfully",
            groups = {
                    "smoke",
                    "regression"
            },
            dataProvider = "validLoginData",
            dataProviderClass = LoginTestData.class
    )
    @Story("Successful Login")
    @Description(
            "Verifies that a valid user can log in "
                    + "and reach the Products page."
    )
    public void validLoginTest(
            String username,
            String password
    ) {

        LoginPage loginPage =
                new LoginPage(getDriver());

        InventoryPage inventoryPage =
                Allure.step(
                        "Login with valid user",
                        () -> loginPage.login(
                                username,
                                password
                        )
                );

        String expectedUrl =
                ConfigReader.get("baseUrl")
                        + TestConstants.INVENTORY_PATH;

        Allure.step(
                "Verify inventory page URL",
                () -> {

                    Assert.assertEquals(
                            inventoryPage.getCurrentUrl(),
                            expectedUrl
                    );
                }
        );

        Allure.step(
                "Verify Products page title",
                () -> {

                    Assert.assertEquals(
                            inventoryPage.getPageTitle(),
                            TestConstants.PRODUCTS_PAGE_TITLE
                    );
                }
        );
    }

    @Test(
            testName = "Invalid user cannot login",
            groups = "regression",
            dataProvider = "invalidLoginData",
            dataProviderClass = LoginTestData.class
    )
    @Story("Invalid Login")
    @Description(
            "Verifies that invalid login credentials "
                    + "show an appropriate error message."
    )
    public void invalidLoginTest(
            String username,
            String password
    ) {

        LoginPage loginPage =
                new LoginPage(getDriver());

        Allure.step(
                "Enter login credentials",
                () -> {
                    loginPage.enterUsername(username);
                    loginPage.enterPassword(password);
                }
        );

        Allure.step(
                "Click login button",
                () -> loginPage.clickLogin()
        );

        Allure.step(
                "Verify login error message",
                () -> {

                    Assert.assertTrue(
                            loginPage.getErrorMessage()
                                    .contains(
                                            TestConstants.LOGIN_ERROR_FRAGMENT
                                    )
                    );
                }
        );
    }
}