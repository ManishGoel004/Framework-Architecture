package utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class ScreenshotUtil {

    private static final Logger logger =
            LoggerFactory.getLogger(
                    ScreenshotUtil.class
            );

    public static Path captureScreenshot(
            WebDriver driver,
            String baseName
    ) throws IOException {

        TakesScreenshot screenshot =
                (TakesScreenshot) driver;

        File sourceFile =
                screenshot.getScreenshotAs(
                        OutputType.FILE
                );

        return saveScreenshot(
                sourceFile,
                baseName
        );
    }

    public static Path captureElementScreenshot(
            WebElement element,
            String baseName
    ) throws IOException {

        File sourceFile =
                element.getScreenshotAs(
                        OutputType.FILE
                );

        return saveScreenshot(
                sourceFile,
                baseName
        );
    }

    private static Path saveScreenshot(
            File sourceFile,
            String baseName
    ) throws IOException {

        LocalDateTime now =
                LocalDateTime.now();

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(
                        "yyyyMMdd_HHmmss_SSS"
                );

        String timestamp =
                now.format(formatter);

        String uniqueId =
                UUID.randomUUID()
                        .toString()
                        .substring(0, 8);

        String fileName =
                baseName
                        + "_"
                        + timestamp
                        + "_"
                        + uniqueId
                        + ".png";

        Path destination =
                Paths.get(
                        "screenshots",
                        fileName
                );

        Files.createDirectories(
                destination.getParent()
        );

        Files.copy(
                sourceFile.toPath(),
                destination,
                StandardCopyOption.REPLACE_EXISTING
        );

        if (!Files.exists(destination)) {

            throw new RuntimeException(
                    "Screenshot was not created: "
                            + destination.toAbsolutePath()
            );
        }

        if (!Files.isRegularFile(destination)) {

            throw new RuntimeException(
                    "Screenshot path is not a regular file: "
                            + destination.toAbsolutePath()
            );
        }

        if (Files.size(destination) == 0) {

            throw new RuntimeException(
                    "Screenshot file is empty: "
                            + destination.toAbsolutePath()
            );
        }

        logger.debug(
                "Screenshot physically saved at: {}",
                destination.toAbsolutePath()
        );

        return destination;
    }
}