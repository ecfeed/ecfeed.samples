package workshop.runner.playwright;

import com.ecfeed.TestHandle;
import com.ecfeed.TestProvider;
import com.ecfeed.params.ParamsNWise;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import workshop.data.*;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.fail;

public class TypeVarious extends EcFeedFixture {
    private static String keyStorePath = "src/test/resources/demo.p12";
    private static String modelID = "6EG2-YL4S-LMAK-Y5VW-VPV9";
    private static String method = "com.example.test.Demo.typeVarious";
    private static String label = "Playwright - Various";


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

            ecfeed.validate();
        } catch (Throwable e) {
            handle.addFeedback(false, e.getMessage());
            fail();
        }

        handle.addFeedback(true);
    }
}
