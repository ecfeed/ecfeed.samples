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

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

public class TypeString {

    private static final String webPageAddress = "https://api.ecfeed.com?mode=error";

//------------------------------------------------------------------------------

    private static Iterable<Object[]> testProviderNWise() {

        var parameters = new HashMap<String, String>();
        parameters.put("keyStorePath", "src/test/resources/demo.p12");

        return TestProvider.create("6EG2-YL4S-LMAK-Y5VW-VPV9", parameters).generateNWise("com.example.test.Demo.typeString", ParamsNWise.create().feedback().label("API"));
    }

    @ParameterizedTest
    @MethodSource("testProviderNWise")
    void apiValidate(String country, String name, String address, String product, String color, String size, String quantity, String payment, String delivery, String phone, String email, TestHandle testHandle) {
        Map<String, Object> parameters = new HashMap<>();

        parameters.put("country", country);
        parameters.put("name", name);
        parameters.put("address", address);
        parameters.put("product", product);
        parameters.put("color", color);
        parameters.put("size", size);
        parameters.put("quantity", quantity);
        parameters.put("payment", payment);
        parameters.put("delivery", delivery);
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
                () -> assertEquals(0, errorInput.length(),
                        () -> testHandle.addFeedback(false, "Input error", IntStream.range(0, errorInput.length()).boxed().collect(Collectors.toMap(e -> "" + (e + 1), e -> "- " + errorInput.getString(e))))),
                () -> assertEquals(0, errorOutput.length(),
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