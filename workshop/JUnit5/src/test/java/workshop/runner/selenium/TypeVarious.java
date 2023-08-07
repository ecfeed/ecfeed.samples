package workshop.runner.selenium;

import com.ecfeed.TestHandle;
import com.ecfeed.TestProvider;
import com.ecfeed.params.ParamsNWise;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.NoSuchElementException;
import workshop.Config;
import workshop.data.*;
import workshop.runner.selenium.fixtures.EcFeedFixture;
import workshop.runner.selenium.helpers.EcFeedHelper;

import java.util.HashMap;

public class TypeVarious extends EcFeedFixture {
    private static String keyStorePath = Config.keyStorePath;
    private static String modelID = Config.modelID;
    private static String method = Config.methodVarious;
    private static String label = "Selenium - Type";

    private static Iterable<Object[]> testProviderNWise() {
        var parameters = new HashMap<String, String>();
        parameters.put("keyStorePath", keyStorePath);

        return TestProvider.create(modelID, parameters).generateNWise(method, ParamsNWise.create().feedback().label(label));
    }

    @ParameterizedTest
    @MethodSource("testProviderNWise")
    void seleniumValidate(Country country, String name, String address, Product product, Color color, Size size, int quantity, Payment payment, Delivery delivery, String phone, String email, TestHandle handle) {

        ecfeed.setFormTextName(name);
        ecfeed.setFormTextAddress(address);
        ecfeed.setFormTextQuantity(quantity);
        ecfeed.setFormTextPhone(phone);
        ecfeed.setFormTextEmail(email);

        try {

            ecfeed.setFormBoxCountry(country);
            ecfeed.setFormBoxProduct(product);
            ecfeed.setFormBoxColor(color);
            ecfeed.setFormBoxSize(size);
            ecfeed.setFormBoxPayment(payment);
            ecfeed.setFormBoxDelivery(delivery);

        } catch (NoSuchElementException e) {
            handleNoSuchElementException(handle, e);
        }

        ecfeed.submit();

        var status = ecfeed.getOutputStatus();
        var response = ecfeed.getOutputResponse();

        EcFeedHelper.log(status + " | " + response);

        Assertions.assertEquals("Request accepted", status,
                () -> EcFeedHelper.sendFeedbackNegative(handle, status, response));

        EcFeedHelper.sendFeedbackPositive(handle);
    }

    private void handleNoSuchElementException(TestHandle handle, NoSuchElementException e) {

        EcFeedHelper.sendFeedbackException(handle, e);

        throw e;
    }
}
