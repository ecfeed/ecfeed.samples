package workshop.runner.selenium.fixtures;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import workshop.runner.selenium.pages.EcFeedPageObject;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EcFeedFixture {
    private static final String webDriver =
            System.getProperty("user.dir") + "/src/test/resources/drivers/geckodriver.exe";

    // To simplify the code and speed up tests, we only use one 'page' object.
    // This might not be a good idea to use in production.

    private static RemoteWebDriver driver;

    static public EcFeedPageObject ecfeed;

    @BeforeAll
    static void setUp() {

        System.setProperty("webdriver.gecko.driver", webDriver);

        driver = new FirefoxDriver();

        ecfeed = new EcFeedPageObject(driver);

        ecfeed.visit();
    }

    @AfterAll
    static void tearDown() {

        driver.quit();
    }
}
