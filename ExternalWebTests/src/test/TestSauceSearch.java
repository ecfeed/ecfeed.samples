package test;

import java.util.concurrent.TimeUnit;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.testify.ecfeed.runner.StaticRunner;
import com.testify.ecfeed.runner.annotations.EcModel;


@RunWith(StaticRunner.class)
@EcModel("src/model.ect")
public class TestSauceSearch {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @Before
  public void setUp() throws Exception {
    driver = new FirefoxDriver();
    baseUrl = "http://localhost:8080";
    driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
  }

  @Test
  public void testSearchSauce(String name, boolean expected) throws Exception {
	setUp();
    driver.get(baseUrl + "/");
    driver.findElement(By.name("q")).clear();
    driver.findElement(By.name("q")).sendKeys(name);
    driver.findElement(By.id("search_button")).click();
    if(expected){
    	assertTrue(isElementPresent(By.cssSelector("div.title")));
        driver.findElement(By.cssSelector("input.addToCart")).click();
    	Thread.sleep(1000);
        assertNotEquals("0", driver.findElement(By.cssSelector("span.headerCartItemsCount")).getText());
    }
    else{
    	assertTrue(!isElementPresent(By.cssSelector("div.title")));
    }

  }

  @After
  public void tearDown() throws Exception {
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }

  private boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      driver.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driver.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }
}
