package other.selenium;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.fail;

public class SkyScanner {

//------------------------------------------------------------------------------

    private static final String webDriver = "/home/krzysztof/geckodriver";
    private static final String webPageAddress = "https://www.skyscanner.net/transport/flights/no/pl/";

//------------------------------------------------------------------------------

    private static void delay() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

//------------------------------------------------------------------------------

    @BeforeAll
    static void beforeAll() {
        System.setProperty("webdriver.gecko.driver", webDriver);
    }

    @Test
    void seleniumValidate() {

        FirefoxDriver driver = new FirefoxDriver();
        driver.get(webPageAddress);

//        delay();

        CharSequence[] clear = {Keys.BACK_SPACE, Keys.BACK_SPACE,Keys.BACK_SPACE,Keys.BACK_SPACE,Keys.BACK_SPACE,Keys.BACK_SPACE,Keys.BACK_SPACE,Keys.BACK_SPACE,Keys.BACK_SPACE,Keys.BACK_SPACE,Keys.BACK_SPACE,Keys.BACK_SPACE,Keys.BACK_SPACE,Keys.BACK_SPACE,Keys.BACK_SPACE};

        WebElement element;
        try {
            element = driver.findElementById("simplified-search-controls-origin");
            element.sendKeys(clear);
            element.sendKeys("Oslo Gardermoen (OSL)");
            element.sendKeys(Keys.TAB);

            element = driver.findElementById("simplified-search-controls-destination");
            element.sendKeys(clear);
            element.sendKeys("Wroclaw (WRO)");
            element.sendKeys(Keys.TAB);

            delay();

            element = driver.findElementById("destinations").findElement(By.className("browse-data-entry"));
            String destination = element.findElement(By.cssSelector("h3")).getText();
            String price = element.findElement(By.className("price")).getText().split(" ")[1];
            System.out.println(destination + " " + price);

        } catch (NoSuchElementException e) {
            fail();
        }

        driver.quit();
    }
}