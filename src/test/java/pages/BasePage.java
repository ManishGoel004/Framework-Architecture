package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ConfigReader;

import java.time.Duration;

public class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver driver) {

        this.driver = driver;

        this.wait =
                new WebDriverWait(
                        driver,
                        Duration.ofSeconds(
                                ConfigReader.getInt(
                                        "explicitWait"
                                )
                        )
                );
    }

    protected void click(By locator) {

        wait.until(
                ExpectedConditions.elementToBeClickable(locator)
        ).click();
    }

    protected void type(
            By locator,
            String text
    ) {

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(locator)
        ).sendKeys(text);
    }

    protected String getText(By locator) {

        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(locator)
        ).getText();
    }

    protected void waitForVisibility(By locator) {

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(locator)
        );
    }

    protected void waitForUrlContains(
            String partialUrl
    ) {

        wait.until(
                ExpectedConditions.urlContains(
                        partialUrl
                )
        );
    }

    protected void waitForText(
            By locator,
            String expectedText
    ) {

        wait.until(
                ExpectedConditions.textToBe(
                        locator,
                        expectedText
                )
        );
    }
}