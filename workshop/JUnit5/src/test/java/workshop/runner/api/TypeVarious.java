package workshop.runner.api;

import com.ecfeed.Param;
import com.ecfeed.TestProvider;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import workshop.data.*;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TypeVarious {

// The following test uses the 'ecFeed' library.
// It is very similar to the previous one, the only difference is that not all parameters are strings.

//------------------------------------------------------------------------------

    private static final String webPageAddress = "https://api.ecfeed.com";

//------------------------------------------------------------------------------

    private static Iterable<Object[]> testProviderNWise() {
        return TestProvider.create("0603-5525-0414-9188-9919").generateNWise("com.example.test.Demo.typeVarious", new Param.ParamsNWise().constraints("NONE"));
    }

    @ParameterizedTest
    @MethodSource("testProviderNWise")
    void apiValidate(Country country, String name, String address, Product product, Color color, Size size, int quantity, Payment payment, Delivery delivery, String phone, String email) {
        Map<String, Object> parameters = new HashMap<>();

        parameters.put("country", country.toString());
        parameters.put("name", name);
        parameters.put("address", address);
        parameters.put("product", product.toString());
        parameters.put("color", color.toString());
        parameters.put("size", size.toString());
        parameters.put("quantity", quantity + "");
        parameters.put("payment", payment.toString());
        parameters.put("delivery", delivery.toString());
        parameters.put("phone", phone);
        parameters.put("email", email);

        try {
            HttpResponse<String> response = Unirest.post(webPageAddress).queryString(parameters).asString();

            String responseBody = response.getBody();
            System.out.println(responseBody);

            assertAll("The returned JSON file contains error description(s).",
                () -> assertTrue(responseBody.contains("\"errorInput\":[]"), "The list of input errors is not empty."),
                () -> assertTrue(responseBody.contains("\"errorOutput\":[]"), "The list of output errors is not empty.")
            );

        } catch (UnirestException e) {
            e.printStackTrace();
            fail();
        }

    }

//------------------------------------------------------------------------------

}