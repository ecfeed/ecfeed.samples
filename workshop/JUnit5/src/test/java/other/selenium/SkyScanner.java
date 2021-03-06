package other.selenium;

import com.ecfeed.junit.annotation.EcFeedInput;
import com.ecfeed.junit.annotation.EcFeedModel;
import com.ecfeed.junit.annotation.EcFeedTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.fail;

public class SkyScanner {

//------------------------------------------------------------------------------

    private static final String webDriver = "/home/krzysztof/geckodriver";
    private static final String webPageAddress = "https://www.skyscanner.net/transport/flights/no/pl/";

//------------------------------------------------------------------------------

    private static void delay(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

//------------------------------------------------------------------------------

    private static RemoteWebDriver driver;
    private static Wait<RemoteWebDriver> driverWait;

    @BeforeAll
    static void beforeAll() {
        System.setProperty("webdriver.gecko.driver", webDriver);
        driver = new FirefoxDriver();

        driverWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofSeconds(5))
                .ignoring(NoSuchElementException.class);

        driver.get(webPageAddress);
    }

    @AfterAll
    static void afterAll() {
         driver.quit();
    }

    @EcFeedTest
    @EcFeedModel("4408-1001-3225-0620-6211")
    @EcFeedInput("'method':'com.ecfeed.SkyScanner.execute', 'dataSource':'genNWise', 'constraints':'ALL'")
    void seleniumValidate(String fFrom, String fTo, String fDepart, String fReturn) {

        CharSequence[] clear = {Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
                                Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
                                Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
                                Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
                                Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
                                Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
                                Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE};

        WebElement element;

        element = driverWait.until(driver -> driver.findElement(By.id("simplified-search-controls-origin")));
        if (element.getAttribute("value").equals(fFrom)) {
            System.out.println("Ignoring: flight origin");
        } else {
            element.sendKeys(clear);
            element.sendKeys(fFrom);
            element.sendKeys(Keys.TAB);
        }

        element = driverWait.until(driver -> driver.findElement(By.id("simplified-search-controls-destination")));
        if (element.getAttribute("value").equals(fTo)) {
            System.out.println("Ignoring: flight destination");
        } else {
            element.sendKeys(clear);
            element.sendKeys(fTo);
            element.sendKeys(Keys.TAB);
        }

        element = driverWait.until(driver -> driver.findElement(By.id("simplified-search-controls-depart-date")));
        driver.executeScript("arguments[0].click()", element);

        element = driverWait.until(driver -> driver.findElement(By.className("monthselector")));
        element = driverWait.until(ExpectedConditions.elementToBeClickable(element.findElement(By.xpath("//*[contains(text(), '" + fDepart + "')]"))));
        driver.executeScript("arguments[0].click()", element);

        element = driverWait.until(driver -> driver.findElement(By.id("simplified-search-controls-return-date")));
        driver.executeScript("arguments[0].click()", element);

        element = driverWait.until(driver -> driver.findElement(By.className("monthselector")));
        element = driverWait.until(ExpectedConditions.elementToBeClickable(element.findElement(By.xpath("//*[contains(text(), '" + fReturn + "')]"))));
        driver.executeScript("arguments[0].click()", element);

        delay(2000);

        element = driverWait.until(driver -> driver.findElement(By.id("destinations")).findElement(By.className("browse-data-entry")));

        try {
            String destination = element.findElement(By.cssSelector("h3")).getText();
            String price = element.findElement(By.className("price")).getText().split(" ")[1];
            System.out.println(destination.toLowerCase() + ": " + price.toLowerCase());
        } catch (Exception e) {
            System.out.println("The location is not available");
            fail();
        }

    }
}