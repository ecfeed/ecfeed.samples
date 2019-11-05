package testify.api;

import com.ecfeed.junit.annotation.EcFeedInput;
import com.ecfeed.junit.annotation.EcFeedModel;
import com.ecfeed.junit.annotation.EcFeedTest;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import testify.data.*;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class Extended {

//------------------------------------------------------------------------------

    // The endpoint address.
    private static final String webPageAddress = "https://api.ecfeed.com/";

//------------------------------------------------------------------------------

    @EcFeedTest
    @EcFeedModel("9835-3029-2264-1682-5114")
    @EcFeedInput("'method':'com.ecfeed.Model.extended', 'dataSource':'genNWise', 'constraints':'NONE'")
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
            HttpResponse<String> response = Unirest.post(webPageAddress)
                .queryString(parameters)
                .asString();

            String body = response.getBody();
            System.out.println(body);

            assertAll("The returned JSON file contains error description(s).",
                () -> assertTrue(body.contains("\"errorInput\":[]"), "The list of input errors is not empty."),
                () -> assertTrue(body.contains("\"errorOutput\":[]"), "The list of output errors is not empty.")
            );

        } catch (UnirestException e) {
            e.printStackTrace();
            fail();
        }

    }
}