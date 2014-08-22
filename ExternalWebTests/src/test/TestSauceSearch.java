package test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.browserlaunchers.Sleeper;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.testify.ecfeed.runner.StaticRunner;
import com.testify.ecfeed.runner.annotations.EcModel;

@RunWith(StaticRunner.class)
@EcModel("src/model.ect")
public class TestSauceSearch {
	private WebDriver driver;
	private String baseUrl = "http://localhost:8080";

	@Test
	public void testSearchSauce(String name, boolean expected) throws Exception {
		try {
			setUp();
			driver.get(baseUrl);

			driver.findElement(By.name("q")).clear();
			driver.findElement(By.name("q")).sendKeys(name);
			driver.findElement(By.id("search_button")).click();
			if (expected) {
				assertTrue(isElementPresent(By.cssSelector("div.title")));
				driver.findElement(By.cssSelector("input.addToCart")).click();
				Sleeper.sleepTightInSeconds(2);
				assertNotEquals("0", driver.findElement(By.cssSelector("span.headerCartItemsCount")).getText());
			} else {
				assertTrue(!isElementPresent(By.cssSelector("div.title")));
			}
		} finally {
			tearDown();
		}
	}

	private void setUp() throws Exception {
		try {
			driver = new FirefoxDriver();
			driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		} catch(Exception e) {
			throw new Error("Failed to initialize Selenium driver");
		}
	}

	private void tearDown() {
		if (driver != null) {
			driver.quit();
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
}
