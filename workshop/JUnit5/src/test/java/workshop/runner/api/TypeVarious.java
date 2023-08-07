package workshop.runner.api;

import com.ecfeed.TestHandle;
import com.ecfeed.TestProvider;
import com.ecfeed.params.ParamsNWise;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import workshop.Config;
import workshop.data.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

public class TypeVarious {

    private static final String webPageAddress = "https://api.ecfeed.com?mode=error";

    private static String keyStorePath = Config.keyStorePath;
    private static String modelID = Config.modelID;
    private static String method = Config.methodVarious;
    private static String label = "API - Type";

//------------------------------------------------------------------------------

    private static Iterable<Object[]> testProviderNWise() {

        var parameters = new HashMap<String, String>();
        parameters.put("keyStorePath", keyStorePath);

        return TestProvider.create(modelID, parameters).generateNWise(method, ParamsNWise.create().feedback().label(label));
    }

    @ParameterizedTest
    @MethodSource("testProviderNWise")
    void apiValidate(Country country, String name, String address, Product product, Color color, Size size, int quantity, Payment payment, Delivery delivery, String phone, String email, TestHandle testHandle) {
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

            JSONObject json = new JSONObject(responseBody);
            JSONArray errorInput = json.getJSONArray("errorInput");
            JSONArray errorOutput = json.getJSONArray("errorOutput");

            assertAll("The returned JSON file contains error description(s).",
                () -> assertTrue(errorInput.length() == 0,
                    () -> testHandle.addFeedback(false, "Input error", IntStream.range(0, errorInput.length()).boxed().collect(Collectors.toMap(e -> "" + (e + 1), e -> "- " + errorInput.getString(e))))),
                () -> assertTrue(errorOutput.length() == 0,
                    () -> testHandle.addFeedback(false, "Output error", IntStream.range(0, errorOutput.length()).boxed().collect(Collectors.toMap(e -> "" + (e + 1), e -> "- " + errorOutput.getString(e)))))
            );

        } catch (UnirestException e) {
            testHandle.addFeedback(false, "Connection error");
            e.printStackTrace();
            fail();
        }

        testHandle.addFeedback(true);
    }

}