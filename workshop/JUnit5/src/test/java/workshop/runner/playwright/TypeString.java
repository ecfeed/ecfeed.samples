package workshop.runner.playwright;

import com.ecfeed.TestHandle;
import com.ecfeed.TestProvider;
import com.ecfeed.params.ParamsNWise;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import workshop.runner.playwright.fixtures.EcFeedFixture;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.fail;

public class TypeString extends EcFeedFixture {
    private static String keyStorePath = "src/test/resources/demo.p12";
    private static String modelID = "6EG2-YL4S-LMAK-Y5VW-VPV9"; // "0603-5525-0414-9188-9919"
    private static String method = "com.example.test.Demo.typeString";
    private static String label = "Playwright";

    private static Iterable<Object[]> testProviderNWise() {
        var parameters = new HashMap<String, String>();
        parameters.put("keyStorePath", keyStorePath);

        return TestProvider.create(modelID, parameters).generateNWise(method, ParamsNWise.create().feedback().label(label));
    }

    @ParameterizedTest
    @MethodSource("testProviderNWise")
    void playwrightValidate(String country, String name, String address, String product, String color, String size, String quantity, String payment, String delivery, String phone, String email, TestHandle handle) {

        try {
            ecfeed.setQuantity(quantity);
            ecfeed.setName(name);
            ecfeed.setAddress(address);
            ecfeed.setPhone(phone);
            ecfeed.setEmail(email);
            ecfeed.setProduct(product);
            ecfeed.setColor(color);
            ecfeed.setSize(size);
            ecfeed.setCountry(country);
            ecfeed.setPayment(payment);
            ecfeed.setDelivery(delivery);

            ecfeed.submit();

            ecfeed.validate();
        } catch (Throwable e) {
            handle.addFeedback(false, e.getMessage());
            fail();
        }

        handle.addFeedback(true);
    }
}
