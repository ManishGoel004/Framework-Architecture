````

# Selenium Test Automation Framework

A Java-based UI automation framework built using Selenium WebDriver, TestNG, Maven, Allure, and GitHub Actions.

The project demonstrates a maintainable SDET-style automation architecture with Page Object Model, environment configuration, test data management, retry handling, logging, screenshots, parallel cross-browser execution, Selenium Grid support, reporting, and CI quality gates.

---

## Application Under Test

**The framework currently automates:**

**SauceDemo**

https://www.saucedemo.com/

SauceDemo is used as a public demo application for automation practice.

The application itself is not part of this repository and is not deployed by this project.

---

## Technology Stack

- Java 26
- Selenium WebDriver 4
- TestNG
- Maven
- Maven Surefire Plugin
- Allure Reporting
- SLF4J
- Logback
- GitHub Actions
- Selenium Grid
- Git

---

## Project Structure

```text
Framework-Architecture/
│
├── README.md
├── pom.xml
├── testng.xml
├── testng-crossbrowser.xml
├── .gitignore
│
├── .github/
│   └── workflows/
│       └── ui-tests.yml
│
└── src/
    └── test/
        ├── java/
        │   ├── base/
        │   │   ├── BaseTest.java
        │   │   └── DriverProvider.java
        │   │
        │   ├── drivers/
        │   │   ├── DriverFactory.java
        │   │   └── DriverManager.java
        │   │
        │   ├── pages/
        │   │   ├── BasePage.java
        │   │   ├── LoginPage.java
        │   │   ├── InventoryPage.java
        │   │   └── CartPage.java
        │   │
        │   ├── tests/
        │   │   ├── LoginTests.java
        │   │   └── CartTests.java
        │   │
        │   ├── data/
        │   │   └── LoginTestData.java
        │   │
        │   ├── constants/
        │   │   └── TestConstants.java
        │   │
        │   ├── listeners/
        │   │   ├── TestListener.java
        │   │   └── SuiteListener.java
        │   │
        │   ├── retry/
        │   │   ├── RetryAnalyzer.java
        │   │   └── RetryTransformer.java
        │   │
        │   └── utils/
        │       ├── ConfigReader.java
        │       ├── ScreenshotUtil.java
        │       └── AllureEnvironmentUtil.java
        │
        └── resources/
            ├── config-qa.properties
            ├── config-uat.properties
            ├── logback.xml
            └── allure.properties

```

**Generated folders such as the following are ignored by Git:**

- `target/`
- `logs/`
- `screenshots/`
- `.idea/`

## Framework Architecture

The framework follows a layered architecture.

```text
GitHub Actions
      ↓
Maven
      ↓
Maven Surefire Plugin
      ↓
TestNG
      ↓
BaseTest
      ↓
DriverFactory
      ↓
DriverManager
      ↓
Test Classes
      ↓
Page Objects
      ↓
BasePage
      ↓
Selenium WebDriver
      ↓
Browser
```

**Supporting components provide:**

- Environment configuration
- Test data
- Constants
- Retry handling
- TestNG listeners
- Screenshots
- Logging
- Allure reporting
- CI artifacts

## Core Framework Components:

### BaseTest

BaseTest handles the common test lifecycle.

**Responsibilities:**

- Read execution configuration
- Determine browser
- Determine environment
- Determine headless mode
- Determine local or remote execution
- Create WebDriver before every test invocation
- Store WebDriver using DriverManager
- Open the application
- Quit and remove WebDriver after every test invocation

**The browser lifecycle is handled using:**

```java
@BeforeMethod
@Test
@AfterMethod
```

Each test invocation receives a fresh browser session.

### BasePage

BasePage contains reusable Selenium page mechanics.

**Responsibilities include:**

- Explicit waits
- Element visibility checks
- Click actions
- Typing
- Text waits
- URL waits
- Common page interaction utilities

This prevents Selenium utility code from being duplicated across multiple Page Objects.

### DriverFactory

DriverFactory is responsible for creating WebDriver instances.

