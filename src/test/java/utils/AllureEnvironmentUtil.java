package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class AllureEnvironmentUtil {

    private static final Logger logger =
            LoggerFactory.getLogger(
                    AllureEnvironmentUtil.class
            );

    private AllureEnvironmentUtil() {
    }

    public static void writeEnvironmentInfo(
            String browsers
    ) {

        Path resultsDirectory =
                Paths.get(
                        "target",
                        "allure-results"
                );

        Path environmentFile =
                resultsDirectory.resolve(
                        "environment.properties"
                );

        Properties properties =
                new Properties();

        properties.setProperty(
                "Environment",
                System.getProperty(
                        "env",
                        "qa"
                )
        );

        properties.setProperty(
                "Browsers",
                browsers
        );

        properties.setProperty(
                "Base URL",
                ConfigReader.get(
                        "baseUrl"
                )
        );

        properties.setProperty(
                "Java Version",
                System.getProperty(
                        "java.version"
                )
        );

        properties.setProperty(
                "Headless",
                System.getProperty(
                        "headless",
                        "false"
                )
        );

        properties.setProperty(
                "Headless",
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

        properties.setProperty(
                "Execution Mode",
                remote
                        ? "Remote / Selenium Grid"
                        : "Local"
        );

        if (remote) {

            properties.setProperty(
                    "Grid URL",
                    System.getProperty(
                            "gridUrl",
                            "http://localhost:4444"
                    )
            );
        }

        try {

            Files.createDirectories(
                    resultsDirectory
            );

            try (OutputStream output =
                         Files.newOutputStream(
                                 environmentFile
                         )) {

                properties.store(
                        output,
                        "Allure Environment Information"
                );
            }

            logger.info(
                    "Allure environment information created | browsers={}",
                    browsers
            );

        } catch (IOException e) {

            logger.error(
                    "Failed to create Allure environment information",
                    e
            );
        }
    }
}