package testify.selenium;

import com.ecfeed.junit.annotation.EcFeedInput;
import com.ecfeed.junit.annotation.EcFeedModel;
import com.ecfeed.junit.annotation.EcFeedTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class Simple {

//------------------------------------------------------------------------------

    // FireFox - The location of the driver (it can be downloaded from 'https://chromedriver.chromium.org/downloads').
    private static final String webDriver = "/home/krzysztof/geckodriver";

    // Chrome - The location of the driver (it can be downloaded from 'https://github.com/mozilla/geckodriver/releases').
    // private static final String webDriver = "/home/krzysztof/chromedriver";

//------------------------------------------------------------------------------

    // The address of the web page.
    private static final String webPageAddress = "http://www.workshop-2019-november.ecfeed.com/?mode=error";

    // Web page elements used to trigger an action.
    private static final String[] webPageFormExecute = { "submit" };

    // Web page elements which describe the outcome of an action.
    private static final String[] webPageFormOutput = { "status", "response" };

    // Web page elements which must be filled before triggering an action.
    private static final String[][] webPageFormInput = {
            {"name", "address", "quantity", "phone", "email"},                  // Input type - Text.
            {"country", "product", "color", "size", "payment", "delivery"}      // Input type - Select.
    };

//------------------------------------------------------------------------------

    private static void setForm(RemoteWebDriver driver, String[][] values) throws IllegalArgumentException {
        validateInput(webPageFormInput, values);

        setFormText(driver, values[0]);
        setFormSelect(driver, values[1]);
    }

    private static void execute(RemoteWebDriver driver) {
        for (String element : webPageFormExecute) {
            driver.findElementById(element).click();
        }
    }

    private static String[] getResponse(RemoteWebDriver driver) {
        String[] response = new String[webPageFormOutput.length];

        for (int i = 0 ; i < webPageFormOutput.length ; i++) {
            response[i] = driver.findElementById(webPageFormOutput[i]).getAttribute("value");
        }

        return response;
    }

//------------------------------------------------------------------------------

    private static void setFormText(RemoteWebDriver driver, String[] values) {
        for (int i = 0 ; i < webPageFormInput[0].length ; i++) {
            WebElement element = driver.findElementById(webPageFormInput[0][i]);
            element.clear();
            element.sendKeys(values[i]);
        }
    }

    private static void setFormSelect(RemoteWebDriver driver, String[] values) {
        for (int i = 0 ; i < webPageFormInput[1].length ; i++) {
            (new Select(driver.findElementById(webPageFormInput[1][i]))).selectByVisibleText(values[i]);
        }
    }

    private static void validateInput(String[][] reference, String[][] input)
            throws IllegalArgumentException {

        if (input.length != 2) {
            throw new IllegalArgumentException("The dimension of the input array is incorrect.");
        }
        if (reference[0].length != input[0].length) {
            throw new IllegalArgumentException("The number of the input text fields is incorrect.");
        }
        if (reference[1].length != input[1].length) {
            throw new IllegalArgumentException("The number of the input select fields is incorrect.");
        }
    }

//------------------------------------------------------------------------------

    private static RemoteWebDriver driver;

    @BeforeAll
    static void beforeAll() {
        System.setProperty("webdriver.gecko.driver", webDriver);
        driver = new FirefoxDriver();

//      System.setProperty("webdriver.chrome.driver", webDriver);
//      driver = new ChromeDriver();

        driver.get(webPageAddress);
    }

    @AfterAll
    static void afterAll() {
         driver.quit();
    }

    @EcFeedTest
    @EcFeedModel("9835-3029-2264-1682-5114")
    @EcFeedInput("'method':'com.ecfeed.Model.simple', 'dataSource':'genNWise', 'constraints':'ALL'")
    void seleniumValidate(String country, String name, String address, String product, String color, String size, String quantity, String payment, String delivery, String phone, String email) {

        String[][] input = {
                {name, address, quantity, phone, email},
                {country, product, color, size, payment, delivery}
        };

        try {
            setForm(driver, input);
        } catch (NoSuchElementException e) {
            fail();
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        execute(driver);

        String[] response = getResponse(driver);
        Arrays.stream(response).forEach(System.out::println);

        assertTrue(response[0].equals(" Request accepted"), "The order was not processed");
    }
}