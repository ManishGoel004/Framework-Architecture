package drivers;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class DriverFactory {

    private static final Logger logger =
            LoggerFactory.getLogger(
                    DriverFactory.class
            );

    public static WebDriver createDriver(
            String browser,
            boolean headless,
            boolean remote,
            String gridUrl
    ) {

        logger.info(
                "Creating WebDriver | browser={} | headless={} | remote={}",
                browser,
                headless,
                remote
        );

        if (browser.equalsIgnoreCase("chrome")) {

            ChromeOptions options =
                    new ChromeOptions();

            if (headless) {
                options.addArguments(
                        "--headless=new"
                );
            }

            if (remote) {
                return createRemoteDriver(
                        gridUrl,
                        options
                );
            }

            return new ChromeDriver(options);

        } else if (
                browser.equalsIgnoreCase("firefox")
        ) {

            FirefoxOptions options =
                    new FirefoxOptions();

            if (headless) {
                options.addArguments(
                        "-headless"
                );
            }

            if (remote) {
                return createRemoteDriver(
                        gridUrl,
                        options
                );
            }

            return new FirefoxDriver(options);

        } else if (
                browser.equalsIgnoreCase("edge")
        ) {

            EdgeOptions options =
                    new EdgeOptions();

            if (headless) {
                options.addArguments(
                        "--headless=new"
                );
            }

            if (remote) {
                return createRemoteDriver(
                        gridUrl,
                        options
                );
            }

            return new EdgeDriver(options);
        }

        logger.error(
                "Unsupported browser requested: {}",
                browser
        );

        throw new IllegalArgumentException(
                "Unsupported browser: "
                        + browser
        );
    }

    private static WebDriver createRemoteDriver(
            String gridUrl,
            Capabilities capabilities
    ) {

        try {

            URL remoteUrl =
                    URI.create(
                            gridUrl
                    ).toURL();

            logger.info(
                    "Connecting to Selenium Grid: {}",
                    remoteUrl
            );

            return new RemoteWebDriver(
                    remoteUrl,
                    capabilities
            );

        } catch (MalformedURLException e) {

            logger.error(
                    "Invalid Selenium Grid URL: {}",
                    gridUrl,
                    e
            );

            throw new IllegalArgumentException(
                    "Invalid Selenium Grid URL: "
                            + gridUrl,
                    e
            );
        }
    }
}