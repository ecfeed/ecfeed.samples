package workshop.runner.selenium;

import com.ecfeed.TestHandle;
import com.ecfeed.TestProvider;
import com.ecfeed.params.ParamsNWise;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import workshop.data.*;
import workshop.runner.selenium.fixtures.EcFeedFixture;
import workshop.runner.selenium.helpers.EcFeedHelper;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.fail;

public class TypeVarious extends EcFeedFixture {
    private static String keyStorePath = "src/test/resources/demo.p12";
    private static String modelID = "6EG2-YL4S-LMAK-Y5VW-VPV9"; // "0603-5525-0414-9188-9919"
    private static String method = "com.example.test.Demo.typeVarious";
    private static String label = "Selenium";

    private static Iterable<Object[]> testProviderNWise() {
        var parameters = new HashMap<String, String>();
        parameters.put("keyStorePath", keyStorePath);

        return TestProvider.create(modelID, parameters).generateNWise(method, ParamsNWise.create().feedback().label(label));
    }

    @ParameterizedTest
    @MethodSource("testProviderNWise")
    void seleniumValidate(Country country, String name, String address, Product product, Color color, Size size, int quantity, Payment payment, Delivery delivery, String phone, String email, TestHandle handle) {

        try {
            ecfeed.setFormText(name, address, quantity, phone, email);
            ecfeed.setFormBox(country, product, color, size, payment, delivery);

            ecfeed.submit();

            var status = ecfeed.getOutputStatus();
            var response = ecfeed.getOutputResponse();

            if (ecfeed.validate(status)) {
                EcFeedHelper.sendFeedbackPositive(handle);
            } else {
                EcFeedHelper.sendFeedbackNegative(handle, status, response);
                fail();
            }
        } catch (Exception e) {

            EcFeedHelper.sendFeedbackException(handle);
        }
    }
}
