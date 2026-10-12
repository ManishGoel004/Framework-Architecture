package drivers;

import org.openqa.selenium.WebDriver;

public final class DriverManager {

    private static final ThreadLocal<WebDriver>
            driverThreadLocal =
            new ThreadLocal<>();

    private DriverManager() {
    }

    public static void setDriver(
            WebDriver driver
    ) {

        driverThreadLocal.set(driver);
    }

    public static WebDriver getDriver() {

        return driverThreadLocal.get();
    }

    public static void quitDriver() {

        WebDriver driver =
                driverThreadLocal.get();

        try {

            if (driver != null) {

                driver.quit();
            }

        } finally {

            driverThreadLocal.remove();
        }
    }
}