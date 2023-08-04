package workshop.runner.selenium.pages;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;

public class EcFeedPageObject {
    private static final String webPageAddress = "http://www.workshop-2021-december.ecfeed.com?mode=error";

    private RemoteWebDriver driver;

    public EcFeedPageObject(RemoteWebDriver driver) {

        this.driver = driver;
    }

    public void visit() {

        driver.get(webPageAddress);
    }

    public void setFormText(Object name, Object address, Object quantity, Object phone, Object email) {

        setText("name", name.toString());
        setText("address", address.toString());
        setText("quantity", quantity.toString());
        setText("phone", phone.toString());
        setText("email", email.toString());
    }

    public void setFormBox(Object country, Object product, Object color, Object size, Object payment, Object delivery) {

        setBox("country", country.toString());
        setBox("product", product.toString());
        setBox("color", color.toString());
        setBox("size", size.toString());
        setBox("payment", payment.toString());
        setBox("delivery", delivery.toString());
    }

    public void submit() {

        driver.findElementById("submit").click();
    }

    public String getOutputStatus() {

        return driver.findElementById("status").getAttribute("value").trim();
    }

    public String getOutputResponse() {

        return driver.findElementById("response").getAttribute("value").trim();
    }

    public boolean validate(String status) {

        return status.equalsIgnoreCase("Request accepted");
    }

    private void setText(String id, String value) {
        var element = driver.findElementById(id);

        element.clear();
        element.sendKeys(value);
    }

    private void setBox(String id, String value) {
        var element = driver.findElementById(id);
        var elementSelect = new Select(element);

        elementSelect.selectByVisibleText(value);
    }
}
