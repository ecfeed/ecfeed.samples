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

// The following test uses the 'ecFeed' library. To run it, you must generate a personal keystore first.
// It can be done on the official 'ecFeed' web page, i.e. 'https://www.ecfeed.com/'.
// After logging in, go to the 'settings' menu (hover the mouse pointer over your email address), open the 'security' section and click on the 'generate keystore' button.
// The name of the downloaded file, which is 'keystore.p12', must not be changed and the keystore should be placed in the '~/.ecfeed/' directory (or `~/ecfeed' for Windows users).
// The described keystore installation procedure must be done only once. It grants automatic access to all current and future 'ecFeed' services.

//------------------------------------------------------------------------------

    private static final String webPageAddress = "https://api.ecfeed.com?mode=error";

//------------------------------------------------------------------------------

// Each user can have multiple models, and therefore, it is required to provide a valid UUID.
// It can be found on the official 'ecFeed' web page, in the 'my projects' section. Also, it can be extracted from the editor window URL.
// Note, that this is the only value that you have to change in order to run the following test.
// The 'constraint' annotation contains a list of constraint names that should be used. It also accepts values 'NONE' and 'ALL' (the default value).
    private static Iterable<Object[]> testProviderNWise() {
//      return TestProvider.create("6EG2-YL4S-LMAK-Y5VW-VPV9").generateNWise("com.example.test.Demo.typeString", ParamsNWise.create().feedback().label("API").constraints("NONE"));
        return TestProvider.create("6EG2-YL4S-LMAK-Y5VW-VPV9").generateNWise("com.example.test.Demo.typeString", ParamsNWise.create().feedback().label("API"));
    }

// The name of the test method can be arbitrary. However, it must contain the same arguments as in the model version positioned in the same order.
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

//------------------------------------------------------------------------------

}