package testify.runner.api;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class Manual {

// First, you should check the connection and generate a sample request.
// This part does not require the 'ecFeed' library.

//------------------------------------------------------------------------------

// The endpoint address.
    private static final String webPageAddress = "https://api.ecfeed.com";

//------------------------------------------------------------------------------

// The annotation states that the method should be invoked as a standard jUnit test (without the 'ecFeed' extension).
    @Test
// The name of the method can be arbitrary.
    void apiValidate() {
// Create a map of query parameters.
        Map<String, Object> parameters = new HashMap<>();
// Specify a value for each query parameter.
        parameters.put("country", "Poland");
        parameters.put("name", "Krzysztof Skorupski");
        parameters.put("address", "ul. Komandorska 6. 50-022 Wroclaw");
        parameters.put("product", "hoodie");
        parameters.put("color", "black");
        parameters.put("size", "L");
        parameters.put("quantity", "3");
        parameters.put("payment", "VISA");
        parameters.put("delivery", "standard");
        parameters.put("phone", "+48123456789");
        parameters.put("email", "k.skorupski@testify.no");

// Try sending the 'POST' request.
        try {
// Generate the 'POST' request.
            HttpResponse<String> response = Unirest.post(webPageAddress).queryString(parameters).asString();
// Extract the response body.
            String responseBody = response.getBody();
// Display the response body (for debugging).
            System.out.println(responseBody);
// Assert that the request was successful.
            assertAll("The returned JSON file contains error description(s).",
// The list of input errors is empty.
                () -> assertTrue(responseBody.contains("\"errorInput\":[]"), "The list of input errors is not empty."),
// The list of output errors is empty.
                () -> assertTrue(responseBody.contains("\"errorOutput\":[]"), "The list of output errors is not empty.")
            );
// The request was not successful (e.g. the endpoint address was erroneous).
        } catch (UnirestException e) {
// Display the cause of the error.
            e.printStackTrace();
// Fail the test.
            fail();
        }
    }

//------------------------------------------------------------------------------

}