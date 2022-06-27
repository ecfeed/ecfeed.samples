package workshop.runner.selenium;

import com.ecfeed.TestHandle;
import com.ecfeed.TestProvider;
import com.ecfeed.params.ParamsNWise;
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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TypeVarious {

    private static final String webDriver = System.getProperty("user.home") + "/selenium/geckodriver.exe";

//------------------------------------------------------------------------------

    private static final String webPageAddress = "http://www.workshop-2021-december.ecfeed.com?mode=error";

    private static final String[] webPageFormExecute = { "submit" };

    private static final String[] webPageFormOutput = { "status", "response" };

    private static final String[][] webPageFormInput = {
            {"name", "address", "quantity", "phone", "email"},
            {"country", "product", "color", "size", "payment", "delivery"}
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

        var parameters = new HashMap<String, String>();
        parameters.put("keyStorePath", "src/test/resources/demo.p12");

        return TestProvider.create("6EG2-YL4S-LMAK-Y5VW-VPV9", parameters).generateNWise("com.example.test.Demo.typeVarious", ParamsNWise.create().feedback().label("Selenium"));
    }

    private static RemoteWebDriver driver;

    @BeforeAll
    static void beforeAll() {
        System.setProperty("webdriver.gecko.driver", webDriver);
        driver = new FirefoxDriver();
        driver.get(webPageAddress);
    }

    @AfterAll
    static void afterAll() {
         driver.quit();
    }

    @ParameterizedTest
    @MethodSource("testProviderNWise")
    void seleniumValidate(Country country, String name, String address, Product product, Color color, Size size, int quantity, Payment payment, Delivery delivery, String phone, String email, TestHandle testHandle) {

        String[][] input = {
                {name, address, quantity + "", phone, email},
                {country.toString(), product.toString(), color.toString(), size.toString(), payment.toString(), delivery.toString()}
        };

        try {
            setForm(driver, input);
        } catch (Exception e) {
            var custom = Map.of("1", e.getMessage().split("\n")[0] + ".");
            testHandle.addFeedback(false, "Input error", custom);
            throw e;
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        execute(driver);

        String[] response = getResponse(driver);
        Arrays.stream(response).forEach(System.out::println);

        var custom = IntStream.range(1, response.length).boxed().collect(Collectors.toMap(e -> "" + e, e -> response[e]));
        assertTrue(response[0].equals(" Request accepted"), () -> testHandle.addFeedback(false, "Output error", custom));
        testHandle.addFeedback(true);
    }

//------------------------------------------------------------------------------

}