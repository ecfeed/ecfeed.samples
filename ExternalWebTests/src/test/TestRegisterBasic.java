package test;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import tools.ConnectionInstance;
import tools.DataSourceFactory;

import com.testify.ecfeed.runner.StaticRunner;
import com.testify.ecfeed.runner.annotations.EcModel;
import com.testify.ecfeed.runner.annotations.TestSuites;

@RunWith(StaticRunner.class)
@EcModel("src/model.ect")
public class TestRegisterBasic {
	private WebDriver driver = null;
	private ConnectionInstance connection;
	private String baseUrl = "http://localhost:8080/";
	private String registerUrl = "https://localhost:8443/register";

	@Test
	@TestSuites("valid data")
	public void testRegisterValidData(String email, String first_name, String last_name, String password) throws Exception {
		try {
			setUp();
			driver.get(baseUrl);
			
			driver.findElement(By.xpath("//div[@id='cart_info']/a[2]/span")).click();
			driver.findElement(By.id("customer.emailAddress")).clear();
			driver.findElement(By.id("customer.emailAddress")).sendKeys(email);
			driver.findElement(By.id("customer.firstName")).clear();
			driver.findElement(By.id("customer.firstName")).sendKeys(first_name);
			driver.findElement(By.id("customer.lastName")).clear();
			driver.findElement(By.id("customer.lastName")).sendKeys(last_name);
			driver.findElement(By.id("password")).clear();
			driver.findElement(By.id("password")).sendKeys(password);
			driver.findElement(By.id("passwordConfirm")).clear();
			driver.findElement(By.id("passwordConfirm")).sendKeys(password);
			driver.findElement(By.xpath("//input[@value='Register']")).click();
	
			boolean email_taken = isElementPresent(By.xpath("//*[contains(.,'" + ErrorMessages.AddressInUse + "')]"));
			if (email_taken) {
				driver.findElement(By.cssSelector("span")).click();
				driver.findElement(By.name("j_username")).clear();
				driver.findElement(By.name("j_username")).sendKeys(email);
				driver.findElement(By.name("j_password")).clear();
				driver.findElement(By.name("j_password")).sendKeys(password);
				driver.findElement(By.xpath("//input[@value='Login']")).click();
			}
			Assert.assertTrue("Not logged in - registration failed!", driver.findElement(By.linkText("Logout")) != null);
			// Revert password change and logout
			driver.findElement(By.cssSelector("a > span")).click();
			Assert.assertTrue("Not logged out!", driver.findElement(By.linkText("Login")) != null);
		} finally {
			cleanUpAfterTest(email);
			tearDown();
		}
	}

	@Test
	public void testRegisterEmailData(String email, String first_name, String last_name, String password, boolean expected_result) throws Exception {
		try {
			setUp();
			driver.get(baseUrl);

			driver.findElement(By.xpath("//div[@id='cart_info']/a[2]/span")).click();
			driver.findElement(By.id("customer.emailAddress")).clear();
			driver.findElement(By.id("customer.emailAddress")).sendKeys(email);
			driver.findElement(By.id("customer.firstName")).clear();
			driver.findElement(By.id("customer.firstName")).sendKeys(first_name);
			driver.findElement(By.id("customer.lastName")).clear();
			driver.findElement(By.id("customer.lastName")).sendKeys(last_name);
			driver.findElement(By.id("password")).clear();
			driver.findElement(By.id("password")).sendKeys(password);
			driver.findElement(By.id("passwordConfirm")).clear();
			driver.findElement(By.id("passwordConfirm")).sendKeys(password);
			driver.findElement(By.xpath("//input[@value='Register']")).click();

			if (!expected_result) {
				Assert.assertTrue(isElementPresent(By.xpath("//*[contains(.,'" + ErrorMessages.AddressInvalid + "')]"))
						|| isElementPresent(By.xpath("//*[contains(.,'" + ErrorMessages.EnterAddress + "')]"))
						|| driver.getCurrentUrl().equals(registerUrl));
			} else {
				Assert.assertTrue((isElementPresent(By.xpath("//*[contains(.,'" + ErrorMessages.AddressInUse + "')]"))
						|| (driver.findElement(By.linkText("Logout")) != null))
						&& !isElementPresent(By.xpath("//*[contains(.,'" + ErrorMessages.AddressInvalid + "')]"))
						&& !isElementPresent(By.xpath("//*[contains(.,'" + ErrorMessages.EnterAddress + "')]")));
			}
		} finally {
			cleanUpAfterTest(email);
			tearDown();
		}
	}

	@Test
	public void testRegisterBasic(String email, String first_name, String last_name, String password, String confpsswd) throws Exception {
		try {
			setUp();
			driver.get(baseUrl);

			driver.findElement(By.xpath("//div[@id='cart_info']/a[2]/span")).click();
			driver.findElement(By.id("customer.emailAddress")).clear();
			driver.findElement(By.id("customer.emailAddress")).sendKeys(email);
			driver.findElement(By.id("customer.firstName")).clear();
			driver.findElement(By.id("customer.firstName")).sendKeys(first_name);
			driver.findElement(By.id("customer.lastName")).clear();
			driver.findElement(By.id("customer.lastName")).sendKeys(last_name);
			driver.findElement(By.id("password")).clear();
			driver.findElement(By.id("password")).sendKeys(password);
			driver.findElement(By.id("passwordConfirm")).clear();
			driver.findElement(By.id("passwordConfirm")).sendKeys(confpsswd);
			driver.findElement(By.xpath("//input[@value='Register']")).click();
			driver.findElement(By.cssSelector("a > span")).click();
		} finally {
			cleanUpAfterTest(email);
			tearDown();
		}
	}

	private void cleanUpAfterTest(String email){
		try{
			connection.tryUpdate("DELETE FROM PUBLIC.blc_customer WHERE EMAIL_ADDRESS='" + email + "';");
		} catch(Exception e){
			throw new Error("Database connection failed");
		}
	}

	private void setUp() {
		try {
			connection = new ConnectionInstance(DataSourceFactory.getHSQLDataSource());
		} catch(Exception e) {
			e.printStackTrace();
			throw new Error("Failed to initialize database connection");
		}

		try {
			driver = new FirefoxDriver();
			driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		} catch(Exception e) {
			throw new Error("Failed to initialize Selenium driver");
		}
	}

	private void tearDown() throws Exception{
		if (driver != null) {
			driver.quit();
		}
		if (connection != null) {
			connection.close();
		}
	}

	private boolean isElementPresent(By by){
		try {
			driver.findElement(by);
			return true;
		} catch(NoSuchElementException e){
			return false;
		}
	}
}