**It supports:**

- Chrome
- Firefox
- Edge
- Headless execution
- Local WebDriver
- RemoteWebDriver
- Selenium Grid

**Conceptually:**

```text
DriverFactory
→ HOW should the WebDriver be created?
```

**Examples:**

```text
Chrome + local
→ ChromeDriver

Firefox + local
→ FirefoxDriver

Edge + local
→ EdgeDriver

Remote execution
→ RemoteWebDriver
```

### DriverManager

DriverManager stores and provides the WebDriver associated with the current Java thread.

**It uses:**

`ThreadLocal<WebDriver>`

This makes browser execution safe during parallel execution.

**Example:**

```text
Thread 1 → ChromeDriver
Thread 2 → FirefoxDriver
Thread 3 → EdgeDriver
```

**Conceptually:**

```text
DriverFactory
→ creates the driver

DriverManager
→ stores and provides the correct driver for the current thread
```

### DriverProvider

DriverProvider acts as a bridge for components such as listeners that need access to the active WebDriver.
It allows supporting framework components to obtain the current thread's driver without placing WebDriver directly inside those components.

### Page Object Model

The framework uses Page Object Model to separate test scenarios from UI implementation details.

**Current Page Objects:**

- LoginPage
- InventoryPage
- CartPage

**Page Objects contain:**

- Locators
- Page-specific actions
- Navigation logic
- Page readiness checks

**Tests describe:**

**WHAT should happen**

**Page Objects describe:**

**HOW it happens in the UI**

**Example:**

```text
Test:
Login
Add Backpack
Open Cart
Verify product

Page Objects:
Locate username field
Locate login button
Locate add-to-cart button
Locate shopping cart link
```

### Page Readiness

Page Objects verify that the expected page has actually loaded.

**For example:**

```text
InventoryPage
→ wait for inventory URL
→ wait for "Products"

CartPage
→ wait for cart URL
→ wait for "Your Cart"
```

This avoids relying only on generic elements that may exist on multiple pages.

### Test Classes

**Test classes contain:**

- Test scenarios
- Business flow
- Assertions
- TestNG metadata
- Allure metadata

**Current test classes:**

- `LoginTests`
- `CartTests`

Tests do not directly contain repeated low-level Selenium locator logic.
That logic belongs inside Page Objects.

## Configuration Management

**Environment-specific configuration is stored under:**

`src/test/resources/`

**Available environment files:**

- `config-qa.properties`
- `config-uat.properties`

**Example:**

```properties
baseUrl=https://www.saucedemo.com/
explicitWait=10
```

**Environment can be selected at runtime using:**

`-Denv=qa`

**or:**

`-Denv=uat`

**The framework reads:**

`System.getProperty("env")`

and loads the appropriate configuration file.

## Configuration vs Test Data vs Constants

### Configuration

Configuration controls where or how the framework executes.

**Examples:**

- `baseUrl`
- `explicitWait`
- `environment`

### Test Data

Test Data represents scenario-specific input.

**Examples:**

- valid username
- invalid username
- invalid password
- test combinations

Test Data can be supplied using TestNG DataProviders.

### Constants

Constants represent stable reusable values.

**Examples:**

- `PRODUCTS_PAGE_TITLE`
- `CART_PAGE_TITLE`
- `BACKPACK_NAME`
- `INVENTORY_PATH`
- `CART_PATH`

Constants should represent values whose meaning remains stable across test scenarios.

## Test Data

TestNG DataProviders are used for scenario-based input.

**Example:**

- invalid username + valid password
- valid username + invalid password
- invalid username + invalid password

DataProviders are intentionally not configured for parallel execution.
This avoids introducing a second concurrency layer on top of cross-browser parallel execution.

## Test Groups

The framework uses TestNG groups to classify tests.

**Current groups:**

- `smoke`
- `regression`

### Smoke Tests

Smoke tests provide fast validation of critical application functionality.

**Example execution:**

```bash
mvn clean test "-Denv=qa" "-Dgroups=smoke"
```

