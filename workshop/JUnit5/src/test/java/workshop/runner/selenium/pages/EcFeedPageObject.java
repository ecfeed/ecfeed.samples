package workshop.runner.selenium.pages;

import org.openqa.selenium.By;
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

    public void setFormTextName(Object name) {

        setText("name", name.toString());
    }

    public void setFormTextAddress(Object address) {

        setText("address", address.toString());
    }

    public void setFormTextQuantity(Object quantity) {

        setText("quantity", quantity.toString());
    }

    public void setFormTextPhone(Object phone) {

        setText("phone", phone.toString());
    }

    public void setFormTextEmail(Object email) {

        setText("email", email.toString());
    }

    public void setFormBoxCountry(Object country) {

        setBox("country", country.toString());
    }

    public void setFormBoxProduct(Object product) {

        setBox("product", product.toString());
    }

    public void setFormBoxColor(Object color) {

        setBox("color", color.toString());
    }

    public void setFormBoxSize(Object size) {

        setBox("size", size.toString());
    }

    public void setFormBoxPayment(Object payment) {

        setBox("payment", payment.toString());
    }

    public void setFormBoxDelivery(Object delivery) {

        setBox("delivery", delivery.toString());
    }

    public void submit() {

        driver.findElement(By.id("submit")).click();
    }

    public String getOutputStatus() {

        return driver.findElement(By.id("status")).getAttribute("value").trim();
    }

    public String getOutputResponse() {

        return driver.findElement(By.id("response")).getAttribute("value").trim();
    }

    private void setText(String id, String value) {
        var element = driver.findElement(By.id(id));

        element.clear();
        element.sendKeys(value);
    }

    private void setBox(String id, String value) {
        var element = driver.findElement(By.id(id));
        var elementSelect = new Select(element);

        elementSelect.selectByVisibleText(value);
    }
}
