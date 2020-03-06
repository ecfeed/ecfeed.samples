package testify.runner.selenium;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.fail;

public class Manual {

//------------------------------------------------------------------------------

// In order to run Selenium tests you need to download a proper driver.
// In this workshop, our choice is FireFox, However, if something goes wrong, please use the second one, i.e. Chrome.
// To change it, comment all lines that indicate the use of the 'gecko' driver and uncomment those pointing to the 'chrome' driver.

// FireFox - The location of the driver (it can be downloaded from 'https://chromedriver.chromium.org/downloads').
// Note, that the default value is most likely erroneous and must be updated.
    private static final String webDriver = "/home/krzysztof/geckodriver";      // If you want to use the 'chrome' driver, comment this line.

// Chrome - The location of the driver (it can be downloaded from 'https://github.com/mozilla/geckodriver/releases').
// Note, that the following value is most likely erroneous must be updated.
// private static final String webDriver = "/home/krzysztof/chromedriver";      // If you want to use the 'chrome' driver, uncomment this line.

//------------------------------------------------------------------------------

// The address of the web page.
    private static final String webPageAddress = "http://www.workshop-2020-march.ecfeed.com";

// The web page elements used to trigger the main action.
// It is equivalent to the 'place order' button.
    private static final String[] webPageFormExecute = { "submit" };

// The web page elements which describe the outcome of the main action.
    private static final String[] webPageFormOutput = { "status", "response" };

// The web page elements which must be filled before invoking the main action.
    private static final String[][] webPageFormInput = {
            {"name", "address", "quantity", "phone", "email"},                  // Input type - Text.
            {"country", "product", "color", "size", "payment", "delivery"}      // Input type - Select.
    };

//------------------------------------------------------------------------------

// The following methods are used to set elements on the web page.
// They are not a part of the 'ecFeed' library, and therefore, are not described in detail.

    private static void setForm(RemoteWebDriver driver, String[][] values) throws IllegalArgumentException {
        validateInput(webPageFormInput, values);

        setFormText(driver, values[0]);
        setFormSelect(driver, values[1]);
    }

    private static void validateInput(String[][] reference, String[][] input) throws IllegalArgumentException {

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

    private static void setFormText(RemoteWebDriver driver, String[] values) {
        for (int i = 0 ; i < webPageFormInput[0].length ; i++) {
            driver.findElementById(webPageFormInput[0][i]).sendKeys(values[i]);
        }
    }

    private static void setFormSelect(RemoteWebDriver driver, String[] values) {
        for (int i = 0 ; i < webPageFormInput[1].length ; i++) {
            (new Select(driver.findElementById(webPageFormInput[1][i]))).selectByVisibleText(values[i]);
        }
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

// The annotation states that the method should be invoked once before all tests.
    @BeforeAll
// The name of the method can be arbitrary.
    static void beforeAll() {
        System.setProperty("webdriver.gecko.driver", webDriver);    // If you want to use the 'chrome' driver, comment this line.
//      System.setProperty("webdriver.chrome.driver", webDriver);   // If you want to use the 'chrome' driver, uncomment this line.
    }

// The annotation states that the method should be invoked as a standard jUnit test (without the 'ecFeed' extension).
    @Test
// The name of the method can be arbitrary.
    void seleniumValidate() {
// Select the Selenium driver.
        RemoteWebDriver driver = new FirefoxDriver();   // If you want to use the 'chrome' driver, comment this line.
//      RemoteWebDriver driver = new ChromeDriver();    // If you want to use the 'chrome' driver, uncomment this line.
// Load the workshop web page.
        driver.get(webPageAddress);
// Prepare the input (in accordance to the 'webPageFormInput' field).
        String[][] input = {
                {"Krzysztof Skorupski", "ul. Komandorska 6. 50-022 Wroclaw", "3", "+48123456789", "k.skorupski@testify.no"},
                {"Poland", "hoodie", "black", "L", "VISA", "express"}
        };
// Fill the main form.
        setForm(driver, input);
// Run the main action (equivalent to clicking on the 'place order' button).
        execute(driver);
// Display the response.
        Arrays.stream(getResponse(driver)).forEach(System.out::println);
// Close the workshop web page.
        driver.quit();
    }

//------------------------------------------------------------------------------

}