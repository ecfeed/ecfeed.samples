package workshop.runner.selenium.fixtures;

import org.junit.jupiter.api.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import workshop.runner.selenium.pages.EcFeedPageObject;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EcFeedFixture {
    private static final String webDriver =
            System.getProperty("user.dir") + "/src/test/resources/drivers/geckodriver.exe";

    private RemoteWebDriver driver;

    public EcFeedPageObject ecfeed;

    @BeforeAll
    static void setUpGlobal() {

        System.setProperty("webdriver.gecko.driver", webDriver);
    }

    @BeforeEach
    void setUp() {
        driver = new FirefoxDriver();

        ecfeed = new EcFeedPageObject(driver);

        ecfeed.visit();
    }

    @AfterEach
    void tearDown() {

        driver.quit();
    }
}
