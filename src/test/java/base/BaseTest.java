package base;

import drivers.DriverFactory;
import drivers.DriverManager;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import utils.ConfigReader;

public class BaseTest
        implements DriverProvider {

    private static final Logger logger =
            LoggerFactory.getLogger(
                    BaseTest.class
            );

    @BeforeMethod
    @Parameters("browser")
    public void setUp(
            @Optional("chrome") String xmlBrowser
    ) {

        String browser =
                System.getProperty(
                        "browser",
                        xmlBrowser
                );

        String environment =
                System.getProperty(
                        "env",
                        "qa"
                );

        boolean headless =
                Boolean.parseBoolean(
                        System.getProperty(
                                "headless",
                                "false"
                        )
                );

        boolean remote =
                Boolean.parseBoolean(
                        System.getProperty(
                                "remote",
                                "false"
                        )
                );

        String gridUrl =
                System.getProperty(
                        "gridUrl",
                        "http://localhost:4444"
                );

        logger.info(
                "Starting test setup | environment={} | browser={} | headless={} | remote={}",
                environment,
                browser,
                headless,
                remote
        );

        if (remote) {

            logger.info(
                    "Remote execution requested | gridUrl={}",
                    gridUrl
            );
        }

        WebDriver newDriver =
                DriverFactory.createDriver(
                        browser,
                        headless,
                        remote,
                        gridUrl
                );

        DriverManager.setDriver(
                newDriver
        );

        getDriver()
                .manage()
                .window()
                .setSize(
                        new Dimension(
                                1920,
                                1080
                        )
                );

        String baseUrl =
                ConfigReader.get(
                        "baseUrl"
                );

        logger.info(
                "Opening application: {}",
                baseUrl
        );

        getDriver().get(
                baseUrl
        );
    }

    @Override
    public WebDriver getDriver() {

        return DriverManager.getDriver();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {

        if (getDriver() != null) {

            logger.info(
                    "Closing browser"
            );

            DriverManager.quitDriver();

        } else {

            logger.warn(
                    "No WebDriver associated with current thread"
            );
        }
    }
}