**Current smoke coverage includes critical flows such as:**

- Valid login
- Add product to cart

### Regression Tests

Regression tests provide broader coverage.

**Example:**

```bash
mvn clean test "-Denv=qa" "-Dgroups=regression"
```

**Regression includes:**

- Valid login
- Invalid login scenarios
- Cart workflow

## Running Tests Locally

### Default Execution

```bash
mvn clean test "-Denv=qa"
```

**Default browser:**

`Chrome`

### Chrome

```bash
mvn clean test "-Denv=qa" "-Dbrowser=chrome"
```

### Firefox

```bash
mvn clean test "-Denv=qa" "-Dbrowser=firefox"
```

### Edge

```bash
mvn clean test "-Denv=qa" "-Dbrowser=edge"
```

### Headless Execution

```bash
mvn clean test "-Denv=qa" "-Dbrowser=chrome" "-Dheadless=true"
```

Headless execution runs the real browser without displaying its UI.
It is useful for CI environments.

## Cross-Browser Execution

**Cross-browser execution is controlled by:**

`testng-crossbrowser.xml`

**The suite contains separate TestNG XML test blocks for:**

- `Chrome`
- `Firefox`
- `Edge`

**and uses:**

`parallel="tests"`

**Example:**

```bash
mvn clean test "-Denv=qa" "-Dheadless=true" "-DsuiteXmlFile=testng-crossbrowser.xml"
```

**Important:**

**Do not pass:**

`-Dbrowser=...`

during cross-browser execution.
The browser values come from the TestNG XML file.
Passing -Dbrowser would override all browser values and defeat the purpose of cross-browser execution.

## Parallel Execution

Cross-browser execution uses TestNG XML-level parallelism.

**Conceptually:**

```text
TestNG
   │
   ├── Thread 1 → Chrome Tests
   ├── Thread 2 → Firefox Tests
   └── Thread 3 → Edge Tests
```

Each thread receives its own WebDriver using DriverManager and ThreadLocal.
Inside each XML test block, test invocations execute sequentially.
The framework currently avoids DataProvider-level parallelism to keep concurrency predictable.

## Remote Execution and Selenium Grid

The framework supports both local and remote browser execution.

```text
Local:
DriverFactory
→ ChromeDriver / FirefoxDriver / EdgeDriver

Remote:
DriverFactory
→ RemoteWebDriver
→ Selenium Grid
```

### Starting Selenium Grid

**Example:**

```bash
java -jar selenium-server-4.46.0.jar standalone --selenium-manager true
```

**Default Grid URL:**

`http://localhost:4444`

### Remote Chrome Execution

```bash
mvn clean test "-Denv=qa" "-Dbrowser=chrome" "-Dremote=true"
```

### Remote Cross-Browser Execution

```bash
mvn clean test "-Denv=qa" "-Dremote=true" "-DsuiteXmlFile=testng-crossbrowser.xml"
```

**The framework can also accept a custom Grid URL using:**

`-DgridUrl=...`

## Retry Handling

Failed tests can be automatically retried.

**The framework uses:**

- `IRetryAnalyzer`
- `IAnnotationTransformer`

**Current retry policy:**

```text
Original attempt
+ Retry 1
+ Retry 2
```

**Maximum total execution attempts:**

3

### RetryAnalyzer

**RetryAnalyzer decides:**

Should TestNG retry this failed invocation?

**It returns:**

```text
true
or
false
```

RetryAnalyzer itself does not rerun the test.
TestNG performs the actual rerun.

**Each retry goes through the normal lifecycle:**

```text
@AfterMethod
↓
old browser closes

@BeforeMethod
↓
fresh browser starts

@Test
↓
test executes again
```

### RetryTransformer

RetryTransformer automatically attaches RetryAnalyzer to tests.

**Conceptually:**

```text
TestNG discovers @Test
↓
RetryTransformer
↓
RetryAnalyzer policy attached
```

## TestNG Listeners

Listeners observe TestNG execution events.

