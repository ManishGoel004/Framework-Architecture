package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    private By usernameInput =
            By.id("user-name");

    private By passwordInput =
            By.id("password");

    private By loginButton =
            By.id("login-button");

    private By errorMessage =
            By.cssSelector("[data-test='error']");

    public LoginPage(WebDriver driver) {

        super(driver);

        waitForVisibility(usernameInput);
    }

    public void enterUsername(String username) {
        type(usernameInput, username);
    }

    public void enterPassword(String password) {
        type(passwordInput, password);
    }

    public void clickLogin() {
        click(loginButton);
    }

    public InventoryPage login(
            String username,
            String password
    ) {

        enterUsername(username);
        enterPassword(password);
        clickLogin();

        return new InventoryPage(driver);
    }

    public String getErrorMessage() {
        return getText(errorMessage);
    }
}