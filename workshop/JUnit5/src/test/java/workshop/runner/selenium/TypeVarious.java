package workshop.runner.selenium;

import com.ecfeed.Param;
import com.ecfeed.TestProvider;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;
import workshop.data.*;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TypeVarious {

// The following test uses the 'ecFeed' library.
// It is very similar to the previous one, the only difference is that not all parameters are strings.

//------------------------------------------------------------------------------

    private static final String webDriver = "/home/krzysztof/geckodriver";      // If you want to use the 'chrome' driver, comment this line.
    // private static final String webDriver = "/home/krzysztof/chromedriver";  // If you want to use the 'chrome' driver, uncomment this line.

//------------------------------------------------------------------------------

    private static final String webPageAddress = "http://www.workshop-2020-march.ecfeed.com";

    private static final String[] webPageFormExecute = { "submit" };

    private static final String[] webPageFormOutput = { "status", "response" };

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

    private static void setFormText(RemoteWebDriver driver, String[] values) {
        for (int i = 0 ; i < webPageFormInput[0].length ; i++) {
            WebElement element = driver.findElementById(webPageFormInput[0][i]);
            element.clear();
            element.sendKeys(values[i]);
        }
    }

    private static void setFormSelect(RemoteWebDriver driver, String[] values) {
        for (int i = 0 ; i < webPageFormInput[1].length ; i++) {
            (new Select(driver.findElementById(webPageFormInput[1][i]))).selectByValue(values[i].toLowerCase());
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

    private static Iterable<Object[]> testProviderNWise() {
//        return TestProvider.create("6EG2-YL4S-LMAK-Y5VW-VPV9").generateNWise("com.example.test.Demo.typeVarious", new Param.ParamsNWise().constraints("NONE"));
        return TestProvider.create("6EG2-YL4S-LMAK-Y5VW-VPV9").generateNWise("com.example.test.Demo.typeVarious");
    }

    private static RemoteWebDriver driver;

    @BeforeAll
    static void beforeAll() {
        System.setProperty("webdriver.gecko.driver", webDriver);        // If you want to use the 'chrome' driver, comment this line.
        driver = new FirefoxDriver();                                   // If you want to use the 'chrome' driver, comment this line.
//      System.setProperty("webdriver.chrome.driver", webDriver);       // If you want to use the 'chrome' driver, uncomment this line
//      driver = new ChromeDriver();                                    // If you want to use the 'chrome' driver, uncomment this line
        driver.get(webPageAddress);
    }

    @AfterAll
    static void afterAll() {
         driver.quit();
    }

    @ParameterizedTest
    @MethodSource("testProviderNWise")
    void seleniumValidate(Country country, String name, String address, Product product, Color color, Size size, int quantity, Payment payment, Delivery delivery, String phone, String email) {

        String[][] input = {
                {name, address, quantity + "", phone, email},
                {country.toString(), product.toString(), color.toString(), size.toString(), payment.toString(), delivery.toString()}
        };

        setForm(driver, input);

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

//------------------------------------------------------------------------------

}