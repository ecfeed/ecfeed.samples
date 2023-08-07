package workshop.runner.playwright;

import com.ecfeed.TestHandle;
import com.ecfeed.TestProvider;
import com.ecfeed.params.ParamsNWise;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import workshop.Config;
import workshop.data.*;
import workshop.runner.playwright.fixtures.EcFeedFixture;
import workshop.runner.playwright.helpers.EcFeedHelper;

import java.util.HashMap;

public class TypeVarious extends EcFeedFixture {
    private static String keyStorePath = Config.keyStorePath;
    private static String modelID = Config.modelID;
    private static String method = Config.methodString;
    private static String label = "Playwright - Type";

    private static Iterable<Object[]> testProviderNWise() {
        var parameters = new HashMap<String, String>();
        parameters.put("keyStorePath", keyStorePath);

        return TestProvider.create(modelID, parameters).generateNWise(method, ParamsNWise.create().feedback().label(label));
    }

    @ParameterizedTest
    @MethodSource("testProviderNWise")
    void playwrightValidate(Country country, String name, String address, Product product, Color color, Size size, int quantity, Payment payment, Delivery delivery, String phone, String email, TestHandle handle) {

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

            var status = ecfeed.getStatus();
            var response = ecfeed.getResponse();

            Assertions.assertEquals("Request accepted", status,
                    () -> EcFeedHelper.sendFeedbackNegative(handle, status, response));

            EcFeedHelper.sendFeedbackPositive(handle);
        } catch (Throwable e) {
            handleThrowable(handle, e);
        }
    }

    private void handleThrowable(TestHandle handle, Throwable e) {

        workshop.runner.selenium.helpers.EcFeedHelper.sendFeedbackException(handle, e);

        throw new RuntimeException(e);
    }
}