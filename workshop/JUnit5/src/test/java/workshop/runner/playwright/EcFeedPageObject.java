package workshop.runner.playwright;

import com.microsoft.playwright.Page;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class EcFeedPageObject {
    private static final String webPageAddress = "http://www.workshop-2021-december.ecfeed.com/?mode=error";

    private Page page;

    public EcFeedPageObject(Page page) {

        this.page = page;
    }

    public void visit() {

        page.navigate(webPageAddress);
    }

    public void setQuantity(Object value) {

        try {
            setValueText("#quantity", value.toString());
        } catch (Throwable e) {
            throw new RuntimeException("- The filed could not be updated - Quantity!");
        }
    }

    public void setName(Object value) {

        try {
            setValueText("#name", value.toString());
        } catch (Throwable e) {
            throw new RuntimeException("- The filed could not be updated - Name!");
        }
    }

    public void setAddress(Object value) {

        try {
            setValueText("#address", value.toString());
        } catch (Throwable e) {
            throw new RuntimeException("- The filed could not be updated - Address!");
        }
    }

    public void setPhone(Object value) {

        try {
            setValueText("#phone", value.toString());
        } catch (Throwable e) {
            throw new RuntimeException("- The filed could not be updated - Phone!");
        }
    }

    public void setEmail(Object value) {

        try {
            setValueText("#email", value.toString());
        } catch (Throwable e) {
            throw new RuntimeException("- The filed could not be updated - Email!");
        }
    }

    public void setProduct(Object value) {

        try {
            setValueBox("#product", value.toString());
        } catch (Throwable e) {
            throw new RuntimeException("- The filed could not be updated - Product!");
        }
    }

    public void setColor(Object value) {

        try {
            setValueBox("#color", value.toString());
        } catch (Throwable e) {
            throw new RuntimeException("- The filed could not be updated - Color!");
        }
    }

    public void setSize(Object value) {

        try {
            setValueBox("#size", value.toString());
        } catch (Throwable e) {
            throw new RuntimeException("- The filed could not be updated - Size!");
        }
    }

    public void setCountry(Object value) {

        try {
            setValueBox("#country", value.toString());
        } catch (Throwable e) {
            throw new RuntimeException("- The filed could not be updated - Country!");
        }
    }

    public void setPayment(Object value) {

        try {
            setValueBox("#payment", value.toString());
        } catch (Throwable e) {
            throw new RuntimeException("- The filed could not be updated - Payment!");
        }
    }

    public void setDelivery(Object value) {

        try {
            setValueBox("#delivery", value.toString());
        } catch (Throwable e) {
            throw new RuntimeException("- The filed could not be updated - Delivery!");
        }
    }

    public void submit() {

        try {
            page.locator("#submit").waitFor();
            page.locator("#submit").click();
        } catch (Throwable e) {
            throw new RuntimeException("- The form could not be submitted!");
        }
    }

    public void validate() {
        var text = "";

        try {
            page.locator("#response").waitFor();
            text = page.locator("#response").textContent();
        } catch (Throwable e) {
            throw new RuntimeException("- The 'response' element could not be accessed!");
        }

        try {
            assertThat(page.locator("#response")).hasValue(Pattern.compile("Order processed without errors"));
        } catch (Throwable e) {
            throw new RuntimeException(text);
        }
    }

    private void setValueText(String selector, Object value) {

        page.locator(selector).waitFor();
        page.locator(selector).fill(value.toString());
    }

    private void setValueBox(String selector, Object value) {

        page.locator(selector).waitFor();
        page.locator(selector).selectOption(value.toString());
    }
}
