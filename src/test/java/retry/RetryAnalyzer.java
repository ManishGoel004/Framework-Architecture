package retry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class RetryAnalyzer
        implements IRetryAnalyzer {

    private static final Logger logger =
            LoggerFactory.getLogger(
                    RetryAnalyzer.class
            );

    private static final int MAX_RETRY_COUNT =
            2;

    private static final ConcurrentHashMap<
            String,
            AtomicInteger
            > retryCounts =
            new ConcurrentHashMap<>();

    @Override
    public boolean retry(
            ITestResult result
    ) {

        String key =
                buildInvocationKey(result);

        AtomicInteger retryCount =
                retryCounts.computeIfAbsent(
                        key,
                        ignored ->
                                new AtomicInteger(0)
                );

        int retryNumber =
                retryCount.incrementAndGet();

        if (retryNumber <= MAX_RETRY_COUNT) {

            logger.warn(
                    "Retrying test '{}' | retry {}/{} | browser={}",
                    result.getName(),
                    retryNumber,
                    MAX_RETRY_COUNT,
                    getBrowser(result)
            );

            return true;
        }

        retryCounts.remove(key);

        logger.warn(
                "Retry limit reached for test '{}' | browser={}",
                result.getName(),
                getBrowser(result)
        );

        return false;
    }

    public static void clearRetryState(
            ITestResult result
    ) {

        retryCounts.remove(
                buildInvocationKey(result)
        );
    }

    private static String buildInvocationKey(
            ITestResult result
    ) {

        return result
                .getTestClass()
                .getName()
                + "#"
                + result
                .getMethod()
                .getMethodName()
                + "#"
                + getBrowser(result)
                + "#"
                + Arrays.deepToString(
                result.getParameters()
        );
    }

    private static String getBrowser(
            ITestResult result
    ) {

        String browser =
                result.getTestContext()
                        .getCurrentXmlTest()
                        .getParameter(
                                "browser"
                        );

        if (browser == null) {

            return "unknown";
        }

        return browser;
    }
}