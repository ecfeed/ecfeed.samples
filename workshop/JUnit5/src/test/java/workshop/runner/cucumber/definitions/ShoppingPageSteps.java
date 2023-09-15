package workshop.runner.cucumber.definitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import workshop.runner.cucumber.drivers.DriverFactory;
import workshop.runner.playwright.pages.EcFeedPageObject;

public class ShoppingPageSteps {
    private EcFeedPageObject ecfeed = DriverFactory.get().getEcfeed();

    @Given("I access the web page")
    public void i_access_the_web_page() {
        ecfeed.visit();
    }

    @When("I enter the product name {word}")
    public void i_enter_the_product_name_t_shirt(String product) {
        ecfeed.setProduct(product);
    }

    @When("I enter the product color {word}")
    public void i_enter_the_product_color_white(String color) {
        ecfeed.setColor(color);
    }

    @When("I enter the product size {word}")
    public void i_enter_the_product_size_m(String size) {
        ecfeed.setSize(size);
    }

    @When("I enter the product quantity {int}")
    public void i_enter_the_product_quantity(Integer quantity) {
        ecfeed.setQuantity(quantity);
    }

    @When("I enter the shipment country {word}")
    public void i_enter_the_shipment_country_norway(String country) {
        ecfeed.setCountry(country);
    }

    @When("I enter the name {string}")
    public void i_enter_the_name(String name) {
        ecfeed.setName(name);
    }

    @When("I enter the address {string}")
    public void i_enter_the_address(String address) {
        ecfeed.setAddress(address);
    }

    @When("I enter the payment method {string}")
    public void i_enter_the_payment_method_visa(String payment) {
        ecfeed.setPayment(payment);
    }

    @When("I enter the delivery option {string}")
    public void i_enter_the_delivery_option_standard(String delivery) {
        ecfeed.setDelivery(delivery);
    }

    @When("I enter the phone number {string}")
    public void i_enter_the_phone_number(String phone) {
        ecfeed.setPhone(phone);
    }

    @When("I enter my email {word}")
    public void i_enter_my_email_h_larsen_data_no(String email) {
        ecfeed.setEmail(email);
    }

    @When("I place the order")
    public void i_place_the_order() {
        ecfeed.submit();
    }

    @Then("I should be presented with a successful submission message")
    public void i_should_be_presented_with_a_successful_submission_message() {
        var status = ecfeed.getStatus();

        Assertions.assertEquals("Request accepted", status);
    }

    @Then("I wait")
    public void i_wait() throws InterruptedException {
        Thread.sleep(5000);
    }
}