**The framework handles events such as:**

- Test Started
- Test Passed
- Test Failed
- Test Skipped
- Suite Started
- Suite Finished

Listeners do not execute the actual test scenario.
They react to events generated by TestNG.

## Failure Handling

**When a test finally fails:**

```text
Test failure
      ↓
RetryAnalyzer
      ↓
Retry available?
   ┌───────┴───────┐
   │               │
  YES              NO
   │               │
Fresh browser   Final failure
   │               │
Rerun test      TestListener
                   │
                   ↓
               Screenshot
                   │
                   ↓
             Allure attachment
                   │
                   ↓
            Logs + CI artifacts
```

## Screenshot Handling

Failure screenshots are captured automatically.

**Screenshots are stored under:**

`screenshots/`

**Screenshot filenames contain:**

- test name
- timestamp
- unique identifier

This prevents filename collisions during parallel execution.
Failure screenshots are also attached to Allure reports.

## Logging

**Logging is implemented using:**

```text
SLF4J
+
Logback
```

SLF4J provides the logging API.
Logback provides the actual logging implementation and output configuration.

**Logs are written to:**

`logs/automation.log`

**The framework logs information such as:**

- Environment
- Browser creation
- Local or remote execution
- Test start
- Test pass/failure
- Retry events
- Browser cleanup
- Suite summary
- Configuration loading

Sensitive credentials should never be written to logs.

## Allure Reporting

Allure is used for human-readable test reporting.

**The framework includes:**

- Test status
- Test steps
- Epic
- Feature
- Story
- Environment information
- Failure screenshots
- Browser/execution metadata

### Generate Allure Report

```bash
mvn allure:report
```

**Generated report:**

`target/site/allure-maven-plugin/index.html`

The project uses single-file Allure report generation so the resulting HTML file can be downloaded from CI and opened locally.

### Local Interactive Allure Report

```bash
mvn allure:serve
```

allure:serve starts a local server.
It remains running until manually stopped.

### Allure Data Flow

```text
TestNG execution
      ↓
Allure TestNG adapter
      ↓
target/allure-results
      ↓
mvn allure:report
      ↓
HTML report
```

target/allure-results contains raw Allure result data.
The generated HTML report is the human-readable report.

## GitHub Actions CI

The framework uses GitHub Actions for continuous integration.

**Workflow file:**

`.github/workflows/ui-tests.yml`

The same framework behaves differently depending on the event that triggered the workflow.

### Pull Request Execution

**When a Pull Request targets main:**

```text
Pull Request
      ↓
GitHub Actions
      ↓
QA
      ↓
Chrome
      ↓
Headless
      ↓
Smoke tests
```

This provides fast validation before code is merged.

### Push / Merge to Main

**When main is updated:**

```text
main updated
      ↓
GitHub Actions
      ↓
QA
      ↓
Chrome
      ↓
Headless
      ↓
Regression tests
```

A PR merge also updates main, which triggers this execution.

### Scheduled Cross-Browser Regression

A scheduled regression runs daily.

**Current schedule:**

`08:00 AM IST`

**Execution:**

```text
Scheduled trigger
      ↓
QA
      ↓
Headless
      ↓
Regression
      ↓
Chrome + Firefox + Edge
```

### Manual GitHub Actions Execution

The workflow can also be started manually.

**Manual execution supports selection of:**

- Environment
- Browser
- Headless mode
- Normal or cross-browser suite
- Smoke or regression scope

**Example:**

```text
Environment: UAT
Browser: Firefox
Headless: true
Suite: normal
Scope: smoke
```

### CI Quality Gate

The main branch is protected using a GitHub ruleset.

**Expected development flow:**

```text
Feature branch
      ↓
Push branch
      ↓
Create Pull Request
      ↓
Smoke CI
      ↓
Required ui-tests check
      ↓
PASS
      ↓
Merge allowed
```

**If the required check fails:**

Merge blocked

Direct changes to main should go through Pull Requests.
Force pushes to main should remain disabled.

