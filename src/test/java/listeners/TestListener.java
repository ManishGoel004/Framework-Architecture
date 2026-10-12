package listeners;

import base.DriverProvider;
import io.qameta.allure.Allure;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestListener;
import org.testng.ITestResult;
import retry.RetryAnalyzer;
import utils.ScreenshotUtil;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class TestListener
        implements ITestListener {

    private static final Logger logger =
            LoggerFactory.getLogger(
                    TestListener.class
            );

    @Override
    public void onTestStart(
            ITestResult result
    ) {

        logger.info(
                "TEST STARTED: {}",
                result.getName()
        );
    }

    @Override
    public void onTestSuccess(
            ITestResult result
    ) {

        RetryAnalyzer.clearRetryState(
                result
        );

        logger.info(
                "TEST PASSED: {}",
                result.getName()
        );
    }

    @Override
    public void onTestFailure(
            ITestResult result
    ) {

        RetryAnalyzer.clearRetryState(
                result
        );

        logger.error(
                "TEST FAILED: {}",
                result.getName(),
                result.getThrowable()
        );

        Object testInstance =
                result.getInstance();

        if (!(testInstance
                instanceof DriverProvider)) {

            logger.warn(
                    "Test instance does not implement DriverProvider: {}",
                    result.getName()
            );

            return;
        }

        DriverProvider driverProvider =
                (DriverProvider) testInstance;

        WebDriver driver =
                driverProvider.getDriver();

        if (driver == null) {

            logger.warn(
                    "No WebDriver available for failed test: {}",
                    result.getName()
            );

            return;
        }

        try {

            Path screenshotPath =
                    ScreenshotUtil
                            .captureScreenshot(
                                    driver,
                                    result.getName()
                                            + "_failure"
                            );

            logger.info(
                    "Failure screenshot saved: {}",
                    screenshotPath
                            .toAbsolutePath()
            );

            try (InputStream inputStream =
                         Files.newInputStream(
                                 screenshotPath
                         )) {

                Allure.attachment(
                        "Failure Screenshot.png",
                        inputStream
                );
            }

            logger.info(
                    "Failure screenshot attached to Allure"
            );

        } catch (Exception e) {

            logger.error(
                    "Could not capture or attach screenshot for test: {}",
                    result.getName(),
                    e
            );
        }
    }

    @Override
    public void onTestSkipped(
            ITestResult result
    ) {

        logger.warn(
                "TEST SKIPPED: {}",
                result.getName()
        );
    }
}