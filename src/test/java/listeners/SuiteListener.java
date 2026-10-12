package listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import utils.AllureEnvironmentUtil;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class SuiteListener
        implements ISuiteListener {

    private static final Logger logger =
            LoggerFactory.getLogger(
                    SuiteListener.class
            );

    @Override
    public void onStart(
            ISuite suite
    ) {

        logger.info(
                "SUITE STARTED: {}",
                suite.getName()
        );

        String browsers =
                suite.getXmlSuite()
                        .getTests()
                        .stream()
                        .map(
                                xmlTest ->
                                        xmlTest.getParameter(
                                                "browser"
                                        )
                        )
                        .filter(
                                Objects::nonNull
                        )
                        .filter(
                                browser ->
                                        !browser.isBlank()
                        )
                        .distinct()
                        .collect(
                                Collectors.joining(
                                        ", "
                                )
                        );

        if (browsers.isBlank()) {

            browsers =
                    "not specified";
        }

        AllureEnvironmentUtil
                .writeEnvironmentInfo(
                        browsers
                );
    }

    @Override
    public void onFinish(
            ISuite suite
    ) {

        int totalPassed = 0;
        int totalFailed = 0;
        int totalSkipped = 0;

        Map<String, ISuiteResult> results =
                suite.getResults();

        for (ISuiteResult suiteResult :
                results.values()) {

            ITestContext context =
                    suiteResult
                            .getTestContext();

            int passed =
                    context
                            .getPassedTests()
                            .size();

            int failed =
                    context
                            .getFailedTests()
                            .size();

            int skipped =
                    context
                            .getSkippedTests()
                            .size();

            logger.info(
                    "Test block '{}' | Passed={} | Failed={} | Skipped={}",
                    context.getName(),
                    passed,
                    failed,
                    skipped
            );

            totalPassed += passed;
            totalFailed += failed;
            totalSkipped += skipped;
        }

        logger.info(
                "SUITE FINISHED: {} | Total Passed={} | Total Failed={} | Total Skipped={}",
                suite.getName(),
                totalPassed,
                totalFailed,
                totalSkipped
        );
    }
}