### CI Event Strategy

The same workflow chooses different execution settings depending on the GitHub event.

```text
pull_request
→ smoke
→ Chrome
→ QA

push to main
→ regression
→ Chrome
→ QA

schedule
→ regression
→ Chrome + Firefox + Edge
→ QA

workflow_dispatch
→ manually selected configuration
```

The GitHub ruleset does not start tests.
The workflow starts tests.
The ruleset uses the result of the required workflow check to decide whether merging is allowed.

## CI Artifacts

GitHub Actions preserves test evidence after execution.
Two main artifact groups are generated.

### Allure Report Artifact

**Contains:**

`index.html`

This is the human-readable Allure report.
The report can be downloaded from the GitHub Actions run and opened locally in a browser.

### Test Evidence Artifact

**Contains:**

- `target/surefire-reports/`
- `target/allure-results/`
- `logs/`
- `screenshots/`

This provides technical debugging evidence.

### Artifact Retention

**Artifacts are currently retained for:**

`7 days`

This keeps recent debugging evidence without unnecessarily storing old artifacts indefinitely.

## CI Failure Investigation

When a workflow fails, failures should first be classified.

**Possible categories:**

- Workflow failure
- Infrastructure failure
- Browser failure
- Test/application failure
- Flaky/intermittent failure
- Reporting failure

**Recommended debugging order:**

1. Identify which GitHub Actions step failed
2. Check whether browser setup completed
3. Check whether the @Test actually started
4. Find the first real exception
5. Check retry history
6. Check failure screenshot
7. Check Allure step failure
8. Check automation.log

### Evidence Sources

Different artifacts answer different questions.

```text
GitHub Actions Logs
→ Did the pipeline itself fail?

Allure Report
→ What happened in the test?

automation.log
→ Why did it fail technically?

Screenshot
→ What was visible in the browser?

Surefire Reports
→ Maven/TestNG execution details
```

### Test Failure vs CI Failure

**A test assertion failure means:**

```text
CI worked
but
test failed
```

**Example:**

```text
Expected: Your Cart
Actual: Products
```

An infrastructure failure means the test may never have started.

**Example:**

- browser could not start
- Java setup failed
- Maven failed
- artifact generation failed

These should be treated differently during debugging.

### Retry and Flaky Tests

Retry should not be used to permanently hide unstable automation.

**Example:**

```text
Attempt 1 → FAIL
Attempt 2 → PASS
```

The final build may become successful, but the test should still be considered potentially flaky.
Retry is a safety mechanism, not a replacement for root-cause analysis.

## Complete Successful Test Runtime Flow

**Example Cart test:**

```text
Maven starts
      ↓
Surefire starts TestNG
      ↓
TestNG selects test
      ↓
@BeforeMethod
      ↓
BaseTest
      ↓
DriverFactory creates WebDriver
      ↓
DriverManager stores WebDriver
      ↓
ConfigReader provides baseUrl
      ↓
Application opens
      ↓
@Test starts
      ↓
LoginPage
      ↓
InventoryPage
      ↓
Add Backpack
      ↓
Open Cart
      ↓
CartPage readiness check
      ↓
Assertions
      ↓
Test passes
      ↓
Listener receives success
      ↓
@AfterMethod
      ↓
DriverManager quits browser
      ↓
Logs and Allure results generated
```

## Complete CI Runtime Flow

```text
GitHub Event
      ↓
GitHub Actions
      ↓
Execution configuration resolved
      ↓
Maven
      ↓
Surefire
      ↓
TestNG
      ↓
BaseTest
      ↓
DriverFactory
      ↓
DriverManager
      ↓
Test
      ↓
Page Objects
      ↓
Selenium
      ↓
Browser
      ↓
PASS / FAIL
      ↓
Retry / Listener
      ↓
Logs
Screenshots
Allure
Surefire
      ↓
GitHub Artifacts
      ↓
GitHub Status Check
```

## Design Principles

**The framework follows the following principles:**

