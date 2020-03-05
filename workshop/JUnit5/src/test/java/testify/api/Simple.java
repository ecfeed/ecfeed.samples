package testify.api;

import com.ecfeed.junit.annotation.EcFeedInput;
import com.ecfeed.junit.annotation.EcFeedModel;
import com.ecfeed.junit.annotation.EcFeedTest;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class Simple {

//------------------------------------------------------------------------------

    // The endpoint address.
    private static final String webPageAddress = "https://workshop-2020-march-api.ecfeed.com";

//------------------------------------------------------------------------------

    @EcFeedTest
    @EcFeedModel("0603-5525-0414-9188-9919")
    @EcFeedInput("'method':'com.example.test.Demo.simple', 'dataSource':'genNWise', 'constraints':'NONE'")
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