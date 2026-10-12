package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private static final Logger logger =
            LoggerFactory.getLogger(
                    ConfigReader.class
            );

    private static final Properties properties =
            new Properties();

    private static final String environment =
            System.getProperty("env", "qa");

    static {

        String fileName =
                "config-" +
                        environment +
                        ".properties";

        try (InputStream input =
                     ConfigReader.class
                             .getClassLoader()
                             .getResourceAsStream(
                                     fileName
                             )) {

            if (input == null) {

                logger.error(
                        "Configuration file not found: {}",
                        fileName
                );

                throw new RuntimeException(
                        "Configuration file not found: "
                                + fileName
                );
            }

            properties.load(input);

            logger.info(
                    "Loaded configuration: {}",
                    fileName
            );

        } catch (IOException e) {

            logger.error(
                    "Failed to load configuration file: {}",
                    fileName,
                    e
            );

            throw new RuntimeException(
                    "Failed to load configuration",
                    e
            );
        }
    }

    public static String get(String key) {

        String value =
                properties.getProperty(key);

        if (value == null) {

            logger.error(
                    "Configuration key not found: {}",
                    key
            );

            throw new IllegalArgumentException(
                    "Configuration key not found: "
                            + key
            );
        }

        return value;
    }

    public static int getInt(String key) {

        return Integer.parseInt(
                get(key)
        );
    }
}