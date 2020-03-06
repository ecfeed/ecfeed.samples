package workshop.runner.api;

import com.ecfeed.junit.annotation.EcFeedInput;
import com.ecfeed.junit.annotation.EcFeedModel;
import com.ecfeed.junit.annotation.EcFeedTest;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TypeString {

// The following test uses the 'ecFeed' library. To run it, you must generate a personal keystore first.
// It can be done on the official 'ecFeed' web page, i.e. 'https://www.ecfeed.com/'.
// After logging in, go to the 'settings' menu (hover the mouse pointer over your email address), open the 'security' section and click on the 'generate keystore' button.
// The name of the downloaded file, which is 'keystore.p12', must not changed and the keystore should be placed in the '~/.ecfeed/' directory (or `~/ecfeed' for Windows users).
// However, if it is not possible, use the '@EcFeedKeyStore' annotation to determine its position.
// The described keystore installation procedure must be done only once. It grants an automatic access to all current and future 'ecFeed' services.

//------------------------------------------------------------------------------

    private static final String webPageAddress = "https://api.ecfeed.com";

//------------------------------------------------------------------------------

// The annotation states that the following test method should be executed with the 'ecFeed' jUnit extension.
    @EcFeedTest
// Each user can have multiple models, and therefore, it is required to provide a valid UUID.
// It can be found on the official 'ecFeed' web page, in the 'my projects' section. Also, it can be extracted from the editor window URL.
// Note, that this is the only value that you have to change in order to run the following test.
    @EcFeedModel("0603-5525-0414-9188-9919")
// Generation parameters.
// The 'method' annotation points to a specific method in the model.
// The 'dataSource' annotation defines the generation technique. Here, it is N-Wise with N=2 (the default value).
// The 'constraint' annotation contains a list of constraint names that should be used. It also accepts values 'NONE' and 'ALL' (the default value).
// The full list of customizable parameters, along with examples, can be found on the web page 'https://ecfeed.com/tutorials/junit-documentation#ecfeedinput'.
    @EcFeedInput("'method':'com.example.test.Demo.typeString', 'dataSource':'genNWise', 'constraints':'NONE'")
// The name of the test method can be arbitrary. However, it must contain the same arguments as in the model version positioned in the same order.
    void apiValidate(String country, String name, String address, String product, String color, String size, String quantity, String payment, String delivery, String phone, String email) {
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