- Tests describe scenarios rather than low-level UI implementation.
- Page Objects own page-specific Selenium interactions.
- BasePage centralizes reusable Selenium mechanics.
- BaseTest controls test lifecycle.
- DriverFactory creates browser sessions.
- DriverManager provides thread-safe driver isolation.
- Each test invocation receives a fresh browser.
- Explicit waits are preferred over fixed sleeps.
- Page readiness is validated using meaningful page-specific conditions.
- Configuration is externalized.
- Test data is separated from framework configuration.
- Constants represent stable reusable values.
- Retry is used only as a safety net.
- Failure evidence is preserved.
- Logging and Allure serve different purposes.
- Cross-browser execution uses one controlled concurrency layer.
- Secrets and real credentials should never be committed to Git.
- Existing platform capabilities should be preferred over unnecessary custom infrastructure.

## CI Notifications

The project currently relies on GitHub's built-in workflow notifications.
A separate custom mailer is intentionally not implemented.
GitHub can notify the repository owner when workflow executions fail.
This avoids unnecessary SMTP, OAuth, or third-party email infrastructure for a training project.

## Continuous Deployment

This repository is an automation test framework.
It does not deploy SauceDemo because SauceDemo is not owned by this repository.
Therefore this project primarily demonstrates CI rather than application CD.
In a real company, the same framework could participate in a deployment pipeline.

**Example:**

```text
Application build
      ↓
Deploy application to QA
      ↓
Trigger Selenium smoke tests
      ↓
PASS
      ↓
Promote deployment
      ↓
Regression / production validation
```

The automation framework can therefore act as a deployment quality gate even when it exists in a separate repository from the application.

## Security Principles

The framework should never commit real sensitive credentials.
Sensitive values such as the following should be stored using a secret-management mechanism:

- real passwords
- API tokens
- cloud Grid credentials
- deployment credentials
- database passwords

**Suitable storage mechanisms include:**

- GitHub Secrets
- AWS Secrets Manager
- Azure Key Vault
- HashiCorp Vault
- Jenkins Credentials

Secrets should follow the principle of least privilege.

## Git Workflow

**Recommended development workflow:**

```text
main
  ↑
Pull Request
  ↑
feature branch
```

**Example:**

```bash
git checkout -b feature-name
```

**Make changes:**

```bash
git add .
git commit -m "Describe the change"
git push -u origin feature-name
```

**Then:**

```text
Create Pull Request
↓
Required smoke CI
↓
Merge only after successful checks
```

## Git Ignore

Generated framework output should not be committed.

**Typical ignored folders include:**

- `target/`
- `logs/`
- `screenshots/`
- `.idea/`

This keeps the repository clean and prevents generated execution artifacts from becoming part of source control.

## Current Framework Capabilities

**The project currently supports:**

- Selenium WebDriver
- TestNG
- Maven
- Page Object Model
- BasePage utilities
- BaseTest lifecycle
- DriverFactory
- ThreadLocal DriverManager
- Chrome
- Firefox
- Edge
- Headless execution
- Cross-browser execution
- Parallel XML test execution
- Selenium Grid
- RemoteWebDriver
- QA/UAT configuration
- DataProviders
- Smoke testing
- Regression testing
- Retry handling
- TestNG listeners
- Failure screenshots
- SLF4J logging
- Logback
- Allure reporting
- Single-file Allure HTML report
- GitHub Actions
- Pull Request quality gate
- Scheduled regression
- Manual CI execution
- CI artifacts
- CI failure evidence

## Current Scope

This project focuses on building and understanding a maintainable Selenium + TestNG UI automation framework.
It intentionally avoids adding unrelated technologies only for the sake of increasing the technology count.
Technologies such as the following can be explored separately when required:

- Docker
- Kubernetes
- Cloud browser providers
- API automation
- Performance testing
- Database validation
- Message queues
- Cucumber

The current project focuses on strong automation architecture and CI fundamentals.

## Author

Created as part of hands-on SDET, Selenium, TestNG, automation framework architecture, and CI practice.
````
