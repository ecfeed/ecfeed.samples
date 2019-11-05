package testify.api;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class Manual {

//------------------------------------------------------------------------------

    // The address of the API.
    private static final String webPageAddress = "https://workshop-2019-november-api.ecfeed.com";

//------------------------------------------------------------------------------

    @Test
    void apiValidate() {
        Map<String, Object> parameters = new HashMap<>();

        parameters.put("country", "Poland");
        parameters.put("name", "Krzysztof Skorupski");
        parameters.put("address", "Somewhere in Poland");
        parameters.put("product", "hoodie");
        parameters.put("color", "black");
        parameters.put("size", "l");
        parameters.put("quantity", "5");
        parameters.put("payment", "bank transfer");
        parameters.put("delivery", "standard");
        parameters.put("phone", "+47123456789");
        parameters.put("email", "k.skorupski@testify.no");

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
        }

    }
}